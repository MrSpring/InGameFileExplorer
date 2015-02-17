package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.llcore.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MrSpring on 16-02-2015 for MC Music Player.
 */
public class GuiDropDownList implements IGui
{
    int x, y, w, h;
    List<ListElement> elements;
    int selected;
    boolean expanded = false;

    public GuiDropDownList(int x, int y, int width, int height, int selected, ListElement... elements)
    {
        this.x = x;
        this.y = y;
        this.h = height;
        this.w = width;
        this.elements = Arrays.asList(elements);
        this.selected = selected;
    }

    public ListElement getSelectedElement()
    {
        return this.elements.get(selected);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        List<ListElement> rendering = new ArrayList<ListElement>(elements);
        ListElement selectedElement = rendering.remove(selected);
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

        helper.drawButtonThingy(new Quad(x, y, w, h), 0, false);
        int offset = selectedElement.draw(mouseX, mouseY, x + 1, y + 2, w - 2) + 3;
        if (expanded)
        {
            final int LINE_SPACING = 6;
            helper.drawShape(new Quad(x + LINE_SPACING, y + offset - 2, w - (2 * LINE_SPACING), 1).setColor(Color.WHITE));
            for (ListElement element : rendering)
            {
                helper.drawShape(new Quad(x + LINE_SPACING, y + offset, w - (2 * LINE_SPACING), 1));
                offset += element.draw(mouseX, mouseY, x + 1, y + offset + 1, w - 2);
            }
            offset += 3;
        }
        h = offset;

        if (LiteModFileExplorer.config.showArrowOnDropDowns)
        {
            float iconSize = Math.min(selectedElement.getHeight() - 4, 15);
            Icon icon = LiteModFileExplorer.core.getIcon("arrow_down");
            if (expanded)
                icon = LiteModFileExplorer.core.getIcon("arrow_up");
            helper.drawIcon(icon, new Quad(x + w - iconSize - 2, y + (selectedElement.getHeight() / 2) - (iconSize / 2) + 1, iconSize, iconSize));
        }
    }

    @Override
    public void update()
    {
        if (expanded)
            for (ListElement element : elements)
                element.update();
        else getSelectedElement().update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        ListElement selectedElement = getSelectedElement();
        if (!expanded)
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, selectedElement.getHeight()))
            {
                expanded = true;
                return true;
            } else return false;
        } else
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, selectedElement.getHeight()))
            {
                expanded = false;
                return true;
            } else
            {
                int offset = selectedElement.getHeight() + 3;
                for (int i = 0; i < elements.size(); i++)
                {
                    if (i != selected)
                    {
                        ListElement element = elements.get(i);
                        int elementHeight = element.getHeight();
                        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y + offset, w, elementHeight))
                        {
                            this.selected = i;
                            this.expanded = false;
                            return true;
                        }
                        offset += elementHeight;
                    }
                }
                return false;
            }
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

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }

    public static class ListElement
    {
        String text;
        Object id;
        int height;
        int alphaProgress = 0;
        int alphaTarget = 0;

        public ListElement(String text, Object id)
        {
            this.text = text;
            this.id = id;
        }

        public <T> T getIdentifier()
        {
            return (T) id;
        }

        public int draw(int mouseX, int mouseY, int x, int y, int w)
        {
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, height))
            {
                alphaTarget = 10;
            } else alphaTarget = 0;
            helper.drawShape(new Quad(
                    new Vector(x + 2, y + 1).setColor(Color.CYAN),
                    new Vector(x + w - 2, y + 1).setColor(Color.CYAN),
                    new Vector(x + w - 2, y + height - 2).setColor(Color.BLUE),
                    new Vector(x + 2, y + height - 2).setColor(Color.BLUE)
            ).setAlpha((float) alphaProgress / 15));
            int lines = helper.drawText(text, new Vector(x + 4, y + 3), 0xFFFFFF, false, w - 2, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            height = (lines * 9) + 5;
            return getHeight();
        }

        public int getHeight()
        {
            return height;
        }

        public void update()
        {
            if (alphaProgress < alphaTarget)
                alphaProgress = Math.min(alphaProgress + 5, alphaTarget);
            else if (alphaProgress > alphaTarget)
                alphaProgress = Math.max(alphaProgress - 1, alphaTarget);
        }
    }
}
