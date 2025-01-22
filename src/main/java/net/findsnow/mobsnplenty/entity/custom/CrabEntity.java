package net.findsnow.mobsnplenty.entity.custom;

import net.findsnow.mobsnplenty.entity.core.AnimValue;
import net.findsnow.mobsnplenty.registry.MNPEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrabEntity extends Animal implements GeoEntity {

	private static final EntityDataAccessor<Boolean> WAVING = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.BYTE);

	protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.crab.idle");
	protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.crab.walk");
	protected static final RawAnimation WAVE = RawAnimation.begin().thenPlay("animation.crab.wave");
	protected static final RawAnimation PINCH = RawAnimation.begin().thenPlay("animation.crab.pinch");

	private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public CrabEntity(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
		this.lookControl = new LookControl(this);
		this.moveControl = new MoveControl(this);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(WAVING, false);
		builder.define(CLIMBING, (byte) 0);
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
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.build();
	}


	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));

		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));

		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
		}
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
		} else {
			Vec3 movement = this.getDeltaMovement();
			if (this.onClimbable()) {
				this.resetFallDistance();
				double dx = Mth.clamp(movement.x, -0.15D, 0.15D);
				double dz = Mth.clamp(movement.z, -0.15D, 0.15D);
				double dy = Math.max(movement.y, -0.15D);

				if (dy < 0.0D && !this.getBlockStateOn().isScaffolding(this)) {
					dy = 0.0D;
				}
				movement = new Vec3(dx, dy, dz);
			}
			super.travel(travelVector);
		}
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
}

