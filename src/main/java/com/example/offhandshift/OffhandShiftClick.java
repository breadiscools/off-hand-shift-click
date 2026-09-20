package com.example.offhandshift;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class OffhandShiftClick implements ClientModInitializer {
    public static final String MOD_ID = "offhandshiftclick";

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static ModConfig config() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
