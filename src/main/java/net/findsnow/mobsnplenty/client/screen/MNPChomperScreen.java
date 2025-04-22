package net.findsnow.mobsnplenty.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.findsnow.mobsnplenty.Mobsnplenty;
import net.findsnow.mobsnplenty.client.menu.MNPChomperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MNPChomperScreen extends AbstractContainerScreen<MNPChomperMenu> {

	public static final ResourceLocation GUI_TEX = ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/gui/chomper/chomper_gui.png");
	public static final ResourceLocation ARROW_TEX = ResourceLocation.fromNamespaceAndPath(Mobsnplenty.MOD_ID, "textures/gui/chomper/chomper_progress.png");

	public MNPChomperScreen(MNPChomperMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}


	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		guiGraphics.blit(GUI_TEX, x, y, 0, 0, imageWidth, imageHeight);
		renderProgressArrow(guiGraphics, x, y);
	}

	private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
		if (menu.isCrafting()) {
			guiGraphics.blit(ARROW_TEX, x + 72,  y + 27, 0, 0, menu.getScaledArrowProgress(), 33, 25, 33);
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}
}
