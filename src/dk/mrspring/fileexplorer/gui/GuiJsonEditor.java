package dk.mrspring.fileexplorer.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by MrSpring on 19-12-2014 for In-Game File Explorer.
 */
public class GuiJsonEditor implements IGui
{
    int x, y, width, height;
    Map<String, Object> jsonObject;

    public GuiJsonEditor(int x, int y, int width, int height, File jsonFile)
    {
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
            this.drawObject(minecraft, yOffset, entry.getKey(), entry.getValue());
        }
    }

    private void drawObject(Minecraft minecraft, int yOffset, String name, Object object)
    {
//        String
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
