package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandItems {
    
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            helper.register(QuicksandAPI.QUICKSAND_BUCKET.getId(), new SolidBucketItem(
                QuicksandAPI.QUICKSAND_BLOCK.get(),
                SoundEvents.SAND_PLACE,
                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC))
            );
            helper.register(QuicksandAPI.RED_QUICKSAND_BUCKET.getId(), new SolidBucketItem(
                QuicksandAPI.RED_QUICKSAND_BLOCK.get(),
                SoundEvents.SAND_PLACE,
                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC))
            );
        });
    }
    
}
