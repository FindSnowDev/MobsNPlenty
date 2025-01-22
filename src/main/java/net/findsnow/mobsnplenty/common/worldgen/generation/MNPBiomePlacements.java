package net.findsnow.mobsnplenty.common.worldgen.generation;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import net.findsnow.mobsnplenty.common.registry.MNPBiomes;
import net.minecraft.world.level.biome.Biomes;

public class MNPBiomePlacements {
	public static void register() {
		BiomePlacement.replaceOverworld(
				Biomes.TAIGA,
				MNPBiomes.LUCI_FOREST,
				0.3
		);
	}
}
