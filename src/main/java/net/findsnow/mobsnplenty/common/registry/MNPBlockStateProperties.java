package net.findsnow.mobsnplenty.common.registry;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class MNPBlockStateProperties {
	public static final IntegerProperty FUNGI_STAGE = IntegerProperty.create("shelf_fungus_stage", 1, 4);
	public static final BooleanProperty VENUS_FLYTRAP_SHUT = BooleanProperty.create("venus_flytrap_shut");
	public static final BooleanProperty VENUS_FLYTRAP_HAS_FLY = BooleanProperty.create("venus_flytrap_has_fly");
}
