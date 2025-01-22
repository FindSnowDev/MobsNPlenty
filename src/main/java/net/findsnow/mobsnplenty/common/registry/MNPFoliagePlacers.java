package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.worldgen.features.foliage.MNPLuciFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNPFoliagePlacers {
	public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Mobsnplenty.MOD_ID);

	public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<MNPLuciFoliagePlacer>> LUCI_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPES.register("luci_foliage_placer", () -> new FoliagePlacerType<>(MNPLuciFoliagePlacer.CODEC));

	public static void register(IEventBus eventBus)  {
		FOLIAGE_PLACER_TYPES.register(eventBus);
	}
}
