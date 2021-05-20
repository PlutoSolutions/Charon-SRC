/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerListItem$AddPlayerData
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 */
package cc.zip.charon.client.managers;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import com.google.common.base.Strings;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

import cc.zip.charon.client.event.events.ConnectionEvent;
import cc.zip.charon.client.event.events.PacketEventShit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class TPSManager
implements Minecraftable,
IListener {
    double d;
    float f;
    private float TPS = 20.0f;
    private long lastUpdate = -1L;
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("##.00#");

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - this.lastUpdate;
        float tickTime = (float)timeDiff / 20.0f;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        float tps = 1000.0f / tickTime;
        if (f > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f2 : this.tpsCounts) {
            total += (double)f2;
        }
        total /= (double)this.tpsCounts.length;
        if (d > 20.0) {
            total = 20.0;
        }
        this.TPS = Float.parseFloat(this.format.format(total));
        this.lastUpdate = currentTime;
    }

    public void init() {
        Charon.INSTANCE.getBus().register(this);
    }

    @Subscribe
    public void onPacketReceive(PacketEventShit.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.update();
        }
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.getAction())) {
                return;
            }
            for (SPacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                if (data == null || Strings.isNullOrEmpty((String)data.getProfile().getName()) && data.getProfile().getId() == null) continue;
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        Charon.INSTANCE.getBus().post(new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = Minecraftable.mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            Charon.INSTANCE.getBus().post(new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        Charon.INSTANCE.getBus().post(new ConnectionEvent(2, id, null));
                    }
                }
            }
        }
    }

    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }

    public final float getTpsFactor() {
        return 20.0f / this.TPS;
    }

    public final float getTPS() {
        return this.TPS;
    }

    public int getPing() {
        if (Minecraftable.mc.player == null || Minecraftable.mc.world == null || Minecraftable.mc.getConnection() == null) {
            return -1;
        }
        return Minecraftable.mc.getConnection().getPlayerInfo(Minecraftable.mc.getConnection().getGameProfile().getId()).getResponseTime();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

