package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.common.entity.Crab;
import net.minecraft.core.registries.Registries;
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



	public static void register(IEventBus eventBus) {
		ENTITY_TYPES.register(eventBus);
	}
}
