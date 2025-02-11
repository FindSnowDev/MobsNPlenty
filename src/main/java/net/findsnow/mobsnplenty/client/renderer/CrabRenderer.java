package net.findsnow.mobsnplenty.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.model.CrabModel;
import net.findsnow.mobsnplenty.common.entity.Crab;
import net.findsnow.mobsnplenty.common.registry.MNPModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrabRenderer extends MobRenderer<Crab, CrabModel> {
	public CrabRenderer(EntityRendererProvider.Context context) {
		super(context, new CrabModel(context.bakeLayer(MNPModelLayers.CRAB)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(Crab entity) {
		return switch (entity.getVariant()) {
			case 1 -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab_green.png");
			case 2 -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab_blue.png");
			default -> ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/crab/crab.png");
		};
	}

	@Override
	public void render(Crab entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (entity.isBaby()) {
			poseStack.scale(0.5f, 0.5f, 0.5f);
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}
}
