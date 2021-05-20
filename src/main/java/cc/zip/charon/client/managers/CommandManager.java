/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package cc.zip.charon.client.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.commands.*;
import cc.zip.charon.client.modules.client.ClickGui;
import cc.zip.charon.client.event.events.PacketEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import tcb.bces.listener.IListener;
import tcb.bces.listener.Subscribe;

public class CommandManager
implements IListener {
    private final List<Command> commands = new ArrayList<Command>();

    @Subscribe
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            this.checkCommands(((CPacketChatMessage)event.getPacket()).getMessage(), event);
        }
    }

    private void checkCommands(String message, PacketEvent event) {
        if (message.startsWith(ClickGui.getInstance().prefix.getValue())) {
            String[] args = message.split(" ");
            String input = message.split(" ")[0].substring(1);
            for (Command command : this.commands) {
                if (!input.equalsIgnoreCase(command.getLabel()) && !this.checkAliases(input, command)) continue;
                event.setCancelled();
                command.execute(args);
            }
            if (!event.isCancelled()) {
                MessageUtil.sendClientMessage("Command " + message + " was not found!", true);
                event.setCancelled();
            }
            event.setCancelled();
        }
    }

    private boolean checkAliases(String input, Command command) {
        for (String str : command.getAliases()) {
            if (!input.equalsIgnoreCase(str)) continue;
            return true;
        }
        return false;
    }

    public void init() {
        this.register(
                new ToggleCommand(),
                new BindCommand(),
                new DrawnCommand(),
                new FriendCommand(),
                new SaveCommand(),
                new TutorialCommand(),
                new HelpCommand());
        Charon.INSTANCE.getBus().register(this);
    }

    public void register(Command ... command) {
        Collections.addAll(this.commands, command);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

