/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.visual;

import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="ShulkerPreview", category= Module.Category.VISUAL)
public class ShulkerPreview
extends Module {
    private static ShulkerPreview INSTANCE = new ShulkerPreview();

    public ShulkerPreview() {
        INSTANCE = this;
    }

    public static ShulkerPreview getInstance() {
        return INSTANCE;
    }
}

