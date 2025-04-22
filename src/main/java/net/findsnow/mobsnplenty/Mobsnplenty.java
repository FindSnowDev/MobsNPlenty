package net.findsnow.mobsnplenty;

import com.mojang.logging.LogUtils;
import net.findsnow.mobsnplenty.client.renderer.CrabRenderer;
import net.findsnow.mobsnplenty.client.renderer.SharkRenderer;
import net.findsnow.mobsnplenty.client.screen.MNPChomperScreen;
import net.findsnow.mobsnplenty.common.registry.*;
import net.findsnow.mobsnplenty.common.worldgen.generation.MNPBiomePlacements;
import net.minecraft.client.particle.AshParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.NoteParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Mobsnplenty.MOD_ID)
public class Mobsnplenty {
	public static final String MOD_ID = "mobsnplenty";
	private static final Logger LOGGER = LogUtils.getLogger();

	public Mobsnplenty(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);

		MNPBlocks.register(modEventBus);
		MNPBlockEntities.register(modEventBus);
		MNPItems.register(modEventBus);
		MNPCreativeTab.register(modEventBus);
		MNPFeatures.register(modEventBus);
		MNPBiomes.register(modEventBus);
		MNPBiomePlacements.register();
		MNPFoliagePlacers.register(modEventBus);
		MNPEntities.register(modEventBus);
		MNPMenuTypes.register(modEventBus);
		MNPParticleTypes.register(modEventBus);
		MNPSounds.register(modEventBus);

		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::addCreative);
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() ->{
			MNPFlammables.register();
			MNPottable.register();
		});
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {

	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			EntityRenderers.register(MNPEntities.CRAB.get(), CrabRenderer::new);
			EntityRenderers.register(MNPEntities.SHARK.get(), SharkRenderer::new);
		}

		@SubscribeEvent
		public static void registerColoredBlocks(RegisterColorHandlersEvent.Block event) {
			event.register((state, level, pos, tintIndex) -> level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.get(0.5D, 1),
					MNPBlocks.SWITCH_GRASS.get(),
					MNPBlocks.RED_SWITCH_GRASS.get());
		}

		@SubscribeEvent
		public static void registerColoredItems(RegisterColorHandlersEvent.Item event) {

		}

		@SubscribeEvent
		public static void registerScreens(RegisterMenuScreensEvent event) {
			event.register(MNPMenuTypes.CHOMPER_MENU.get(), MNPChomperScreen::new);
		}
	}
}