package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 08-12-2014 for In-Game File Explorer.
 */
public class GuiFileNew extends GuiFileBase
{
    GuiSimpleButton createButton, cancelButton;
    GuiCustomTextField nameField;

    public GuiFileNew(int x, int y, int w, int h)
    {
        super(x, y, w, h);

        this.nameField = new GuiCustomTextField(x + 5, y + 5, w - 20, h - 10, "Enter File Name");

        this.createButton = new GuiSimpleButton(x + w - 5 - 10, y + (h / 2) - 5, 10, 10, "");
        this.cancelButton = new GuiSimpleButton(x + w - 5, y + (h / 2) - 5, 10, 10, "");
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.draw(minecraft, mouseX, mouseY);

        DrawingHelper.drawButtonThingy(x, y, w, h, 0, true);
        this.nameField.draw(minecraft, mouseX, mouseY);

        this.createButton.draw(minecraft, mouseX, mouseY);
        DrawingHelper.drawIcon(DrawingHelper.checkMarkIcon, x + w - 5 - 10, y + (h / 2) - 5, 10, 10, false);

        this.cancelButton.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void update()
    {
        super.update();
        this.createButton.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return this.nameField.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        nameField.handleKeyTyped(keyCode, character);
    }

    @Override
    public void setX(int x)
    {
        super.setX(x);
        this.nameField.setX(x + 5);
        this.createButton.setX(x + w - 5 - 10);
        this.cancelButton.setX(x + w - 5);
    }

    @Override
    public void setY(int y)
    {
        super.setY(y);
        this.nameField.setY(y + 5);
        this.createButton.setY(y + (h / 2) - 5);
        this.cancelButton.setY(y + (h / 2) - 5);
    }

    @Override
    public void setWidth(int w)
    {
        super.setWidth(w);
        if (h >= 30)
        {
            this.nameField.setW(w - 23);
            this.createButton.setY(y + 5);
            this.cancelButton.setY(y + h - 15);
        } else
        {
            this.nameField.setW(w - 33);
            this.createButton.setY(y + (h / 2) - 5);
            this.cancelButton.setY(y + (h / 2) - 5);
        }
        this.setX(x);
    }

    @Override
    public void setHeight(int h)
    {
        super.setHeight(h);
        this.nameField.setH(h);
        this.setY(y);
    }
}
