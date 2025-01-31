package net.findsnow.mobsnplenty.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.worldgen.features.ambience.MNPFallenLogFeature;
import net.findsnow.mobsnplenty.worldgen.features.ambience.MNPFallenLogFeatureConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Mobsnplenty.MOD_ID);

	public static final Supplier<Feature<MNPFallenLogFeatureConfig>> FALLEN_LOG = FEATURES.register("fallen_log",
			() -> new MNPFallenLogFeature(MNPFallenLogFeatureConfig.CODEC));


	public static final ResourceKey<ConfiguredFeature<?, ?>> LUCI = registerKey("luci");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_LUCI = registerKey("large_luci");



	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, name));
	}

	public static void register(IEventBus eventBus) {
		FEATURES.register(eventBus);
	}
}
