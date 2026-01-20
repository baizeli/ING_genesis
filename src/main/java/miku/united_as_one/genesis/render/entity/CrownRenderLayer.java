package miku.united_as_one.genesis.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.item.armor.GoldCrownArmorItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CrownRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public CrownRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (HaloRenderLayer.isEquippedAndVisible(player, ItemRegistry.DEV_CROWN.get())) {
            ItemStack crownStack = ItemRegistry.DEV_CROWN.get().getDefaultInstance();

            if (!crownStack.isEmpty()) {
                GoldCrownArmorItem item = (GoldCrownArmorItem) crownStack.getItem();
                GeoArmorRenderer<?> renderer = item.supplyRenderer();
                renderer.prepForRender(player, crownStack, EquipmentSlot.HEAD, this.getParentModel());
                renderer.renderToBuffer(poseStack, null, packedLight, 0, 0, 0, 0, 0);
            }
        }
    }
}