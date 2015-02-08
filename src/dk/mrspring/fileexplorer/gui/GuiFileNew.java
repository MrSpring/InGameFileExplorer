package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.helper.DrawingHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 08-12-2014 for In-Game File Explorer.
 */
public class GuiFileNew extends GuiFileBase
{
    GuiSimpleButton createButton, cancelButton;
    GuiCustomTextField nameField;
    INewFileEvents events;

    public GuiFileNew(int x, int y, int w, int h, INewFileEvents events)
    {
        super(x, y, w, h);

        this.nameField = new GuiCustomTextField(x + 5, y + 5, w - 20, h - 10, "Enter File Name");

        this.createButton = new GuiSimpleButton(x + w - 5 - 10, y + (h / 2) - 5, 10, 10, "");
        this.cancelButton = new GuiSimpleButton(x + w - 5, y + (h / 2) - 4, 10, 10, "");

        this.events = events;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.draw(minecraft, mouseX, mouseY);

        if (h >= 30)
            DrawingHelper.drawButtonThingy(x, y, w, h, 0, true);
        this.nameField.draw(minecraft, mouseX, mouseY);

        this.createButton.draw(minecraft, mouseX, mouseY);
        DrawingHelper.drawIcon(DrawingHelper.checkMarkIcon, createButton.getX() + 2, createButton.getY() + 2, 6, 6, false);

        this.cancelButton.draw(minecraft, mouseX, mouseY);
        DrawingHelper.drawIcon(DrawingHelper.crossIcon, cancelButton.getX() + 2, cancelButton.getY() + 2, 6, 6, false);
    }

    @Override
    public void update()
    {
        super.update();
        this.createButton.update();
        this.cancelButton.update();
        this.nameField.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.nameField.mouseDown(mouseX, mouseY, mouseButton))
            return true;
        else if (this.createButton.mouseDown(mouseX, mouseY, mouseButton))
            this.events.onCreated(this, nameField.getText());
        else if (this.cancelButton.mouseDown(mouseX, mouseY, mouseButton))
            this.events.onCanceled(this, nameField.getText());
        else return false;
        return true;
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
        this.setPositionAndSize();
    }

    @Override
    public void setY(int y)
    {
        super.setY(y);
        this.setPositionAndSize();
    }

    @Override
    public void setWidth(int w)
    {
        super.setWidth(w);
        this.setPositionAndSize();
    }

    @Override
    public void setHeight(int h)
    {
        super.setHeight(h);
        this.setPositionAndSize();
    }

    private void setPositionAndSize()
    {
        if (h >= 30)
        {
            this.cancelButton.setX(x + w - 5 - 9);
            this.cancelButton.setY(y + h - 14);
            this.createButton.setX(x + w - 5 - 9);
            this.createButton.setY(y + 4);
            this.nameField.setX(x + 5);
            this.nameField.setY(y + 5);
            this.nameField.setW(w - 22);
            this.nameField.setH(h - 10);
        } else
        {
            this.createButton.setX(x + w - 22);
            this.createButton.setY(y + (h / 2) - 5);
            this.cancelButton.setX(x + w - 10);
            this.cancelButton.setY(y + (h / 2) - 5);
            this.nameField.setX(x);
            this.nameField.setY(y);
            this.nameField.setW(w - 24);
            this.nameField.setH(h);
        }
    }

    public interface INewFileEvents
    {
        public void onCreated(GuiFileNew guiFileNew, String path);

        public void onCanceled(GuiFileNew guiFileNew, String path);
    }
}
