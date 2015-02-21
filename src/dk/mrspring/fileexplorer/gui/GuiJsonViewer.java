package dk.mrspring.fileexplorer.gui;

import com.google.gson.internal.LinkedTreeMap;
import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by MrSpring on 19-12-2014 for In-Game File Explorer.
 */
public class GuiJsonViewer implements IGui, IMouseListener
{
    int x, y, width, height, jsonHeight, scrollHeight = 0;
    File jsonFile;
    Map<String, Object> jsonObject;

    public GuiJsonViewer(int x, int y, int width, int height, File file)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.jsonFile = file;

        try
        {
            String jsonCode = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(jsonFile);
            jsonObject = LiteModFileExplorer.core.getJsonHandler().loadFromJson(jsonCode, LinkedTreeMap.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public GuiJsonViewer(int x, int y, int width, int height, Map<String, Object> objectMap)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.jsonObject = objectMap;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        GLClippingPlanes.glEnableClipping(x, x + width, y, y + height);
        int yOffset = -scrollHeight, xOffset = 0;
        try
        {
            if (jsonHeight > height)
            {
                this.drawScrollBar();
                xOffset = 5;
            }
            this.jsonHeight = 0;
            for (Map.Entry<String, Object> entry : this.jsonObject.entrySet())
            {
                int objectHeight = this.drawObject(minecraft, xOffset, yOffset, entry.getKey(), entry.getValue());
                yOffset += objectHeight;
                this.jsonHeight += objectHeight;
            }

        } catch (StackOverflowError error)
        {
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            helper.drawShape(new Quad(x, y, width, height).setColor(Color.BLACK));
            helper.drawText(TranslateHelper.translate("gui.json_editor.not_enough_space").replace("\\n", "\n"), new Vector(width / 2 + x, y + 15), 0xFFFFFF, false, width - 8, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
        }
        GLClippingPlanes.glDisableClipping();
    }

    private void drawScrollBar()
    {
        float scrollBarYRange = (height - 40);
        float maxScrollHeight = getMaxScrollHeight();
        float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
        float scrollBarY = scrollBarYRange * scrollProgress;
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        helper.drawShape(new Quad(x + 1, y + scrollBarY + 1, 1, 40).setColor(Color.DK_GREY));
        helper.drawShape(new Quad(x, y + scrollBarY, 1, 40).setColor(Color.WHITE));
    }

    /**
     * Renders the Object and returns its display height.
     *
     * @param minecraft The Minecraft game object, used to get the Font Renderer for rendering text.
     * @param xOffset   X offset to render the object at, used for indentation.
     * @param yOffset   Y offset to render the object at.
     * @param name      The name of the object being rendered.
     * @param object    The object being rendered.
     * @return Returns the height of the rendered object, which gets added to the yOffset to render the next object below this.
     */
    private int drawObject(Minecraft minecraft, int xOffset, int yOffset, String name, Object object)
    {
        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        if (object instanceof String || object instanceof Boolean || object instanceof Number)
        {
            return this.drawSimpleTextValue(minecraft.fontRendererObj, name, object, x + xOffset, y + yOffset);
        } else if (object instanceof ArrayList)
        {
            ArrayList<Object> list = (ArrayList<Object>) object;
            int height = 11;

            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            for (int i = 0; i < list.size(); i++)
            {
                String entryName = String.valueOf(i);
                Object entryObject = list.get(i);
                int objectHeight = this.drawObject(minecraft, xOffset + 5, yOffset + height, entryName, entryObject);
                float lineHeight = objectHeight;
                if (i == list.size() - 1)
                    lineHeight = 5F;
                helper.drawShape(new Quad(x + xOffset + 1, y + yOffset + height + 1, 1, lineHeight).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(x + xOffset, y + yOffset + height, 1, lineHeight).setColor(Color.WHITE));

                helper.drawShape(new Quad(x + xOffset + 2, y + yOffset + height + 5, 3, 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(x + xOffset + 1, y + yOffset + height + 4, 3, 1).setColor(Color.WHITE));
                height += objectHeight;
            }
            return height;
        } else if (object instanceof LinkedTreeMap)
        {
            LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap<String, Object>) object;
            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            int height = 11;
            Iterator<Map.Entry<String, Object>> iterator = treeMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, Object> entry = iterator.next();
                String entryName = entry.getKey();
                Object entryObject = entry.getValue();
                int objectHeight = this.drawObject(minecraft, xOffset + 5, yOffset + height, entryName, entryObject);
                float lineHeight = objectHeight;
                if (!iterator.hasNext())
                    lineHeight = 5F;
                helper.drawShape(new Quad(x + xOffset + 1, y + yOffset + height + 1, 1, lineHeight).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(x + xOffset, y + yOffset + height, 1, lineHeight).setColor(Color.WHITE));

                helper.drawShape(new Quad(x + xOffset + 2, y + yOffset + height + 5, 3, 1).setColor(Color.DK_GREY));
                helper.drawShape(new Quad(x + xOffset + 1, y + yOffset + height + 4, 3, 1).setColor(Color.WHITE));
                height += objectHeight;
            }
            return height;
        } else return 0;
    }

    private int drawSimpleTextValue(FontRenderer renderer, String name, Object object, int x, int y)
    {
        int nameWidth = renderer.getStringWidth(name + ": ");
        renderer.drawString(name + ": ", x, y, 0xFFFFFF, true);
        return (9 * LiteModFileExplorer.core.getDrawingHelper().drawText(String.valueOf(object), new Vector(x + nameWidth, y), 0xFFFFFF, true, width - nameWidth - (x - this.x), DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP)) + 2;
    }

    @Override
    public void update()
    {
        if (jsonHeight < height)
            scrollHeight = 0;
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

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
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
        return jsonHeight - height + 10;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }
}
