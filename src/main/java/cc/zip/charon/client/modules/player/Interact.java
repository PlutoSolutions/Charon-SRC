/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 */
package cc.zip.charon.client.modules.player;

import me.dev.mexen.api.mixin.accessors.IMinecraft;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.init.Items;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Interact", category= Module.Category.PLAYER)
public class Interact
extends Module {
    public final Setting<Boolean> fastEXP = this.register(new Setting<Boolean>("FastEXP", true));

    @Subscribe
    public void onUpdate(TickEvent event) {
        if (this.mc.player == null || this.mc.world == null) {
            return;
        }
        if (this.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && this.fastEXP.getValue().booleanValue()) {
            ((IMinecraft)this.mc).setRightClickDelayTimer(0);
        }
    }
}

