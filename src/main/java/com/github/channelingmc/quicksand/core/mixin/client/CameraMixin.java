package com.github.channelingmc.quicksand.core.mixin.client;

import com.github.channelingmc.quicksand.api.access.QuicksandFogCamera;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.block.QuicksandBlock;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public class CameraMixin implements QuicksandFogCamera {
    
    @Shadow private BlockGetter level;
    
    @Shadow @Final private BlockPos.MutableBlockPos blockPosition;
    
    @Unique
    private int quicksand$fogColor = -1;
    
    @ModifyVariable(
        method = "getFluidInCamera",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/BlockGetter;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;",
            ordinal = 1
        ),
        name = "blockpos",
        index = 6
    )
    private BlockPos getFluidInCamera$updateQuicksandFog(BlockPos blockpos) {
        if (QuicksandConfigs.CLIENT.quicksandRenderFog.get()) {
            BlockState state = level.getBlockState(blockPosition);
            if (state.getBlock() instanceof QuicksandBlock quicksand) {
                quicksand$fogColor = quicksand.getDustColor(state, level, blockPosition);
                return blockpos;
            }
        }
        quicksand$fogColor = -1;
        return blockpos;
    }
    
    @ModifyExpressionValue(method = "getFluidInCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean getFluidInCamera$returnPowderSnowLikeFog(boolean original) {
        return original || quicksand$fogColor > -1;
    }
    
    @Override
    public int getQuicksandFogColor() {
        return quicksand$fogColor;
    }
    
}
