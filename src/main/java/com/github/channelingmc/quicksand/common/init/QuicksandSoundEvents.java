package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandSoundEvents {
    
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> {
            helper.register(QuicksandAPI.ZOMBIE_CONVERTS_TO_HUSK.getId(),
                new SoundEvent(QuicksandAPI.ZOMBIE_CONVERTS_TO_HUSK.getId())
            );
            helper.register(QuicksandAPI.DROWNED_CONVERTS_TO_ZOMBIE.getId(),
                new SoundEvent(QuicksandAPI.DROWNED_CONVERTS_TO_ZOMBIE.getId())
            );
        });
    }
    
}
