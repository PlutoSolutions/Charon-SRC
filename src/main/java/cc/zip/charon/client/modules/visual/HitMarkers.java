/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.modules.visual;

import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

@ModuleManifest(label="HitMarkers", category=Module.Category.CLIENT, listen=false)
public class HitMarkers
        extends Module {
    private static HitMarkers INSTANCE = new HitMarkers();

    boolean attacking = false;
    public HitMarkers() {
        this.setInstance();
    }
    private void setInstance() {
        INSTANCE = this;
    }

    public static HitMarkers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HitMarkers();
        }
        return INSTANCE;
    }

    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(mc);
        RenderUtil.drawLineV2(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLineV2(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLineV2(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLineV2(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }

    @Override
    public void onRender2D() {
        if (isNull()) {
            return;
        }
        if (!mc.player.isSwingInProgress){
            attacking = false;
        } else attacking = true;

        if (attacking){
            drawHitMarkers();
        }
    }


}

