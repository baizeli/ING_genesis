package miku.united_as_one.genesis.contents.workbench.arcane_cauldron;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.gui.overlays.ScreenTooltipOverlay;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArcaneCauldronRenderer implements BlockEntityRenderer<ArcaneCauldronBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ArcaneCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ArcaneCauldronBlockEntity cauldron, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = cauldron.getLevel();
        int waterLevel = cauldron.getFluidAmount();
        float waterOffset = Mth.lerp(waterLevel / 1000f, .25f, .9f);

        if (level != null && waterLevel > 0) {
            renderWater(cauldron, poseStack, bufferSource, packedLight, waterOffset);
        }

        for (int i = 0; i < cauldron.inputItems.size(); i++) {
            ItemStack itemStack = cauldron.inputItems.get(i);
            if (!itemStack.isEmpty()) {
                float time = level != null && waterLevel > 0 ? level.getGameTime() + partialTick : 15;
                Vec2 floatOffset = getFloatingItemOffset(time, i * 587);
                float yRot = (time + i * 213) / (i + 1) * 1.5f;
                renderItem(
                        itemStack,
                        new Vec3(floatOffset.x, waterOffset + i * .01f, floatOffset.y),
                        yRot,
                        cauldron,
                        poseStack,
                        bufferSource,
                        packedOverlay
                );
            }
        }

        renderLookTooltip(cauldron);
    }

    public Vec2 getFloatingItemOffset(float time, int offset) {
        float xSpeed = offset % 2 == 0 ? .0075f : .025f * (1 + (offset % 88) * .001f);
        float ySpeed = offset % 2 == 0 ? .025f : .0075f * (1 + (offset % 88) * .001f);
        float x = (time + offset) * xSpeed;
        x = (Math.abs((x % 2) - 1) + 1) / 2;
        float y = (time + offset + 4356) * ySpeed;
        y = (Math.abs((y % 2) - 1) + 1) / 2;
        x = Mth.lerp(x, -.2f, .75f);
        y = Mth.lerp(y, -.2f, .75f);
        return new Vec2(x, y);
    }

    private void renderWater(ArcaneCauldronBlockEntity cauldron, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float waterOffset) {
        Matrix4f pose = poseStack.last().pose();
        float totalFluid = cauldron.getFluidAmount();
        float runningFluid = totalFluid;
        float f = 0;
        float padding = 1 / 16f;

        for (FluidStack fluid : cauldron.fluidInventory.fluids()) {
            int skylight = packedLight >> 4 & 15;
            int luminosity = Math.max(skylight, fluid.getFluid().getFluidType().getLightLevel(fluid));
            int fluidLight = packedLight & 0xF00000 | luminosity << 4;
            IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid.getFluid());
            Function<ResourceLocation, TextureAtlasSprite> spriteAtlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
            TextureAtlasSprite texture = spriteAtlas.apply(clientFluid.getStillTexture(fluid.getFluid().defaultFluidState(), cauldron.getLevel(), cauldron.getBlockPos()));
            VertexConsumer consumer = texture.wrap(bufferSource.getBuffer(RenderType.translucent()));
            Vector3f rgb = colorFromLong(clientFluid.getTintColor(fluid) & clientFluid.getTintColor(fluid.getFluid().defaultFluidState(), cauldron.getLevel(), cauldron.getBlockPos()));
            float opacity = runningFluid / totalFluid;
            runningFluid -= fluid.getAmount();

            consumer.vertex(pose, 1 - padding, waterOffset + f, padding).color(rgb.x(), rgb.y(), rgb.z(), opacity).uv(1 - padding, padding).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fluidLight).normal(0, 1, 0).endVertex();
            consumer.vertex(pose, padding, waterOffset + f, padding).color(rgb.x(), rgb.y(), rgb.z(), opacity).uv(padding, padding).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fluidLight).normal(0, 1, 0).endVertex();
            consumer.vertex(pose, padding, waterOffset + f, 1 - padding).color(rgb.x(), rgb.y(), rgb.z(), opacity).uv(padding, 1 - padding).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fluidLight).normal(0, 1, 0).endVertex();
            consumer.vertex(pose, 1 - padding, waterOffset + f, 1 - padding).color(rgb.x(), rgb.y(), rgb.z(), opacity).uv(1 - padding, 1 - padding).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(fluidLight).normal(0, 1, 0).endVertex();
            f += 0.001f;
        }
    }

    private Vector3f colorFromLong(long color) {
        return new Vector3f(
                ((color >> 16) & 0xFF) / 255.0f,
                ((color >> 8) & 0xFF) / 255.0f,
                (color & 0xFF) / 255.0f
        );
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, ArcaneCauldronBlockEntity cauldron, PoseStack poseStack, MultiBufferSource bufferSource, int packedOverlay) {
        Level level = cauldron.getLevel();
        if (level == null) {
            return;
        }

        poseStack.pushPose();
        int renderId = (int) cauldron.getBlockPos().asLong();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(0.4f, 0.4f, 0.4f);
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(level, cauldron.getBlockPos()), packedOverlay, poseStack, bufferSource, level, renderId);
        poseStack.popPose();
    }

    private void renderLookTooltip(ArcaneCauldronBlockEntity cauldron) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        if (Math.abs(player.getX() - cauldron.getBlockPos().getX()) >= 5
                || Math.abs(player.getY() - cauldron.getBlockPos().getY()) >= 5
                || Math.abs(player.getZ() - cauldron.getBlockPos().getZ()) >= 5) {
            return;
        }

        if (!player.isCrouching()
                || !(Minecraft.getInstance().hitResult instanceof BlockHitResult blockHitResult)
                || !blockHitResult.getBlockPos().equals(cauldron.getBlockPos())) {
            return;
        }

        List<Component> text = new ArrayList<>();
        text.add(Component.translatable("block.genesis_magic.arcane_cauldron").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.WHITE));
        List<FluidStack> fluids = cauldron.fluidInventory.fluids();
        if (fluids.isEmpty()) {
            text.add(Component.translatable("ui.irons_spellbooks.empty").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else {
            List<ObjectIntImmutablePair<MutableComponent>> fluidInfo = new ArrayList<>();
            for (int i = fluids.size() - 1; i >= 0; i--) {
                FluidStack fluid = fluids.get(i);
                fluidInfo.add(new ObjectIntImmutablePair<>(fluid.getFluid().getFluidType().getDescription(fluid).copy().withStyle(ChatFormatting.DARK_AQUA), fluid.getAmount()));
            }

            for (ObjectIntImmutablePair<MutableComponent> info : fluidInfo) {
                text.add(Component.literal("  ").append(info.left()).append(": ").append(Component.literal(info.rightInt() + "mb").withStyle(ChatFormatting.GOLD)));
            }
        }
        ScreenTooltipOverlay.renderTooltip(text, (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> new Vector2i(screenWidth / 2 + 30, screenHeight / 2 - tooltipHeight / 2));
    }
}
