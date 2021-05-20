/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package cc.zip.charon.client.modules.visual;

import java.awt.Color;

import cc.zip.charon.Charon;
import me.dev.mexen.api.mixin.mixins.render.IEntityRenderer;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.util.math.RayTraceResult;

@ModuleManifest(label="BlockHighlight", category= Module.Category.VISUAL, listen=false)
public class BlockHighlight
extends Module {
    public final Setting<Float> lineWidth = this.register(new Setting<Float>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(4.0f)));
    public final Setting<Boolean> sync = this.register(new Setting<Boolean>("Sync", true));
    public final Setting<Integer> red = this.register(new Setting<Integer>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> green = this.register(new Setting<Integer>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));
    public final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.sync.getValue() == false));

    @Override
    public void onEnable() {
        ((IEntityRenderer)this.mc.entityRenderer).setDrawBlockOutline(false);
    }

    @Override
    public void onDisable() {
        ((IEntityRenderer)this.mc.entityRenderer).setDrawBlockOutline(true);
    }

    @Override
    public void onRender3D() {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            RenderUtil.renderProperOutline(this.mc.objectMouseOver.getBlockPos(), this.sync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()), this.lineWidth.getValue().floatValue());
        }
    }
}

