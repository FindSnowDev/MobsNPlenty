package net.findsnow.mobsnplenty.block;

import net.findsnow.mobsnplenty.registry.MNPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class MNPLogBlock extends RotatedPillarBlock {
	public MNPLogBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return true;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}

	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		if (context.getItemInHand().getItem() instanceof AxeItem) {
			if (state.is(MNPBlocks.LUCI_LOG.get())) {
				return MNPBlocks.STRIPPED_LUCI_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
			} else if (state.is(MNPBlocks.LUCI_WOOD.get())) {
				return MNPBlocks.STRIPPED_LUCI_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
			}
		}
		return super.getToolModifiedState(state, context, itemAbility, simulate);
	}
}
