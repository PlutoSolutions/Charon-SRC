/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package cc.zip.charon.client.event.events;

import net.minecraft.entity.player.EntityPlayer;
import tcb.bces.event.Event;

public class TotemPopEvent
extends Event {
    private final Stage stage;
    private final EntityPlayer player;

    public TotemPopEvent(Stage stage, EntityPlayer player) {
        this.stage = stage;
        this.player = player;
    }

    public Stage getStage() {
        return this.stage;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public static enum Stage {
        POP,
        DEATH;

    }
}

