/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event.events;

import tcb.bces.event.Event;

public class UpdateEvent
extends Event {
    private final int stage;

    public UpdateEvent(int stage) {
        this.stage = stage;
    }

    public final int getStage() {
        return this.stage;
    }
}

