package com.Meli4.lumos.common.core.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeybindsInit {
    public static KeyBinding setActiveSkill;

    public static void register(final FMLClientSetupEvent event){
        setActiveSkill = create("LumosSetBonus", KeyEvent.VK_G);

        ClientRegistry.registerKeyBinding(setActiveSkill);
    }

    private static KeyBinding create(String name,int key){
        return new KeyBinding("Active set bonus skill", key, "Lumos");
    }
}
