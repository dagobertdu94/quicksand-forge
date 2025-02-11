package com.github.channelingmc.quicksand.core.mixin.client;

import com.github.channelingmc.quicksand.api.access.QuicksandConvertableEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractZombieRenderer.class)
public abstract class AbstractZombieRendererMixin<T extends Zombie, M extends ZombieModel<T>> {
	
	@ModifyReturnValue(method = "isShaking(Lnet/minecraft/world/entity/monster/Zombie;)Z", at = @At("RETURN"))
	private boolean isShaking$modify(boolean original, T zombie) {
		return original || ((QuicksandConvertableEntity)zombie).isQuicksandConverting();
	}
	
}
