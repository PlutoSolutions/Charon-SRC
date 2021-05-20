/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 */
package cc.zip.charon.client.gui.components;

import java.awt.Color;
import java.util.ArrayList;

import cc.zip.charon.Charon;
import cc.zip.charon.api.interfaces.Minecraftable;
import cc.zip.charon.api.util.ColorUtil;
import cc.zip.charon.api.util.MathUtil;
import cc.zip.charon.api.util.RenderUtil;
import cc.zip.charon.client.gui.Gui;
import cc.zip.charon.client.managers.ColorManager;
import cc.zip.charon.client.modules.client.ClickGui;
import cc.zip.charon.client.gui.components.items.Item;
import cc.zip.charon.client.gui.components.items.buttons.Button;
import cc.zip.charon.client.modules.client.GuiGradient;
import cc.zip.charon.client.modules.visual.HUD;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

public class Component
implements Minecraftable {
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private final int barHeight;
    public boolean drag;
    private final ArrayList<Item> items = new ArrayList();
    private boolean hidden = false;
    private final String name;
    public int dragY;
    public Component(String name, int x, int y, boolean open) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 18;
        this.barHeight = 15;
        this.open = open;
        this.setupItems();
    }
    private int startcolor;
    private int endcolor;

    public final String getName() {
        return this.name;
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drag(mouseX, mouseY);
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        startcolor = ColorUtil.toRGBA(GuiGradient.getInstance().g_red.getValue(), GuiGradient.getInstance().g_green.getValue(), GuiGradient.getInstance().g_blue.getValue(), GuiGradient.getInstance().g_alpha.getValue());
        endcolor =  ColorUtil.toRGBA(GuiGradient.getInstance().g_red1.getValue(), GuiGradient.getInstance().g_green1.getValue(), GuiGradient.getInstance().g_blue1.getValue(), GuiGradient.getInstance().g_alpha1.getValue());;
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height - 3, ColorManager.toRGBA(116,75,229,255));
        RenderUtil.drawGradientSideways(this.x - 1, this.y, this.x + this.width + 1, this.y + this.barHeight - 2.0f, startcolor, endcolor);

        //RenderUtil.drawRect(this.x, (float)this.y + 0, this.x + this.width - 98, (float)(this.y) + 13, ColorManager.toRGBA(116, 75, 229, 255));
        //RenderUtil.drawRect(this.x  + 98, (float)this.y - 0, this.x + this.width + 1, (float)(this.y) + 13, ColorManager.toRGBA(116, 75, 229, 255));

        if (this.open) {
            //RenderUtil.drawRect(this.x, (float)this.y + 12.5f, this.x + this.width, (float)(this.y + this.height) + totalItemHeight, this.changeAlpha(0x77000000, ClickGui.getInstance().alpha.getValue()));
            RenderUtil.drawGradientSideways(this.x - 1, (float)this.y + 13.2f, this.x + this.width + 1, this.y + totalItemHeight + 19, startcolor, endcolor);
            RenderUtil.drawRect(this.x, (float)this.y + 13.2f, this.x + this.width, (float)(this.y + this.height) + totalItemHeight, ColorManager.toRGBA(0,0,0,255));

            //RenderUtil.drawRect(this.x, (float)this.y + 12.5f, this.x + this.width - 98, (float)(this.y + this.height) + totalItemHeight , ColorManager.toRGBA(116, 75, 229, 255));
            //RenderUtil.drawRect(this.x  + 98, (float)this.y + 12.5f, this.x + this.width, (float)(this.y + this.height) + totalItemHeight , ColorManager.toRGBA(116, 75, 229, 255));

            if (ClickGui.getInstance().outline.getValue().booleanValue()) {

                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel(7425);
                GL11.glBegin(2);
                Color outlineColor = ClickGui.getInstance().rainbow.getValue() != false ? Charon.INSTANCE.getColorManager().getColor() : new Color(ColorManager.toARGB(165,23,55,255));
                GL11.glColor4f((float) outlineColor.getRed(), (float) outlineColor.getGreen(), (float) outlineColor.getBlue(), (float) outlineColor.getAlpha());
                //GL11.glVertex3f((float) this.x, (float) this.y - 1.5f, 0.0f);
                //GL11.glVertex3f((float) (this.x + this.width), (float) this.y - 1.5f, 0.0f);
                GL11.glVertex3f((float) (this.x + this.width), (float) (this.y + this.height) + totalItemHeight, 5.0f);
                GL11.glVertex3f((float) this.x, (float) (this.y + this.height) + totalItemHeight, 5.0f);

                GL11.glEnd();
                GlStateManager.shadeModel(7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();

            }

        }
        Charon.fontManager.drawCenteredString(this.getName(), (float)this.x + (float)this.width / 2.0f, (float)this.y - 4.0f - (float) Gui.getClickGui().getTextOffset(), 0xFFFFFF, true);
        if (this.open) {
            float y = (float)(this.getY() + this.getHeight()) - 3.0f;
            for (int i = 0; i < this.getItems().size(); ++i) {
                Item item = this.getItems().get(i);
                if (item.isHidden()) continue;
                item.setLocation((float)this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += (float)item.getHeight() + 1.5f;
            }
        }
    }

    public int changeAlpha(int originalColor, int userInputedAlpha) {
        return userInputedAlpha << 24 | (originalColor &= 0xFFFFFF);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            Gui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Minecraftable.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.BLOCK_ANVIL_FALL, (float)10.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float)item.getHeight() + 1.5f;
        }
        return height;
    }
}

