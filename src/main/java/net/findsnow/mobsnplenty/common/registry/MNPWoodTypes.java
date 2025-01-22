package net.findsnow.mobsnplenty.common.registry;

import net.findsnow.mobsnplenty.Mobsnplenty;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import static net.minecraft.world.level.block.state.properties.WoodType.register;

public class MNPWoodTypes {
	public static final WoodType LUCI = register(new WoodType(Mobsnplenty.MOD_ID + ":luci", MNPBlockSetTypes.LUCI));
}
