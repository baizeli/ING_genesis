package miku.united_as_one.genesis.client.renderer.spell.celestial_source;

import miku.united_as_one.genesis.Genesis;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlazedFlowerRainRenderer {
    private static final Minecraft minecraft = Minecraft.instance;
    private static final ResourceLocation GLAZED_FLOWER_RAIN_LOCATION = new ResourceLocation(Genesis.MODID, "textures/environment/glazed_flower_rain.png");
    private int rainSoundTime;

    public void renderSnowAndRain(LightTexture lightTexture, float partialTick, double entityX, double entityY, double entityZ, double camX, double camY, double camZ) {
        if (minecraft.level != null && minecraft.player != null) {
            float f = 1.0F;
            lightTexture.turnOnLightLayer();
            ClientLevel level = minecraft.level;
            int i = Mth.floor(entityX);
            int j = Mth.floor(entityY);
            int k = Mth.floor(entityZ);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int l = 15;

            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            int i1 = -1;
            float f1 = (float) minecraft.levelRenderer.ticks + partialTick;
            RenderSystem.setShader(GameRenderer::getParticleShader);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int j1 = k - l; j1 <= k + l; ++j1) {
                for (int k1 = i - l; k1 <= i + l; ++k1) {
                    int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                    double d0 = (double) minecraft.levelRenderer.rainSizeX[l1] * (double) 0.5F;
                    double d1 = (double) minecraft.levelRenderer.rainSizeZ[l1] * (double) 0.5F;
                    blockpos$mutableblockpos.set(k1, entityY, j1);
                    Biome biome = level.getBiome(blockpos$mutableblockpos).value();
                    if (biome.hasPrecipitation()) {
                        int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
                        int j2 = j - l;
                        int k2 = j + l;
                        if (j2 < i2) {
                            j2 = i2;
                        }

                        if (k2 < i2) {
                            k2 = i2;
                        }

                        int l2 = Math.max(i2, j);

                        if (j2 != k2) {
                            RandomSource randomsource = RandomSource.create((long) k1 * k1 * 3121 + k1 * 45238971L ^ (long) j1 * j1 * 418711 + j1 * 13761L);
                            blockpos$mutableblockpos.set(k1, j2, j1);
                            Biome.Precipitation biome$precipitation = biome.getPrecipitationAt(blockpos$mutableblockpos);
                            if (biome$precipitation == Biome.Precipitation.RAIN) {
                                if (i1 != 0) {
                                    if (i1 >= 0) {
                                        tesselator.end();
                                    }

                                    i1 = 0;
                                    RenderSystem.setShaderTexture(0, GLAZED_FLOWER_RAIN_LOCATION);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                int i3 = minecraft.levelRenderer.ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                                float f2 = -((float) i3 + partialTick) / 32.0F * (3.0F + randomsource.nextFloat());
                                double d2 = (double) k1 + (double) 0.5F - camX;
                                double d4 = (double) j1 + (double) 0.5F - camZ;
                                float f3 = (float) Math.sqrt(d2 * d2 + d4 * d4) / (float) l;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                                blockpos$mutableblockpos.set(k1, l2, j1);
                                int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                                bufferbuilder.vertex((double) k1 - camX - d0 + (double) 0.5F, (double) k2 - camY, (double) j1 - camZ - d1 + (double) 0.5F).uv(0.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - camX + d0 + (double) 0.5F, (double) k2 - camY, (double) j1 - camZ + d1 + (double) 0.5F).uv(1.0F, (float) j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - camX + d0 + (double) 0.5F, (double) j2 - camY, (double) j1 - camZ + d1 + (double) 0.5F).uv(1.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                                bufferbuilder.vertex((double) k1 - camX - d0 + (double) 0.5F, (double) j2 - camY, (double) j1 - camZ - d1 + (double) 0.5F).uv(0.0F, (float) k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
                            } else if (biome$precipitation == Biome.Precipitation.SNOW) {
                                if (i1 != 1) {
                                    if (i1 == 0) {
                                        tesselator.end();
                                    }

                                    i1 = 1;
                                    RenderSystem.setShaderTexture(0, LevelRenderer.SNOW_LOCATION);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                float f5 = -((float) (minecraft.levelRenderer.ticks & 511) + partialTick) / 512.0F;
                                float f6 = (float) (randomsource.nextDouble() + (double) f1 * 0.01 * (double) ((float) randomsource.nextGaussian()));
                                float f7 = (float) (randomsource.nextDouble() + (double) (f1 * (float) randomsource.nextGaussian()) * 0.001);
                                double d3 = (double) k1 + (double) 0.5F - camX;
                                double d5 = (double) j1 + (double) 0.5F - camZ;
                                float f8 = (float) Math.sqrt(d3 * d3 + d5 * d5) / (float) l;
                                float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * f;
                                blockpos$mutableblockpos.set(k1, l2, j1);
                                int k3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
                                int l3 = k3 >> 16 & '\uffff';
                                int i4 = k3 & '\uffff';
                                int j4 = (l3 * 3 + 240) / 4;
                                int k4 = (i4 * 3 + 240) / 4;
                                bufferbuilder.vertex((double) k1 - camX - d0 + (double) 0.5F, (double) k2 - camY, (double) j1 - camZ - d1 + (double) 0.5F).uv(0.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - camX + d0 + (double) 0.5F, (double) k2 - camY, (double) j1 - camZ + d1 + (double) 0.5F).uv(1.0F + f6, (float) j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - camX + d0 + (double) 0.5F, (double) j2 - camY, (double) j1 - camZ + d1 + (double) 0.5F).uv(1.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double) k1 - camX - d0 + (double) 0.5F, (double) j2 - camY, (double) j1 - camZ - d1 + (double) 0.5F).uv(0.0F + f6, (float) k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            }
                        }
                    }
                }
            }

            if (i1 >= 0) {
                tesselator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            lightTexture.turnOffLightLayer();
        }
    }

    public void tickRain(LivingEntity entity) {
        if (minecraft.level != null) {
            RandomSource randomsource = RandomSource.create((long) minecraft.levelRenderer.ticks * 312987231L);
            LevelReader levelreader = minecraft.level;
            BlockPos blockpos = BlockPos.containing(entity.position());
            BlockPos blockpos1 = null;
            int i = (int) 100.0F / (minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

            for (int j = 0; j < i; ++j) {
                int k = randomsource.nextInt(21) - 10;
                int l = randomsource.nextInt(21) - 10;
                BlockPos blockpos2 = levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
                if (blockpos2.getY() > levelreader.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10) {
                    Biome biome = levelreader.getBiome(blockpos2).value();
                    if (biome.getPrecipitationAt(blockpos2) == Biome.Precipitation.RAIN) {
                        blockpos1 = blockpos2.below();
                        if (minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
                            break;
                        }

                        double d0 = randomsource.nextDouble();
                        double d1 = randomsource.nextDouble();
                        BlockState blockstate = levelreader.getBlockState(blockpos1);
                        FluidState fluidstate = levelreader.getFluidState(blockpos1);
                        VoxelShape voxelshape = blockstate.getCollisionShape(levelreader, blockpos1);
                        double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
                        double d3 = fluidstate.getHeight(levelreader, blockpos1);
                        double d4 = Math.max(d2, d3);
                        ParticleOptions particleoptions = !fluidstate.is(net.minecraft.tags.FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
                        minecraft.level.addParticle(particleoptions, (double) blockpos1.getX() + d0, (double) blockpos1.getY() + d4, (double) blockpos1.getZ() + d1, 0.0F, 0.0F, 0.0F);
                    }
                }
            }

            if (blockpos1 != null && randomsource.nextInt(3) < this.rainSoundTime++) {
                this.rainSoundTime = 0;
                if (blockpos1.getY() > blockpos.getY() + 1 && levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float) blockpos.getY())) {
                    minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
                } else {
                    minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }
}