/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.init.Items
 */
package cc.zip.charon.client.modules.player;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;

@ModuleManifest(label="CSGO", category= Module.Category.VISUAL)
public class CSGO
        extends Module {
    private int startcolor1;
    private int endcolor1;

    private final Setting<Boolean> watermarkcsgo = this.register(new Setting<Boolean>("CSGO", true));
    private final Setting<Boolean> colorcs = this.register(new Setting<Boolean>("Color", true, v -> watermarkcsgo.getValue()));
    public final Setting<Integer> redCS = this.register(new Setting<Integer>("Red", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue() ));
    public final Setting<Integer> greenCS = this.register(new Setting<Integer>("Green", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> blueCS = this.register(new Setting<Integer>("Blue", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> redCS1 = this.register(new Setting<Integer>("Red 2", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> greenCS1 = this.register(new Setting<Integer>("Green 2", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> blueCS1 = this.register(new Setting<Integer>("Blue 2", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> alphaCS = this.register(new Setting<Integer>("Alpha 1", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));
    public final Setting<Integer> alphaCS1 = this.register(new Setting<Integer>("Alpha 2", 255, 0, 255, v -> colorcs.getValue() && watermarkcsgo.getValue()));


    @Override
    public void onRender2D() {
        if (this.watermarkcsgo.getValue().booleanValue()) {

            startcolor1 = ColorUtil.toRGBA(redCS.getValue().intValue(), greenCS.getValue().intValue(), blueCS.getValue().intValue(), alphaCS.getValue().intValue());
            endcolor1 = ColorUtil.toRGBA(redCS1.getValue().intValue(), greenCS1.getValue().intValue(), blueCS1.getValue().intValue(), alphaCS1.getValue().intValue());
            RenderUtil.drawRectangleCorrectly(4, 5, 51, 12, ColorUtil.toRGBA(20, 20, 20, 200));
            RenderUtil.drawGradientSideways(4,
                    4,
                   55,
                    6.8 ,
                    startcolor1,
                    endcolor1);
            Charon.fontManager.drawString("charon.eu", 5.5f, +7.0f, Charon.INSTANCE.getColorManager().getColorAsInt());
        }
    }
}
