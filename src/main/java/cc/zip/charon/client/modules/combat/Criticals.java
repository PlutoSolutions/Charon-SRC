/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 */
package cc.zip.charon.client.modules.combat;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.PacketEventShit;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Criticals", category=Module.Category.COMBAT)
public class Criticals
extends Module {
    private final Setting<Integer> packets = this.register(new Setting<Integer>("Packets", 2, 1, 5));
    private final double[] packetzzz = new double[]{0.11, 0.11, 0.1100013579, 0.1100013579, 0.1100013579, 0.1100013579};

    @Subscribe
    public void onPacket(PacketEventShit.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)this.mc.world) instanceof EntityLivingBase) {
            if (!this.mc.player.onGround) {
                return;
            }
            for (int i = 0; i < this.packets.getValue(); ++i) {
                this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + this.packetzzz[i], this.mc.player.posZ, false));
            }
            this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false));
        }
    }
}

