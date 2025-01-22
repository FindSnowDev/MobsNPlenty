package net.findsnow.mobsnplenty.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.entity.custom.CrabEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrabRenderer extends GeoEntityRenderer<CrabEntity> {
	public CrabRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CrabModel());
	}

	@Override
	public ResourceLocation getTextureLocation(CrabEntity animatable) {
		return Mobsnplenty.id("textures/entity/crab/crab.png");
	}

	@Override
	public void render(CrabEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
	                   MultiBufferSource bufferSource, int packedLight) {
		if (entity.isBaby()) {
			poseStack.scale(0.4F, 0.4F, 0.4F);
		}
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
}
