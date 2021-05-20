/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package cc.zip.charon.client.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Date;
import me.dev.mexen.api.mixin.mixins.network.ISPacketChat;
import cc.zip.charon.api.property.Setting;
import cc.zip.charon.client.event.events.PacketEventShit;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.ModuleManifest;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import tcb.bces.listener.Subscribe;

@ModuleManifest(label="Manage", category=Module.Category.CLIENT, persistent=true, enabled=true, drawn=false)
public class Manage
extends Module {
    public final Setting<String> prefixName = this.register(new Setting<String>("Prefix Name", "charon.eu"));

    public final Setting<Boolean> unfocusedLimit = this.register(new Setting<Boolean>("Limit Unfocused", true));
    public final Setting<Integer> unfocusedFPS = this.register(new Setting<Integer>("Unfocused FPS", Integer.valueOf(60), Integer.valueOf(1), Integer.valueOf(240), v -> this.unfocusedLimit.getValue()));
    public final Setting<Boolean> tabTweaks = this.register(new Setting<Boolean>("Tab Tweaks", true));
    public final Setting<Boolean> colorFriends = this.register(new Setting<Boolean>("Color Friends", Boolean.valueOf(true), v -> this.tabTweaks.getValue()));
    public final Setting<Boolean> chatTweaks = this.register(new Setting<Boolean>("Chat Tweaks", true));
    public final Setting<Boolean> timestamps = this.register(new Setting<Boolean>("Timestamps", Boolean.valueOf(true), v -> this.chatTweaks.getValue()));
    public final Setting<Boolean> giantBeetleSoundsLikeAJackhammer = this.register(new Setting<Boolean>("No Rect", Boolean.valueOf(true), v -> this.chatTweaks.getValue()));
    public final Setting<Boolean> customFont = this.register(new Setting<Boolean>("Chat Font", Boolean.valueOf(true), v -> this.chatTweaks.getValue()));
    private static Manage INSTANCE = new Manage();

    public Manage() {
        INSTANCE = this;
    }

    public static Manage getInstance() {
        return INSTANCE;
    }

    @Subscribe
    public void onPacketReceive(PacketEventShit.Receive event) {
        if (this.timestamps.getValue().booleanValue() && event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat)event.getPacket();
            if (this.timestamps.getValue().booleanValue()) {
                Date date = new Date();
                ISPacketChat chatPacket = (ISPacketChat)event.getPacket();
                String time = (Object)ChatFormatting.DARK_PURPLE + "<" + (Object)ChatFormatting.LIGHT_PURPLE + date.getHours() + ":" + date.getMinutes() + (Object)ChatFormatting.DARK_PURPLE + "> " + (Object)ChatFormatting.RESET;
                chatPacket.setChatComponent((ITextComponent)new TextComponentString(time + packet.getChatComponent().getFormattedText()));
            }
        }
    }
}

