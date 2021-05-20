/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.visual;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="NoRender", category= Module.Category.VISUAL)
public class NoRender
extends Module {
    public final Setting<Boolean> noBossOverlay = this.register(new Setting<Boolean>("NoBoss", true));
    public final Setting<Boolean> boxedVines = this.register(new Setting<Boolean>("Vines", true));
    private static NoRender INSTANCE = new NoRender();

    public NoRender() {
        INSTANCE = this;
    }

    public static NoRender getInstance() {
        return INSTANCE;
    }
}

