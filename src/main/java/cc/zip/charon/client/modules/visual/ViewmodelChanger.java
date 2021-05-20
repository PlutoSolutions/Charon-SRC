/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.visual;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.UpdateEvent;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import com.google.common.eventbus.Subscribe;


@ModuleManifest(label="ViewModelChanger", category= Module.Category.VISUAL, listen=false)
public class ViewmodelChanger
extends Module {
    public final Setting<Float> size = this.register(new Setting<Float>("Size", Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(15.0f)));
    public final Setting<Float> offsetX = this.register(new Setting<Float>("OffsetX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> offsetY = this.register(new Setting<Float>("OffsetY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> offsetZ = this.register(new Setting<Float>("OffsetZ", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> offhandX = this.register(new Setting<Float>("OffhandX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> offhandY = this.register(new Setting<Float>("OffhandY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> offhandZ = this.register(new Setting<Float>("OffhandZ", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public final Setting<Float> armPitch = this.register(new Setting<Float>("Arm Pitch", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    private static ViewmodelChanger INSTANCE = new ViewmodelChanger();

    public ViewmodelChanger() {
        INSTANCE = this;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event){
        if(isNull()){
            return;
        }

        mc.player.renderArmPitch = (float) armPitch.getValue();
    }

    public static ViewmodelChanger getInstance() {
        return INSTANCE;
    }
}

