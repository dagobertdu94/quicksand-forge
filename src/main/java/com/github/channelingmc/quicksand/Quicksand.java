package com.github.channelingmc.quicksand;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(QuicksandAPI.ID)
public class Quicksand {
	
	public Quicksand() {
		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.CLIENT, QuicksandConfigs.CLIENT_SPEC);
		context.registerConfig(ModConfig.Type.COMMON, QuicksandConfigs.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.SERVER, QuicksandConfigs.SERVER_SPEC);
	}
	
}
