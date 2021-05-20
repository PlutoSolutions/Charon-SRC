/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package cc.zip.charon.client.modules.misc;

import cc.zip.charon.client.event.events.TotemPopEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import com.mojang.text2speech.Narrator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(label="Narrator", category=Module.Category.MISC)
public class BotNarrator
        extends Module {
    private final Narrator narrator = Narrator.getNarrator();

    @Override
    public void onEnable() {
        if (isNull()){
            return;
        }

        this.narrator.say("Привет лютый кристал певепешер");
    }
    @Override
    public void onDisable() {
        if (isNull()){
            return;
        }
        this.narrator.clear();
    }


}

