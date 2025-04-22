package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MNPParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
			DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Mobsnplenty.MOD_ID);

	public static final Supplier<SimpleParticleType> FLY = register("fly_particle");

	public static void register(IEventBus eventBus) {
		PARTICLE_TYPES.register(eventBus);
	}

	public static Supplier<SimpleParticleType> register(String name) {
		return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(false));
	}
}
