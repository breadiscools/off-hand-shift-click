package com.example.offhandshift;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Config(name = OffhandShiftClick.MOD_ID)
public class ModConfig implements ConfigData {

    public boolean enabled = true;

    @ConfigEntry.Gui.Tooltip
    public Mode mode = Mode.DENY_LIST;

    @ConfigEntry.Gui.Tooltip
    public List<String> denyList = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    public List<String> allowList = new ArrayList<>();

    public enum Mode implements SelectionListEntry.Translatable {
        DENY_LIST,
        ALLOW_LIST;

        @Override
        public String getKey() {
            return "text.autoconfig." + OffhandShiftClick.MOD_ID + ".mode." + name();
        }
    }

    /** Should shift-clicking this stack send it to the off hand? */
    public boolean allows(ItemStack stack) {
        if (!enabled || stack.isEmpty()) return false;
        return switch (mode) {
            case DENY_LIST -> !matchesAny(denyList, stack);
            case ALLOW_LIST -> matchesAny(allowList, stack);
        };
    }

    private static boolean matchesAny(List<String> entries, ItemStack stack) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        for (String raw : entries) {
            String e = raw.trim();
            if (e.isEmpty()) continue;

            if (e.startsWith("#")) {
                // Tag, e.g. #minecraft:swords
                Identifier tagId = Identifier.tryParse(e.substring(1));
                if (tagId != null && stack.is(TagKey.create(Registries.ITEM, tagId))) return true;
            } else if (e.endsWith(":*")) {
                // Whole namespace, e.g. create:*
                if (itemId.getNamespace().equals(e.substring(0, e.length() - 2))) return true;
            } else {
                Identifier id = Identifier.tryParse(e);
                if (id != null && id.equals(itemId)) return true;
            }
        }
        return false;
    }
}
