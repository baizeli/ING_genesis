package miku.united_as_one.genesis.client.render.cosmic;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.TrailRender;
import miku.united_as_one.genesis.registries.item.ItemRegistry;
import miku.united_as_one.genesis.registries.spell.SpellSchoolRegistry;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.item.Scroll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

public final class CosmicBakeModel implements BakedModel {
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();
    private final List<ResourceLocation> maskSprite;
    private final BakedModel wrapped;
    private final ItemOverrides overrideList;
    private final ModelState parentState;
    private LivingEntity entity;
    private ClientLevel world;
    private static final List<DeferredCosmicItem> DEFERRED_HAND_ITEMS = new ArrayList<>();

    public CosmicBakeModel(final BakedModel wrapped, final List<ResourceLocation> maskSprite) {
        this.overrideList = new ItemOverrides() {
            @Override
            public BakedModel resolve(final @NotNull BakedModel originalModel, final @NotNull ItemStack stack, final ClientLevel world, final LivingEntity entity, final int seed) {
                CosmicBakeModel.this.entity = entity;
                CosmicBakeModel.this.world = ((world == null) ? ((entity == null) ? null : ((ClientLevel) entity.level())) : null);
                return CosmicBakeModel.this.wrapped.getOverrides().resolve(originalModel, stack, world, entity, seed);
            }
        };
        this.wrapped = wrapped;
        this.parentState = TransformUtils.stateFromItemTransforms(wrapped.getTransforms());
        this.maskSprite = maskSprite;
    }

    private static final Map<Item, EffectConfig> COSMIC_EFFECTS = new HashMap<>();

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        // 渲染基础模型
        BakedModel model = this.wrapped.getOverrides().resolve(this.wrapped, stack, this.world, this.entity, 0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        assert model != null;

        for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
            for (RenderType rendertype : bakedModel.getRenderTypes(stack, true)) {
                itemRenderer.renderModelLists(bakedModel, stack, packedLight, packedOverlay, pStack, buffers.getBuffer(rendertype));
            }
        }

        // 检查并渲染特效
        EffectConfig config = COSMIC_EFFECTS.get(stack.getItem());
        if (config != null) {
           // int shadersType = stack.getItem() instanceof AvaritiaSword ? 1 : 2;
            renderCosmicEffect(stack, transformType, pStack, buffers, packedLight, packedOverlay, config.type, config.scale, config.v4f,2);
        }

