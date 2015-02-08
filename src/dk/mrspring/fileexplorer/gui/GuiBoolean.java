package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class GuiBoolean implements IGui
{
    int x;
    int y;

    public boolean value;

    private GuiSimpleButton trueButton;
    private GuiSimpleButton falseButton;

    public GuiBoolean(int x, int y, boolean startValue)
    {
        this.x = x;
        this.y = y;

        this.value = startValue;

        this.trueButton = new GuiSimpleButton(x, y, 16, 16, "").setIcon(DrawingHelper.checkMarkIcon);
        this.falseButton = new GuiSimpleButton(x + 18, y, 16, 16, "").setIcon(DrawingHelper.crossIcon);

        this.reloadButtons();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        trueButton.draw(minecraft, mouseX, mouseY);
        falseButton.draw(minecraft, mouseX, mouseY);
    }

    public void setX(int x)
    {
        this.x = x;
        this.trueButton.setX(x);
        this.falseButton.setX(x + 18);
    }

    public void setY(int y)
    {
        this.y = y;
        this.trueButton.setY(y);
        this.falseButton.setY(y);
    }

    @Override
    public void update()
    {
        this.trueButton.update();
        this.falseButton.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.trueButton.mouseDown(mouseX, mouseY, mouseButton))
        {
            this.value = true;
            this.reloadButtons();
        } else if (this.falseButton.mouseDown(mouseX, mouseY, mouseButton))
        {
            this.value = false;
            this.reloadButtons();
        }
        return false;
    }

    private void reloadButtons()
    {
        this.trueButton.setHighlight(value);
        this.falseButton.setHighlight(!value);
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

    public boolean getValue()
    {
        return value;
    }
}
