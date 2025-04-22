package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.menu.MNPChomperMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPE =
			DeferredRegister.create(Registries.MENU, Mobsnplenty.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<MNPChomperMenu>> CHOMPER_MENU =
			registerMenuType("chomper_menu", MNPChomperMenu::new);


	private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
		return MENU_TYPE.register(name, () -> IMenuTypeExtension.create(factory));
	}


	public static void register(IEventBus eventBus) {
		MENU_TYPE.register(eventBus);
	}
}
