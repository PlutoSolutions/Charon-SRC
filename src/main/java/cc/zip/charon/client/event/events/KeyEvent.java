/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event.events;

import tcb.bces.event.Event;

public class KeyEvent
extends Event {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

