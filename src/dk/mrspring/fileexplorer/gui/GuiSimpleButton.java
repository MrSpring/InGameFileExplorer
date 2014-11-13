package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiSimpleButton implements IGui
{
    int x, y, width, height;
    String text;

    boolean wasMouseHoveringLastFrame = false, isEnabled = true,highlight=false;
    int alphaProgress = 0;
    int alphaTarget = 0;

    public GuiSimpleButton(int xPos, int yPos, int width, int height, String message)
    {
        this.x = xPos;
        this.y = yPos;

        this.width = width;
        this.height = height;

        this.text = message;
    }

    public void disable()
    {
        this.isEnabled = false;
    }

    public void enable()
    {
        this.isEnabled = true;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawButtonThingy(x, y, width, height, Color.BLACK, 0.25F, Color.WHITE, 1F);
        wasMouseHoveringLastFrame = GuiHelper.isMouseInBounds(mouseX, mouseY, this.x, this.y, this.width, this.height);
        if (wasMouseHoveringLastFrame)
            alphaTarget = 10;
        else alphaTarget = 0;
        if (alphaProgress != 0 && isEnabled)
            DrawingHelper.drawVerticalGradient(x + 2, y + 2, width - 4, height - 4, Color.CYAN, ((float) alphaProgress) / 10 * 0.25F, Color.BLUE, ((float) alphaProgress) / 10 * 0.5F);

        int textColor = 0xFFFFFF;
        if (!isEnabled)
        {
            textColor = 0xAFAFAF;
            DrawingHelper.drawVerticalGradient(x + 2, y + 2, width - 4, height - 4, Color.LTGREY, 0.5F, Color.DKGREY, 0.8F);
        }

        float textY = (this.height / 2 - 4) + y, textX = (this.width / 2 - (minecraft.fontRendererObj.getStringWidth(this.text) / 2)) + x;
        minecraft.fontRendererObj.drawStringWithShadow(this.text, textX, textY, textColor);
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void makeHighlighted()
    {
        this.highlight=true;
    }

    public void stopBeingHighlighted()
    {
        this.highlight=false;
    }

    public boolean isHighlighted()
    {
        return highlight;
    }

    public void toggleHightlight()
    {
        this.highlight=!this.highlight;
    }

    @Override
    public void update()
    {
        if (highlight)
            alphaTarget=15;
        if (alphaProgress < alphaTarget)
            alphaProgress += 5;
        else if (alphaProgress > alphaTarget)
            alphaProgress -= 1;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height) && isEnabled)
        {
            alphaProgress +=5;
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
}
