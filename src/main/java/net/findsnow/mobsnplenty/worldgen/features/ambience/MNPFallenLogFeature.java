package net.findsnow.mobsnplenty.worldgen.features.ambience;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class MNPFallenLogFeature extends Feature<MNPFallenLogFeatureConfig> {
	public MNPFallenLogFeature(Codec<MNPFallenLogFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<MNPFallenLogFeatureConfig> featurePlaceContext) {
		BlockPos blockPos = featurePlaceContext.origin();
		WorldGenLevel worldGenLevel = featurePlaceContext.level();
		RandomSource randomSource = featurePlaceContext.random();
		MNPFallenLogFeatureConfig fallenLogFeatureConfig = featurePlaceContext.config();
		BlockState blockState = fallenLogFeatureConfig.stateProvider.getState(randomSource, blockPos);

		int size = randomSource.nextInt(4, 6);
		Direction.Axis axis = Direction.Axis.getRandom(randomSource);
		if (blockState.hasProperty(BlockStateProperties.AXIS)) {
			blockState = blockState.setValue(BlockStateProperties.AXIS, axis);
		}
		boolean validArea = true;
		for (int i = 0; i < size; i++) {
			BlockPos pos = blockPos.relative(axis, i);
			validArea = validArea && worldGenLevel.isEmptyBlock(pos) && worldGenLevel.getBlockState(pos.below()).is(BlockTags.DIRT);
		}
		if (validArea) {
			for (int i = 0; i < size; i++) {
				BlockPos cur = blockPos.relative(axis, i);
				worldGenLevel.setBlock(cur, blockState, 4);
			}
		}
		return validArea;
	}
}
