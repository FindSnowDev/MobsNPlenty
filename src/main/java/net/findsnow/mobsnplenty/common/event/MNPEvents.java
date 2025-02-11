package net.findsnow.mobsnplenty.common.event;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.model.CrabModel;
import net.findsnow.mobsnplenty.common.entity.Crab;
import net.findsnow.mobsnplenty.common.registry.MNPEntities;
import net.findsnow.mobsnplenty.common.registry.MNPModelLayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Mobsnplenty.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MNPEvents {

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(MNPEntities.CRAB.get(), Crab.setAttributes());
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(MNPModelLayers.CRAB, CrabModel::createBodyLayer);
	}
}
