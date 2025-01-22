package net.findsnow.mobsnplenty.common.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class MNPFlammables {
	public static void register() {
		FireBlock fireBlock = (FireBlock) Blocks.FIRE;

		fireBlock.setFlammable(MNPBlocks.SWITCH_GRASS.get(), 30, 60);
		fireBlock.setFlammable(MNPBlocks.RED_SWITCH_GRASS.get(), 30, 60);


		fireBlock.setFlammable(MNPBlocks.LUCI_LEAVES.get(), 30, 60);
		fireBlock.setFlammable(MNPBlocks.LUCI_PLANKS.get(), 5, 20);
		fireBlock.setFlammable(MNPBlocks.LUCI_LOG.get(), 5, 5);
	}
}
