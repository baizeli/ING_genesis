package miku.united_as_one.genesis.registries.fluid;

import com.tterrag.registrate.util.entry.FluidEntry;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.item.CreativeTabRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidRegistry {

    public static final FluidEntry<ForgeFlowingFluid.Flowing> SOURCE_FLUID = Genesis.L2_REGISTRATE
            .fluid("source_fluid",
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/source_fluid_still"),
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/source_fluid_flow")
            )
            .fluidProperties(p -> p.levelDecreasePerBlock(1).tickRate(5).slopeFindDistance(4).explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .tag(FluidTags.WATER)
            .renderType(RenderType::translucent)
            .bucket()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> BLACKWATER_FLUID = Genesis.L2_REGISTRATE
            .fluid("blackwater_fluid",
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/blackwater_fluid_still"),
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/blackwater_fluid_flow")
            )
            .fluidProperties(p -> p.levelDecreasePerBlock(1).tickRate(5).slopeFindDistance(4).explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .tag(FluidTags.WATER)
            .renderType(RenderType::translucent)
            .bucket()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> BLOOD_FLUID = Genesis.L2_REGISTRATE
            .fluid("blood_fluid",
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/blood_fluid_still"),
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/blood_fluid_flow")
            )
            .fluidProperties(p -> p.levelDecreasePerBlock(1).tickRate(5).slopeFindDistance(4).explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .tag(FluidTags.WATER)
            .renderType(RenderType::translucent)
            .bucket()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static void register() {}
}
