package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiNumberField implements IGui
{
    int x, y, width;
    int value = 0;
    int increment;
    int minimum, maximum;
    Type type;

    GuiTextField textField;

    GuiSimpleButton decrease, increase;

    public GuiNumberField(int xPos, int yPos, int width, Type displayType, FontRenderer fontRenderer)
    {
        this.x = xPos;
        this.y = yPos;

        this.width = width;

        type = displayType;

        if (this.width < type.getMinWidth())
            this.width = type.getMinWidth();

        minimum = 0;
        maximum = 100;
        increment = 1;

//        this.minimum = minimum;
//        this.maximum = maximum;

        this.textField = new GuiTextField(10, fontRenderer, this.x + 20, this.y, this.width - 40, 22);
        decrease = new GuiSimpleButton(x, y + 1, 20, 20, "-");
        increase = new GuiSimpleButton(x + this.width - 20, y + 1, 20, 20, "+");
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
//        textField.drawTextBox();
//        DrawingHelper.drawRect(x,y,100,30,Color.BLACK,0.8F);

        decrease.draw(minecraft, mouseX, mouseY);
        increase.draw(minecraft, mouseX, mouseY);
        textField.drawTextBox();

        DrawingHelper.drawOutline(x + 19, y - 1, width - 38, 24, Color.WHITE, 1F);
    }

    @Override
    public void update()
    {
        if (value >= maximum)
            increase.disable();
        else increase.enable();

        if (value <= minimum)
            decrease.disable();
        else decrease.enable();

        decrease.update();
        increase.update();
        String stringValue = String.valueOf(value);
        switch (type)
        {
            case PERCENTAGE:
                stringValue += "%";
                break;
        }
        textField.setText(stringValue);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (decrease.mouseDown(mouseX, mouseY, mouseButton) && value > minimum)
        {
            if (mouseButton == 0)
                this.value -= this.increment;
            else if (mouseButton == 1)
                this.value -= (this.increment * 10);
            else if (mouseButton == 2)
                this.value = minimum;

            if (value < minimum)
                value = minimum;

            return true;
        } else if (increase.mouseDown(mouseX, mouseY, mouseButton) && value < maximum)
        {
            if (mouseButton == 0)
                this.value += this.increment;
            else if (mouseButton == 1)
                this.value += (this.increment * 10);
            else if (mouseButton == 2)
                this.value = maximum;

            if (value > maximum)
                value = maximum;

            return true;
        } else return false;
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

    public enum Type
    {
        PERCENTAGE(75),
        DOUBLE(70),
        INTEGER(70);

        int minWidth;

        private Type(int width)
        {
            minWidth = width;
        }

        public int getMinWidth()
        {
            return minWidth;
        }
    }
}
