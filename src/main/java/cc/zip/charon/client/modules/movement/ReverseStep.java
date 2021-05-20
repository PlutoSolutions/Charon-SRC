/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.movement;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="ReverseStep", category= Module.Category.MOVEMENT)
public class ReverseStep
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 10, 1, 20));

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.isNull() || this.mc.player.isInWater() || this.mc.player.isInLava() || this.mc.player.isOnLadder()) {
            return;
        }
        if (this.mc.player.onGround) {
            this.mc.player.motionY -= (double)((float)this.speed.getValue().intValue() / 10.0f);
        }
    }
}

