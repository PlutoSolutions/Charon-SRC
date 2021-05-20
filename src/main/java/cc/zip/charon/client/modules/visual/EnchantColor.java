/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.visual;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="EnchantColor", category= Module.Category.VISUAL, listen=false)
public class EnchantColor
extends Module {
    public final Setting<Boolean> sync = this.register(new Setting<Boolean>("Sync", true));
    public final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    private static EnchantColor INSTANCE = new EnchantColor();

    public EnchantColor() {
        INSTANCE = this;
    }

    public static EnchantColor getINSTANCE() {
        return INSTANCE;
    }

    public int getColor() {
        return new Color(this.sync.getValue() != false ? Charon.INSTANCE.getColorManager().getColorAsInt() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB()).getRGB();
    }
}

