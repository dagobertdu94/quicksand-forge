package com.github.channelingmc.quicksand;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.init.QuicksandBlocks;
import com.github.channelingmc.quicksand.common.init.QuicksandItems;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(QuicksandAPI.ID)
public class Quicksand {
	
	public Quicksand() {
		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.CLIENT, QuicksandConfigs.CLIENT_SPEC);
		context.registerConfig(ModConfig.Type.COMMON, QuicksandConfigs.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.SERVER, QuicksandConfigs.SERVER_SPEC);
		
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		modBus.register(QuicksandItems.ITEMS);
		modBus.register(QuicksandBlocks.BLOCKS);
	}
	
}
