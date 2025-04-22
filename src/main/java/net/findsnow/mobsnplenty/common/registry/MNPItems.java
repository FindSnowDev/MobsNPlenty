package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MNPItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mobsnplenty.MOD_ID);

	// MNP Food
	public static final DeferredItem<Item> FROG_LEG = ITEMS.registerSimpleItem("frog_leg");
	public static final DeferredItem<Item> COOKED_FROG_LEG = ITEMS.registerSimpleItem("cooked_frog_leg");
	public static final DeferredItem<Item> SHARK_MEAT = ITEMS.registerSimpleItem("shark_meat");
	public static final DeferredItem<Item> COOKED_SHARK_MEAT = ITEMS.registerSimpleItem("cooked_shark_meat");
	public static final DeferredItem<Item> RAW_SHRIMP = ITEMS.registerSimpleItem("raw_shrimp");
	public static final DeferredItem<Item> COOKED_SHRIMP = ITEMS.registerSimpleItem("cooked_shrimp");

	// MNP Mob Drops
	public static final DeferredItem<Item> CRAB_CLAW = ITEMS.registerSimpleItem("crab_claw");
	public static final DeferredItem<Item> SHRIMP = ITEMS.registerSimpleItem("shrimp");
	public static final DeferredItem<Item> SCUTE = ITEMS.registerSimpleItem("scute");

	// MNP Items
	public static final DeferredItem<Item> FROSTITE = ITEMS.registerSimpleItem("frostite");
	public static final DeferredItem<Item> FROSTITE_NUGGET = ITEMS.registerSimpleItem("frostite_nugget");
	public static final DeferredItem<Item> NEPHRITE = ITEMS.registerSimpleItem("nephrite");
	public static final DeferredItem<Item> NEPHRITE_NUGGET = ITEMS.registerSimpleItem("nephrite_nugget");

	// MNP Spawn Eggs
	public static final DeferredItem<Item> CRAB_SPAWN_EGG = ITEMS.register("crab_spawn_egg",
			() -> new DeferredSpawnEggItem(MNPEntities.CRAB, 0x561414, 0x9f2922, new Item.Properties()));

	public static final DeferredItem<Item> SHARK_SPAWN_EGG = ITEMS.register("shark_spawn_egg",
			() -> new DeferredSpawnEggItem(MNPEntities.SHARK, 0x561414, 0x9f2922, new Item.Properties()));





	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
