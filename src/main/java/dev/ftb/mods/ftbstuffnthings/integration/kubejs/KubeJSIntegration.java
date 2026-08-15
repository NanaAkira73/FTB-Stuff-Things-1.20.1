package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class KubeJSIntegration extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(FTBStuffNThings.id("jar"), JarRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("temperature_source"), TemperatureSourceRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("hammer"), HammerRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("crook"), CrookRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("fusing_machine"), FusingMachineRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("supercooler"), SuperCoolerRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("sluice"), SluiceRecipeSchema.SCHEMA);
        event.register(FTBStuffNThings.id("wooden_basin"), WoodenBasinSchema.SCHEMA);

        FTBStuffNThings.LOGGER.info("Registered KubeJS recipe schemas");
    }
}
