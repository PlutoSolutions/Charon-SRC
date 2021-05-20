/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.client;

import cc.zip.charon.Charon;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="Font", category=Module.Category.CLIENT, listen=false)
public class FontModule
extends Module {
    private static FontModule INSTANCE = new FontModule();

    public FontModule() {
        INSTANCE = this;
    }

    public static FontModule getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        Charon.fontManager.setCustomFont(true);
    }

    @Override
    public void onDisable() {
        Charon.fontManager.setCustomFont(false);
    }
}

