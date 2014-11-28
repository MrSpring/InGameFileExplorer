package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiSlider implements IGui
{
    int x, y, width, height;
    Type type;
    int value;
    int maximum = 100, minimum = 0;
    boolean dragging = false;
    int alphaProgress = 0;
    int alphaTarget = 0;
    DecimalFormat format = new DecimalFormat("#.##");

    public GuiSlider(int xPos, int yPos, int width, int height, Type type, int startValue)
    {
        this.x = xPos;
        this.y = yPos;

        this.width = width;
        this.height = height;

        this.value = startValue;

        this.type = type;

        if (value > maximum)
            value = maximum;
        else if (value < minimum)
            value = minimum;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawButtonThingy(x, y + 1, width, height - 2, Color.BLACK, 0.25F, Color.WHITE, 1F);
        float sliderWidth = 10F, sliderHeight = height, sliderXpos = x, sliderYpos = y;
        float progress = (((float) this.getValue()) / this.maximum);
        sliderXpos += progress * this.width - (sliderWidth * progress);
        DrawingHelper.drawButtonThingy(sliderXpos, sliderYpos, sliderWidth, sliderHeight, Color.BLACK, 0.25F, Color.WHITE, 1F);
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height))
            alphaTarget = 10;
        else alphaTarget = 0;

        if (alphaProgress != 0)
            DrawingHelper.drawVerticalGradient(sliderXpos + 2, sliderYpos + 2, sliderWidth - 4, sliderHeight - 4, Color.CYAN, ((float) alphaProgress) / 10 * 0.5F, Color.BLUE, ((float) alphaProgress) / 10 * 0.7F);

        if (dragging)
        {
            switch (this.type)
            {
                case PERCENTAGE:
                    minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(this.getValue()) + "%", mouseX, mouseY-10, 0xFFFFFF);
                    break;
                case DOUBLE:
                    double dValue=((double)this.getValue())/100;
                    minecraft.fontRendererObj.drawStringWithShadow(format.format(dValue), mouseX, mouseY, 0xFFFFFF);
                    break;
            }
        }
    }

    @Override
    public void update()
    {
        if (dragging)
        {
            alphaTarget = 15;
        }

        if (alphaProgress < alphaTarget)
            alphaProgress += 5;
        else if (alphaProgress > alphaTarget)
            alphaProgress -= 1;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height) && mouseButton == 0)
        {
            dragging = true;
            return true;
        } else return false;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {
        dragging = false;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {
        if (mouseButton == 0 && dragging)
        {
            float progress = (((float) mouseX) - x) / width;
            value = (int) (progress * maximum);

            if (value > maximum)
                value = maximum;
            else if (value < minimum)
                value = minimum;
        }
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    public enum Type
    {
        PERCENTAGE,
        DOUBLE
    }
}