        if (stack.getItem() instanceof Scroll) {
            SchoolType schoolType = ISpellContainer.getOrCreate(stack).getSpellAtIndex(0).getSpell().getSchoolType();
            if (schoolType.equals(SpellSchoolRegistry.CELESTIAL_SOURCE.get())) {
                renderCosmicEffect(stack, transformType, pStack, buffers, packedLight, packedOverlay, 15, 0.6F, new Vector4f(0.1F, 0.1F, 0.1F, 1.0F),2);
            }
        }
    }

    private static class EffectConfig {
        final int type;
        final float scale;
        final Vector4f v4f;

        EffectConfig(int type, float scale, Vector4f v4f) {
            this.type = type;
            this.scale = scale;
            this.v4f = v4f;
        }
    }

    private void renderCosmicEffect(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource buffers, int packedLight, int packedOverlay, int useType, float starScale, Vector4f vec4,int shaderType) {
        if (shouldDeferHandCosmic(transformType)) {
            queueDeferredHandCosmic(stack, transformType, pStack, packedLight, packedOverlay, useType, starScale, vec4);
            return;
        }

        if (buffers instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch();
        }

        RenderTarget mainTarget = Minecraft.instance.mainRenderTarget;
        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        float screenWidth = (float)mainTarget.width;
        float screenHeight = (float)mainTarget.height;
        float scale = starScale;

        // 根据渲染环境调整参数
        if (AvaritiaShaders.inventoryRender || transformType == ItemDisplayContext.GUI) {
            scale = 100.0F;
            AvaritiaShaders.cosmicIs2D.set(1);
        } else {
            assert mc.player != null;
            yaw = (float) (mc.player.getYRot() * 2.0F * Math.PI / 360.0);
            pitch = -(float) (mc.player.getXRot() * 2.0F * Math.PI / 360.0);
            AvaritiaShaders.cosmicIs2D.set(0);
        }

        // 设置着色器参数
        AvaritiaShaders.cosmicTime.set((System.currentTimeMillis() - AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        AvaritiaShaders.cosmicOpacity.set(1.0F);
        AvaritiaShaders.useType.set(useType);
        AvaritiaShaders.cosmicColor.set(vec4);
        AvaritiaShaders.cosmicScreenSize.set(screenWidth, screenHeight);

        // 准备纹理UV
        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);

        // 创建顶点消费者
        VertexConsumer cons;
       // else {
            cons = buffers.getBuffer(AvaritiaShaders.COSMIC_RENDER_TYPE);
        //}
        List<TextureAtlasSprite> atlasSprite = new ArrayList<>();

        // 加载遮罩纹理
        for (ResourceLocation res : maskSprite)
        {
            atlasSprite.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
        }

        // 生成渲染四边形
        LinkedList<BakedQuad> quads = new LinkedList<>();
        for (TextureAtlasSprite sprite : atlasSprite) {
            List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(
                    atlasSprite.indexOf(sprite),
                    "layer" + atlasSprite.indexOf(sprite),
                    sprite.contents()
            );

            for (BlockElement element : unbaked) {
                for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(
                            element.from,
                            element.to,
                            entry.getValue(),
                            sprite,
                            entry.getKey(),
                            new PerspectiveModelState(ImmutableMap.of()),
                            element.rotation,
                            element.shade,
                            ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "dynamic")
                    ));
                }
            }
        }

        // 渲染星空效果
        mc.getItemRenderer().renderQuadList(pStack, cons, quads, stack, packedLight, packedOverlay);
    }

    private boolean shouldDeferHandCosmic(ItemDisplayContext transformType) {
        return TrailRender.shouldDeferWorldEffects()
                && (transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
    }

    private void queueDeferredHandCosmic(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack,
                                         int packedLight, int packedOverlay, int useType, float starScale,
                                         Vector4f color) {
        DEFERRED_HAND_ITEMS.add(new DeferredCosmicItem(
                stack.copy(),
                transformType,
                new Matrix4f(poseStack.last().pose()),
                new Matrix4f(RenderSystem.getProjectionMatrix()),
                new ArrayList<>(this.maskSprite),
                packedLight,
                packedOverlay,
                useType,
                starScale,
                new Vector4f(color)
        ));
    }

    public static void clearDeferredHandItems() {
        DEFERRED_HAND_ITEMS.clear();
    }

    public static void flushDeferredHandItems(MultiBufferSource.BufferSource buffers) {
        if (DEFERRED_HAND_ITEMS.isEmpty() || AvaritiaShaders.cosmicShader == null || AvaritiaShaders.useType == null) {
            DEFERRED_HAND_ITEMS.clear();
            return;
        }

        RenderSystem.backupProjectionMatrix();
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();

        try {
            modelViewStack.setIdentity();
            RenderSystem.applyModelViewMatrix();

            for (DeferredCosmicItem item : DEFERRED_HAND_ITEMS) {
                RenderSystem.setProjectionMatrix(new Matrix4f(item.projection), VertexSorting.DISTANCE_TO_ORIGIN);

                PoseStack itemPose = new PoseStack();
                itemPose.mulPoseMatrix(item.pose);
                renderDeferredHandCosmic(item, itemPose, buffers);
                buffers.endBatch(AvaritiaShaders.COSMIC_HAND_RENDER_TYPE);
            }
        } finally {
            modelViewStack.popPose();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.applyModelViewMatrix();
            DEFERRED_HAND_ITEMS.clear();
        }
    }

    private static void renderDeferredHandCosmic(DeferredCosmicItem item, PoseStack poseStack,
                                                 MultiBufferSource buffers) {
        RenderTarget mainTarget = Minecraft.instance.mainRenderTarget;
        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        if (mc.player != null) {
            yaw = (float) (mc.player.getYRot() * 2.0F * Math.PI / 360.0);
            pitch = -(float) (mc.player.getXRot() * 2.0F * Math.PI / 360.0);
        }

        AvaritiaShaders.cosmicTime.set((System.currentTimeMillis() - AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(item.starScale);
        AvaritiaShaders.cosmicOpacity.set(1.0F);
        AvaritiaShaders.useType.set(item.useType);
        AvaritiaShaders.cosmicColor.set(item.color);
        AvaritiaShaders.cosmicScreenSize.set((float) mainTarget.width, (float) mainTarget.height);
        AvaritiaShaders.cosmicIs2D.set(0);

        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "item/misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);

        VertexConsumer consumer = buffers.getBuffer(AvaritiaShaders.COSMIC_HAND_RENDER_TYPE);
        mc.getItemRenderer().renderQuadList(poseStack, consumer, buildMaskQuads(item.maskSprite), item.stack,
                item.packedLight, item.packedOverlay);
    }

    private static LinkedList<BakedQuad> buildMaskQuads(List<ResourceLocation> maskSprites) {
        List<TextureAtlasSprite> atlasSprite = new ArrayList<>();
        for (ResourceLocation res : maskSprites) {
            atlasSprite.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
        }

        LinkedList<BakedQuad> quads = new LinkedList<>();
        for (TextureAtlasSprite sprite : atlasSprite) {
            List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(
                    atlasSprite.indexOf(sprite),
                    "layer" + atlasSprite.indexOf(sprite),
                    sprite.contents()
            );

            for (BlockElement element : unbaked) {
                for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(
                            element.from,
                            element.to,
                            entry.getValue(),
                            sprite,
                            entry.getKey(),
                            new PerspectiveModelState(ImmutableMap.of()),
                            element.rotation,
                            element.shade,
                            ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "dynamic")
                    ));
                }
            }
        }
        return quads;
    }

    private static final class DeferredCosmicItem {
        private final ItemStack stack;
        private final ItemDisplayContext transformType;
        private final Matrix4f pose;
        private final Matrix4f projection;
        private final List<ResourceLocation> maskSprite;
        private final int packedLight;
        private final int packedOverlay;
        private final int useType;
        private final float starScale;
        private final Vector4f color;

        private DeferredCosmicItem(ItemStack stack, ItemDisplayContext transformType, Matrix4f pose,
                                   Matrix4f projection, List<ResourceLocation> maskSprite, int packedLight,
                                   int packedOverlay, int useType, float starScale, Vector4f color) {
            this.stack = stack;
            this.transformType = transformType;
            this.pose = pose;
            this.projection = projection;
            this.maskSprite = maskSprite;
            this.packedLight = packedLight;
            this.packedOverlay = packedOverlay;
            this.useType = useType;
            this.starScale = starScale;
            this.color = color;
        }
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext context, @NotNull PoseStack pStack, boolean leftFlip) {
        PerspectiveModelState modelState = (PerspectiveModelState) this.parentState;
        if (modelState != null) {
            Transformation transform = ((PerspectiveModelState) this.parentState).getTransform(context);
            Vector3f trans = transform.getTranslation();
            Vector3f scale = transform.getScale();
            pStack.translate(trans.x(), trans.y(), trans.z());
            pStack.mulPose(transform.getLeftRotation());
            pStack.scale(scale.x(), scale.y(), scale.z());
            pStack.mulPose(transform.getRightRotation());
            if (leftFlip) {
                pStack.mulPose(Axis.YN.rotationDegrees(180.0f));
            }
            return this;
        }
        return BakedModel.super.applyTransform(context, pStack, leftFlip);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource rand) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.wrapped.getParticleIcon();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.wrapped.getParticleIcon(data);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return this.overrideList;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }

    static
    {
            COSMIC_EFFECTS.put(ItemRegistry.INFINITY_SWORD.get(), new EffectConfig(10, 0.6F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));
            /*COSMIC_EFFECTS.put(ModItems.INFINITY_ETERNAL_HELMET.get(), new EffectConfig(0, 0.5F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));
            COSMIC_EFFECTS.put(ModItems.INFINITY_ETERNAL_CHESTPLATE.get(), new EffectConfig(0, 0.5F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));
            COSMIC_EFFECTS.put(ModItems.INFINITY_ETERNAL_LEGGINGS.get(), new EffectConfig(0, 0.5F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));
            COSMIC_EFFECTS.put(ModItems.INFINITY_ETERNAL_BOOTS.get(), new EffectConfig(0, 0.5F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));*/
            COSMIC_EFFECTS.put(ItemRegistry.VIOLET_GALAXY_INGOT.get(), new EffectConfig(0, 0.6F, new Vector4f(0.0F, 0.02F, 0.03F, 1F)));
            COSMIC_EFFECTS.put(ItemRegistry.AVARITIA_SWORD.get(), new EffectConfig(15, 0.6F, new Vector4f(0.1F, 0.1F, 0.1F, 1.0F)));
    }
}
