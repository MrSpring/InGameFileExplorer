package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.helper.TextHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
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
    boolean focused, enabled = true;
    int flashCount = 0;

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

    public GuiCustomTextField setEnabled(boolean isEnabled)
    {
        this.enabled = isEnabled;
        return this;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    private int getRelativeCursorPos(FontRenderer renderer)
    {
        return renderer.getStringWidth(text.substring(renderStart, cursorPos));
    }

    protected void loadRenderLimits(FontRenderer renderer)
    {
        if (renderer.getStringWidth(text) > w - 8)
        {
            int relativeCursorPos = getRelativeCursorPos(renderer);
            int minRender = 15;
            int maxRender = w - 20;
            if (relativeCursorPos < minRender && renderStart > 0)
                while (relativeCursorPos < minRender && renderStart > 0)
                {
                    renderStart--;
                    this.loadRenderEnd(renderer);
                    relativeCursorPos = getRelativeCursorPos(renderer);
                }
            if (relativeCursorPos > maxRender && renderEnd < text.length())
                while (relativeCursorPos > maxRender && renderEnd < text.length())
                {
                    renderStart++;
                    this.loadRenderEnd(renderer);
                    relativeCursorPos = getRelativeCursorPos(renderer);
                }

            this.loadRenderEnd(renderer);

            if (renderEnd > text.length())
                renderEnd = text.length();
            if (renderStart < 0)
                renderStart = 0;
        } else
        {
            renderStart = 0;
            renderEnd = text.length();
        }
    }

    private void loadRenderEnd(FontRenderer renderer)
    {
        String renderingRightNow = text.substring(renderStart);
        renderingRightNow = renderer.trimStringToWidth(renderingRightNow, w - 8);
        renderEnd = renderStart + renderingRightNow.length();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawButtonThingy(x, y, w, h, focused || !isEnabled() ? 1 : 0, isEnabled(), Color.BLACK, 0.85F, Color.BLACK, 0.85F);

        if (renderEnd > text.length() || renderStart < 0)
            this.loadRenderLimits(minecraft.fontRendererObj);

        try
        {
            String trimmedString = text.substring(renderStart, renderEnd);
            char[] characters = trimmedString.toCharArray();
            int xOffset = 0;
            int textY = y + (h / 2) - 4;
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

            float selectionX = 0, selectionWidth = 0;

            for (int i = 0; i < characters.length; i++)
            {
                if (i + renderStart == selectionStart)
                    selectionX = xOffset + 1;

                char character = characters[i];

                minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(character), x + 4 + xOffset, textY, isEnabled() ? 0xFFFFFF : 0xCCCCCC);

                if (i + renderStart == cursorPos && focused && isEnabled())
                {
                    if (!(flashCount > 10))
                        minecraft.fontRendererObj.drawString("|", x + xOffset + 3, textY, 0xFF0000, false);
                } else if (i + renderStart + 1 == cursorPos && focused && isEnabled() && !(flashCount > 10))
                {
                    if (!(flashCount > 10))
                        minecraft.fontRendererObj.drawString("|", x + xOffset + 3 + minecraft.fontRendererObj.getCharWidth(character), textY, 0xFF0000, false);
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
            this.drawScrollBar();
        } catch (Exception e)
        {
            System.err.println("Failed to render text: \"" + text + "\"!");
            e.printStackTrace();
            this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
        }
    }

    private void drawScrollBar()
    {
        if (renderEnd < text.length() || renderStart > 0)
        {
            float scrollX = x + 3, scrollY = y + h - 4, scrollXEnd = x - 3;

            float renderStartProgressThrough = ((float) renderStart) / ((float) text.length());
            float renderEndProgressThrough = ((float) renderEnd) / ((float) text.length());

            scrollX += w * renderStartProgressThrough;
            scrollXEnd += w * renderEndProgressThrough;

            DrawingHelper.drawQuad(scrollX, scrollY, scrollXEnd - scrollX, 1, Color.WHITE, 1F);
            DrawingHelper.drawQuad(scrollX + 1, scrollY + 1, scrollXEnd - scrollX, 1, Color.DKGREY, 1F);
        }
    }

    @Override
    public void update()
    {
        if (focused && isEnabled())
        {
            flashCount++;
            if (flashCount > 20)
                flashCount = 0;
        }
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        focused = GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h);

        if (focused && isEnabled())
        {
            String rendering = text.substring(renderStart, renderEnd);
            FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
            String clicked = renderer.trimStringToWidth(rendering, mouseX - x);
            setCursorPos(this.renderStart + clicked.length(), false);
            flashCount = 0;
        }

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

    public void setCursorPos(int newCursorPos, boolean moveSelection)
    {
        if (newCursorPos >= 0 && newCursorPos < text.length() + 1)
        {
            this.cursorPos = newCursorPos;
            if (moveSelection)
            {
                if (!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))
                    this.selectionStartPos = newCursorPos;
            } else this.selectionStartPos = newCursorPos;
            flashCount = 0;
        }
        this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        if (focused && isEnabled())
            if (keyCode == Keyboard.KEY_RIGHT)
            {
                this.setCursorPos(this.cursorPos + 1, true);
            } else if (keyCode == Keyboard.KEY_LEFT)
            {
                this.setCursorPos(this.cursorPos - 1, true);
            } else if (keyCode == Keyboard.KEY_BACK)
                this.backspace();
            else if (keyCode == Keyboard.KEY_DELETE)
                this.delete();
            else if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
                this.paste();
            else if (keyCode == Keyboard.KEY_C && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
                this.copySelection();
            else if (keyCode == Keyboard.KEY_X && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)))
                this.cut();
            else if (keyCode == Keyboard.KEY_RETURN)
                this.focused = false;
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

    private void cut()
    {
        this.copySelection();
        this.deleteSelection();
    }

    public void setText(String text)
    {
        this.text = text;
        flashCount = 0;
    }

    public String getText()
    {
        return text;
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
        this.setCursorPos(selectionStart, true);
        selectionStartPos = cursorPos;
        this.setText(builder.toString());
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
                this.setText(builder.toString());
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
                this.setText(builder.toString());
//                this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
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
        this.setText(builder.toString());
        this.setCursorPos(this.cursorPos + string.length(), false);
//        this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
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

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setW(int w)
    {
        this.w = w;
        this.loadRenderLimits(Minecraft.getMinecraft().fontRendererObj);
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public boolean isFocused()
    {
        return focused;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getW()
    {
        return w;
    }

    public int getH()
    {
        return h;
    }
}
