/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.client;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.client.event.events.TickEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Colors", category=Module.Category.CLIENT, persistent=true)
public class Colors
extends Module {
    public final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public final Setting<Float> saturation = this.register(new Setting<Float>("Saturation", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", Integer.valueOf(5000), Integer.valueOf(1), Integer.valueOf(10000), v -> this.rainbow.getValue()));
    private static Colors INSTANCE = new Colors();

    public Colors() {
        INSTANCE = this;
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.getStage() == 0 || this.mc.player == null || this.mc.world == null) {
            return;
        }
        Color color = this.rainbow.getValue() != false ? new Color(ColorUtil.getRainbow(this.speed.getValue(), 0, this.saturation.getValue().floatValue() / 255.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
        Charon.INSTANCE.getColorManager().setColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    public static Colors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Colors();
        }
        return INSTANCE;
    }
}

