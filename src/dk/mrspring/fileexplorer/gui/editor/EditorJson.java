package dk.mrspring.fileexplorer.gui.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiJsonViewer;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.editor.json.*;
import dk.mrspring.fileexplorer.helper.Color;
import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.loader.FileLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public class EditorJson extends Editor implements IMouseListener
{
    File jsonFile;

    GuiJsonEditor editor;
    GuiJsonViewer viewer;

    GuiSimpleButton editButton;
    GuiSimpleButton saveButton, cancelButton;
    boolean editing = false;

    public EditorJson(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h);

        this.jsonFile = file;

        Map<String, Object> json = FileLoader.readJsonFile(jsonFile);

        viewer = new GuiJsonViewer(x, y, w, h, json);
        editor = new GuiJsonEditor(x, y, w, h, json);

        editButton = new GuiSimpleButton(x - 62, y + h - 45, 50, 20, "gui.json_editor.edit");
        saveButton = new GuiSimpleButton(x - 62, y + h - 15, 50, 20, "gui.json_editor.save");
        cancelButton = new GuiSimpleButton(x - 62, y + h - 45, 50, 20, "gui.json_editor.cancel");
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (editing)
        {
            this.editor.draw(minecraft, mouseX, mouseY);
            this.saveButton.draw(minecraft, mouseX, mouseY);
            this.cancelButton.draw(minecraft, mouseX, mouseY);
        } else
        {
            this.viewer.draw(minecraft, mouseX, mouseY);
            this.editButton.draw(minecraft, mouseX, mouseY);
        }
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;

        if (!LiteModFileExplorer.config.acceptFileManipulation)
            this.saveButton.disable();

        if (editing)
        {
            this.saveButton.setY(y + h - 15);
            this.saveButton.setX(x - 62);
            this.saveButton.update();

            this.cancelButton.setY(y + h - 40);
            this.cancelButton.setX(x - 62);
            this.cancelButton.update();

            this.editor.setHeight(h);
            this.editor.setWidth(w);
            this.editor.update();
        } else
        {
            this.editButton.setY(y + h - 15);
            this.editButton.setX(x - 62);
            this.editButton.update();

            this.viewer.setWidth(w);
            this.viewer.setHeight(h);
            this.viewer.update();
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
        this.editor = null;
        this.editing = false;
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
        this.reloadViewer();
        this.stopEditing();
    }

    private void reloadViewer()
    {
        Map<String, Object> json = FileLoader.readJsonFile(jsonFile);
        viewer = new GuiJsonViewer(x, y, w, h, json);
    }

    private void reloadEditor()
    {
        Map<String, Object> json = FileLoader.readJsonFile(jsonFile);
        editor = new GuiJsonEditor(x, y, w, h, json);
    }

    private void startEditing()
    {
        this.reloadEditor();
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

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                if (this.editing)
                    this.editor.handleMouseWheel(mouseX, mouseY, mouseWheel);
                else this.viewer.handleMouseWheel(mouseX, mouseY, dWheelRaw);
        }
    }

    public class GuiJsonEditor implements IGui, IMouseListener
    {
        List<JsonEditorElement> elements;
        int x, y, width, height, scrollHeight;
        GuiSimpleButton newBoolean, newDouble, newString, newArray, newMap;

        public GuiJsonEditor(int x, int y, int w, int h, Map<String, Object> jsonObject)
        {
            this.x = x;
            this.y = y;

            this.width = w;
            this.height = h;

            elements = new ArrayList<JsonEditorElement>();
            newString = new GuiSimpleButton(x, y, 16, 16, "S");
            newBoolean = new GuiSimpleButton(x, y, 16, 16, "B");
            newDouble = new GuiSimpleButton(x, y, 16, 16, "D");
            newArray = new GuiSimpleButton(x, y, 16, 16, "A");
            newMap = new GuiSimpleButton(x, y, 16, 16, "M");

            for (Map.Entry<String, Object> entry : jsonObject.entrySet())
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
            try
            {
                int xOffset = 18, yOffset = -scrollHeight;

                int totalHeight = this.getListHeight();
                if (this.height < totalHeight)
                {
                    xOffset += 4;
                    float scrollBarYRange = (height - 40);
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
                minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.json_editor.add_new") + ": ", x + xOffset - 18, y + yOffset + 3, 0xFFFFFF, true);

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
            } catch (StackOverflowError error)
            {
                DrawingHelper.drawSplitCenteredString(minecraft.fontRendererObj, w / 2 + x, y + 10, StatCollector.translateToLocal("gui.json_editor.not_enough_space"), 0xFFFFFF, w);
            }
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
