package net.findsnow.mobsnplenty.client.model;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.entity.CrabEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CrabModel extends DefaultedEntityGeoModel<CrabEntity> {

	public CrabModel() {
		super(Mobsnplenty.id("crab"), true);
	}

	@Override
	public ResourceLocation getModelResource(CrabEntity animatable) {
		return Mobsnplenty.id("geo/entity/crab.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CrabEntity animatable) {
		return switch (animatable.getVariant()) {
			case 1 -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab_green.png");
			case 2 -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab_blue.png");
			default -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(CrabEntity animatable) {
		return Mobsnplenty.id("animations/crab/crab.animation.json");
	}

	@Override
	public void setCustomAnimations(CrabEntity animatable, long instanceId, AnimationState<CrabEntity> animationState) {
		GeoBone head = getAnimationProcessor().getBone("head");
		GeoBone body = getAnimationProcessor().getBone("crab");
		GeoBone bone = getAnimationProcessor().getBone("bone");

		if (bone != null) {
			if (!animatable.isClimbing()) {
				bone.setRotY(90.0F);
			} else {
				bone.setRotY(0.0F);
			}
		}

		if (body != null) {
			Direction climbDirection = animatable.getClimbDirection();

			if (animatable.isClimbing()) {
				switch (climbDirection) {
					case UP:
						body.setRotX(-90 * Mth.DEG_TO_RAD);
						break;
					case DOWN:
						body.setRotX(90 * Mth.DEG_TO_RAD);
						break;
					case NORTH, WEST:
						body.setRotZ(90 * Mth.DEG_TO_RAD);
						break;
					case SOUTH, EAST:
						body.setRotZ(-90 * Mth.DEG_TO_RAD);
						break;
				}
			} else {
				body.setRotX(0);
				body.setRotZ(0);
			}
			if (head != null) {
				EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
				float scale = 0.5f;
				head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD * scale);
				head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD * scale);
			}
		}
	}
}
