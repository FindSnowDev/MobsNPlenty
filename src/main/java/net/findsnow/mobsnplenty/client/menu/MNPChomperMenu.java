package net.findsnow.mobsnplenty.client.menu;

import net.findsnow.mobsnplenty.common.block.MNPChomperBlock;
import net.findsnow.mobsnplenty.common.blockentities.MNPChomperBE;
import net.findsnow.mobsnplenty.common.registry.MNPBlocks;
import net.findsnow.mobsnplenty.common.registry.MNPMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class MNPChomperMenu extends AbstractContainerMenu {

	public final MNPChomperBE blockEntity;
	public final Level level;
	public final ContainerData containerData;

	public MNPChomperMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
		this(containerId, inventory, inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(2));
	}

	public MNPChomperMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData containerData) {
		super(MNPMenuTypes.CHOMPER_MENU.get(), containerId);
		this.blockEntity = ((MNPChomperBE) blockEntity);
		this.level = inventory.player.level();
		this.containerData = containerData;

		addPlayerInventory(inventory);
		addPlayerHotbar(inventory);

		this.addSlot(new SlotItemHandler(this.blockEntity.itemStackHandler, 0, 50, 33));
		this.addSlot(new SlotItemHandler(this.blockEntity.itemStackHandler, 1, 110, 35));

		addDataSlots(containerData);
	}

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int TE_INVENTORY_SLOT_COUNT = 2;

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot sourceSlot = slots.get(index);
		if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
					+ TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.out.println("Invalid slotIndex:" + index);
			return ItemStack.EMPTY;
		}
		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(player, sourceStack);
		return copyOfSourceStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, MNPBlocks.CHOMPER.get());
	}

	private void addPlayerInventory(Inventory playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
			}
		}
	}

	private void addPlayerHotbar(Inventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	public boolean isCrafting() {
		return containerData.get(0) > 0;
	}

	public int getScaledArrowProgress() {
		int progress = this.containerData.get(0);
		int maxProgress = this.containerData.get(1);
		int arrowPixelSide = 24;

		return maxProgress != 0 && progress != 0 ? progress * arrowPixelSide / maxProgress : 0;
	}
}
