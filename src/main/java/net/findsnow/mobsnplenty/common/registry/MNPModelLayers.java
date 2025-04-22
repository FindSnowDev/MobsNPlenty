package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class MNPModelLayers {
	public static final ModelLayerLocation CRAB = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "crab"), "main");
	public static final ModelLayerLocation SHARK = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "shark"), "main");
}
