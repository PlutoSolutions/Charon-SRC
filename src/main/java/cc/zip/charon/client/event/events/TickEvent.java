/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event.events;

import tcb.bces.event.Event;

public class TickEvent
extends Event {
    private final int stage;

    public TickEvent(int stage) {
        this.stage = stage;
    }

    public final int getStage() {
        return this.stage;
    }
}

