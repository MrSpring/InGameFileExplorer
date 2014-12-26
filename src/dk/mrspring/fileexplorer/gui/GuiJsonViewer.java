package dk.mrspring.fileexplorer.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;

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
            this.drawObject(minecraft, 0, yOffset, entry.getKey(), entry.getValue());
            yOffset += 12;
        }
    }

    private void drawObject(Minecraft minecraft, int xOffset, int yOffset, String name, Object object)
    {
//        System.out.println("Drawing object: " + name + ", of type: " + object.getClass().getName());
        if (object instanceof String)
        {
            String string = (String) object;
            minecraft.fontRendererObj.drawString(name + ": " + string, x + xOffset, y + yOffset, 0xFFFFFF, true);
        } else if (object instanceof Boolean)
        {
            boolean bool = (Boolean) object;
            minecraft.fontRendererObj.drawString(name + ": " + String.valueOf(bool), x + xOffset, y + yOffset, 0xFFFFFF, true);
        } else if (object instanceof Double)
        {
            double doub = (Double) object;
            minecraft.fontRendererObj.drawString(name + ": " + String.valueOf(doub), x + xOffset, y + yOffset, 0xFFFFFF, true);
        } else if (object instanceof ArrayList)
        {
            ArrayList list = (ArrayList) object;
            minecraft.fontRendererObj.drawString(name + ":", x + xOffset, y + yOffset, 0xFFFFFF, true);
            yOffset += 10;
            DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset+1, 1, 3l, Color.DKGREY, 1F);
            int yOffsetAtListStart = yOffset;
            for (int i = 0, listSize = list.size(); i < listSize; i++)
            {
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + 4, 3, 1, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset, y + yOffset + 3, 3, 1, Color.WHITE, 1F);
                if (i < listSize - 1)
                    DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + 5, 1, 9, Color.DKGREY, 1F);
                Object listObject = list.get(i);
                this.drawObject(minecraft, xOffset + 5, yOffset, String.valueOf(i), listObject);
                yOffset += 10;
            }
            DrawingHelper.drawQuad(x + xOffset, y + yOffsetAtListStart, 1, yOffset - yOffsetAtListStart - 6, Color.WHITE, 1F);
        }
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
