package miku.united_as_one.genesis.common.items.weapon.sword;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.fiery_dagger.FieryDaggerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Gungnir extends SwordItem {
    public Gungnir(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            Vec3 pos = player.position();
            int count = 2;
            int delay = Utils.random.nextIntBetweenInclusive(30, 70);
            float yAngle = 0;//-Utils.getAngle(living.getX(), living.getZ(), player.getX(), player.getZ()) + ((float) Math.PI / 2F);

            for(int i = 0; i < count; ++i) {
                Vec3 offset = new Vec3((double) 1.5F * (double) player.getScale(), 0.0, 0.0).zRot(Mth.lerp((float) i / ((float) count - 1.0F), 0.0F, -(float) Math.PI)).yRot(yAngle).add(0.0, player.getEyeHeight(), 0.0);
                FieryDaggerEntity dagger = new FieryDaggerEntity(level);
                dagger.setOwner(player);
                dagger.ownerTrack = offset;
                //dagger.setTarget(living);
                dagger.setPos(pos.add(offset.yRot(player.getYRot())));
                dagger.delay = delay + i * 2;
                dagger.setDamage(12 * 0.15F);
                player.level.addFreshEntity(dagger);
            }

            player.getCooldowns().addCooldown(this, 240);
        }

        return InteractionResultHolder.success(stack);
    }
}
