package com.example.offhandshift;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> buildScreen(parent);
    }

    // Cloth Config moved this method between versions, so try both places.
    private static Screen buildScreen(Screen parent) {
        for (String className : new String[] {
                "me.shedaniel.autoconfig.AutoConfigClient",
                "me.shedaniel.autoconfig.AutoConfig" }) {
            try {
                Method method = Class.forName(className)
                        .getMethod("getConfigScreen", Class.class, Screen.class);
                Object supplier = method.invoke(null, ModConfig.class, parent);
                return (Screen) ((Supplier<?>) supplier).get();
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return parent;
    }
}
