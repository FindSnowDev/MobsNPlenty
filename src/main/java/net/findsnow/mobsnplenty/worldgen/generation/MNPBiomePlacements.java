package net.findsnow.mobsnplenty.worldgen.generation;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import net.findsnow.mobsnplenty.registry.MNPBiomes;
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
