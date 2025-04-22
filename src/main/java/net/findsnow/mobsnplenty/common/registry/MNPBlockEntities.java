package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.block.MNPChomperBlock;
import net.findsnow.mobsnplenty.common.blockentities.MNPChomperBE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class MNPBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mobsnplenty.MOD_ID);

	public static final Supplier<BlockEntityType<MNPChomperBE>> CHOMPER_BE =
			BLOCK_ENTITIES.register("chomper_be", () -> BlockEntityType.Builder.of(
					MNPChomperBE::new, MNPBlocks.CHOMPER.get()).build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}
