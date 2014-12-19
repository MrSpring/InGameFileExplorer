package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.helper.TextHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by MrSpring on 14-11-2014 for In-Game File Explorer.
 */
public class GuiCustomTextField implements IGui
{
    int x, y, w, h;
    int cursorPos = 0;
    int selectionStartPos = 0;
    int renderStart, renderEnd;
    String text;
    boolean focused;

    public GuiCustomTextField(int x, int y, int width, int height, String startText)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.text = startText;
        renderStart = 0;
        renderEnd = Minecraft.getMinecraft().fontRendererObj.trimStringToWidth(text, w - 8).length();
    }

    private void loadRenderLimits(FontRenderer renderer)
    {
        if (this.cursorPos > renderEnd - 4)
        {
            while (this.cursorPos > renderEnd - 4 && renderEnd < text.length())
            {
                this.renderStart++;
                System.out.println("Render Start: " + renderStart + ", Render End: " + renderEnd + ", Cursor Position: " + cursorPos + ", Text Length: " + text.length());
                String cutString = text.substring(renderStart);
                renderEnd = renderStart + renderer.trimStringToWidth(cutString, w - 8).length();
            }
        } else if (this.cursorPos < renderStart + 4)
        {
            while (this.cursorPos < renderStart + 4 && renderStart > 0)
            {
                this.renderStart--;
                String cutString = text.substring(renderStart);
                renderEnd = renderStart + renderer.trimStringToWidth(cutString, w - 8).length();
            }
        }
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawIcon(DrawingHelper.hoverIcon, x, y, w, h, false);

        String trimmedString = text.substring(renderStart, renderEnd);
        char[] characters = trimmedString.toCharArray();
        int xOffset = 0;
        int selectionStart, selectionEnd;
        int textY = y + (h / 2) - 4;

        if (selectionStartPos > cursorPos)
        {
            selectionStart = cursorPos;
            selectionEnd = selectionStartPos;
        } else
        {
            selectionStart = selectionStartPos;
            selectionEnd = cursorPos;
        }

        float selectionX = 0, selectionWidth = 0;

        for (int i = 0; i < characters.length; i++)
        {
            if (i + renderStart == selectionStart)
                selectionX = xOffset + 1;

            char character = characters[i];
            minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(character), x + 4 + xOffset, textY, 0xFFFFFF);
            if (i + renderStart == cursorPos)
            {
                minecraft.fontRendererObj.drawString("|", x + xOffset + 3F, textY, 0xFF0000, false);
            } else if (i + renderStart + 1 == cursorPos)
            {
                minecraft.fontRendererObj.drawString("|", x + xOffset + 3F + minecraft.fontRendererObj.getCharWidth(character), textY, 0xFF0000, false);
            }
            if (i + renderStart == selectionEnd)
                selectionWidth = xOffset - selectionX + 1;
            xOffset += minecraft.fontRendererObj.getCharWidth(character);
        }
        if (cursorPos != selectionStartPos)
        {
            if (selectionStart < renderStart)
                selectionX = 0;
            if (selectionEnd >= renderEnd)
                selectionWidth = w - 4 - selectionX;

            if (cursorPos < selectionStartPos)
            {
                selectionX++;
                selectionWidth--;
            }

            DrawingHelper.drawQuad(x + selectionX + 2, y + 2, selectionWidth, h - 4, Color.BLUE, 0.5F);
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            focused = true;
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

    public void setCursorPos(int cursorPos, boolean moveSelection)
    {
        if (cursorPos >= 0 && cursorPos < text.length() + 1)
        {
            this.cursorPos = cursorPos;
            if (moveSelection)
            {
                if (!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))
                    this.selectionStartPos = cursorPos;
            } else this.selectionStartPos = cursorPos;
        }
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        if (keyCode == Keyboard.KEY_RIGHT)
        {
            this.setCursorPos(this.cursorPos + 1, true);
            this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
        } else if (keyCode == Keyboard.KEY_LEFT)
        {
            this.setCursorPos(this.cursorPos - 1, true);
            this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
        } else if (keyCode == Keyboard.KEY_BACK)
            this.backspace();
        else if (keyCode == Keyboard.KEY_DELETE)
            this.delete();
        else if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
            this.paste();
        else if (keyCode == Keyboard.KEY_C && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
            this.copySelection();
        else if (TextHelper.isKeyWritable(keyCode))
            this.writeCharacter(character);
    }

    private void copySelection()
    {
        StringSelection selection = new StringSelection(this.getSelected());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        clipboard.setContents(selection, selection);
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

    public void deleteSelection()
    {
        StringBuilder builder = new StringBuilder(this.text);
        int selectionStart, selectionEnd;

        if (selectionStartPos > cursorPos)
        {
            selectionStart = cursorPos;
            selectionEnd = selectionStartPos;
        } else
        {
            selectionStart = selectionStartPos;
            selectionEnd = cursorPos;
        }
        builder.delete(selectionStart, selectionEnd);
        cursorPos = selectionStart;
        selectionStartPos = cursorPos;
        text = builder.toString();
    }

    public void backspace()
    {
        if (cursorPos != selectionStartPos)
            this.deleteSelection();
        else
        {
            if (this.cursorPos > 0)
            {
                StringBuilder builder = new StringBuilder(this.text);
                builder.delete(this.cursorPos - 1, this.cursorPos);
                text = builder.toString();
                this.setCursorPos(cursorPos - 1, false);
            }
        }
    }

    public void delete()
    {
        if (cursorPos != selectionStartPos)
            this.deleteSelection();
        else
        {
            if (this.cursorPos <= this.text.length())
            {
                StringBuilder builder = new StringBuilder(this.text);
                builder.delete(this.cursorPos, this.cursorPos + 1);
                text = builder.toString();
            }
        }
    }

    public void writeCharacter(char character)
    {
        this.writeString(String.valueOf(character));
    }

    public void writeString(String string)
    {
        if (this.cursorPos != selectionStartPos)
            this.deleteSelection();
        StringBuilder builder = new StringBuilder(this.text);
        builder.insert(this.cursorPos, string);
        this.text = (builder.toString());
        this.setCursorPos(this.cursorPos + string.length(), false);
        this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
    }

    public String getSelected()
    {
        if (selectionStartPos != cursorPos)
        {
            int selectionStart, selectionEnd;

            if (selectionStartPos > cursorPos)
            {
                selectionStart = cursorPos;
                selectionEnd = selectionStartPos;
            } else
            {
                selectionStart = selectionStartPos;
                selectionEnd = cursorPos;
            }
            return text.substring(selectionStart, selectionEnd);
        } else return "";
    }
}
