package net.findsnow.mobsnplenty.common.block;

import com.mojang.serialization.MapCodec;
import net.findsnow.mobsnplenty.common.blockentities.MNPChomperBE;
import net.findsnow.mobsnplenty.common.registry.MNPBlockEntities;
import net.findsnow.mobsnplenty.common.registry.MNPSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

public class MNPChomperBlock extends BaseEntityBlock {

	public static final MapCodec<MNPChomperBlock> CODEC = simpleCodec(MNPChomperBlock::new);
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty CHOMPING = BooleanProperty.create("chomping");

	public MNPChomperBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(CHOMPING, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, CHOMPING);
	}

	// Block Entity

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MNPChomperBE(pos, state);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(CHOMPING)) {
			if (random.nextInt(4) == 0) {
				float pitch = 0.95F + random.nextFloat() * 0.1F;
				level.playLocalSound(
						pos.getX() + 0.5D,
						pos.getY() + 0.5D,
						pos.getZ() + 0.5D,
						MNPSounds.CHOMPER.get(), SoundSource.BLOCKS, 0.8F, pitch, false);
			}
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof MNPChomperBE chomperBE) {
				MenuProvider menuProvider = new SimpleMenuProvider(chomperBE, Component.literal("Chomper"));
				player.openMenu(menuProvider, pos);
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
		return InteractionResult.SUCCESS;
	}


	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide()) {
			return null;
		}
		return createTickerHelper(blockEntityType, MNPBlockEntities.CHOMPER_BE.get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
	}
}
