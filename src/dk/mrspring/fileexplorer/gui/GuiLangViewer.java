package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.EditorLang;
import dk.mrspring.fileexplorer.gui.editor.lang.Lang;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Created by Konrad on 18-02-2015.
 */
public class GuiLangViewer implements IGui, IMouseListener
{
    Lang lang;
    int x, y, w, h, scroll, langHeight;

    public GuiLangViewer(int x, int y, int width, int height, Lang lang)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.lang = lang;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        List<Lang.ILangElement> map = lang.getElements();
        int yOffset = -scroll, xOffset = 0;
        LiteModFileExplorer.core.getDrawingHelper().drawVerticalLine(new Vector(x + (w / 2) - 2, y), h, 1, true);
        if (langHeight > h)
        {
            this.drawScrollBar();
            xOffset = 5;
        }
        langHeight = 0;
        for (Lang.ILangElement entry : map)
        {
            if (entry instanceof Lang.LangElement)
            {
                Lang.LangElement element = (Lang.LangElement) entry;
                int height = drawElement(element.getKey(), element.getValue(), x + xOffset, y + yOffset, w - xOffset) +2;
                yOffset += height;
                langHeight += height;
            } else if (entry instanceof Lang.SpacerElement)
            {
                yOffset += 8;
                langHeight += 8;
            } else if (entry instanceof Lang.CommentElement)
            {
                Lang.CommentElement element = (Lang.CommentElement) entry;
                int height = drawComment(element.getComment(), x + xOffset, y + yOffset, w - xOffset);
                yOffset += height;
                langHeight += height;
            }
        }
    }


    private void drawScrollBar()
    {
        float scrollBarYRange = (h - 40);
        float maxScrollHeight = getMaxScrollHeight();
        float scrollProgress = (float) this.scroll / maxScrollHeight;
        float scrollBarY = scrollBarYRange * scrollProgress;
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        helper.drawShape(new Quad(x, y + scrollBarY + 1, 2, 40).setColor(Color.DK_GREY));
        helper.drawShape(new Quad(x - 1, y + scrollBarY, 2, 40).setColor(Color.WHITE));
    }

    private int drawComment(String comment, int x, int y, int totalWidth)
    {
        int sideWidth = ((totalWidth - 10) / 2);
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        helper.drawHorizontalLine(new Vector(x + 5, y), sideWidth - 10, 1, true);
        int lines = helper.drawText(comment, new Vector(x, y + 3), 0xFFFFFF, true, sideWidth, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        helper.drawHorizontalLine(new Vector(x + 5, y + (lines * 9) + 5), sideWidth - 10, 1, true);
        return lines * 9 + 9;
    }

    private int drawElement(String key, String value, int x, int y, int totalWidth)
    {
        int sideWidth = ((totalWidth - 10) / 2);
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        int keyLines = helper.drawText(key, new Vector(x, y), 0xFFFFFF, true, sideWidth, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        int valueLines = helper.drawText(value, new Vector(x + sideWidth + 10, y), 0xFFFFFF, true, sideWidth, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
        return Math.max(keyLines * 9, valueLines * 9);
    }

    public void addScroll(int amount)
    {
        int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scroll + amount;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scroll = scrollHeightAfterAddition;
    }

    private int getMaxScrollHeight()
    {
        return langHeight - h + 10;
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

    public void setLang(Lang lang)
    {
        this.lang = lang;
    }

    public Lang getLang()
    {
        return lang;
    }

    public GuiLangViewer setX(int x)
    {
        this.x = x;
        return this;
    }

    public GuiLangViewer setY(int y)
    {
        this.y = y;
        return this;
    }

    public GuiLangViewer setWidth(int w)
    {
        this.w = w;
        return this;
    }

    public GuiLangViewer setHeight(int h)
    {
        this.h = h;
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
        return w;
    }

    public int getHeight()
    {
        return h;
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
}
