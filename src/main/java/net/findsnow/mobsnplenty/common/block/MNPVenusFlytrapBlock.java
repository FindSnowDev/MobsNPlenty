package net.findsnow.mobsnplenty.common.block;

import net.findsnow.mobsnplenty.common.registry.MNPBlockSetTypes;
import net.findsnow.mobsnplenty.common.registry.MNPBlockStateProperties;
import net.findsnow.mobsnplenty.common.registry.MNPParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class MNPVenusFlytrapBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty SHUT = MNPBlockStateProperties.VENUS_FLYTRAP_SHUT;
	public static final BooleanProperty HAS_FLY = MNPBlockStateProperties.VENUS_FLYTRAP_HAS_FLY;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

	private static final int PLAYER_SHUT_TIME = 200;
	private static final int FLY_ATTRACTION_TIME = 200;
	private static final int FLY_DIGESTION_TIME = 1200;

	public MNPVenusFlytrapBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(getStateDefinition().any()
				.setValue(WATERLOGGED, Boolean.FALSE)
				.setValue(FACING, Direction.NORTH)
				.setValue(SHUT, Boolean.FALSE)
				.setValue(HAS_FLY, Boolean.FALSE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SHUT, WATERLOGGED, FACING, HAS_FLY);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}


	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {


		if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}

		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity) {
			entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));

			if (!level.isClientSide() && entity instanceof Player && !state.getValue(SHUT)) {
				shutTrap(level, pos, state, PLAYER_SHUT_TIME);
			}
		}
	}

	private void shutTrap(Level level, BlockPos pos, BlockState state, int playerShutTime) {
		level.setBlock(pos, state.setValue(SHUT, Boolean.TRUE),3);
		level.playSound(null, pos, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, SoundSource.BLOCKS, 1.0F, 1.0F);

		if (level instanceof ServerLevel) {
			level.scheduleTick(pos, this, playerShutTime);
		}
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(SHUT)) {
			if (!state.getValue(HAS_FLY)) {
				level.setBlock(pos, state.setValue(SHUT, Boolean.FALSE), 3);
				level.playSound(null, pos, SoundEvents.BIG_DRIPLEAF_TILT_UP, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.scheduleTick(pos, this, getRandomFlyCheckTime(random));
			} else {
				level.setBlock(pos, state.setValue(SHUT, Boolean.FALSE).setValue(HAS_FLY, Boolean.FALSE), 3);
				level.playSound(null, pos, SoundEvents.BIG_DRIPLEAF_TILT_UP, SoundSource.BLOCKS, 1.0F, 1.0F);

				level.scheduleTick(pos, this, getRandomFlyCheckTime(random));
			}
		} else if (state.getValue(HAS_FLY)) {
			shutTrap(level, pos, state, FLY_DIGESTION_TIME);
		} else {
			if (random.nextInt(10) < 8) {
				level.setBlock(pos, state.setValue(HAS_FLY, Boolean.TRUE), 3);

				level.scheduleTick(pos, this, FLY_ATTRACTION_TIME);
			} else {
				level.scheduleTick(pos, this, getRandomFlyCheckTime(random));
			}
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.isClientSide() && state.getValue(HAS_FLY) && !state.getValue(SHUT)) {
			double x = pos.getX() + 0.5D;
			double y = pos.getY() + 0.5D;
			double z = pos.getZ() + 0.5D;

			for (int i = 0; i < random.nextInt(3) + 1; i++) {
				double offsetX = (random.nextDouble() * 0.6D - 0.3D);
				double offsetY = (random.nextDouble() * 0.6D - 0.0D);
				double offsetZ = (random.nextDouble() * 0.6D - 0.3D);

				level.addParticle(MNPParticleTypes.FLY.get(), x + offsetX, y + offsetY, z + offsetZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	private int getRandomFlyCheckTime(RandomSource random) {
		return 600 + random.nextInt(1200);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(SHUT, false);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = level.getBlockState(blockPos);

		return !blockState.is(this) && blockState.isFaceSturdy(level, blockPos, Direction.UP) && super.canSurvive(state, level, pos);
	}
}
