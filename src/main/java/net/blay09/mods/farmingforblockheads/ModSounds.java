package net.blay09.mods.farmingforblockheads;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	public static SoundEvent falling;

	public static void register(IForgeRegistry<SoundEvent> registry) {
		registry.registerAll(
				falling = newSoundEvent("falling")
		);
	}

	private static SoundEvent newSoundEvent(String name) {
		ResourceLocation location = new ResourceLocation(FarmingForBlockheads.MOD_ID, name);
		SoundEvent sound = new SoundEvent(location);
		sound.setRegistryName(location);
		return sound;
	}

}
