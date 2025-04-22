package net.findsnow.mobsnplenty.client.renderer;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.model.SharkModel;
import net.findsnow.mobsnplenty.common.entity.Shark;
import net.findsnow.mobsnplenty.common.registry.MNPModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SharkRenderer extends MobRenderer<Shark, SharkModel> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/entity/shark/shark.png");
	public SharkRenderer(EntityRendererProvider.Context context) {
		super(context, new SharkModel(context.bakeLayer(MNPModelLayers.SHARK)), 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Shark shark) {
		return TEXTURE;
	}
}
