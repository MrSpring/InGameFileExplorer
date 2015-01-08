package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiNumberField implements IGui
{
    int x, y, w, h;
    int flashCount;
    boolean focused;
    BigDecimal value;
    int cursorPosition;
    DecimalFormat format;

    int cursorMin, cursorMax;

    public GuiNumberField(int x, int y, int w, int h, double startValue)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        setFormat(new DecimalFormat(LiteModFileExplorer.config.number_format));
        String formatArray = new StringBuilder(LiteModFileExplorer.config.number_format).reverse().toString();

        char character;
        if (formatArray.contains("."))
            character = '.';
        else character = ',';

        cursorMin = -formatArray.indexOf(character);
        cursorMax = formatArray.length() + cursorMin - 1;

        setCursorPosition(1);
        flashCount = 0;

        value = new BigDecimal(startValue);
    }

    public GuiNumberField(int x, int y, int w, int h)
    {
        this(x, y, w, h, 0.0);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawButtonThingy(x, y, w, h, focused ? 1 : 0, true, Color.BLACK, 0.85F, Color.BLACK, 0.85F);

        String formattedDouble = format.format(value.doubleValue());
        char[] characters = formattedDouble.toCharArray();
        ArrayUtils.reverse(characters);

        int xOffset = -10;
        int controllerOffset = 0;
        for (char character : characters)
        {
            if (character == ',' || character == '.')
                controllerOffset = xOffset;

            DrawingHelper.drawSplitCenteredString(minecraft.fontRendererObj, x + w + xOffset, h / 2 + y - 4, String.valueOf(character), 0xFFFFFF, 100);
            xOffset -= 8;
        }

        if (!(flashCount > 10) && focused)
            drawControllers(controllerOffset - (cursorPosition * 8));
    }

    private void drawControllers(int xOffset)
    {
        DrawingHelper.drawIcon(DrawingHelper.downArrow, x + w + xOffset - 3, h / 2 + y + 5, 6, 6, false);
        DrawingHelper.drawIcon(DrawingHelper.upArrow, x + w + xOffset - 3, h / 2 + y - 4 - 8, 6, 6, false);
    }

    @Override
    public void update()
    {
        flashCount++;

        int maxFlash = 20;

        if (flashCount > maxFlash)
            flashCount = 0;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        setFocused(GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h));
        return isFocused();
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        if (focused)
        {
            if (keyCode == Keyboard.KEY_LEFT && cursorPosition + 1 <= cursorMax)
            {
                cursorPosition++;
                if (cursorPosition == 0)
                    cursorPosition++;
                flashCount = 0;
            } else if (keyCode == Keyboard.KEY_RIGHT && cursorPosition - 1 >= cursorMin)
            {
                cursorPosition--;
                if (cursorPosition == 0)
                    cursorPosition--;
                flashCount = 0;
            } else if (keyCode == Keyboard.KEY_UP)
                this.increase();
            else if (keyCode == Keyboard.KEY_DOWN)
                this.decrease();
            else if (keyCode == Keyboard.KEY_RETURN)
                this.setFocused(false);
        }
    }

    private void decrease()
    {
        if (cursorPosition != 0)
            if (cursorPosition > 0)
            {
                double change = -Math.pow(10, cursorPosition - 1);
                value = value.add(BigDecimal.valueOf(change));
                flashCount = 0;
            } else
            {
                double change = -Math.pow(10, cursorPosition);
                value = value.add(BigDecimal.valueOf(change));
                flashCount = 0;
            }
    }

    private void increase()
    {
        if (cursorPosition != 0)
            if (cursorPosition > 0)
            {
                double change = Math.pow(10, cursorPosition - 1);
                value = value.add(BigDecimal.valueOf(change));
                flashCount = 0;
            } else
            {
                double change = Math.pow(10, cursorPosition);
                value = value.add(BigDecimal.valueOf(change));
                flashCount = 0;
            }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    public boolean isFocused()
    {
        return focused;
    }

    public GuiNumberField setFormat(DecimalFormat format)
    {
        this.format = format;
        return this;
    }

    public GuiNumberField setFocused(boolean focused)
    {
        this.focused = focused;
        this.flashCount = 0;
        return this;
    }

    public double getValue()
    {
        return value.doubleValue();
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    public void setCursorPosition(int cursorPosition)
    {
        this.cursorPosition = cursorPosition;
    }
}
