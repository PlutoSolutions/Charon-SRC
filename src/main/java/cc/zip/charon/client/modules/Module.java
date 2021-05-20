/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 */
package cc.zip.charon.client.modules;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Bind;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.gui.Gui;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import tcb.bces.listener.IListener;

public class Module
implements IListener {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private List<Setting> settings = new ArrayList<Setting>();
    public final Setting<Boolean> drawn = this.register(new Setting<Boolean>("Drawn", true));
    public final Setting<Bind> bind = this.register(new Setting<Bind>("Bind", new Bind(-1)));
    public final Setting<Boolean> enabled = this.register(new Setting<Boolean>("Enabled", false));
    private String label;
    private String suffix = "";
    private Category category;
    private boolean persistent;
    private boolean listenable = true;

    public Module() {
        if (this.getClass().isAnnotationPresent(ModuleManifest.class)) {
            ModuleManifest moduleManifest = this.getClass().getAnnotation(ModuleManifest.class);
            this.label = moduleManifest.label();
            this.category = moduleManifest.category();
            this.bind.setValue(new Bind(moduleManifest.key()));
            this.drawn.setValue(moduleManifest.drawn());
            this.persistent = moduleManifest.persistent();
            this.listenable = moduleManifest.listen();
        }
        Charon.INSTANCE.getBus().register(this);
    }

    public Setting register(Setting setting) {
        setting.setModule(this);
        this.settings.add(setting);
        if (this.mc.currentScreen instanceof Gui) {
            Gui.getInstance().updateModule(this);
        }
        return setting;
    }

    public final List<Setting> getSettings() {
        return this.settings;
    }

    public Setting getSettingByName(String name) {
        Setting setting = null;
        for (Setting set : this.settings) {
            if (!set.getName().equalsIgnoreCase(name)) continue;
            setting = set;
        }
        return setting;
    }

    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
        this.onToggle();
        String white =  "" + ChatFormatting.WHITE;

        if (enabled) {
            this.onEnable();
            MessageUtil.sendClientMessage(white + (Object)ChatFormatting.BOLD + this.getLabel() + (Object)ChatFormatting.GRAY + (Object)ChatFormatting.BOLD + " :" + (Object)ChatFormatting.GREEN + " Enabled", -44444);
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_WATERLILY_PLACE, 1.0f));
        } else {
            this.onDisable();
            MessageUtil.sendClientMessage(white + (Object)ChatFormatting.BOLD + this.getLabel() + (Object)ChatFormatting.GRAY + (Object)ChatFormatting.BOLD +  " :" + (Object)ChatFormatting.RED + " Disabled", -44444);
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_WOOD_PLACE, 1.0f));
        }
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public void toggle() {
        this.setEnabled(this.enabled.getValue() == false);
    }

    public void disable() {
        this.setEnabled(false);
    }
    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }
    public void onRender3D() {
    }

    public void onRender2D() {
    }

    public void onToggle() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLoad() {
    }

    public void onDisconnect() {
    }

    public boolean isNull() {
        return this.mc.player == null || this.mc.world == null;
    }

    public final int getKey() {
        return this.bind.getValue().getKey();
    }

    @Override
    public final boolean isEnabled() {
        return this.mc.player != null && this.enabled.getValue() != false;
    }

    public final boolean isHidden() {
        return this.drawn.getValue() == false;
    }

    public final boolean isPersistent() {
        return this.persistent;
    }

    public final Category getCategory() {
        return this.category;
    }

    public final String getLabel() {
        return this.label;
    }

    public void clearSuffix() {
        this.suffix = "";
    }

    public void setSuffix(String suffix) {
        this.suffix = "[" + suffix + "]";
    }

    public final String getSuffix() {
        return this.suffix;
    }

    public static enum Category {
        COMBAT("Combat", -65536),
        MOVEMENT("Movement", -16745217),
        PLAYER("Player", -16711936),
        CLIENT("Client", 10660302),
        VISUAL("Visual", -24064),
        MISC("Misc", -8650497),
        OYVEY("OyVey", -853895);

        final int color;
        final String label;

        private Category(String label, int color) {
            this.color = color;
            this.label = label;
        }

        public final int getColor() {
            return this.color;
        }

        public final String getLabel() {
            return this.label;
        }
    }



}

