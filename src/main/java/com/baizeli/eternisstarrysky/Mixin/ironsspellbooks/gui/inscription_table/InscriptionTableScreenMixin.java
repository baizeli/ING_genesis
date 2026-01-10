package com.baizeli.eternisstarrysky.Mixin.ironsspellbooks.gui.inscription_table;

import com.baizeli.eternisstarrysky.mixinutil.SpellSlotInfoAccessor;
import com.baizeli.eternisstarrysky.spell.celestial_source.FinalWhisper;
import com.baizeli.eternisstarrysky.spell.chaos.WarpedBarrierSpell;
import com.baizeli.eternisstarrysky.spell.chaos.WarpedBloodBurstSpell;
import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(InscriptionTableScreen.class)
public abstract class InscriptionTableScreenMixin {
    @Shadow private int selectedSpellIndex;

    @Shadow protected ArrayList<Object> spellSlots;

    @Shadow protected abstract boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY);

    @Shadow protected abstract void drawTextWithShadow(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale);

    @Shadow protected abstract int drawStatText(Font font, GuiGraphics guiHelper, int x, int y, String translationKey, Style textStyle, MutableComponent stat, Style statStyle, float scale);

    @Shadow protected abstract int drawText(Font font, GuiGraphics guiHelper, Component text, int x, int y, int color, float scale);

    @Inject(method = "renderLorePage(Lnet/minecraft/client/gui/GuiGraphics;FII)V", at = @At("HEAD"), remap = false, cancellable = true)
    private void renderLorePage(GuiGraphics guiHelper, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
        int x = screen.getGuiLeft() + 176;
        int y = screen.getGuiTop();
        int margin = 2;
        Style textColor = Style.EMPTY.withColor(3288106);
        PoseStack poseStack = guiHelper.pose();
        boolean spellSelected = this.selectedSpellIndex >= 0 && this.selectedSpellIndex < this.spellSlots.size() && SpellSlotInfoAccessor.hasSpell(this.spellSlots.get(this.selectedSpellIndex));
        MutableComponent title = this.selectedSpellIndex < 0 ? Component.translatable("ui.irons_spellbooks.no_selection") : (spellSelected ? SpellSlotInfoAccessor.getSpellSlot(this.spellSlots.get(this.selectedSpellIndex)).getSpell().getDisplayName(Minecraft.getInstance().player) : Component.translatable("ui.irons_spellbooks.empty_slot"));
        List<FormattedCharSequence> titleLines = screen.font.split(title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int titleY = y + 10;

        for(FormattedCharSequence line : titleLines) {
            int titleWidth = screen.font.width(line);
            int titleX = x + (80 - titleWidth) / 2;
            guiHelper.drawString(screen.font, line, titleX, titleY, 16777215, false);
            if (spellSelected) {
                Objects.requireNonNull(screen.font);
                if (this.isHovering(titleX, titleY, titleWidth, 9, mouseX, mouseY)) {
                    guiHelper.renderTooltip(screen.font, TooltipsUtils.createSpellDescriptionTooltip(SpellSlotInfoAccessor.getSpellSlot(this.spellSlots.get(this.selectedSpellIndex)).getSpell(), screen.font), mouseX, mouseY);
                }
            }

            Objects.requireNonNull(screen.font);
            titleY += 9;
        }

        int titleHeight = screen.font.wordWrapHeight(title.withStyle(ChatFormatting.UNDERLINE).withStyle(textColor), 80);
        int descLine = titleY + 4;
        if (this.selectedSpellIndex >= 0 && this.selectedSpellIndex < this.spellSlots.size() && SpellSlotInfoAccessor.hasSpell(this.spellSlots.get(this.selectedSpellIndex))) {
            Style colorMana = Style.EMPTY.withColor(17577);
            Style colorCooldown = Style.EMPTY.withColor(1135889);
            AbstractSpell spell = SpellSlotInfoAccessor.getSpellSlot(this.spellSlots.get(this.selectedSpellIndex)).getSpell();
            int spellLevel = SpellSlotInfoAccessor.getSpellSlot(this.spellSlots.get(this.selectedSpellIndex)).getLevel();
            float textScale = 1.0F;
            float reverseScale = 1.0F / textScale;
            Component school = spell.getSchoolType().getDisplayName();
            poseStack.scale(textScale, textScale, textScale);
            this.drawTextWithShadow(screen.font, guiHelper, school, x + (80 - screen.font.width(school.getString())) / 2, descLine, 16777215, 1.0F);
            float var10000 = (float) descLine;
            Objects.requireNonNull(screen.font);
            descLine = (int) (var10000 + 9.0F * textScale);
            MutableComponent levelText = Component.translatable("ui.irons_spellbooks.level", spellLevel).withStyle(textColor);
            guiHelper.drawString(screen.font, levelText, x + (80 - screen.font.width(levelText.getString())) / 2, descLine, 16777215, false);
            var10000 = (float) descLine;
            Objects.requireNonNull(screen.font);
            descLine = (int) (var10000 + 9.0F * textScale * 2.0F);
            Font var10002 = screen.font;
            int var10004 = x + margin;
            int var10008 = spell.getManaCost(spellLevel);
            descLine += this.drawStatText(var10002, guiHelper, var10004, descLine, "ui.irons_spellbooks.mana_cost", textColor, Component.translatable("" + var10008), colorMana, textScale);
            descLine += this.drawText(screen.font, guiHelper, TooltipsUtils.getCastTimeComponent(spell.getCastType(), Utils.timeFromTicks((float) spell.getEffectiveCastTime(spellLevel, null), 1)), x + margin, descLine, textColor.getColor().getValue(), textScale);

            if (spell instanceof WarpedBloodBurstSpell || spell instanceof WarpedBarrierSpell || spell instanceof FinalWhisper) {
                for (MutableComponent component : spell.getUniqueInfo(spellLevel, null)) {
                    if (Objects.equals(((TranslatableContents) component.getContents()).getKey(), "ui.irons_spellbooks.cooldown")) {
                        descLine += this.drawStatText(screen.font, guiHelper, x + margin, descLine, "ui.irons_spellbooks.cooldown", textColor, Component.translatable((String) ((TranslatableContents) component.getContents()).getArgs()[0]), colorCooldown, textScale);
                    } else {
                        descLine += this.drawText(screen.font, guiHelper, component, x + margin, descLine, textColor.getColor().getValue(), 1.0F);
                    }
                }
            } else {
                descLine += this.drawStatText(screen.font, guiHelper, x + margin, descLine, "ui.irons_spellbooks.cooldown", textColor, Component.translatable(Utils.timeFromTicks((float) spell.getSpellCooldown(), 1)), colorCooldown, textScale);
                for (MutableComponent component : spell.getUniqueInfo(spellLevel, null)) {
                    descLine += this.drawText(screen.font, guiHelper, component, x + margin, descLine, textColor.getColor().getValue(), 1.0F);
                }
            }

            poseStack.scale(reverseScale, reverseScale, reverseScale);
        }
        ci.cancel();
    }
}
