package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandItems {
    
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, QuicksandAPI.ID);
	
	public static final RegistryObject<Item> QUICKSAND_BUCKET = ITEMS.register("quicksand_bucket", () -> new SolidBucketItem(
			QuicksandBlocks.QUICKSAND.get(),
            SoundEvents.SAND_PLACE,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> RED_QUICKSAND_BUCKET = ITEMS.register("quicksand_bucket", () -> new SolidBucketItem(
            QuicksandBlocks.RED_QUICKSAND.get(),
            SoundEvents.SAND_PLACE,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    
	@SubscribeEvent
	public void OnTabPopulate(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			ITEMS.getEntries().forEach((ro) -> ro.ifPresent(event::accept));
		}
	}
	
}
