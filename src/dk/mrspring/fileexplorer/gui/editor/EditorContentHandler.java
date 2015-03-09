package dk.mrspring.fileexplorer.gui.editor;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiContentViewer;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.editor.json.*;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public class EditorContentHandler extends Editor implements IMouseListener
{
    File openFile;

    GuiContentEditor editor;
    GuiContentViewer viewer;

    GuiSimpleButton editButton;
    GuiSimpleButton saveButton, cancelButton;
    boolean editing = false;

    ContentHandler contentHandler;

    public EditorContentHandler(int x, int y, int w, int h, File file, ContentHandler contentHandler)
    {
        super(x, y, w, h);

        this.openFile = file;
        this.contentHandler = contentHandler;

        Map<String, Object> content = contentHandler.getMapFromFile(openFile);

        viewer = new GuiContentViewer(x, y, w, h, content);
        editor = new GuiContentEditor(x, y, w, h, content);

        editButton = new GuiSimpleButton(x - 62, y + h - 50, 50, 20, "gui.json_editor.edit");
        saveButton = new GuiSimpleButton(x - 62, y + h - 20, 50, 20, "gui.json_editor.save");
        cancelButton = new GuiSimpleButton(x - 62, y + h - 50, 50, 20, "gui.json_editor.cancel");
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
            this.saveButton.setY(y + h - 20);
            this.saveButton.setX(x - 62);
            this.saveButton.update();

            this.cancelButton.setY(y + h - 45);
            this.cancelButton.setX(x - 62);
            this.cancelButton.update();

            this.editor.setHeight(h);
            this.editor.setWidth(w);
            this.editor.update();
        } else
        {
            this.editButton.setY(y + h - 20);
            this.editButton.setX(x - 62);
            this.editButton.update();

            this.viewer.setY(y);
            this.viewer.setX(x);
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

    public void save()
    {
        Map<String, Object> fromEditor = editor.toMap();
        String fromMap = contentHandler.getFileFromMap(fromEditor);
        try
        {
            LiteModFileExplorer.core.getFileLoader().writeToFile(openFile, fromMap);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.reloadViewer();
        this.stopEditing();
    }

    private void reloadViewer()
    {
        Map<String, Object> content = contentHandler.getMapFromFile(openFile);
        viewer = new GuiContentViewer(x, y, w, h, content);
    }

    private void reloadEditor()
    {
        Map<String, Object> content = contentHandler.getMapFromFile(openFile);
        editor = new GuiContentEditor(x, y, w, h, content);
    }

    private void startEditing()
    {
        this.reloadEditor();
        this.editing = true;
    }

    @Override
    public String getOpenFileName()
    {
        return openFile.getName();
    }

    @Override
    public boolean hasUnsavedChanges()
    {
        return editing;
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

    public class GuiContentEditor implements IGui, IMouseListener
    {
        List<ContentEditorElement> elements;
        int x, y, width, height, scrollHeight;
        GuiSimpleButton newBoolean, newDouble, newString, newArray, newMap;

        public GuiContentEditor(int x, int y, int w, int h, Map<String, Object> jsonObject)
        {
            this.x = x;
            this.y = y;

            this.width = w;
            this.height = h;

            elements = new ArrayList<ContentEditorElement>();
            newString = new GuiSimpleButton(x, y, 16, 16, "S");
            newBoolean = new GuiSimpleButton(x, y, 16, 16, "B");
            newDouble = new GuiSimpleButton(x, y, 16, 16, "D");
            newArray = new GuiSimpleButton(x, y, 16, 16, "A");
            newMap = new GuiSimpleButton(x, y, 16, 16, "M");

            if (jsonObject != null)
                for (Map.Entry<String, Object> entry : jsonObject.entrySet())
                {
                    Object value = entry.getValue();
                    String name = entry.getKey();
                    IContentType type = contentHandler.getContentType(name, value, true);
                    if (type != null)
                        this.elements.add(type.getAsEditorElement(x, y, width, name, value, true, contentHandler));
                    /*if (value instanceof Boolean)
                        this.elements.add(new ContentBooleanElement(x, y, width, name, (Boolean) value));
                    else if (value instanceof String)
                        this.elements.add(new ContentStringElement(x, y, width, name, (String) value));
                    else if (value instanceof Number)
                        this.elements.add(new ContentDoubleElement(x, y, width, name, (Double) value));
                    else if (value instanceof ArrayList)
                        this.elements.add(new ContentArrayElement(x, y, width, name, (List<Object>) value));
                    else if (value instanceof Map)
                        this.elements.add(new ContentMapElement(x, y, width, name, (Map<String, Object>) value));*/
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
            GLClippingPlanes.glEnableClipping(x, x + w, y, y + h);
//            GL11.glPushMatrix();
            try
            {
                int xOffset = 18, yOffset = -scrollHeight;

//                GL11.glTranslatef(0, -scrollHeight, 0);

                int totalHeight = this.getListHeight();
                if (this.height < totalHeight)
                {
                    xOffset += 4;
                    float scrollBarYRange = (height - 40);
                    float maxScrollHeight = this.getMaxScrollHeight();
                    float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
                    float scrollBarY = scrollBarYRange * scrollProgress;
                    LiteModFileExplorer.core.getDrawingHelper().drawShape(new Quad(x, y + scrollBarY + 1, 2, 40).setColor(Color.DK_GREY));
                    LiteModFileExplorer.core.getDrawingHelper().drawShape(new Quad(x - 1, y + scrollBarY, 2, 40).setColor(Color.WHITE));
//                    DrawingHelper.drawQuad(x, y + scrollBarY + 1, 2, 40, Color.DK_GREY, 1F);
//                    DrawingHelper.drawQuad(x - 1, y + scrollBarY, 2, 40, Color.WHITE, 1F);
                }

                for (ContentEditorElement element : elements)
                {
                    element.drawElement(x + xOffset, y + yOffset, width - xOffset, mouseX, mouseY, minecraft);

                    element.getDeleteButton().setX(x + xOffset - 18);
                    element.getDeleteButton().setY(y + yOffset);
                    element.getDeleteButton().draw(minecraft, mouseX, mouseY);

                    yOffset += element.getHeight() + 3;
                }
                minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.json_editor.add_new") + ": ", x + xOffset - 18, y + yOffset + 3, 0xFFFFFF, true);

                int textWidth = minecraft.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.json_editor.add_new") + ": ");

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
                LiteModFileExplorer.core.getDrawingHelper().drawText(TranslateHelper.translate("gui.json_editor.not_enough_space"), new Vector(w / 2 + x, y + 10), 0xFFFFFF, true, w, DrawingHelper.VerticalTextAlignment.CENTER, DrawingHelper.HorizontalTextAlignment.TOP);
            }
//            GL11.glPopMatrix();
            GLClippingPlanes.glDisableClipping();
        }

        @Override
        public void update()
        {
            if (this.getListHeight() < height)
                scrollHeight = 0;

            for (ContentEditorElement element : elements)
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
                ContentEditorElement element = elements.get(i);
                if (element.mouseDown(mouseX, mouseY, mouseButton))
                    anythingClicked = true;
                else if (element.getDeleteButton().mouseDown(mouseX, mouseY, mouseButton))
                    elements.remove(i);
            }

            if (!anythingClicked)
            {
                if (this.newDouble.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new ContentDoubleElement(0, 0, 0, "name", 10.0));
                else if (this.newBoolean.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new ContentBooleanElement(0, 0, 0, "name", false));
                else if (this.newString.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new ContentStringElement(0, 0, 0, "name", "value"));
                else if (this.newArray.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new ContentArrayElement(0, 0, 0, "name", new ArrayList<Object>(), contentHandler));
                else if (this.newMap.mouseDown(mouseX, mouseY, mouseButton))
                    this.elements.add(new ContentMapElement(0, 0, 0, "name", new LinkedHashMap<String, Object>(), contentHandler));
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
            for (ContentEditorElement element : elements)
                element.handleKeyTypes(character, keyCode);
        }

        public Map<String, Object> toMap()
        {
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            for (ContentEditorElement element : elements)
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
            for (ContentEditorElement element : elements)
                height += element.getHeight() + 3;
            return height;
        }

        private int getMaxScrollHeight()
        {
            return getListHeight() + 10 - this.height;
        }
    }
}
