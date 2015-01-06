package dk.mrspring.fileexplorer.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.json.*;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.loader.FileLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by MrSpring on 19-12-2014 for In-Game File Explorer.
 */
public class GuiJsonViewer implements IGui, IMouseListener
{
    int x, y, width, height, jsonHeight, scrollHeight = 0;
    File jsonFile;
    Map<String, Object> jsonObject;
    GuiJsonEditor editor;
    GuiSimpleButton editButton;
    GuiSimpleButton saveButton, cancelButton;
    boolean editing = false;

    public GuiJsonViewer(int x, int y, int width, int height, File file)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.jsonFile = file;

        editButton = new GuiSimpleButton(x - 62, y + height - 45, 50, 20, "Edit");
        saveButton = new GuiSimpleButton(x - 62, y + 50, 50, 20, "Save");
        cancelButton = new GuiSimpleButton(x - 62, y + 80, 50, 20, "Cancel");

        this.loadFromFile();
    }

    private void loadFromFile()
    {
        try
        {
            FileReader reader = new FileReader(this.jsonFile);
            Gson gson = new Gson();
            Type stringStringMap = new TypeToken<Map<String, Object>>()
            {
            }.getType();
            jsonObject = gson.fromJson(reader, stringStringMap);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if (jsonObject == null)
            jsonObject = new HashMap<String, Object>();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (!editing)
        {
            this.editButton.draw(minecraft, mouseX, mouseY);

            int yOffset = -scrollHeight;
            this.jsonHeight = 0;
            for (Map.Entry<String, Object> entry : this.jsonObject.entrySet())
            {
                int objectHeight = this.drawObject(minecraft, 5, yOffset, entry.getKey(), entry.getValue());
                yOffset += objectHeight;
                this.jsonHeight += objectHeight;
            }

            if (jsonHeight > height)
            {
                this.drawScrollBar();
            }
        } else
        {
            this.editor.draw(minecraft, mouseX, mouseY);
            this.saveButton.draw(minecraft, mouseX, mouseY);
            this.cancelButton.draw(minecraft, mouseX, mouseY);
        }
    }

    private void drawScrollBar()
    {
        float scrollBarYRange = (height - 50);
        float maxScrollHeight = getMaxScrollHeight();
        float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
        float scrollBarY = scrollBarYRange * scrollProgress;
        DrawingHelper.drawQuad(x, y + scrollBarY + 1, 2, 40, Color.DKGREY, 1F);
        DrawingHelper.drawQuad(x - 1, y + scrollBarY, 2, 40, Color.WHITE, 1F);
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
        if (object instanceof String || object instanceof Boolean || object instanceof Double)
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
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + height + 1, 1, lineHeight, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset, y + yOffset + height, 1, lineHeight, Color.WHITE, 1F);

                DrawingHelper.drawQuad(x + xOffset + 2, y + yOffset + height + 5, 3, 1, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + height + 4, 3, 1, Color.WHITE, 1F);
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
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + height + 1, 1, lineHeight, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset, y + yOffset + height, 1, lineHeight, Color.WHITE, 1F);

                DrawingHelper.drawQuad(x + xOffset + 2, y + yOffset + height + 5, 3, 1, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x + xOffset + 1, y + yOffset + height + 4, 3, 1, Color.WHITE, 1F);
                height += objectHeight;
            }
            return height;
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
     * @return Returns the height of the rendered object.
     */
    private int drawSimpleTextValue(FontRenderer renderer, String name, Object object, int x, int y)
    {
        int nameWidth = renderer.getStringWidth(name + ": ");
        renderer.drawString(name + ": ", x, y, 0xFFFFFF, true);
        return (9 * DrawingHelper.drawSplitString(renderer, x + nameWidth, y, String.valueOf(object), 0xFFFFFF, width - nameWidth - (x - this.x), true)) + 2;
    }

    @Override
    public void update()
    {
        if (jsonHeight < height)
            scrollHeight = 0;

        if (!LiteModFileExplorer.config.acceptFileManipulation)
            this.saveButton.disable();

        if (editing)
        {
            this.saveButton.update();
            this.cancelButton.update();
            this.editor.update();
        } else
        {
            this.editButton.update();
        }
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (!editing)
        {
            if (this.editButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.startEditing();
                return true;
            } else return false;
        } else
        {
            if (this.saveButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.save();
                return true;
            } else if (this.cancelButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.stopEditing();
                return true;
            } else return this.editor.mouseDown(mouseX, mouseY, mouseButton);
        }
    }

    private void stopEditing()
    {
        this.editing = false;
        this.editor = null;
    }

    private void save()
    {
        Map<String, Object> fromEditor = editor.toJsonMap();
        GsonBuilder builder = new GsonBuilder();
        if (LiteModFileExplorer.config.json_usePrettyPrinting)
            builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonCode = gson.toJson(fromEditor);
        FileLoader.writeToFile(jsonFile, jsonCode);
        this.stopEditing();
        this.loadFromFile();
        System.out.println(jsonCode);
    }

    private void startEditing()
    {
        this.editor = new GuiJsonEditor(this);
        this.editing = true;
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
        if (editing)
            this.editor.handleKeyTyped(keyCode, character);
    }

    public void setWidth(int width)
    {
        this.width = width;
        if (this.editing)
            this.editor.setWidth(width);
    }

    public void setHeight(int height)
    {
        this.height = height;
        if (this.editing)
        {
            this.editor.setHeight(height);
            this.saveButton.setY(y + height - 25 - 25);
            this.cancelButton.setY(y + height - 25);
        } else this.editButton.setY(y + height - 25);
    }

    private void addScroll(int amount)
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
                if (this.editing)
                    this.editor.handleMouseWheel(mouseX, mouseY, mouseWheel);
                else this.addScroll(-mouseWheel);
        }
    }

    public class GuiJsonEditor implements IGui, IMouseListener
    {
        List<JsonEditorElement> elements;
        int x, y, width, height, scrollHeight;
        GuiSimpleButton newBoolean, newDouble, newString, newArray, newMap;

        public GuiJsonEditor(GuiJsonViewer guiJsonViewer)
        {
            this.x = guiJsonViewer.x;
            this.y = guiJsonViewer.y;

            this.width = guiJsonViewer.width;
            this.height = guiJsonViewer.height;

            Map<String, Object> objectsFromJson = guiJsonViewer.jsonObject;

            elements = new ArrayList<JsonEditorElement>();
            newString = new GuiSimpleButton(x, y, 16, 16, "S");
            newBoolean = new GuiSimpleButton(x, y, 16, 16, "B");
            newDouble = new GuiSimpleButton(x, y, 16, 16, "D");
            newArray = new GuiSimpleButton(x, y, 16, 16, "A");
            newMap = new GuiSimpleButton(x, y, 16, 16, "M");

            for (Map.Entry<String, Object> entry : objectsFromJson.entrySet())
            {
                Object value = entry.getValue();
                String name = entry.getKey();

                if (value instanceof Boolean)
                    this.elements.add(new JsonBooleanElement(x, y, width, name, (Boolean) value));
                else if (value instanceof String)
                    this.elements.add(new JsonStringElement(x, y, width, name, (String) value));
                else if (value instanceof ArrayList)
                    this.elements.add(new JsonArrayElement(x, y, width, name, (ArrayList<Object>) value));
                else if (value instanceof LinkedTreeMap)
                    this.elements.add(new JsonMapElement(x, y, width, name, (LinkedTreeMap<String, Object>) value));
            }
        }

        public void setWidth(int width)
        {
            this.width = width;
        }

        public void setHeight(int height)
        {
            this.height = height;
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            int xOffset = 18, yOffset = -scrollHeight;

            int totalHeight = this.getListHeight();
            if (this.height < totalHeight)
            {
                xOffset += 4;
                float scrollBarYRange = (height - 50);
                float maxScrollHeight = this.getMaxScrollHeight();
                float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
                float scrollBarY = scrollBarYRange * scrollProgress;
                DrawingHelper.drawQuad(x, y + scrollBarY + 1, 2, 40, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(x - 1, y + scrollBarY, 2, 40, Color.WHITE, 1F);
            }

            for (JsonEditorElement element : elements)
            {
                element.drawElement(x + xOffset, y + yOffset, width - xOffset, mouseX, mouseY, minecraft);

                element.getDeleteButton().setX(x + xOffset - 18);
                element.getDeleteButton().setY(y + yOffset);
                element.getDeleteButton().draw(minecraft, mouseX, mouseY);

                yOffset += element.getHeight() + 3;
            }
            minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.json_editor.add_new") + ": ", x, y + yOffset + 3, 0xFFFFFF, true);

            int textWidth = minecraft.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.json_editor.add_new") + ": ");

            this.newString.setX(x + textWidth);
            this.newString.setY(y + yOffset);

            this.newBoolean.setX(x + 18 + textWidth);
            this.newBoolean.setY(y + yOffset);

            this.newDouble.setX(x + 18 + 18 + textWidth);
            this.newDouble.setY(y + yOffset);

            this.newArray.setX(x + 18 + 18 + 18 + textWidth);
            this.newArray.setY(y + yOffset);

            this.newMap.setX(x + 18 + 18 + 18 + 18 + textWidth);
            this.newMap.setY(y + yOffset);

            this.newString.draw(minecraft, mouseX, mouseY);
            this.newBoolean.draw(minecraft, mouseX, mouseY);
            this.newDouble.draw(minecraft, mouseX, mouseY);
            this.newArray.draw(minecraft, mouseX, mouseY);
            this.newMap.draw(minecraft, mouseX, mouseY);
        }

        @Override
        public void update()
        {
            if (this.getListHeight() < height)
                scrollHeight = 0;

            for (JsonEditorElement element : elements)
            {
                element.updateElement();
                element.getDeleteButton().update();
            }
            this.newDouble.update();
            this.newBoolean.update();
            this.newString.update();
            this.newArray.update();
            this.newMap.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            boolean anythingClicked = false;
            for (int i = 0; i < elements.size(); i++)
            {
                JsonEditorElement element = elements.get(i);
                if (element.mouseDown(mouseX, mouseY, mouseButton))
                    anythingClicked = true;
                else if (element.getDeleteButton().mouseDown(mouseX, mouseY, mouseButton))
                    elements.remove(i);
            }

            if (!anythingClicked)
            {
                if (this.newDouble.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new JsonDoubleElement(0, 0, 0, "name", 10.0));
                else if (this.newBoolean.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new JsonBooleanElement(0, 0, 0, "name", false));
                else if (this.newString.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new JsonStringElement(0, 0, 0, "name", "value"));
                else if (this.newArray.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new JsonArrayElement(0, 0, 0, "name", new ArrayList<Object>()));
                else if (this.newMap.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new JsonMapElement(0, 0, 0, "name", new LinkedTreeMap<String, Object>()));
            }

            return anythingClicked;
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
            for (JsonEditorElement element : elements)
                element.handleKeyTypes(character, keyCode);
        }

        public Map<String, Object> toJsonMap()
        {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

            for (JsonEditorElement element : elements)
            {
                String elementName = element.getName();
                Object elementValue = element.getValue();
                map.put(elementName, elementValue);
            }

            return map;
        }

        @Override
        public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
        {
            this.addScroll(-dWheelRaw);
        }

        private void addScroll(int amount)
        {
            int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scrollHeight + amount;

            if (scrollHeightAfterAddition > maxScrollHeight)
                scrollHeightAfterAddition = maxScrollHeight;
            else if (scrollHeightAfterAddition < minScrollHeight)
                scrollHeightAfterAddition = minScrollHeight;

            this.scrollHeight = scrollHeightAfterAddition;
        }

        private int getListHeight()
        {
            int height = 16;
            for (JsonEditorElement element : elements)
                height += element.getHeight() + 3;
            return height;
        }

        private int getMaxScrollHeight()
        {
            return getListHeight() + 10 - this.height;
        }
    }
}
