/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package cc.zip.charon.client.modules.misc;

import cc.zip.charon.client.event.events.PacketEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="PacketCanceller", category=Module.Category.MISC)
public class PacketCanceller
extends Module {
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == this.mc.player.getEntityId() || event.getPacket() instanceof SPacketExplosion) {
            event.setCancelled();
        }
    }
}

