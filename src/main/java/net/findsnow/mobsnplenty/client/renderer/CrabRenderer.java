package net.findsnow.mobsnplenty.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.findsnow.mobsnplenty.client.model.CrabModel;
import net.findsnow.mobsnplenty.common.entity.CrabEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrabRenderer extends GeoEntityRenderer<CrabEntity> {
	public CrabRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new CrabModel());
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