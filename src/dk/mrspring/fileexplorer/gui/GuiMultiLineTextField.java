package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.helper.TextHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * Created by MrSpring on 10-11-2014 for In-Game File Explorer.
 */
public class GuiMultiLineTextField implements IGui, IMouseListener
{
    int x, y, w, h;
    String text;
    String line;
    boolean focused, drawBackground = true;
    int cursorPos;
    int flashCount = 0;
    int padding = 4;
    int scrollHeight = 0;
    int lines = 0;

    int cursorLine = 0, cursorRelativePos = 0;

    public GuiMultiLineTextField(int x, int y, int w, int h, String text)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
        this.focused = false;
        this.cursorPos = 0;
        this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
    }

    public GuiMultiLineTextField hideBackground()
    {
        drawBackground = false;
        padding = 0;
        return this;
    }

    private void loadCursorPosition(FontRenderer renderer)
    {
        int xOffset = 0;
        if (lines * 9 > h)
            xOffset += 4;

        List<String> lines = renderer.listFormattedStringToWidth(text, w - (padding * 2) - xOffset);
        int lineStartIndexInText = 0;
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i) + "\n";
            if (cursorPos >= lineStartIndexInText + line.length())
            {
                lineStartIndexInText += line.length();
            } else
            {
                cursorRelativePos = cursorPos - lineStartIndexInText;
                cursorLine = i;
                this.line = (String) renderer.listFormattedStringToWidth(text, w - (padding * 2) - xOffset).get(cursorLine);
                this.flashCount = 0;
                break;
            }
        }
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int xOffset = 0, yOffset = -scrollHeight;
        if (lines * 9 > h)
        {
            this.drawScrollBar();
            xOffset += 5;
        }

        if (drawBackground)
            DrawingHelper.drawButtonThingy(x, y, w, h, focused ? 1 : 0, true, Color.BLACK, 0.85F, Color.BLACK, 0.85F);

//        minecraft.fontRendererObj.drawString("Cursor Line: " + cursorLine, x + padding + xOffset, y + padding + yOffset, 0xFFFFFF, true);
//        minecraft.fontRendererObj.drawString("Cursor Pos: " + cursorPos, x + padding + xOffset, y + 10 + padding + yOffset, 0xFFFFFF, true);
//        minecraft.fontRendererObj.drawString("Cursor Relative Pos: " + cursorRelativePos, x + padding + xOffset, y + 20 + padding + yOffset, 0xFFFFFF, true);

        lines = DrawingHelper.drawSplitString(minecraft.fontRendererObj, x + padding + xOffset, y + padding + yOffset, text, 0xFFFFFF, w - (padding * 2) - xOffset, true);

        String cutLine = line.substring(0, cursorRelativePos);

        int cursorXOffset = minecraft.fontRendererObj.getStringWidth(cutLine)+xOffset;

        if (focused && !(flashCount > 10))
        {
            DrawingHelper.drawQuad(x + cursorXOffset + padding-1, y + (cursorLine * 9) + padding + yOffset-1, 1, 9, Color.GREEN, 1F);
        }

    }

    private void drawScrollBar()
    {
        float scrollBarYRange = (h - 40);
        float maxScrollHeight = getMaxScrollHeight();
        float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
        float scrollBarY = scrollBarYRange * scrollProgress;
        DrawingHelper.drawQuad(x, y + scrollBarY + 1, 2, 40, Color.DKGREY, 1F);
        DrawingHelper.drawQuad(x - 1, y + scrollBarY, 2, 40, Color.WHITE, 1F);
    }

    @Override
    public void update()
    {
        flashCount++;
        if (flashCount > 20)
            flashCount = 0;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        this.focused = GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h);
        this.flashCount = 0;
        return focused;
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
        if (focused)
            if (keyCode == Keyboard.KEY_RIGHT)
                this.setCursorPos(this.cursorPos + 1);
            else if (keyCode == Keyboard.KEY_LEFT)
                this.setCursorPos(this.cursorPos - 1);
            else if (keyCode == Keyboard.KEY_BACK)
                this.backspace();
            else if (keyCode == Keyboard.KEY_DELETE)
                this.delete();
            else if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
                this.paste();
            else if (keyCode == Keyboard.KEY_RETURN)
                this.writeCharacter('\n');
            else if (TextHelper.isKeyWritable(keyCode))
                this.writeCharacter(character);
    }

    public void writeCharacter(char character)
    {
        this.writeString(String.valueOf(character));
    }

    public void writeString(String string)
    {
        StringBuilder builder = new StringBuilder(this.text);
        builder.insert(this.cursorPos, string);
        this.setText(builder.toString());
        this.setCursorPos(this.cursorPos + string.length());
    }

    private void paste()
    {
        try
        {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            String fromClipboard = (String) clipboard.getData(DataFlavor.stringFlavor);
            if (fromClipboard != null)
                this.writeString(fromClipboard);
        } catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void backspace()
    {
        if (this.cursorPos > 0)
        {
            StringBuilder builder = new StringBuilder(this.text);
            builder.delete(this.cursorPos - 1, this.cursorPos);
            this.setText(builder.toString());
            this.setCursorPos(cursorPos - 1);
        }
    }

    private void delete()
    {
        if (this.cursorPos <= this.text.length())
        {
            StringBuilder builder = new StringBuilder(this.text);
            builder.delete(this.cursorPos, this.cursorPos + 1);
            this.setText(builder.toString());
        }
    }

    private void setCursorPos(int cursorPos)
    {
        this.cursorPos = cursorPos;

        if (this.cursorPos < 0)
            this.cursorPos = 0;
        if (this.cursorPos > text.length())
            this.cursorPos = text.length();

        this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
    }

    public void setText(String text, boolean resetCursorPos)
    {
        this.text = text;
        this.flashCount = 0;
        if (resetCursorPos)
        {
            this.cursorPos = 0;
            this.loadCursorPosition(Minecraft.getMinecraft().fontRendererObj);
        }
    }

    public void setText(String text)
    {
        this.setText(text, false);
    }

    public void setWidth(int width)
    {
        this.w = width;
    }

    public void setHeight(int height)
    {
        this.h = height;
    }

    public String getText()
    {
        return text;
    }

    public void addScroll(int amount)
    {
        int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scrollHeight + amount;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scrollHeight = scrollHeightAfterAddition;
    }

    private int getMaxScrollHeight()
    {
        return (lines * 9) - h + padding + padding;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }
/*
    int x, y;

    int lastWidth, lastHeight;

    int cursorPosition = 1;
    String text;

    public GuiMultiLineTextField(int xPos, int yPos, String text)
    {
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
            cursorPosX = textX - 1*/
/*+minecraft.fontRendererObj.getCharWidth(chars[chars.length-1])*//*
;
            cursorPosY = textY;
        }

//        minecraft.fontRendererObj.drawStringWithShadow("|", cursorPosX, cursorPosY, 0xAFAFAF);
        DrawingHelper.drawQuad(cursorPosX, cursorPosY-1, 1, 9, Color.LTGREY, 1F);

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

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }
*/
}
