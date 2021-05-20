/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package cc.zip.charon.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import cc.zip.charon.Charon;
import cc.zip.charon.client.gui.components.Component;
import cc.zip.charon.client.gui.components.items.Item;
import cc.zip.charon.client.gui.components.items.buttons.ModuleButton;
import cc.zip.charon.client.modules.Module;
import cc.zip.charon.client.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class Gui
extends GuiScreen {
    private final ArrayList<Component> components = new ArrayList();
    private static Gui INSTANCE = new Gui();

    public Gui() {
        INSTANCE = this;
        this.load();
    }

    public static Gui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    public static Gui getClickGui() {
        return Gui.getInstance();
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : Module.Category.values()) {
            this.components.add(new Component(category.getLabel(), x += 110, 4, true){

                @Override
                public void setupItems() {
                    Charon.INSTANCE.getModuleManager().getModulesByCategory(category).forEach(module -> this.addButton(new ModuleButton((Module)module)));
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Item::getName)));
    }

    public void updateModule(Module module) {
        block0: for (int i = 0; i < this.components.size(); ++i) {
            ArrayList<Item> items = this.components.get(i).getItems();
            for (int j = 0; j < items.size(); ++j) {
                Item item = items.get(j);
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
                continue block0;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
    @Override
    public void onGuiClosed() {
        try {
            super.onGuiClosed();
            if (ClickGui.getInstance().blur.getValue()) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

