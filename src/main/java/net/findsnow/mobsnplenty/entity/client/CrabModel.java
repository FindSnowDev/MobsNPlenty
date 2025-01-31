package net.findsnow.mobsnplenty.entity.client;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.entity.custom.CrabEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
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
		return Mobsnplenty.id("textures/entity/crab/crab.png");
	}

	@Override
	public ResourceLocation getAnimationResource(CrabEntity animatable) {
		return Mobsnplenty.id("animations/crab/crab.animation.json");
	}

	@Override
	public void setCustomAnimations(CrabEntity animatable, long instanceId, AnimationState<CrabEntity> animationState) {
		GeoBone head = getAnimationProcessor().getBone("head");
		GeoBone body = getAnimationProcessor().getBone("crab");

		if (body != null) {
			body.setRotY(90 * Mth.DEG_TO_RAD);
		}

		if (head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

			float scale = 0.5f;
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD * scale);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD * scale);
		}
	}
}
