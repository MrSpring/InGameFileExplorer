package dk.mrspring.fileexplorer.gui;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by MrSpring on 19-12-2014 for In-Game File Explorer.
 */
public class GuiJsonViewer implements IGui
{
    int x, y, width, height;
    Map<String, Object> jsonObject;

    public GuiJsonViewer(int x, int y, int width, int height, File jsonFile)
    {
        System.out.println("Creating json Editor");

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try
        {
            FileReader reader = new FileReader(jsonFile);
            Gson gson = new Gson();
            Type stringStringMap = new TypeToken<Map<String, Object>>()
            {
            }.getType();
            jsonObject = gson.fromJson(reader, stringStringMap);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int yOffset = 0;
        for (Map.Entry<String, Object> entry : this.jsonObject.entrySet())
        {
            yOffset += this.drawObject(minecraft, 0, yOffset, entry.getKey(), entry.getValue()) + 2;
        }
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
//        System.out.println("Drawing object: " + name + ", of type: " + object.getClass().getName() + " at Y: " + yOffset);
        if (object instanceof String || object instanceof Boolean || object instanceof Double)
        {
            this.drawSimpleTextValue(minecraft.fontRendererObj, name, object, x + xOffset, y + yOffset);
            return 10;
        } else if (object instanceof ArrayList)
        {
            ArrayList<Object> list = (ArrayList<Object>) object;
            int height = 10;

            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            for (int i = 0; i < list.size(); i++)
            {
                this.drawSimpleTextValue(minecraft.fontRendererObj, String.valueOf(i), list.get(i), x + xOffset + 5, y + yOffset + height);
                height += 10;
            }
            return height;

            /*ArrayList list = (ArrayList) object;
            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            yOffset += 10;
            DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + 1, 1, 3l, Color.DKGREY, 1F);
            int yOffsetAtListStart = yOffset;
            for (int i = 0, listSize = list.size(); i < listSize; i++)
            {
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + 4, 3, 1, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset, y + yOffset + 3, 3, 1, Color.WHITE, 1F);
                if (i < listSize - 1)
                    DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + 5, 1, 9, Color.DKGREY, 1F);
                Object listObject = list.get(i);
                this.drawObject(minecraft, xOffset + 5, yOffset, String.valueOf(i), listObject);
                if (i < listSize - 1)
                    yOffset += 10;
            }
            DrawingHelper.drawQuad(x + xOffset, y + yOffsetAtListStart, 1, yOffset - yOffsetAtListStart + 4, Color.WHITE, 1F);*/
        } else if (object instanceof LinkedTreeMap)
        {
            LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap<String, Object>) object;
            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            yOffset += 10;
            for (Map.Entry<String, Object> entry : treeMap.entrySet())
            {
                String entryName = entry.getKey();
                Object entryObject = entry.getValue();
                this.drawObject(minecraft, xOffset + 5, yOffset, entryName, entryObject);
                yOffset += 10;
            }
            return 10;
        } else return 0;
    }

    /**
     * Draws a simple key, value line.
     *
     * @param renderer The Font Renderer object used to render the text.
     * @param name     The name/key for the value.
     * @param object   The value being rendered.
     * @param x        The X position to render the key, value set.
     * @param y        The Y position to render the key, value set.
     */
    private void drawSimpleTextValue(FontRenderer renderer, String name, Object object, int x, int y)
    {
        renderer.drawString(name + ": " + String.valueOf(object), x, y, 0xFFFFFF, true);
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
