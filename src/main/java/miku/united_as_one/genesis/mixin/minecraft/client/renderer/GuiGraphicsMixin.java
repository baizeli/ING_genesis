package miku.united_as_one.genesis.mixin.minecraft.client.renderer;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registry.spell.SpellSchoolRegistry;
import miku.united_as_one.genesis.util.RenderUtils;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.item.Scroll;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Shadow(remap = false)
    private ItemStack tooltipStack;

    @Inject(method = "renderTooltipInternal", at = @At(value = "HEAD"), cancellable = true)
    private void renderTooltipInternal(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner, CallbackInfo ci) {
        GuiGraphics guiGraphics = ((GuiGraphics) (Object) this);

        if (!components.isEmpty()) {
            RenderTooltipEvent.Pre preEvent = ForgeHooksClient.onRenderTooltipPre(this.tooltipStack, guiGraphics, mouseX, mouseY, guiGraphics.guiWidth(), guiGraphics.guiHeight(), components, font, tooltipPositioner);
            if (preEvent.isCanceled()) {
                return;
            }

            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            for(ClientTooltipComponent clienttooltipcomponent : components) {
                int k = clienttooltipcomponent.getWidth(preEvent.getFont());
                if (k > i) {
                    i = k;
                }

                j += clienttooltipcomponent.getHeight();
            }

            Vector2ic vector2ic = tooltipPositioner.positionTooltip(guiGraphics.guiWidth(), guiGraphics.guiHeight(), preEvent.getX(), preEvent.getY(), i, j);
            int l = vector2ic.x();
            int i1 = vector2ic.y();
            guiGraphics.pose.pushPose();
            int j1 = 400;
            int finalJ = j;
            int finalI = i;
            guiGraphics.drawManaged(() -> {
                RenderTooltipEvent.Color colorEvent = ForgeHooksClient.onRenderTooltipColor(this.tooltipStack, guiGraphics, l, i1, preEvent.getFont(), components);
                TooltipRenderUtil.renderTooltipBackground(guiGraphics, l, i1, finalI, finalJ, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd(), colorEvent.getBorderStart(), colorEvent.getBorderEnd());
            });
            guiGraphics.pose.translate(0.0F, 0.0F, 400.0F);
            int k1 = i1;

            for(int l1 = 0; l1 < components.size(); ++l1) {
                ClientTooltipComponent clienttooltipcomponent1 = components.get(l1);
                clienttooltipcomponent1.renderText(preEvent.getFont(), l, k1, guiGraphics.pose.last().pose(), guiGraphics.bufferSource);
                k1 += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
            }

            k1 = i1;

            for(int k2 = 0; k2 < components.size(); ++k2) {
                ClientTooltipComponent clienttooltipcomponent2 = components.get(k2);
                clienttooltipcomponent2.renderImage(preEvent.getFont(), l, k1, guiGraphics);
                k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
            }

            Item item = this.tooltipStack.getItem();
            boolean isChaosSpell = false;
            boolean isCelestialSourceSpell = false;

            if (item instanceof Scroll) {
                SchoolType schoolType = ISpellContainer.getOrCreate(this.tooltipStack).getSpellAtIndex(0).getSpell().getSchoolType();
                isChaosSpell = schoolType.equals(SpellSchoolRegistry.CHAOS.get());
                isCelestialSourceSpell = schoolType.equals(SpellSchoolRegistry.CELESTIAL_SOURCE.get());
            }

            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);

            double centerX = l + i / 2.0;
            double centerY = i1 + j / 2.0;

            if (registryName != null && registryName.getNamespace().equals(Genesis.MOD_ID)) {
                RenderUtils.renderCosmicBackground(guiGraphics.pose(), guiGraphics.bufferSource(), null, i + 3, j + 3, centerX, centerY, 15728880, 15);
            } else if (isChaosSpell) {
                RenderUtils.renderCosmicBackground(guiGraphics.pose(), guiGraphics.bufferSource(), null, i + 3, j + 3, centerX, centerY, 15728880, 14);
            } else if (isCelestialSourceSpell) {
                RenderUtils.renderCosmicBackground(guiGraphics.pose(), guiGraphics.bufferSource(), null, i + 3, j + 3, centerX, centerY, 15728880, 9);
            }

            guiGraphics.pose.popPose();

        }
        ci.cancel();
    }
}