/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;
import cc.zip.charon.client.modules.client.ClickGui;
import com.mojang.realmsclient.gui.ChatFormatting;

@CommandManifest(label="Help", aliases={"h", "H", "command", "commands"})
public class HelpCommand
        extends Command {
    @Override
    public void execute(String[] args) {
        MessageUtil.sendClientMessage("Prefix: " + ClickGui.getInstance().prefix, -545);
        MessageUtil.sendClientMessage("Name: Charon.EU 1.0.4 build", -21333);
        MessageUtil.sendClientMessage(" ", -456678);
        MessageUtil.sendClientMessage("Commands: ", -56777);
        MessageUtil.sendClientMessage("" + ChatFormatting.GOLD +ClickGui.getInstance().prefix.getValue() + "save" + ChatFormatting.GRAY +"- saves the config", -23411);
        MessageUtil.sendClientMessage("" + ChatFormatting.GOLD +ClickGui.getInstance().prefix.getValue() + "draw" + ChatFormatting.GRAY + "- to hide the module with the array list", -63454);
        MessageUtil.sendClientMessage("" + ChatFormatting.GOLD + ClickGui.getInstance().prefix.getValue() + "friend add [friendName]" + ChatFormatting.GRAY + "- add a player as a friend", -34224);
        MessageUtil.sendClientMessage("" + ChatFormatting.GOLD +ClickGui.getInstance().prefix.getValue() + "toggle [moduleName]" + ChatFormatting.GRAY + "- off / on the module", -34563);
        MessageUtil.sendClientMessage("" + ChatFormatting.GOLD +ClickGui.getInstance().prefix.getValue() + "Tutorial [step] "+ ChatFormatting.GRAY + " - idk.", -123123);


    }
}

