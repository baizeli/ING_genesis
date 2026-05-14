package miku.united_as_one.genesis.contents.items;

import miku.united_as_one.genesis.Genesis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.registries.*;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static java.lang.Math.sqrt;
import static net.minecraft.util.Mth.square;

@SuppressWarnings("removal")
public class FlyingSwallowThroughWillow extends SwordItem implements GeoItem {
    public FlyingSwallowThroughWillow() {
        super(
            new ForgeTier(
                0,
                1451,
                12f,
                0f,
                35,
                BlockTags.NEEDS_STONE_TOOL,
                () -> Ingredient.of(
                    ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("irons_spellbooks", "arcane_ingot")
                    )
                )
            ), 
            6,
            -2.4f,
            new Item.Properties().rarity(Rarity.EPIC)
        );
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        Vec3 lookVec = player.getLookAngle();
        player.push(lookVec.x * 3, lookVec.y * 3, lookVec.z * 3);

        if (!world.isClientSide) {
            Vec3 start = player.position();
            Vec3 end = start.add(lookVec.scale(6.0));
            
            drawLine(0.01, end, start, ParticleTypes.CLOUD, (ServerLevel) world);
        }

        player.getPersistentData().putLong("FlyingSwallowFallImmunity", world.getGameTime() + 60);

        player.getCooldowns().addCooldown(this, 20);
        
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static void drawLine(double interval, Vec3 to, Vec3 from, SimpleParticleType type, ServerLevel level) {
        double deltax = to.x - from.x, deltay = to.y - from.y, deltaz = to.z - from.z;
        double length = sqrt(square(deltax) + square(deltay) + square(deltaz));
        int amount = (int) (length / interval);
        for (int i = 0; i <= amount; i++) {
            level.sendParticles(type, from.x + deltax * i / amount, from.y + deltay * i / amount, from.z + deltaz * i / amount, 0, 0, 0, 0, 0);
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoItemRenderer<FlyingSwallowThroughWillow> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeoItemRenderer<>(new GeoModel<>() {
                        @Override
                        public ResourceLocation getModelResource(FlyingSwallowThroughWillow object) {
                            return new ResourceLocation(Genesis.MOD_ID, "geo/item/flying_swallow_through_willow.geo.json");
                        }

                        @Override
                        public ResourceLocation getTextureResource(FlyingSwallowThroughWillow object) {
                            return new ResourceLocation(Genesis.MOD_ID, "textures/item/flying_swallow_through_willow_models.png");
                        }

                        @Override
                        public ResourceLocation getAnimationResource(FlyingSwallowThroughWillow animatable) {
                            return new ResourceLocation(Genesis.MOD_ID, "animations/item/flying_swallow_through_willow.animation.json");
                        }
                    });
                
                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}