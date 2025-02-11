package com.github.channelingmc.quicksand.core.mixin.common;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.api.access.QuicksandConvertableEntity;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Drowned.class)
public abstract class DrownedMixin extends Zombie implements QuicksandConvertableEntity {
    
    private DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public boolean convertsInQuicksand() {
        return QuicksandConfigs.SERVER.quicksandConvertsDrowned.get();
    }
    
    @Override
    public void doQuicksandConversion() {
        this.convertTo(EntityType.ZOMBIE, true);
        this.playSound(QuicksandAPI.DROWNED_CONVERTS_TO_ZOMBIE.get(), 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
    }
    
}
