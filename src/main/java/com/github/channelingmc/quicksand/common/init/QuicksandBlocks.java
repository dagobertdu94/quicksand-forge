package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.block.QuicksandBlock;
import com.github.channelingmc.quicksand.common.block.QuicksandCauldronBlock;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandBlocks {
    
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, QuicksandAPI.ID);
	
	public static final RegistryObject<Block> QUICKSAND = BLOCKS.register("quicksand", () -> new QuicksandBlock(
                BlockBehaviour.Properties.copy(Blocks.SAND),
                14406560,
                QuicksandAPI.QUICKSAND_BUCKET));
	public static final RegistryObject<Block> RED_QUICKSAND = BLOCKS.register("red_quicksand", () -> new QuicksandBlock(
            BlockBehaviour.Properties.copy(Blocks.RED_SAND),
            11098145,
            QuicksandAPI.RED_QUICKSAND_BUCKET));
	public static final RegistryObject<Block> QUICKSAND_CAULDRON = BLOCKS.register("quicksand_cauldron", () -> new QuicksandCauldronBlock(
            BlockBehaviour.Properties.copy(Blocks.CAULDRON),
            QuicksandAPI.QUICKSAND_BUCKET,
            QuicksandAPI.QUICKSAND_INTERACTIONS));
	public static final RegistryObject<Block> RED_QUICKSAND_CAULDRON = BLOCKS.register("red_quicksand", () -> new QuicksandCauldronBlock(
            BlockBehaviour.Properties.copy(Blocks.CAULDRON),
            QuicksandAPI.RED_QUICKSAND_BUCKET,
            QuicksandAPI.RED_QUICKSAND_INTERACTIONS));
    
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((QuicksandCauldronBlock)QUICKSAND_CAULDRON.get()).registerEmptyFillInteractions();
            ((QuicksandCauldronBlock)RED_QUICKSAND_CAULDRON.get()).registerEmptyFillInteractions();
            if (QuicksandConfigs.COMMON.quicksandRenewable.get()) {
                CauldronInteraction.WATER.put(Items.SAND, ((QuicksandCauldronBlock)QUICKSAND_CAULDRON.get())::renewFromSand);
                CauldronInteraction.WATER.put(Items.RED_SAND, ((QuicksandCauldronBlock)RED_QUICKSAND_CAULDRON.get())::renewFromSand);
            }
        });
    }
    
}
