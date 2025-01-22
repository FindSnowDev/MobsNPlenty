package net.findsnow.mobsnplenty.worldgen.features.ambience;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class MNPFallenLogFeatureConfig implements FeatureConfiguration {
	public static final Codec<MNPFallenLogFeatureConfig> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider)
			).apply(instance, MNPFallenLogFeatureConfig::new)
	);

	public final BlockStateProvider stateProvider;

	public MNPFallenLogFeatureConfig(BlockStateProvider stateProvider) {
		this.stateProvider = stateProvider;
	}
}
