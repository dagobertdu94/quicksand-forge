package com.github.channelingmc.quicksand.common.block;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.api.access.QuicksandFogCamera;
import com.github.channelingmc.quicksand.api.access.QuicksandSubmergingEntity;
import com.github.channelingmc.quicksand.api.tag.QuicksandTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(Dist.CLIENT)
public class QuicksandBlock extends SandBlock implements BucketPickup {
	
	private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9f, 1.0);

	public final Supplier<? extends Item> bucket;

	public QuicksandBlock(BlockBehaviour.Properties settings, int color, Supplier<? extends Item> bucket) {
		super(color, settings);
		this.bucket = bucket;
	}
	
	@SubscribeEvent
	public static void fogColor(ViewportEvent.ComputeFogColor event) {
	    int color = ((QuicksandFogCamera)event.getCamera()).getQuicksandFogColor();
	    if (color > -1) {
	        event.setRed(((color & 0xFF0000) >> 16) / 255F);
	        event.setGreen(((color & 0xFF00) >> 8) / 255F);
	        event.setBlue(((color & 0xFF)) / 255F);
	    }
	}
	
	public static void spawnParticles(Level world, BlockState state, Vec3 pos) {
		if (world.isClientSide) {
			RandomSource random = world.getRandom();
			
			for (int i = 0; i < random.nextInt(3); ++i) {
				world.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), pos.x+(Mth.randomBetween(random, -1.0f, 1.0f)), pos.y+(Mth.randomBetween(random, -1.0f, 1.0f)), pos.z+(Mth.randomBetween(random, -1.0f, 1.0f)), (-1.0F + random.nextFloat() * 2.0F) / 12.0F, 0.05, (-1.0F + random.nextFloat() * 2.0F) / 12.0F);
			}
		}
	}
	
	public static boolean canWalkOnQuicksand(Entity entity) {
		if (entity.getType().is(QuicksandTags.QUICKSAND_WALKABLE_MOBS)) {
			return true;
		} else {
			return entity instanceof LivingEntity living &&
				living.getItemBySlot(EquipmentSlot.FEET).canWalkOnPowderedSnow(living);
		}
	}
	
	public static boolean canSurviveInQuicksand(LivingEntity living) {
		return living.getMobType() == MobType.UNDEAD || living.getType().is(QuicksandTags.SURVIVES_IN_QUICKSAND);
	}
	
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity.getFeetBlockState().is(QuicksandTags.QUICKSAND)) {
			entity.makeStuckInBlock(state, new Vec3(0.6D, 0.4D, 0.6D));
		}
		if (!entity.isSpectator() && hasEntityMoved(entity)) {
			if (entity instanceof LivingEntity living && ((QuicksandSubmergingEntity)entity).isSubmergedInQuicksand() && !canSurviveInQuicksand(living)) {
				living.hurt(new DamageSource(Holder.direct(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(QuicksandAPI.QUICKSAND_DAMAGE).get())), 1F);
			}
			if(world.getRandom().nextBoolean())
				spawnParticles(world, state, new Vec3(entity.getX(), pos.getY(), entity.getZ()));
		}
	}

	public boolean hasEntityMoved(Entity entity) {
		return entity.xOld - entity.getX() >= 0.001 ||
			   entity.yOld - entity.getY() >= 0.001 ||
			   entity.zOld - entity.getZ() >= 0.001;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity != null) {
				if (entity instanceof FallingBlockEntity || (canWalkOnQuicksand(entity) && context.isAbove(Shapes.block(), pos, false) && !context.isDescending())) {
					return super.getCollisionShape(state, world, pos, context);
				} else if (entity.fallDistance > 2.5f) {
					return FALLING_COLLISION_SHAPE;
				}
			}
		}
		return Shapes.empty();
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	public Item getBucket() {
		return bucket.get();
	}

	@Override
	public ItemStack pickupBlock(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_IMMEDIATE | Block.UPDATE_ALL);
		if (!world.isClientSide()) {
			world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
		}
		return getBucket().getDefaultInstance();
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.SAND_BREAK);
	}

	@Override
	protected void falling(FallingBlockEntity entity) {
		entity.dropItem = false;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		return type == PathComputationType.LAND;
	}
	
}
