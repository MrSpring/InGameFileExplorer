package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.helper.Color;
import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IDelayedDraw;
import dk.mrspring.fileexplorer.gui.interfaces.IDrawable;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.text.DecimalFormat;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiSlider implements IGui, IDelayedDraw
{
    int x, y, width, height; // TODO: Rewrite again
    int value;
    int maximum = 100, minimum = 0;
    boolean dragging = false;
    int alphaProgress = 0;
    int alphaTarget = 0;
    IDrawable latestDrawable;
    DecimalFormat format = new DecimalFormat("#.##");

    public GuiSlider(int xPos, int yPos, int width, int height, int startValue)
    {
        this.x = xPos;
        this.y = yPos;

        this.width = width;
        this.height = height;

        this.value = startValue;

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
        DrawingHelper.drawButtonThingy(x, y + 1, width, height - 2, 0, true);
        float sliderWidth = 10F, sliderHeight = height, sliderXPos = x, sliderYPos = y;
        float progress = (((float) this.getValue()) / this.maximum);
        sliderXPos += progress * this.width - (sliderWidth * progress);
        DrawingHelper.drawButtonThingy(sliderXPos, sliderYPos, sliderWidth, sliderHeight, 0, true);
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height))
            alphaTarget = 10;
        else alphaTarget = 0;

        if (alphaProgress != 0)
            DrawingHelper.drawVerticalGradient(sliderXPos + 2, sliderYPos + 2, sliderWidth - 4, sliderHeight - 4, Color.CYAN, ((float) alphaProgress) / 10 * 0.5F, Color.BLUE, ((float) alphaProgress) / 10 * 0.7F);
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

    @Override
    public IDrawable getDelayedDrawable()
    {
        return new IDrawable()
        {
            @Override
            public void draw(Minecraft minecraft, int mouseX, int mouseY)
            {
                if (GuiSlider.this.dragging)
                {
                    String line = String.valueOf(GuiSlider.this.getValue()) + "%";
                    GuiSlider.this.drawHoveringText(line, mouseX, mouseY, minecraft.fontRendererObj);
                }
            }
        };
    }

    private void drawHoveringText(String line, int mouseX, int mouseY, FontRenderer fontRendererObj)
    {
        int lineWidth = fontRendererObj.getStringWidth(line) + 7;
        DrawingHelper.drawIcon(DrawingHelper.hoverIcon, mouseX, mouseY - 16, lineWidth, 16, false);
        DrawingHelper.drawCenteredString(fontRendererObj, mouseX + (lineWidth / 2)+1, mouseY - 12, line, 0xFFFFFF, true);
    }
}
