/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.Charon;
import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;
import cc.zip.charon.client.modules.Module;

@CommandManifest(label="Drawn", aliases={"Hide", "d"})
public class DrawnCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            return;
        }
        Module module = Charon.INSTANCE.getModuleManager().getModuleByLabel(args[1]);
        if (module != null) {
            module.setDrawn(!module.isHidden());
            MessageUtil.sendClientMessage(module.getLabel() + " has been " + (module.isHidden() ? "hidden" : "unhidden"), false);
        }
    }
}

