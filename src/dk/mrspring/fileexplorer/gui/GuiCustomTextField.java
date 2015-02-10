package dk.mrspring.fileexplorer.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.helper.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Created by MrSpring on 14-11-2014 for In-Game File Explorer.
 */
public class GuiCustomTextField implements IGui // TODO: Rewrite using ClippingPlanes
{
    String text;
    int x, y, w, h;
    boolean focused, enabled = true;
    int selectionStart = 0;
    int cursorPos = 0;
    int scroll = 0;
    final int PADDING = 3;

    public GuiCustomTextField(int x, int y, int width, int height, String text)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.text = text;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        float backgroundAlpha = 0.25F;
        if (focused)
            backgroundAlpha = 0.8F;
        if (!enabled)
            backgroundAlpha = 0.25F;

        DrawingHelper.drawQuad(x + 1, y, w - 2, h, Color.BLACK, backgroundAlpha);
        DrawingHelper.drawQuad(x, y + 1, 1, h - 2, Color.BLACK, backgroundAlpha);
        DrawingHelper.drawQuad(x + w - 1, y + 1, 1, h - 2, Color.BLACK, backgroundAlpha);

        DrawingHelper.drawQuad(x + 1, y + 1, w - 2, 1, Color.WHITE, 1F);
        DrawingHelper.drawQuad(x + 1, y + h - 2, w - 2, 1, Color.LT_GREY, 1F);
        DrawingHelper.drawQuad(x + 1, y + 1, 1, h - 3, Color.WHITE, 1F);
        DrawingHelper.drawQuad(x + w - 2, y + 2, 1, h - 3, Color.LT_GREY, 1F);

        if (selectionStart != cursorPos)
        {
            int minSelectionOrCursor = Math.min(cursorPos, selectionStart);
            int maxSelectionOrCursor = Math.max(cursorPos, selectionStart);

            int selectionRenderStart = minecraft.fontRendererObj.getStringWidth(getText().substring(0, minSelectionOrCursor));
            int selectionRenderWidth = minecraft.fontRendererObj.getStringWidth(getText().substring(minSelectionOrCursor, maxSelectionOrCursor));

            GLClippingPlanes.glEnableClipping(x + PADDING - 1, x + w - PADDING + 1, y, y + h);
            DrawingHelper.drawQuad(x + PADDING + selectionRenderStart - scroll - 1, y + PADDING - 1, selectionRenderWidth + 1, h - (2 * PADDING) + 2, Color.BLUE, 0.9F);
            GLClippingPlanes.glDisableClipping();
        }

        GLClippingPlanes.glEnableClipping(x + PADDING, x + w - PADDING, y + PADDING, y + h - PADDING);
        GL11.glPushMatrix();

        minecraft.fontRendererObj.drawString(getText(), x + PADDING - scroll, y + PADDING, 0xFFFFFF, false);

        int cursorX = 0;
        if (cursorPos > 0 && cursorPos < getText().length())
            cursorX = minecraft.fontRendererObj.getStringWidth(getText().substring(0, cursorPos));
        else if (cursorPos == getText().length())
            cursorX = minecraft.fontRendererObj.getStringWidth(getText());

        GL11.glPopMatrix();

        GLClippingPlanes.glDisableClipping();

        minecraft.fontRendererObj.drawString("|", x + cursorX + PADDING - 1 - scroll, y + PADDING, 0xFF0000, false);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
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
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void writeString(String string)
    {
        StringBuilder builder = new StringBuilder(getText());
        builder.insert(cursorPos, string);
        setText(builder.toString());
        setCursorPos(cursorPos + string.length());
    }

    public void writeCharacter(char character)
    {
        this.writeString(String.valueOf(character));
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        focused = GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h);
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
        if (keyCode == Keyboard.KEY_RIGHT)
            setCursorPos(cursorPos + 1, !isShiftDown());
        else if (keyCode == Keyboard.KEY_LEFT)
            setCursorPos(cursorPos - 1, !isShiftDown());
        else if (keyCode == Keyboard.KEY_BACK)
            this.handleDelete(false);
        else if (keyCode == Keyboard.KEY_DELETE)
            this.handleDelete(true);
        else if (TextHelper.isKeyWritable(keyCode))
            this.writeCharacter(character);
    }

    private void delete(int deleteStart, int deleteEnd)
    {
        StringBuilder builder = new StringBuilder(getText());
        builder.delete(deleteStart, deleteEnd);
        setText(builder.toString());
        setCursorPos(deleteStart);
    }

    private void delete(int amount)
    {
        delete(Math.min(cursorPos + amount, cursorPos), Math.max(cursorPos + amount, cursorPos));
    }

    private void handleDelete(boolean isDeleteKey)
    {
        if (selectionStart != cursorPos)
            delete(Math.min(selectionStart, cursorPos), Math.max(selectionStart, cursorPos));
        else delete(isDeleteKey ? 1 : -1);
    }

    private boolean isShiftDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    private void pasteText()
    {
        String fromClipboard = ClipboardHelper.paste();
        this.writeString(fromClipboard);
    }

    public void setCursorPos(int cursorPos)
    {
        this.setCursorPos(cursorPos, true);
    }

    public void setCursorPos(int newCursorPosition, boolean moveSelection)
    {
        if (newCursorPosition < 0)
            this.cursorPos = 0;
        else if (newCursorPosition > getText().length())
            this.cursorPos = getText().length();
        else this.cursorPos = newCursorPosition;

        if (moveSelection)
            this.selectionStart = cursorPos;

        int renderWindowMin = scroll;
        int renderWindowMax = renderWindowMin + w - (2 * PADDING);

        int cursorRenderPosition = Minecraft.getMinecraft().fontRendererObj.getStringWidth(getText().substring(0, cursorPos));

        if (cursorRenderPosition > (renderWindowMax - (2 * PADDING)))
            scroll = cursorRenderPosition - (w - (4 * PADDING));
        else if (cursorRenderPosition < renderWindowMin + (2 * PADDING))
            scroll = cursorRenderPosition - (2 * PADDING);

        if (scroll < 0)
            scroll = 0;
    }
/*
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
            DrawingHelper.drawQuad(scrollX + 1, scrollY + 1, scrollXEnd - scrollX, 1, Color.DK_GREY, 1F);
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
*/
}
