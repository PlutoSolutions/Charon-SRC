/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.movement;

import me.dev.mexen.api.mixin.mixins.entity.IEntity;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="WebFall", category= Module.Category.MOVEMENT)
public class WebFall
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 1, 0, 10));

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (this.mc.player == null) {
            return;
        }

        if (((IEntity)this.mc.player).getIsInWeb()) {
            this.mc.player.motionY -= (double)this.speed.getValue().intValue();
        }
    }
}

