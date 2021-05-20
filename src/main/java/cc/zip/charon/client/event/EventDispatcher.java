/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event;

import tcb.bces.bus.compilation.Dispatcher;
import tcb.bces.event.Event;

public class EventDispatcher
extends Dispatcher {
    @Override
    public <T extends Event> T dispatchEvent(T event) {
        Dispatcher.dispatch();
        return event;
    }
}

