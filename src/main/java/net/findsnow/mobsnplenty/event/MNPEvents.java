package net.findsnow.mobsnplenty.event;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.entity.custom.CrabEntity;
import net.findsnow.mobsnplenty.registry.MNPEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Mobsnplenty.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MNPEvents {

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(MNPEntities.CRAB.get(), CrabEntity.setAttributes());
	}
}
