package net.findsnow.mobsnplenty.common.block;

import com.mojang.serialization.MapCodec;
import net.findsnow.mobsnplenty.common.registry.MNPBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * All Credit For Base Of This Class Belongs To The FrozenBlock Team
 * https://modrinth.com/organization/frozenblock
 * https://modrinth.com/mod/wilder-wild
 * https://discord.com/invite/EpkBWm844s || FrozenBlock Modding Oasis
 * If any member from FrozenBlock has an issue with this being used, let me know and I will remove it!
 */

public class MNPLuciFungiShelf extends FaceAttachedHorizontalDirectionalBlock implements BonemealableBlock {
	public static final int GROWTH_BRIGHTNESS_OFFSET = 2;
	public static final int MAX_STAGE = 4;
	public static final int MAX_AGE = 2;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	public static final IntegerProperty STAGE = MNPBlockStateProperties.FUNGI_STAGE;
	public static final MapCodec<MNPLuciFungiShelf> CODEC = simpleCodec(MNPLuciFungiShelf::new);
	protected static final VoxelShape NORTH_WALL_SHAPE = Block.box(0D, 0D, 13D, 16D, 16D, 16D);
	protected static final VoxelShape SOUTH_WALL_SHAPE = Block.box(0D, 0D, 0D, 16D, 16D, 3D);
	protected static final VoxelShape WEST_WALL_SHAPE = Block.box(13D, 0D, 0D, 16D, 16D, 16D);
	protected static final VoxelShape EAST_WALL_SHAPE = Block.box(0D, 0D, 0D, 3D, 16D, 16D);
	protected static final VoxelShape FLOOR_SHAPE = Block.box(0D, 0D, 0D, 16D, 3D, 16D);
	protected static final VoxelShape CEILING_SHAPE = Block.box(0D, 13D, 0D, 16D, 16D, 16D);

	public MNPLuciFungiShelf(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(FACE, AttachFace.WALL)
				.setValue(STAGE, 1));
	}

	@Override
	protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		return super.isRandomlyTicking(state);
	}

	@NotNull
	public static AttachFace getFace(@NotNull Direction direction) {
		if (direction.getAxis() == Direction.Axis.Y) {
			return direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR;
		}
		return AttachFace.WALL;
	}

	@NotNull
	private static boolean isFullyGrown(@NotNull BlockState state) {
		return state.getValue(STAGE) == MAX_STAGE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(Items.SHEARS) && onShear(level, pos, state, player)) {
			stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		}
	}

	public static boolean onShear(Level level, BlockPos pos, BlockState state, Entity entity) {
		int stage = state.getValue(STAGE);
		if (stage > 1) {
			popResource(level, pos, new ItemStack(state.getBlock()));
			level.setBlockAndUpdate(pos, state.setValue(STAGE, stage - 1));
			level.playSound(null, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1F, 1F);
			level.gameEvent(entity, GameEvent.SHEAR, pos);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACE, FACING, AGE, STAGE);
	}


	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(STAGE) < MAX_STAGE || super.canBeReplaced(state, useContext);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState insideState = context.getLevel().getBlockState(context.getClickedPos());
		if (insideState.is(this)) {
			return insideState.setValue(STAGE, Math.min(MAX_STAGE, insideState.getValue(STAGE) + 1));
		}
		for (Direction direction : context.getNearestLookingDirections()) {
			BlockState state;
			if (direction.getAxis() == Direction.Axis.Y) {
				state = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection());
			} else {
				state = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
			}
			if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
				return state;
			}
		}
		return null;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACE)) {
			case FLOOR -> FLOOR_SHAPE;
			case WALL -> switch (state.getValue(FACING)) {
				case EAST -> EAST_WALL_SHAPE;
				case WEST -> WEST_WALL_SHAPE;
				case SOUTH -> SOUTH_WALL_SHAPE;
				default -> NORTH_WALL_SHAPE;
			};
			case CEILING -> CEILING_SHAPE;
		};
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(AGE) == MAX_AGE;
	}

	@Override
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if (random.nextInt(level.getMaxLocalRawBrightness(pos) + GROWTH_BRIGHTNESS_OFFSET) == 1) {
			if (!isMaxAge(state)) {
				level.setBlock(pos, state.cycle(AGE), UPDATE_CLIENTS);
			} else if (!isFullyGrown(state)) {
				level.setBlock(pos, state.cycle(STAGE).setValue(AGE, 0), UPDATE_CLIENTS);
			}
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		return !isFullyGrown(blockState);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		serverLevel.setBlock(blockPos, blockState.cycle(STAGE).setValue(AGE, 0), UPDATE_CLIENTS);
	}

	@Override
	public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
		super.destroy(level, pos, state);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.playerDestroy(level, player, pos, state, blockEntity, tool);
	}
}
