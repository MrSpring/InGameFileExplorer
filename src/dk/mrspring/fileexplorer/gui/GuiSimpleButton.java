package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.helper.IIcon;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiSimpleButton implements IGui
{
    int x, y, width, height;
    String text;
    IIcon icon;

    boolean wasMouseHoveringLastFrame = false, isEnabled = true, highlight = false, drawBackground = true, autoHeight = false;
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

    public GuiSimpleButton setAutoHeight(boolean auto)
    {
        this.autoHeight = auto;
        return this;
    }

    public GuiSimpleButton setIcon(IIcon icon)
    {
        this.icon = icon;
        return this;
    }

    public IIcon getIcon()
    {
        return icon;
    }

    public GuiSimpleButton disable()
    {
        this.isEnabled = false;
        return this;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void enable()
    {
        this.isEnabled = true;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        wasMouseHoveringLastFrame = GuiHelper.isMouseInBounds(mouseX, mouseY, this.x, this.y, this.width, this.height);
        if (wasMouseHoveringLastFrame)
            alphaTarget = 10;
        else alphaTarget = 0;

        int textColor = 0xFFFFFF;
        DrawingHelper.drawButtonThingy(x, y, width, height, (float) alphaProgress / 10, isEnabled);
        if (!isEnabled)
            textColor = 0xAFAFAF;

        if (icon != null)
            DrawingHelper.drawIcon(this.getIcon(), x + 2, y + 2, width - 4, height - 4, false);

        String translatedText = StatCollector.translateToLocal(this.text);

        int textY = (this.height / 2) + y, textX = (this.width / 2) + x;
//        minecraft.fontRendererObj.drawStringWithShadow(translatedText, textX, textY, textColor);
        int lines = DrawingHelper.drawSplitCenteredString(minecraft.fontRendererObj, textX, textY, translatedText, textColor, true, width - 6, true);
        if (autoHeight)
            this.setHeight(lines * 9 + 6);
    }

    public GuiSimpleButton hideBackground()
    {
        this.drawBackground = false;
        return this;
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
        this.highlight = true;
    }

    public void setHighlight(boolean highlight)
    {
        this.highlight = highlight;
    }

    public void stopBeingHighlighted()
    {
        this.highlight = false;
    }

    public boolean isHighlighted()
    {
        return highlight;
    }

    public void toggleHightlight()
    {
        this.highlight = !this.highlight;
    }

    @Override
    public void update()
    {
        if (highlight)
            alphaTarget = 15;
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
            alphaProgress = 9;
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

    public void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
    }
}
