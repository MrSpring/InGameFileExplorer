package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
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

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        List<ListElement> rendering = new ArrayList<ListElement>(elements);
        ListElement selectedElement = rendering.remove(selected);

        int offset = selectedElement.draw(mouseX, mouseY, x + 1, y + 1, w - 2);
        if (expanded)
            for (ListElement element : rendering)
                offset += element.draw(mouseX, mouseY, x + 1, y + offset + 1, w - 2) + 3;
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (!expanded)
        {
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
            {
                expanded = true;
                return true;
            }
        } else
        {

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

    public class ListElement
    {
        //        GuiSimpleButton button;
        String text;
        int id;
        int height;

        public ListElement(String text, int id)
        {
//            this.button = new GuiSimpleButton(0, 0, 0, 0, text);
            this.text = text;
            this.id = id;
        }

        public int draw(int mouseX, int mouseY, int x, int y, int w)
        {
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, height))
                helper.drawShape(new Quad(
                        new Vector(x + 1, y + 1).setColor(Color.LT_GREY),
                        new Vector(x + w - 1, y + 1).setColor(Color.LT_GREY),
                        new Vector(x + w - 1, y + height + 1).setColor(Color.DK_GREY),
                        new Vector(x + 1, y + height + 1).setColor(Color.DK_GREY)
                ));
            int lines = helper.drawText(text, new Vector(x + 1, y + 1), 0xFFFFFF, false, w - 2, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            height = (lines * 9) + 2;
            return getHeight();
        }

        public int getHeight()
        {
            return height;
        }

        public void update()
        {

        }
    }
}
