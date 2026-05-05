package miku.united_as_one.genesis.init.registry;

import com.tterrag.registrate.util.entry.FluidEntry;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidRegistry {

    public static final FluidEntry<ForgeFlowingFluid.Flowing> SOURCE_FLUID = Genesis.L2_REGISTRATE
            .fluid("source_fluid",
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/source_fluid_still"),
                    ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "block/source_fluid_flow")
            )
            .fluidProperties(p -> p.levelDecreasePerBlock(1)
                    .tickRate(5)
                    .slopeFindDistance(4)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tab(CreativeTabRegistry.IRON_SPELLS_GENESIS_BLOCK)
            .build()
            .register();

    public static void register() {}
}