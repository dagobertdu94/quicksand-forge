package com.github.channelingmc.quicksand.core.mixin.common;

import com.github.channelingmc.quicksand.api.access.QuicksandConvertableEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Husk.class)
public abstract class HuskMixin extends Zombie implements QuicksandConvertableEntity {
	
	private HuskMixin(Level world) {
		super(world);
	}

	@Override
	public boolean convertsInQuicksand() {
		return false;
	}
	
}
