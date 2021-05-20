/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package cc.zip.charon.client.modules.visual;

import java.awt.Color;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@ModuleManifest(label="Chams", category= Module.Category.VISUAL)
public class Chams
extends Module {
    public final Setting<Boolean> chams = this.register(new Setting<Boolean>("Player Chams", false));
    public final Setting<Boolean> wireframe = this.register(new Setting<Boolean>("Player Wireframe", false));
    public final Setting<Boolean> crystalChams = this.register(new Setting<Boolean>("Crystal Chams", false));
    public final Setting<Boolean> crystalWireframe = this.register(new Setting<Boolean>("Crystal Wireframe", false));
    public final Setting<Float> scale = this.register(new Setting<Float>("Scale", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(1.1f)));
    public final Setting<Float> lineWidth = this.register(new Setting<Float>("Linewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f), v -> this.crystalWireframe.getValue()));
    public final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), v -> this.colorSync.getValue() == false));
    public final Setting<Integer> saturation = this.register(new Setting<Integer>("Saturation", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", Integer.valueOf(40), Integer.valueOf(1), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public final Setting<Integer> red = this.register(new Setting<Integer>("Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public final Setting<Integer> green = this.register(new Setting<Integer>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    public final Setting<Integer> crystalAlpha = this.register(new Setting<Integer>("Crystal Alpha", 255, 0, 255));
    public final Setting<Integer> playerAlpha = this.register(new Setting<Integer>("Player Alpha", 255, 0, 255));
    public static Chams INSTANCE;

    public Chams() {
        INSTANCE = this;
    }

    public void onRenderModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        Color rainbowColor = new Color(ColorUtil.getRainbow(this.speed.getValue() * 100, 0, (float)this.saturation.getValue().intValue() / 100.0f));
        Color color = this.colorSync.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255);
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6913);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        if (entity instanceof EntityPlayer) {
            GL11.glDisable((int)2929);
        }
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        if (entity instanceof EntityPlayer && Charon.INSTANCE.getFriendManager().isFriend(entity.getName())) {
            ColorUtil.color(-11157267);
        } else {
            ColorUtil.color(this.rainbow.getValue() != false ? rainbowColor.getRGB() : color.getRGB());
        }
        GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
        base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        GL11.glPopAttrib();
    }
}

