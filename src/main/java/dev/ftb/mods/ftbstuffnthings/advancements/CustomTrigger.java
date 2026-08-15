/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.ftb.mods.ftbstuffnthings.advancements;

import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CustomTrigger extends SimpleCriterionTrigger<CustomTrigger.Instance> {
    private final ResourceLocation triggerID;

    public CustomTrigger(String parString) {
        this(FTBStuffNThings.id(parString));
    }

    public CustomTrigger(ResourceLocation parRL) {
        super();
        triggerID = parRL;
    }

    @Override
    public ResourceLocation getId() {
        return triggerID;
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        return new Instance(triggerID);
    }

    public void trigger(ServerPlayer parPlayer) {
        this.trigger(parPlayer, Instance::test);
    }

    public Instance getInstance() {
        return new Instance(triggerID);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ResourceLocation id) {
            super(id, ContextAwarePredicate.ANY);
        }

        public boolean test() {
            return true;
        }
    }
}
