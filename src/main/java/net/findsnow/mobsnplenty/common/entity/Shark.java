package net.findsnow.mobsnplenty.common.entity;

import net.findsnow.mobsnplenty.common.entity.animationstate.SharkAnimationState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Shark extends WaterAnimal {
	private static final double CRAWLING_SPEED = 0.08D;
	private static final EntityDataAccessor<Boolean> CRAWLING = SynchedEntityData.defineId(Shark.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> ANIMATION_STATE = SynchedEntityData.defineId(Shark.class, EntityDataSerializers.STRING);

	private long inStateTicks = 0;
	private int feedingCooldown = 0;
	private int attackCooldown = 0;
	private WaterAnimal targetedFish;

	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState crawlingAnimationState = new AnimationState();
	public final AnimationState swimmingAnimationState = new AnimationState();
	public final AnimationState biteAnimationState = new AnimationState();


	public Shark(EntityType<? extends WaterAnimal> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new SmoothSwimmingMoveControl(this, 20, 3, 0.05F, 0.1F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 3);
		this.maxUpStep();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CRAWLING, false);
		builder.define(ANIMATION_STATE, SharkAnimationState.SWIMMING.getSerializedName());
	}

	public static AttributeSupplier setAttributes() {
		return Animal.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 1.0F)
				.add(Attributes.ATTACK_DAMAGE, 5.0f)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.8F)
				.build();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("AnimationState", getCurrentState().getSerializedName());
		compound.putBoolean("Crawling", this.entityData.get(CRAWLING));
		compound.putInt("AttackCooldown", this.attackCooldown);
		compound.putInt("FeedingCooldown", this.feedingCooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setCurrentState(SharkAnimationState.valueOf(compound.getString("AnimationState")));
		this.entityData.set(CRAWLING, compound.getBoolean("Crawling"));
		this.feedingCooldown = compound.getInt("FeedingCooldown");
		this.attackCooldown = compound.getInt("AttackCooldown");
	}

	public SharkAnimationState getCurrentState() {
		return SharkAnimationState.fromString(this.entityData.get(ANIMATION_STATE));
	}

	public void setCurrentState(SharkAnimationState state) {
        this.entityData.set(ANIMATION_STATE, state.getSerializedName());
		this.inStateTicks = 0L;
    }

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Dolphin.class, 8.0F, 1.0, 1.0));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0, 1.0));
		this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 10));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 7.0F));
	}

	private void setupAnimationStates() {
		switch(getCurrentState()) {
			case IDLE:
				idleAnimationState.startIfStopped(this.tickCount);
				swimmingAnimationState.stop();
				biteAnimationState.stop();
				crawlingAnimationState.stop();
				break;
			case SWIMMING:
				idleAnimationState.stop();
				swimmingAnimationState.startIfStopped(tickCount);
				biteAnimationState.stop();
				crawlingAnimationState.stop();
				break;
			case CRAWLING:
				idleAnimationState.stop();
                swimmingAnimationState.stop();
                biteAnimationState.stop();
                crawlingAnimationState.startIfStopped(tickCount);
                break;
			case BITE:
				idleAnimationState.stop();
				biteAnimationState.startIfStopped(tickCount);
				crawlingAnimationState.stop();
				break;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			setupAnimationStates();
		}

		if (feedingCooldown > 0) {
			feedingCooldown--;
		}

		if (attackCooldown > 0) {
			attackCooldown--;
		}

		if (spotsNearbyFish() && isHungry()) {
			targetFishForMeal();
		}
	}

	public void travel(Vec3 travelVector) {
		if (this.isControlledByLocalInstance() && this.isInWater()) {
			this.moveRelative(this.getSpeed(), travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.3));
		} else {
			super.travel(travelVector);
		}

	}

	private void targetFishForMeal() {
		if (targetedFish != null) {
			if (!targetedFish.isAlive() || this.distanceToSqr(targetedFish) > 64.0D) {
				targetedFish = null;
			}
		}

		// Find a new target if needed
		if (targetedFish == null && !this.level().isClientSide) { // Fixed: simpler check for server-side
			// Get all nearby water animals
			List<WaterAnimal> nearbyFish = this.level().getEntitiesOfClass(
					WaterAnimal.class,
					this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
					(entity) -> !(entity instanceof Shark) && entity.isAlive()
			);

			// Find the nearest one
			double closestDistSq = Double.MAX_VALUE;
			for (WaterAnimal fish : nearbyFish) {
				double distSq = this.distanceToSqr(fish);
				if (distSq < closestDistSq) {
					closestDistSq = distSq;
					targetedFish = fish;
				}
			}
		}

		// Interact with targeted fish
		if (targetedFish != null) {
			this.getLookControl().setLookAt(targetedFish, 10.0F, (float)this.getMaxHeadXRot());
			this.getNavigation().moveTo(targetedFish, 1.0D); // Fixed: simplified movement

			if (this.distanceToSqr(targetedFish) < 2.0D) {
				if (attackCooldown <= 0) {
					setCurrentState(SharkAnimationState.BITE);
					targetedFish.hurt(this.damageSources().mobAttack(this),
							(float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
					attackCooldown = 30;
					feedingCooldown = 2000;

					if (!targetedFish.isAlive()) {
						targetedFish = null;
					}
				}
			}
		}
	}

	private boolean isHungry() {
		return feedingCooldown <= 0;
	}

	private boolean spotsNearbyFish() {
		List<WaterAnimal> nearbyFish = this.level().getEntitiesOfClass(
				WaterAnimal.class,
				this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
				(entity) -> !(entity instanceof Shark) && entity.isAlive()
		);
		return !nearbyFish.isEmpty();
	}


	@Override
	public double getEyeY() {
		return this.getY() + this.getBbHeight() * 2.0f;
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.TURTLE_SWIM;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new AmphibiousPathNavigation(this, level);
	}
}
