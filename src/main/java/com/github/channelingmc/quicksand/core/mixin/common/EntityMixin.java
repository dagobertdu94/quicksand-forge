package com.github.channelingmc.quicksand.core.mixin.common;

import com.github.channelingmc.quicksand.api.access.QuicksandSubmergingEntity;
import com.github.channelingmc.quicksand.api.tag.QuicksandTags;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, priority = 900)
public abstract class EntityMixin implements QuicksandSubmergingEntity {
    
    @Shadow public abstract Level getLevel();
    
    @Shadow public abstract int getBlockX();
    
    @Shadow public abstract double getEyeY();
    
    @Shadow public abstract int getBlockZ();
    
    @Shadow public abstract BlockPos getOnPos();
    
    @Shadow public Level level;
    private boolean submergedInQuicksand;
    
    @Override
    public boolean isSubmergedInQuicksand() {
        return this.submergedInQuicksand;
    }
    
    @Override
    public void updateSubmergedInQuicksand() {
        this.submergedInQuicksand = this.getLevel()
            .getBlockState(new BlockPos(this.getBlockX(), this.getEyeY() - 0.11111111F, this.getBlockZ()))
            .is(QuicksandTags.QUICKSAND);
    }
    
    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidOnEyes()V"))
    private void baseTick$updateSubmergedInQuicksand(CallbackInfo ci) {
        this.updateSubmergedInQuicksand();
    }
    
    @ModifyExpressionValue(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean move$checkIsOnQuicksand(boolean original) {
        if (original) return true;
        BlockPos blockpos = this.getOnPos();
        BlockState blockstate = this.level.getBlockState(blockpos);
        return blockstate.is(QuicksandTags.QUICKSAND);
    }
    
    @ModifyExpressionValue(method = "move", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/world/entity/Entity;isInPowderSnow:Z"))
    private boolean move$checkIsInQuicksand(boolean original) {
        return original || (this.submergedInQuicksand && QuicksandConfigs.SERVER.quicksandExtinguishesFire.get());
    }
    
}
