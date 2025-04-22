package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.entity.Crab;
import net.findsnow.mobsnplenty.common.entity.Shark;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(Registries.ENTITY_TYPE, Mobsnplenty.MOD_ID);

	public static final Supplier<EntityType<Crab>> CRAB =
			ENTITY_TYPES.register("crab", () -> EntityType.Builder.of(Crab::new, MobCategory.CREATURE)
					.sized(0.5F, 0.5F)
					.build("crab"));

	public static final Supplier<EntityType<Shark>> SHARK =
			ENTITY_TYPES.register("shark", () -> EntityType.Builder.of(Shark::new, MobCategory.WATER_CREATURE)
					.sized(1.4F, 1.3F)
					.build("shark"));

	public static void register(IEventBus eventBus) {
		ENTITY_TYPES.register(eventBus);
	}
}
