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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrabEntity extends Animal implements GeoEntity {
	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.INT);

	private static final EntityDataAccessor<Boolean> WAVING = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.BYTE);

	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.crab.idle");
	protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.crab.walk");
	protected static final RawAnimation WAVE = RawAnimation.begin().thenPlay("animation.crab.wave");

	private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public CrabEntity(EntityType<? extends Animal> entityType, Level level) {
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
	}

	@Override
	public boolean isFood(ItemStack itemStack) {
		return itemStack.is(Items.SEAGRASS);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return MNPEntities.CRAB.get().create(serverLevel);
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
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(VARIANT, compound.getInt("Variant"));
	}

 @Override
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

		this.goalSelector.addGoal(5, new CrabLookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new CrabRandomLookAroundGoal(this));
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

	// Movement
	@Override
	public float maxUpStep() {
		return 0.25F;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.setClimbing(this.horizontalCollision);

			if (this.isClimbing()) {
				Vec3 movement = this.getDeltaMovement();
				this.setDeltaMovement(movement.x * 0.3, movement.y * 0.3, movement.z * 0.3);
			}

			if (!this.isWaving() && !this.isClimbing() && !this.isInWater()) {
				if (this.random.nextFloat() < 0.5F) {
					startWaving();
				}
			}
		}
	}

	private void startWaving() {
		this.setWaving(true);
		this.level().getServer().tell(new TickTask(
				this.level().getServer().getTickCount() + 40,
				this::stopWaving
		));
	}

	private void stopWaving() {
		this.setWaving(false);
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

	// Geckolib stuff
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
	}


	private PlayState predicate(AnimationState<CrabEntity> event) {
		if (this.isWaving()) {
			event.setAnimation(WAVE);
		} else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 || this.isClimbing()) {
			event.setAnimation(WALK);
		} else {
			event.setAnimation(IDLE);
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

    private class CrabLookAtPlayerGoal extends LookAtPlayerGoal {
        public CrabLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> targetType, float maxDistance) {
            super(mob, targetType, maxDistance);
        }

        @Override
        public boolean canUse() {
            return !CrabEntity.this.isClimbing() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !CrabEntity.this.isClimbing() && super.canContinueToUse();
        }
    }

    private class CrabRandomLookAroundGoal extends RandomLookAroundGoal {
        public CrabRandomLookAroundGoal(Mob mob) {
            super(mob);
        }

        @Override
        public boolean canUse() {
            return !CrabEntity.this.isClimbing() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !CrabEntity.this.isClimbing() && super.canContinueToUse();
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

