package com.github.channelingmc.quicksand.core.mixin.common;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.api.access.QuicksandSubmergingEntity;
import com.github.channelingmc.quicksand.api.tag.QuicksandTags;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.block.QuicksandBlock;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements QuicksandSubmergingEntity {
	
	@Shadow
	protected boolean jumping;

	private LivingEntityMixin(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "handleRelativeFrictionAndCalculateMovement", at = @At("RETURN"), name = "vec3", index = 3)
	public Vec3 handleRelativeFrictionAndCalculateMovement$modifyMovementY(Vec3 original) {
		if ((this.horizontalCollision || this.jumping) &&
			this.getFeetBlockState().is(QuicksandTags.QUICKSAND) &&
			QuicksandBlock.canWalkOnQuicksand(this))
		{
			return new Vec3(original.x, 0.1D, original.z);
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidType;isAir()Z", remap = false))
	private boolean baseTick$checkSubmergedInQuicksand(boolean original) {
		return original && !this.isSubmergedInQuicksand();
	}
	
	@ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canDrownInFluidType(Lnet/minecraftforge/fluids/FluidType;)Z", remap = false))
	private boolean baseTick$checkDrownInQuicksand(boolean original) {
		return original || (
			this.isSubmergedInQuicksand() &&
			QuicksandConfigs.SERVER.quicksandDrownsEntities.get() &&
			!QuicksandBlock.canSurviveInQuicksand((LivingEntity)(Object)this)
		);
	}
	
	@ModifyExpressionValue(method = "baseTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/damagesource/DamageSource;DROWN:Lnet/minecraft/world/damagesource/DamageSource;"))
	private DamageSource baseTick$modifyQuicksandDrownDamageSource(DamageSource original) {
		return this.isSubmergedInQuicksand() ? new DamageSource(Holder.direct(level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(QuicksandAPI.QUICKSAND_DAMAGE).get())) : original;
	}
	
}
