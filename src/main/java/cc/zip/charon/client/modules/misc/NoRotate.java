/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package cc.zip.charon.client.modules.misc;

import me.dev.mexen.api.mixin.mixins.network.ISPacketPlayerPosLook;
import cc.zip.charon.client.event.events.PacketEventShit;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="NoRotate", category=Module.Category.MISC)
public class NoRotate
extends Module {
    @Subscribe
    public void onPacket(PacketEventShit.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            ISPacketPlayerPosLook packet = (ISPacketPlayerPosLook)event.getPacket();
            packet.setPitch(this.mc.player.rotationPitch);
            packet.setYaw(this.mc.player.rotationYaw);
        }
    }
}

