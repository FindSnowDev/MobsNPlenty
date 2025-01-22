package net.findsnow.mobsnplenty.worldgen.features.foliage;

import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.findsnow.mobsnplenty.registry.MNPFoliagePlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Set;

public class MNPLuciFoliagePlacer extends FoliagePlacer {
	public static final MapCodec<MNPLuciFoliagePlacer> CODEC;

	private final IntProvider increase;
	private final IntProvider branchGetter;
	private final FloatProvider leafGetter;

	public Set<BlockPos> leafPos;
	public Set<BlockPos> probLeafPos;

	public MNPLuciFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider increase, IntProvider branchGetter, FloatProvider leafGetter) {
		super(radius, offset);
		this.branchGetter = branchGetter;
		this.increase = increase;
		this.leafGetter = leafGetter;
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return MNPFoliagePlacers.LUCI_FOLIAGE_PLACER.get();
	}

	@Override
	protected void createFoliage(LevelSimulatedReader levelSimulatedReader, FoliageSetter foliageSetter, RandomSource randomSource, TreeConfiguration treeConfiguration, int i, FoliageAttachment foliageAttachment, int i1, int i2, int i3) {
		this.leafPos = Sets.newHashSet();
		this.probLeafPos = Sets.newHashSet();

		int branchCount = (i1 - 6) / 2;
		for (int j = 0; j < 4; j++) {
			Direction direction = Direction.from2DDataValue(j);
			this.leafPos.add(foliageAttachment.pos().below(2).relative(direction));
			this.leafPos.add(foliageAttachment.pos().below(3).relative(direction).relative(direction.getClockWise()));
			this.leafPos.add(foliageAttachment.pos().below().relative(direction));
			this.leafPos.add(foliageAttachment.pos().below().relative(direction).relative(direction.getClockWise()));
			this.leafPos.add(foliageAttachment.pos().relative(direction));
		}

		this.leafPos.add(foliageAttachment.pos());
		this.leafPos.add(foliageAttachment.pos().above());
		this.leafPos.add(foliageAttachment.pos().above(2));
		this.probLeafPos.add(foliageAttachment.pos().above(3));

		int numBranchCounted = branchGetter.sample(randomSource);
		for (int k = 0; k < (Math.min(branchCount, numBranchCounted)); k++) {
			BlockPos localOrigin = foliageAttachment.pos().below(4 + 2 * k);
			for (int x = -i2; x <= i2; x++) {
				for (int z = -i2; z <= i2; z++) {
					final int i4 = Math.abs(x) + Math.abs(z);
					if (i4 < i2 + 2) {
						this.leafPos.add(localOrigin.offset(x, 0, z));
					}
					if (i4 < i2) {
						this.leafPos.add(localOrigin.offset(x, 1, z));
					}
				}
			}
		}
		if (branchCount >= numBranchCounted) {
			for (int m = 0; m < branchCount - numBranchCounted; m++) {
				BlockPos localOrigin = foliageAttachment.pos().below(4 + 2 * numBranchCounted + 2 * m);
				int size = i2 + increase.sample(randomSource);
				for (int x = -size; x <= size; x++) {
					for (int z = -size; z <= size; z++) {
						if (Math.abs(x) + Math.abs(z) < size + 2) {
							this.leafPos.add(localOrigin.offset(x, 0, z));
						}
						if (Math.abs(x) + Math.abs(z) < size) {
							this.leafPos.add(localOrigin.offset(x, 1, z));
						}
					}
				}
			}
		}
		for (BlockPos leafPos : leafPos) {
			tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, leafPos);
		}
		float leafProbabilitySampled = leafGetter.sample(randomSource);
		for (BlockPos leafPos : probLeafPos) {
			if (randomSource.nextFloat() > leafProbabilitySampled)
				tryPlaceLeaf(levelSimulatedReader, foliageSetter, randomSource, treeConfiguration, leafPos);
		}
	}

	@Override
	public int foliageHeight(RandomSource randomSource, int i, TreeConfiguration treeConfiguration) {
		return i;
	}

	@Override
	protected boolean shouldSkipLocation(RandomSource randomSource, int i, int i1, int i2, int i3, boolean b) {
		return false;
	}
	
	static {
		CODEC = RecordCodecBuilder.mapCodec(instance ->
				foliagePlacerParts(instance).and(instance.group(
						IntProvider.codec(-4, 4).fieldOf("increase").forGetter(tree -> tree.increase),
						IntProvider.codec(1, 8).fieldOf("branch_getter").forGetter(tree -> tree.branchGetter),
						FloatProvider.codec(0.0F, 1.0F).fieldOf("leaf_getter").forGetter(tree -> tree.leafGetter)
				)).apply(instance, MNPLuciFoliagePlacer::new)
		);
	}
}
