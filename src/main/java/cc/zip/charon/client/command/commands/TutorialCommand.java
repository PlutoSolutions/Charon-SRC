/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.tutorial.TutorialSteps
 */
package cc.zip.charon.client.command.commands;

import cc.zip.charon.api.util.MessageUtil;
import cc.zip.charon.client.command.Command;
import cc.zip.charon.client.command.CommandManifest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.tutorial.TutorialSteps;

@CommandManifest(label="Tutorial", aliases={"tut"})
public class TutorialCommand
extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft.getMinecraft().gameSettings.tutorialStep = TutorialSteps.NONE;
        Minecraft.getMinecraft().getTutorial().setStep(TutorialSteps.NONE);
        MessageUtil.sendClientMessage("Set tutorial step to none!", -11114);
    }
}

