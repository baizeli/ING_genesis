package miku.united_as_one.genesis.api.mixinutil;

import io.redspace.ironsspellbooks.api.spells.SpellSlot;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SpellSlotInfoAccessor {
    private static Method hasSpellMethod;
    private static Field spellSlotField;

    static {
        try {
            Class<?> spellSlotInfoClass = Class.forName("io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen$SpellSlotInfo");
            hasSpellMethod = spellSlotInfoClass.getDeclaredMethod("hasSpell");
            hasSpellMethod.setAccessible(true);
            spellSlotField = spellSlotInfoClass.getDeclaredField("spellSlot");
            spellSlotField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasSpell(Object spellSlotInfo) {
        try {
            return (boolean) hasSpellMethod.invoke(spellSlotInfo);
        } catch (Exception e) {
            return false;
        }
    }

    public static SpellSlot getSpellSlot(Object info) {
        try {
            return (SpellSlot) spellSlotField.get(info);
        } catch (Exception e) {
            return null;
        }
    }
}