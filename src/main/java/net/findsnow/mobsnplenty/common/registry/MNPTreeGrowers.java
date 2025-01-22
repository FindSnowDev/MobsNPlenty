package net.findsnow.mobsnplenty.common.registry;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class MNPTreeGrowers {
	public static final TreeGrower LUCI = new TreeGrower(
			"luci",
			0.2f,
			Optional.empty(),
			Optional.empty(),
			Optional.of(MNPFeatures.LUCI),
			Optional.of(MNPFeatures.LARGE_LUCI),
			Optional.empty(),
			Optional.empty()
	);
}
