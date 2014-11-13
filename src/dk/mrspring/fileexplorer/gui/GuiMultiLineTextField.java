package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by MrSpring on 10-11-2014 for In-Game File Explorer.
 */
public class GuiMultiLineTextField implements IGui
{
    int x, y;

    int lastWidth, lastHeight;

    int cursorPosition = 1;
    String text;

    public GuiMultiLineTextField(int xPos, int yPos, String text)
    {
        System.out.println("Constructor is called!");
        this.text = text;

        this.x = xPos;
        this.y = yPos;
    }

    public int getLastWidth()
    {
        return lastWidth;
    }

    public int getLastHeight()
    {
        return lastHeight;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int textY = 10 + y, textX = 10 + x, maxTextX = 0;
        char[] chars = text.toCharArray();
        int charPos = 0;

        int cursorPosX = 0, cursorPosY = 0;
        for (char character : chars)
        {
            charPos++;
            if (charPos == cursorPosition)
            {
                cursorPosX = textX - 1;
                cursorPosY = textY;
            }

            if (character == '\n')
            {
                textY += 10;
                textX = 10 + x;
            } else
            {
                minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(character), textX, textY, 0xFFFFFF);
                textX += minecraft.fontRendererObj.getCharWidth(character);
            }

            if (textX > maxTextX)
                maxTextX = textX;
        }
        if (cursorPosition == chars.length + 1)
        {
            cursorPosX = textX - 1/*+minecraft.fontRendererObj.getCharWidth(chars[chars.length-1])*/;
            cursorPosY = textY;
        }

//        minecraft.fontRendererObj.drawStringWithShadow("|", cursorPosX, cursorPosY, 0xAFAFAF);
        DrawingHelper.drawRect(cursorPosX, cursorPosY-1, 1, 9, Color.LTGREY, 1F);

        this.lastWidth = maxTextX + 10 - x;
        this.lastHeight = textY + 10 - y;

        DrawingHelper.drawOutline(x, y, lastWidth, lastHeight, Color.WHITE, 1F);
    }

    public String getText()
    {
        return text;
    }

    public void handleKeystroke(int keyCode, char character)
    {
        if (keyCode == Keyboard.KEY_LEFT)
        {
            if (cursorPosition > 1)
                cursorPosition--;
        } else if (keyCode == Keyboard.KEY_RIGHT)
        {
            if (cursorPosition < text.length() + 1)
                cursorPosition++;
        } else if (keyCode == Keyboard.KEY_BACK)
            removeCharacter();
        else if (keyCode == Keyboard.KEY_RETURN)
            insertCharacter('\n');
        else if (keyCode == Keyboard.KEY_DELETE)
            deleteCharacter();
        else if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))
        {
            try
            {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipboard = toolkit.getSystemClipboard();
                String fromClipboard = (String) clipboard.getData(DataFlavor.stringFlavor);
                if (fromClipboard != null)
                    this.insertString(fromClipboard);
            } catch (UnsupportedFlavorException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else if (keyCode == Keyboard.KEY_LSHIFT || keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_LCONTROL || keyCode == Keyboard.KEY_RCONTROL)
        {
        } else
            this.insertCharacter(character);
    }

    public void insertString(String toInsert)
    {
        text = new StringBuilder(text).insert(cursorPosition - 1, toInsert).toString();
        cursorPosition += toInsert.length();
    }

    public void insertCharacter(char character)
    {
        text = new StringBuilder(text).insert(cursorPosition - 1, character).toString();
        cursorPosition++;
    }

    public void removeCharacter()
    {
        if (cursorPosition > 1)
        {
            text = new StringBuilder(text).replace(cursorPosition - 2, cursorPosition - 1, "").toString();
            cursorPosition--;
            if (cursorPosition < 0)
                cursorPosition = 0;
        }
    }

    public void deleteCharacter()
    {
        if (cursorPosition < text.length() - 1)
            text = new StringBuilder(text).replace(cursorPosition - 1, cursorPosition, "").toString();
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return false;
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
