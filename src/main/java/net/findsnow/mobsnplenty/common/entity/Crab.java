package net.findsnow.mobsnplenty.common.entity;

import net.findsnow.mobsnplenty.common.registry.MNPBiomes;
import net.findsnow.mobsnplenty.common.registry.MNPEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Crab extends Animal {
	private static final double CLIMBING_SPEED = 0.08D;

	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> WAVING = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<String> ANIMATION_STATE = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.STRING);

	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState walkAnimationState = new AnimationState();
	public final AnimationState waveAnimationState = new AnimationState();
	public final AnimationState pinchAnimationState = new AnimationState();

	private long inStateTicks = 0;
	private int wavingTicks = 0;
	private boolean shouldStopWaving = false;

	public Crab(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		this.lookControl = new LookControl(this);
		this.moveControl = new MoveControl(this);
		this.jumpControl = new JumpControl(this);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(WAVING, false);
		builder.define(CLIMBING, (byte) 0);
		builder.define(VARIANT, 0);
		builder.define(ANIMATION_STATE, CrabAnimationState.IDLE.getSerializedName());
	}


	@Override
	public boolean isFood(ItemStack itemStack) {
		return itemStack.is(Items.SEAGRASS);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		return MNPEntities.CRAB.get().create(level);
	}

	public static AttributeSupplier setAttributes() {
		return Animal.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.build();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Variant", this.getVariantType());
		compound.putString("AnimationState", getCurrentState().getSerializedName());
		compound.putBoolean("IsWaving", this.isWaving());
		compound.putByte("Climbing", this.entityData.get(CLIMBING));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setCurrentState(CrabAnimationState.valueOf(compound.getString("AnimationState")));
		this.entityData.set(VARIANT, compound.getInt("Variant"));
		this.entityData.set(CLIMBING, compound.getByte("Climbing"));
	}

	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		Holder<Biome> holder = level.getBiome(this.blockPosition());
		if (holder.is(Biomes.SWAMP) || holder.is(Biomes.MANGROVE_SWAMP)) {
			this.setVariant(2);
		} else if (holder.is(MNPBiomes.LUCI_FOREST)) {
			this.setVariant(1);
		} else {
			this.setVariant(0);
		}
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));

		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.5D));

		this.goalSelector.addGoal(5, new Crab.CrabLookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new Crab.CrabRandomLookAroundGoal(this));
	}

	public boolean isWaving() {
		return this.entityData.get(WAVING);
	}
	public void setWaving(boolean waving) {
		this.entityData.set(WAVING, waving);
	}

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose);
	}


	// Tick & AI
	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide) {
			this.setClimbing(this.horizontalCollision && !this.isInWater());

			// Adjust movement speed
			if (this.isClimbing()) {
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D);
			} else {
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
			}
		}

		if (shouldStopWaving) {
			wavingTicks++;
			if (wavingTicks >= 40) {
				stopWaving();
				shouldStopWaving = false;
			}
		}

		if (this.level().isClientSide()) {
			setupAnimationStates();
		}
	}

	// Movement
	private void setupAnimationStates() {
		switch(getCurrentState()) {
			case IDLE:
				idleAnimationState.startIfStopped(this.tickCount);
				walkAnimationState.stop();
				waveAnimationState.stop();
				break;
			case WALKING:
			case CLIMBING:
				idleAnimationState.stop();
                walkAnimationState.startIfStopped(this.tickCount);
                waveAnimationState.stop();
                break;
            case WAVING:
				idleAnimationState.stop();
				walkAnimationState.stop();
				waveAnimationState.startIfStopped(this.tickCount);
				break;
		}
	}

	public CrabAnimationState getCurrentState() {
		return CrabAnimationState.fromString(this.entityData.get(ANIMATION_STATE));
	}

	private void setCurrentState(CrabAnimationState state) {
		this.entityData.set(ANIMATION_STATE, state.getSerializedName());
		this.inStateTicks = 0L;
	}

	@Override
	protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
		return super.calculateFallDamage(fallDistance, damageMultiplier) - 10;
	}

	@Override
	public float maxUpStep() {
		return 0.25F;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level().isClientSide) {
			return;
		}

		boolean isClimbing = this.horizontalCollision;
		this.setClimbing(isClimbing);

		if (!this.isWaving()) {
			CrabAnimationState currentState = getCurrentState();
			if (isClimbing) {
				setCurrentState(CrabAnimationState.CLIMBING);
			} else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-8) {
				setCurrentState(CrabAnimationState.WALKING);
			} else if (currentState == CrabAnimationState.WALKING || currentState == CrabAnimationState.CLIMBING) {
				setCurrentState(CrabAnimationState.IDLE);
			}
		}

		if (isClimbing && getCurrentState() == CrabAnimationState.CLIMBING) {
			Vec3 movement = this.getDeltaMovement();
			setCurrentState(CrabAnimationState.WALKING);
			this.setDeltaMovement(movement.x * 0.3, movement.y * 0.3, movement.z * 0.3);
		}

		if (!this.isWaving() && !isClimbing && !this.isInWater() && getCurrentState() == CrabAnimationState.IDLE) {
			if (this.random.nextFloat() < 0.005F) {
				startWaving();
			}
		}
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isClimbing()) {
			if (this.horizontalCollision) {
				// Slower vertical climbing
				this.setDeltaMovement(this.getDeltaMovement().x,
						this.zza > 0 ? CLIMBING_SPEED : this.getDeltaMovement().y,
						this.getDeltaMovement().z);

				if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
				}

				if (this.zza != 0 && getCurrentState() != CrabAnimationState.WALKING) {
					setCurrentState(CrabAnimationState.WALKING);
				}
			}
		}
		super.travel(travelVector);
	}

	private void startWaving() {
		this.setWaving(true);
		setCurrentState(CrabAnimationState.WAVING);
		this.inStateTicks = 0L;
		this.wavingTicks = 0;
		this.shouldStopWaving = true;
	}

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	private void stopWaving() {
		this.setWaving(false);
		setCurrentState(CrabAnimationState.IDLE);
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new WallClimberNavigation(this, level);
	}

	@Override
	public boolean onClimbable() {
		return isClimbing();
	}

	public boolean isClimbing() {
		return (this.entityData.get(CLIMBING) & 1) != 0;
	}

	public void setClimbing(boolean climbing) {
		byte b0 = this.entityData.get(CLIMBING);
		if (climbing) {
			b0 |= 1;
		} else {
			b0 &= ~1;
		}
		this.entityData.set(CLIMBING, b0);
	}

	public Direction getClimbDirection() {
		if (!isClimbing()) {
			return Direction.DOWN;
		}
		if (horizontalCollision) {
			if (getDeltaMovement().x > 0) return Direction.EAST;
			if (getDeltaMovement().x < 0) return Direction.WEST;
			if (getDeltaMovement().z > 0) return Direction.SOUTH;
			if (getDeltaMovement().z < 0) return Direction.NORTH;
		}
		if (verticalCollision) {
			return getDeltaMovement().y > 0 ? Direction.UP : Direction.DOWN;
		}

		return Direction.DOWN;
	}

	// State
	public enum CrabAnimationState implements StringRepresentable {
		IDLE("idle", 0),
		WALKING("walking", 10),
		CLIMBING("climbing", 10),
		WAVING("waving", 40),
		PINCH("pinch", 10);

		private final String name;
		private final int duration;

		CrabAnimationState(String name, int duration) {
			this.name = name;
			this.duration = duration;
		}

		public static CrabAnimationState fromString(String name) {
			for (CrabAnimationState state : values()) {
				if (state.getSerializedName().equals(name)) {
					return state;
				}
			}
			return IDLE;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public int getDuration() {
			return duration;
		}
	}


	// Goals
	private class CrabLookAtPlayerGoal extends LookAtPlayerGoal {
		public CrabLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> targetType, float maxDistance) {
			super(mob, targetType, maxDistance);
		}

		@Override
		public boolean canUse() {
			return !Crab.this.isClimbing() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !Crab.this.isClimbing() && super.canContinueToUse();
		}
	}

	private class CrabRandomLookAroundGoal extends RandomLookAroundGoal {
		public CrabRandomLookAroundGoal(Mob mob) {
			super(mob);
		}

		@Override
		public boolean canUse() {
			return !Crab.this.isClimbing() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !Crab.this.isClimbing() && super.canContinueToUse();
		}
	}

	// Crab Variants
	private int getVariantType() {
		return this.entityData.get(VARIANT);
	}

	public int getVariant() {
		return Mth.clamp(this.entityData.get(VARIANT), 0, 2);
	}

	private void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}
}
