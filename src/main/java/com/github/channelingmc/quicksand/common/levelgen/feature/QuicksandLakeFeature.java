package com.github.channelingmc.quicksand.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class QuicksandLakeFeature extends Feature<QuicksandLakeFeature.Configuration> {
    
    public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateProvider.CODEC.fieldOf("block")
            .forGetter(Configuration::block),
        BlockStateProvider.CODEC.fieldOf("barrier")
            .forGetter(Configuration::barrier)
    ).apply(instance, Configuration::new));
    
    public QuicksandLakeFeature() {
        super(CODEC);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        Configuration config = context.config();
        if (pos.getY() <= worldgenlevel.getMinBuildHeight() + 4) {
            return false;
        } else {
            pos = pos.below(4);
            boolean[] gen = new boolean[2048];
            int amount = randomsource.nextInt(4) + 4;
        
            for(int index = 0; index < amount; ++index) {
                double xSize = randomsource.nextDouble() * 6.0D + 3.0D;
                double ySize = randomsource.nextDouble() * 4.0D + 2.0D;
                double zSize = randomsource.nextDouble() * 6.0D + 3.0D;
                double xO = randomsource.nextDouble() * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D;
                double yO = randomsource.nextDouble() * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D;
                double zO = randomsource.nextDouble() * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D;
            
                for(int x = 1; x < 15; ++x) {
                    for(int z = 1; z < 15; ++z) {
                        for(int y = 1; y < 7; ++y) {
                            double disX = ((double)x - xO) / (xSize / 2.0D);
                            double disY = ((double)y - yO) / (ySize / 2.0D);
                            double disZ = ((double)z - zO) / (zSize / 2.0D);
                            double dis = disX * disX + disY * disY + disZ * disZ;
                            if (dis < 1.0D) {
                                gen[(x * 16 + z) * 8 + y] = true;
                            }
                        }
                    }
                }
            }
    
            BlockState air = Blocks.CAVE_AIR.defaultBlockState();
            BlockState quickSand = config.block.getState(randomsource, pos);
        
            for(int x = 0; x < 16; ++x) {
                for(int z = 0; z < 16; ++z) {
                    for(int y = 0; y < 8; ++y) {
                        boolean flag = !gen[(x * 16 + z) * 8 + y] && (
                            x < 15 && gen[((x + 1) * 16 + z) * 8 + y] ||
                            x > 0 && gen[((x - 1) * 16 + z) * 8 + y] ||
                            z < 15 && gen[(x * 16 + z + 1) * 8 + y] ||
                            z > 0 && gen[(x * 16 + (z - 1)) * 8 + y] ||
                            y < 7 && gen[(x * 16 + z) * 8 + y + 1] ||
                            y > 0 && gen[(x * 16 + z) * 8 + (y - 1)]
                        );
                        if (flag) {
                            BlockState bs = worldgenlevel.getBlockState(pos.offset(x, y, z));
                            if (y >= 4 && bs.liquid()) {
                                return false;
                            }
                            if (y < 4 && !bs.isSolid() && worldgenlevel.getBlockState(pos.offset(x, y, z)) != quickSand) {
                                return false;
                            }
                        }
                    }
                }
            }
        
            for(int x = 0; x < 16; ++x) {
                for(int z = 0; z < 16; ++z) {
                    for(int y = 0; y < 8; ++y) {
                        if (gen[(x * 16 + z) * 8 + y]) {
                            BlockPos genPos = pos.offset(x, y, z);
                            if (this.canReplaceBlock(worldgenlevel.getBlockState(genPos))) {
                                boolean flag = y >= 4;
                                worldgenlevel.setBlock(genPos, flag ? air : quickSand, 2);
                                if (flag) {
                                    worldgenlevel.scheduleTick(genPos, air.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldgenlevel, genPos);
                                }
                            }
                        }
                    }
                }
            }
        
            BlockState barrier = config.barrier().getState(randomsource, pos);
            if (!barrier.isAir()) {
                for(int x = 0; x < 16; ++x) {
                    for(int z = 0; z < 16; ++z) {
                        for(int y = 0; y < 8; ++y) {
                            boolean flag = !gen[(x * 16 + z) * 8 + y] && (
                                x < 15 && gen[((x + 1) * 16 + z) * 8 + y] ||
                                x > 0 && gen[((x - 1) * 16 + z) * 8 + y] ||
                                z < 15 && gen[(x * 16 + z + 1) * 8 + y] ||
                                z > 0 && gen[(x * 16 + (z - 1)) * 8 + y] ||
                                y < 7 && gen[(x * 16 + z) * 8 + y + 1] ||
                                y > 0 && gen[(x * 16 + z) * 8 + (y - 1)]
                            );
                            if (flag && (y < 4 || randomsource.nextInt(2) != 0)) {
                                BlockState blockstate = worldgenlevel.getBlockState(pos.offset(x, y, z));
                                if (blockstate.isSolid() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                    BlockPos genPos = pos.offset(x, y, z);
                                    worldgenlevel.setBlock(genPos, barrier, 2);
                                    this.markAboveForPostProcessing(worldgenlevel, genPos);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
    
    private boolean canReplaceBlock(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }
    
    public record Configuration(BlockStateProvider block, BlockStateProvider barrier) implements FeatureConfiguration {}
    
}
