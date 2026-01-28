package miku.united_as_one.genesis.items.pickaxe;

import net.minecraft.world.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("removal")
public class MithrilPickaxe extends PickaxeItem {
    public MithrilPickaxe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entityLiving) {
        if (!level.isClientSide) {
            if (level.getRandom().nextFloat() < 0.005) {
                Item arcaneEssence = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation("irons_spellbooks", "arcane_essence")
                );

                if (arcaneEssence != null) {
                    Containers.dropItemStack(
                        level, 
                        pos.getX(), 
                        pos.getY(), 
                        pos.getZ(), 
                        new ItemStack(arcaneEssence, 1)
                    );
                }
            }
        }

        return super.mineBlock(stack, level, state, pos, entityLiving);
    }
}