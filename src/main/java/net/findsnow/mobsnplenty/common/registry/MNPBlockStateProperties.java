package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNPBlockStateProperties {
	public static final IntegerProperty FUNGI_STAGE = IntegerProperty.create("shelf_fungus_stage", 1, 4);

}
