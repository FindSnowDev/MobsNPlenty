package net.findsnow.mobsnplenty.common.blockentities;

import net.findsnow.mobsnplenty.client.menu.MNPChomperMenu;
import net.findsnow.mobsnplenty.common.block.MNPChomperBlock;
import net.findsnow.mobsnplenty.common.registry.MNPBlockEntities;
import net.findsnow.mobsnplenty.common.registry.MNPBlocks;
import net.findsnow.mobsnplenty.common.registry.MNPItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class MNPChomperBE extends BlockEntity implements MenuProvider {

	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;

	private final ContainerData containerData;
	private int progress = 0;
	private int maxProgress = 72;
	private final int DEFAULT_MAX_PROGRESS = 72;

	public  final ItemStackHandler itemStackHandler = new ItemStackHandler(2);

	public MNPChomperBE(BlockPos pos, BlockState blockState) {
		super(MNPBlockEntities.CHOMPER_BE.get(), pos, blockState);
		this.containerData = new ContainerData() {
			@Override
			public int get(int index) {
				return switch(index) {
					case 0 -> MNPChomperBE.this.progress;
					case 1 -> MNPChomperBE.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch(index) {
                    case 0 -> MNPChomperBE.this.progress = value;
                    case 1 -> MNPChomperBE.this.maxProgress = value;
                };
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("blockentity.mobsnplenty.chomper");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new MNPChomperMenu(containerId, playerInventory, this, this.containerData);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		tag.put("inventory", itemStackHandler.serializeNBT(registries));
		tag.putInt("chomper.progress", progress);
		tag.putInt("chomper.maxProgress", maxProgress);
		super.saveAdditional(tag, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
		progress = tag.getInt("chomper.progress");
		maxProgress = tag.getInt("chomper.maxProgress");
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
			inventory.setItem(i, itemStackHandler.getStackInSlot(i));
		}
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}

	public void tick(Level level, BlockPos pos, BlockState state) {
		if (hasRecipe() && isOutputSlotEmpty()) {
			increaseCraftingProgress();
			setChanged(level, pos, state);
			if (!state.getValue(MNPChomperBlock.CHOMPING)) {
				level.setBlock(pos, state.setValue(MNPChomperBlock.CHOMPING, true), 3);
			}
			if (hasCraftingFinished()) {
				craftItem();
				resetProgress();
				level.setBlock(pos, state.setValue(MNPChomperBlock.CHOMPING, false), 3);
			}
		} else if (state.getValue(MNPChomperBlock.CHOMPING)) {
			resetProgress();
			level.setBlock(pos, state.setValue(MNPChomperBlock.CHOMPING, false), 3);
		}
	}

	private void resetProgress() {
		this.progress = 0;
		this.maxProgress = DEFAULT_MAX_PROGRESS;
	}

	private void craftItem() {
		ItemStack output = new ItemStack(Items.STRING);
		itemStackHandler.extractItem(INPUT_SLOT, 1, false);
		itemStackHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(), itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
	}

	private boolean hasCraftingFinished() {
		return this.progress >= this.maxProgress;
	}

	private void increaseCraftingProgress() {
		progress++;
	}

	private boolean isOutputSlotEmpty() {
		return itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
				this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
	}

	private boolean hasRecipe() {
		ItemStack input = new ItemStack(MNPItems.CRAB_CLAW.get());
		ItemStack output = new ItemStack(Items.STRING);

		return canInstertAmountIntoOutput(output.getCount()) && canInstertItemIntoOutput(output) && 
				this.itemStackHandler.getStackInSlot(INPUT_SLOT).getItem() == input.getItem();
	}

	private boolean canInstertItemIntoOutput(ItemStack output) {
		return itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
				itemStackHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
	}

	private boolean canInstertAmountIntoOutput(int count) {
		int maxCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
		int currentCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount();

		return maxCount >= currentCount + count;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveWithoutMetadata(registries);
	}
}
