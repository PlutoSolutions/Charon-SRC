/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.Charon;
import cc.zip.charon.api.property.Bind;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;
import cc.zip.charon.client.modules.Module;
import org.lwjgl.input.Keyboard;

@CommandManifest(label="Bind", aliases={"b"})
public class BindCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            MessageUtil.sendClientMessage("Use the command like this -> (module, bind)", true);
            return;
        }
        Module module = Charon.INSTANCE.getModuleManager().getModuleByLabel(args[1]);
        if (module != null) {
            int index = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            module.bind.setValue(new Bind(index));
            MessageUtil.sendClientMessage(module.getLabel() + " has been bound to " + Keyboard.getKeyName((int)index), false);
        }
    }
}

