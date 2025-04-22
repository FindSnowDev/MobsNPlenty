package net.findsnow.mobsnplenty.common.event;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.particle.FlyParticle;
import net.findsnow.mobsnplenty.common.registry.MNPParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = Mobsnplenty.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MNPClientSetupEvents {


	@SubscribeEvent
	public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MNPParticleTypes.FLY.get(), sprites
				-> (simpleParticleType, clientLevel, d, e, f, g, h, i)
				-> new FlyParticle(clientLevel, d, e, f, g, h, i, sprites));
	}

}
