/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.world.World
 */
package cc.zip.charon.client.managers;

import java.util.HashMap;
import java.util.Map;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.modules.client.PopCounter;
import cc.zip.charon.client.event.events.PacketEventShit;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.event.events.TotemPopEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class PopManager
implements Minecraftable,
IListener {
    private final Map<String, Integer> popMap = new HashMap<String, Integer>();

    @Subscribe
    public void onUpdate(TickEvent event) {
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
            return;
        }
        for (int i = 0; i < Minecraftable.mc.world.playerEntities.size(); ++i) {
            EntityPlayer player = (EntityPlayer)Minecraftable.mc.world.playerEntities.get(i);
            if (!(player.getHealth() <= 0.0f) || !this.popMap.containsKey(player.getName())) continue;
            if (PopCounter.getInstance().isEnabled()) {
                MessageUtil.sendClientMessage(player.getName() + " died after popping their " + this.popMap.get(player.getName()) + this.getNumberStringThing(this.popMap.get(player.getName())) + " totem.", player.getEntityId());
            }
            this.popMap.remove(player.getName(), this.popMap.get(player.getName()));
        }
    }

    public String getNumberStringThing(int number) {
        if (number > 3) {
            return "th";
        }
        switch (number) {
            case 2: {
                return "nd";
            }
            case 3: {
                return "rd";
            }
        }
        return "";
    }

    @Subscribe
    public void onPacket(PacketEventShit.Receive event) {
        SPacketEntityStatus packet;
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35) {
            Entity entity = packet.getEntity((World)Minecraftable.mc.world);
            Charon.INSTANCE.getBus().post(new TotemPopEvent(TotemPopEvent.Stage.POP, (EntityPlayer)entity));
            if (this.popMap.get(entity.getName()) == null) {
                this.popMap.put(entity.getName(), 1);
                if (PopCounter.getInstance().isEnabled()) {
                    MessageUtil.sendClientMessage(entity.getName() + " popped a totem.", entity.getEntityId());
                }
            } else if (this.popMap.get(entity.getName()) != null) {
                int popCounter = this.popMap.get(entity.getName());
                int newPopCounter = popCounter + 1;
                this.popMap.put(entity.getName(), newPopCounter);
                if (PopCounter.getInstance().isEnabled()) {
                    MessageUtil.sendClientMessage(entity.getName() + " popped their " + newPopCounter + this.getNumberStringThing(newPopCounter) + " totem.", entity.getEntityId());
                }
            }
        }
    }

    public final Map<String, Integer> getPopMap() {
        return this.popMap;
    }

    public void init() {
        Charon.INSTANCE.getBus().register(this);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

