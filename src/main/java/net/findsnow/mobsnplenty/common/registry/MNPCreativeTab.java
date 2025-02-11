package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPCreativeTab {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mobsnplenty.MOD_ID);

	public static final Supplier<CreativeModeTab> MNP_TABS =
			CREATIVE_MODE_TABS.register("mnp_tabs", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.mobsnplenty.mnp_tabs"))
					.icon(() -> new ItemStack(MNPBlocks.LUCI_PLANKS.get().asItem()))
					.displayItems((itemDisplayParameters, output) -> {
						output.accept(MNPBlocks.LUCI_PLANKS);
						output.accept(MNPBlocks.LUCI_LOG);
						output.accept(MNPBlocks.STRIPPED_LUCI_LOG);
						output.accept(MNPBlocks.LUCI_WOOD);
						output.accept(MNPBlocks.STRIPPED_LUCI_WOOD);
						output.accept(MNPBlocks.LUCI_LEAVES);
						output.accept(MNPBlocks.LUCI_SAPLING);
						output.accept(MNPBlocks.LUCI_SHELF_FUNGI);
						output.accept(MNPBlocks.ALKANET);
						output.accept(MNPBlocks.BLUE_FLAURELLE);
						output.accept(MNPBlocks.PURPLE_FLAURELLE);
						output.accept(MNPBlocks.RED_SWITCH_GRASS);
						output.accept(MNPBlocks.SWITCH_GRASS);
						output.accept(MNPBlocks.CLOVER_PATCH);

						output.accept(MNPItems.CRAB_SPAWN_EGG);

						output.accept(MNPItems.CRAB_CLAW);
					})
					.build());

	public static void register(IEventBus eventBus) {
		CREATIVE_MODE_TABS.register(eventBus);
	}
}
