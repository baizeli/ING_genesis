package miku.united_as_one.genesis.network.manuscript;

import miku.united_as_one.genesis.items.ModItems;
import miku.united_as_one.genesis.spell.SpellSchool;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LearnSpellPacket {
    private final byte hand;
    private final String spell;

    public LearnSpellPacket(InteractionHand interactionHand, String spell) {
        this.hand = handToByte(interactionHand);
        this.spell = spell;
    }

    public LearnSpellPacket(FriendlyByteBuf buf) {
        hand = buf.readByte();
        spell = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(hand);
        buf.writeUtf(spell);
    }

    public static LearnSpellPacket decode(FriendlyByteBuf buf) {
        return new LearnSpellPacket(buf);
    }

    public static void handle(LearnSpellPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            ItemStack itemStack = serverPlayer.getItemInHand(byteToHand(packet.hand));
            AbstractSpell spell = SpellRegistry.getSpell(packet.spell);
            var data = MagicData.getPlayerMagicData(serverPlayer).getSyncedData();
            
            boolean canLearn = false;

            // 混沌手稿
            if (itemStack.is(ModItems.CHAOS_MANUSCRIPT.get()) && 
                spell.getSchoolType().equals(SpellSchool.CHAOS.get())) {
                canLearn = true;
            }
            
            // 星源手稿
            if (itemStack.is(ModItems.CELESTIAL_SOURCE_MANUSCRIPT.get()) && 
                spell.getSchoolType().equals(SpellSchool.CELESTIAL_SOURCE.get())) {
                canLearn = true;
            }

            if (spell != SpellRegistry.none() && !data.isSpellLearned(spell) 
                && canLearn && itemStack.getCount() > 0) {
                
                data.learnSpell(spell);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    public static byte handToByte(InteractionHand hand) {
        return (byte) (hand == InteractionHand.MAIN_HAND ? 1 : 0);
    }

    public static InteractionHand byteToHand(byte b) {
        return b > 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}