package com.baizeli.eternisstarrysky.Items;

import net.minecraft.core.particles.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;

import java.util.*;

import static java.lang.Math.sqrt;
import static net.minecraft.util.Mth.square;

public class FlyingSwallowThroughWillow extends SwordItem {
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
}