/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.misc;

import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="AutoRespawn", category=Module.Category.MISC)
public class AutoRespawn
extends Module {
    @Subscribe
    public void onTickPost(TickEvent event) {
        if (event.getStage() == 1 && (this.mc.player.isDead || this.mc.player.getHealth() <= 0.0f)) {
            this.mc.player.respawnPlayer();
        }
    }
}

