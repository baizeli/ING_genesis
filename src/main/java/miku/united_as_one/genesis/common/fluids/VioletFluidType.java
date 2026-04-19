package miku.united_as_one.genesis.common.fluids;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class VioletFluidType extends FluidType {
    // 定义贴图路径（指向 assets/united_as_one/textures/block/ 下的图片）
    public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOW = new ResourceLocation("block/water_flow");

    public VioletFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                // 如果你还没做新贴图，先用原版水的路径占位，防止崩溃
                return WATER_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return WATER_FLOW;
            }

            @Override
            public int getTintColor() {
                // 设置流体的颜色（ARGB格式），这里设置为半透明紫色
                return 0x99FF00FF;
            }
        });
    }
}