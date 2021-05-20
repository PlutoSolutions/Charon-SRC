/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.event.events;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import tcb.bces.event.EventCancellable;

public class ClientEvent
extends EventCancellable {
    Module module;
    Setting setting;
    private int stage;

    public ClientEvent(Setting setting) {
        this.setting = setting;
    }

    public Module getModule() {
        return this.module;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public ClientEvent() {
    }

    public ClientEvent(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}

