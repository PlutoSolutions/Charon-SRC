/*
 * Decompiled with CFR 0.150.
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.Charon;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;

@CommandManifest(label="Save", aliases={"s"})
public class SaveCommand
extends Command {
    @Override
    public void execute(String[] args) {
        Charon.INSTANCE.getConfigManager().saveConfig(Charon.INSTANCE.getConfigManager().config.replaceFirst("Charon/", ""));
    }
}

