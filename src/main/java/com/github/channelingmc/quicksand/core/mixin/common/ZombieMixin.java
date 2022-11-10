package com.github.channelingmc.quicksand.core.mixin.common;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.api.access.QuicksandConvertableEntity;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements QuicksandConvertableEntity {
	
	@Shadow protected abstract void convertToZombieType(EntityType<? extends Zombie> pEntityType);
	
	private static final EntityDataAccessor<Boolean> DATA_QUICKSAND_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

	private int inQuicksandTime;
	private int quickSandConversionTime;

	private ZombieMixin(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public boolean isQuicksandConverting() {
		return this.getEntityData().get(DATA_QUICKSAND_CONVERSION_ID);
	}

	@Override
	public boolean convertsInQuicksand() {
		return this.getType() != EntityType.ZOMBIE || QuicksandConfigs.SERVER.quicksandConvertsZombie.get();
	}

	@Override
	public void startQuicksandConversion(int conversionTime) {
		this.quickSandConversionTime = conversionTime;
		this.getEntityData().set(DATA_QUICKSAND_CONVERSION_ID, true);
	}

	@Override
	public void doQuicksandConversion() {
		this.convertToZombieType(EntityType.HUSK);
		this.playSound(QuicksandAPI.ZOMBIE_CONVERTS_TO_HUSK.get(), 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
	}
	
	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void injectDataTracker(CallbackInfo ci) {
		this.getEntityData().define(DATA_QUICKSAND_CONVERSION_ID, false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void injectAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		nbt.putInt("InQuicksandTime", this.isSubmergedInQuicksand() ? this.inQuicksandTime : -1);
		nbt.putInt("QuicksandConversionTime", this.isQuicksandConverting() ? this.quickSandConversionTime : -1);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void injectReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
		this.inQuicksandTime = nbt.getInt("InQuicksandTime");
		if (nbt.contains("QuicksandConversionTime", Tag.TAG_ANY_NUMERIC) && nbt.getInt("QuicksandConversionTime") > -1) {
			this.startQuicksandConversion(nbt.getInt("QuicksandConversionTime"));
		}
	}

	@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;isUnderWaterConverting()Z"))
	private void injectTick(CallbackInfo ci) {
		if (this.convertsInQuicksand()) {
			if (this.isQuicksandConverting()) {
				--this.quickSandConversionTime;
				if (this.quickSandConversionTime < 0 &&
					ForgeEventFactory.canLivingConvert(this, EntityType.HUSK,
						timer -> this.quickSandConversionTime = timer))
				{
					this.doQuicksandConversion();
				}
			} else if (this.isSubmergedInQuicksand()) {
				++this.inQuicksandTime;
				if (this.inQuicksandTime >= 600) {
					this.startQuicksandConversion(300);
				}
			} else {
				this.inQuicksandTime = -1;
			}
		}
	}

}
