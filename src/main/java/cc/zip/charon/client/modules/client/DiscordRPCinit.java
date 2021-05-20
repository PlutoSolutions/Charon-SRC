/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.client;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.DisRPC;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Discord", category=Module.Category.CLIENT, persistent=true)
public class DiscordRPCinit
        extends Module {

    public void onEnable(){
        DisRPC.startRPC();
    }
    public void onDisable(){
        DisRPC.stopRPC();
    }
}

