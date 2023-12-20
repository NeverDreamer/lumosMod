package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.Meli4.lumos.common.core.init.KeybindsInit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InputEvents {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event){
        Minecraft mc = Minecraft.getInstance();
        if(mc.world == null){return;}
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseInputEvent event){
        Minecraft mc = Minecraft.getInstance();
        if(mc.world == null){return;}
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action){

        if(mc.currentScreen == null && KeybindsInit.setActiveSkill.isPressed()){
            LumosNetwork.CHANNEL.sendToServer(new InputMessage(0));
        }

    }
}
