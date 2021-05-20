/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package cc.zip.charon.api.util;

import cc.zip.charon.client.modules.client.Manage;
import com.mojang.realmsclient.gui.ChatFormatting;
import cc.zip.charon.api.interfaces.Minecraftable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil
implements Minecraftable {

    public static void sendClientMessage(String string, boolean deleteOld) {
        if (Minecraftable.mc.player == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Manage.getInstance().prefixName.getPlannedValue() +ChatFormatting.GRAY + "]" + " " +ChatFormatting.GRAY + string);
        if (deleteOld) {
            Minecraftable.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)component, -727);
        } else {
            Minecraftable.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)component);
        }
    }

    public static void sendClientMessage(String string, int id) {
        if (Minecraftable.mc.player == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Manage.getInstance().prefixName.getPlannedValue() + ChatFormatting.GRAY + "]" + " " + ChatFormatting.GRAY + string);
        Minecraftable.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)component, id);
    }
}

