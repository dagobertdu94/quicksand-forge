package com.github.channelingmc.quicksand.common.block;

import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.init.QuicksandBlocks;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class QuicksandCauldronBlock extends AbstractCauldronBlock {
    
    protected final Supplier<? extends Item> bucket;
    private final Map<Item, CauldronInteraction> inte;
    
    public QuicksandCauldronBlock(Properties properties, Supplier<? extends Item> bucket, Map<Item, CauldronInteraction> interactions) {
        super(properties, interactions);
        this.bucket = bucket;
        this.inte = interactions;
        
        registerEmptyFillInteractions();
    }
    
    @Override
    protected double getContentHeight(BlockState state) {
        return 0.9375D;
    }
    
    @Override
    public boolean isFull(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return 3;
    }
    
    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide &&
            QuicksandConfigs.SERVER.quicksandExtinguishesFire.get() &&
            pEntity.isOnFire() &&
            this.isEntityInsideContent(pState, pPos, pEntity))
        {
            pEntity.clearFire();
        }
    }
    
    @Nullable
    public Item getBucket() {
        return this.bucket.get();
    }
    
    public void registerEmptyFillInteractions() {
        this.inte.put(Items.BUCKET, (blockState, level, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(
                blockState, level, pos, player, hand, stack,
                this.bucket.get().getDefaultInstance(),
                x -> x.is(this),
                SoundEvents.SAND_BREAK
            ));
        CauldronInteraction.EMPTY.put(this.bucket.get(), (blockState, level, pos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(
                level, pos, player, hand, stack,
                this.defaultBlockState(), SoundEvents.SAND_PLACE
            ));
    }
    
    public static InteractionResult renewFromSand(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if (blockState.is(Blocks.WATER_CAULDRON) &&
            blockState.getValue(LayeredCauldronBlock.LEVEL) == 1)
        {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, (item == Items.RED_SAND ? QuicksandBlocks.RED_QUICKSAND_CAULDRON : QuicksandBlocks.QUICKSAND_CAULDRON).get().defaultBlockState());
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
}
