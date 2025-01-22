package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.block.MNPBushBlock;
import net.findsnow.mobsnplenty.common.block.MNPLogBlock;
import net.findsnow.mobsnplenty.common.block.MNPLuciFungiShelf;
import net.findsnow.mobsnplenty.common.block.MNPPatchFoliageBlock;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Mobsnplenty.MOD_ID);
	
	// MNP Luci
	public static final DeferredBlock<Block> LUCI_PLANKS = registerBlock("luci_planks",
			() -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
	public static final DeferredBlock<Block> LUCI_LOG = registerBlock("luci_log",
			() -> new MNPLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
	public static final DeferredBlock<Block> LUCI_WOOD = registerBlock("luci_wood",
			() -> new MNPLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
	public static final DeferredBlock<Block> STRIPPED_LUCI_LOG = registerBlock("stripped_luci_log",
			() -> new MNPLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
	public static final DeferredBlock<Block> STRIPPED_LUCI_WOOD = registerBlock("stripped_luci_wood",
			() -> new MNPLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
	public static final DeferredBlock<Block> LUCI_LEAVES = registerBlock("luci_leaves",
			() -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
					.isViewBlocking((blockState, blockGetter, blockPos) -> false)
					.isSuffocating((blockState, blockGetter, blockPos) -> false)));
	public static final DeferredBlock<Block> LUCI_SAPLING = registerBlock("luci_sapling",
            () -> new SaplingBlock(MNPTreeGrowers.LUCI, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

	// MNP Plants
	public static final DeferredBlock<Block> LUCI_SHELF_FUNGI = registerBlock("luci_shelf_fungi",
			() -> new MNPLuciFungiShelf(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM_BLOCK)
					.lightLevel(state -> 1)
					.noCollission()
					.randomTicks()
					.noOcclusion()
					.sound(SoundType.MOSS)
					.pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> ALKANET = registerBlock("alkanet",
			() -> new FlowerBlock(MobEffects.WEAKNESS, 9.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TULIP)));
	public static final DeferredBlock<Block> BLUE_FLAURELLE = registerBlock("blue_flaurelle",
			() -> new FlowerBlock(MobEffects.WEAKNESS, 9.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TULIP)));
	public static final DeferredBlock<Block> PURPLE_FLAURELLE = registerBlock("purple_flaurelle",
			() -> new FlowerBlock(MobEffects.WEAKNESS, 9.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TULIP)));
	public static final DeferredBlock<Block> RED_SWITCH_GRASS = registerBlock("red_switch_grass",
			() -> new MNPBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
	public static final DeferredBlock<Block> SWITCH_GRASS = registerBlock("switch_grass",
			() -> new MNPBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
	public static final DeferredBlock<Block> CLOVER_PATCH = registerBlock("clover_patch",
			() -> new MNPPatchFoliageBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)
					.sound(SoundType.MOSS)
					.strength(1.0F)));


	// Helper
	private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
		DeferredBlock<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
		MNPItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
