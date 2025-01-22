package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNPBiomes {
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, Mobsnplenty.MOD_ID);

	// Surface Biomes
	public static final ResourceKey<Biome> LUCI_FOREST = registerKey("luci_forest");


	public static ResourceKey<Biome> registerKey(String name) {
		return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, name));
	}

	public static void register(IEventBus eventBus) {
		BIOMES.register(eventBus);
	}
}
