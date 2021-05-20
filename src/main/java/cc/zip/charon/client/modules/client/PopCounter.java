/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.client;

import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="PopCounter", category=Module.Category.CLIENT, listen=false)
public class PopCounter
extends Module {
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }
}

