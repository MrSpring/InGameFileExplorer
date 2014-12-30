package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiCheckbox implements IGui
{
    int x, y, w, h;
    boolean checked = false;
    GuiSimpleButton button;

    public GuiCheckbox(int xPos, int yPos, int width, int height, boolean value)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;

        checked = value;

        button = new GuiSimpleButton(x, y, w, h, "");
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        button.draw(minecraft, mouseX, mouseY);
        if (isChecked())
            DrawingHelper.drawIcon(DrawingHelper.checkMarkIcon, x + 2, y + 2, w - 4, h - 4);
    }

    @Override
    public void update()
    {
        button.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (button.mouseDown(mouseX, mouseY, mouseButton))
        {
            this.toggleChecked();
            return true;
        } else return false;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public void setIsEnabled(boolean isEnabled)
    {
        this.button.setEnabled(isEnabled);
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void toggleChecked()
    {
        this.checked = !checked;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    public int getY()
    {
        return y;
    }
}
