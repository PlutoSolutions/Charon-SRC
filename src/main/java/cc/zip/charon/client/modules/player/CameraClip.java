package cc.zip.charon.client.modules.player;


import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="CameraClip", category= Module.Category.VISUAL)
public class CameraClip
        extends Module {
    private static CameraClip INSTANCE = new CameraClip();
    public Setting<Boolean> extend = this.register(new Setting<Boolean>("Extend", false));
    public Setting<Double> distance = this.register(new Setting<Object>("Distance", 10.0, 0.0, 50.0, v -> this.extend.getValue(), "By how much you want to extend the distance."));


    public static CameraClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

