package dk.mrspring.fileexplorer.gui.editor.json;

import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class JsonArrayElement extends JsonEditorElement<ArrayList<Object>>
{
    GuiCustomTextField nameField;
    ArrayList<JsonEditorElement> elements;
    GuiSimpleButton newBoolean, newDouble, newString, newArray, newMap, collapse;
    boolean canEditName, collapsed = false;
    int collapseWidth = 12;

    public JsonArrayElement(int x, int y, int maxWidth, String name, ArrayList<Object> list, boolean canEditName)
    {
        super(x, y, maxWidth, name, list);
        this.canEditName = canEditName;

        if (!LiteModFileExplorer.config.json_allowArrayCollapsing)
            collapseWidth = 0;

        int width = maxWidth;
        if (width > 200)
            width = 200;

        nameField = new GuiCustomTextField(x + collapseWidth, y, width - collapseWidth, 16, name);
        elements = new ArrayList<JsonEditorElement>();
        newString = new GuiSimpleButton(x, y, 16, 16, "S");
        newBoolean = new GuiSimpleButton(x, y, 16, 16, "B");
        newDouble = new GuiSimpleButton(x, y, 16, 16, "D");
        newArray = new GuiSimpleButton(x, y, 16, 16, "A");
        newMap = new GuiSimpleButton(x, y, 16, 16, "M");
        collapse = new GuiSimpleButton(x, y, 10, 16, "");

        for (int i = 0; i < list.size(); i++)
        {
            Object object = list.get(i);
            if (object instanceof String)
                this.elements.add(new JsonStringElement(0, 0, 0, String.valueOf(i) + ": ", (String) object, false));
            else if (object instanceof Boolean)
                this.elements.add(new JsonBooleanElement(0, 0, 0, String.valueOf(i) + ": ", (Boolean) object, false));
            else if (object instanceof ArrayList)
                this.elements.add(new JsonArrayElement(0, 0, 0, String.valueOf(i) + ": ", (ArrayList) object, false));
            else if (object instanceof Double)
                this.elements.add(new JsonDoubleElement(0, 0, 0, String.valueOf(i) + ": ", (Double) object, false));
            else if (object instanceof LinkedTreeMap)
                this.elements.add(new JsonMapElement(0, 0, 0, String.valueOf(i) + ": ", (LinkedTreeMap) object, false));
        }
    }

    public JsonArrayElement(int x, int y, int maxWidth, String name, ArrayList<Object> list)
    {
        this(x, y, maxWidth, name, list, true);
    }

    @Override
    public int getHeight()
    {
        int height = 16;

        if (!collapsed)
        {
            height += 19;
            for (JsonEditorElement element : elements) height += element.getHeight() + 3;
        } else if (LiteModFileExplorer.config.json_showCollapsedArraySize)
            height += 10;

        return height;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        int width = maxWidth;
        if (width > 400)
            width = 400;

        if (this.canEditName)
        {
            nameField.setX(xPosition + collapseWidth);
            nameField.setY(yPosition);
            nameField.setW(width - collapseWidth);
            nameField.draw(minecraft, mouseX, mouseY);
        } else
            minecraft.fontRendererObj.drawString(this.getName(), xPosition + collapseWidth, yPosition + 3, 0xFFFFFF, true);

        collapse.setX(xPosition);
        collapse.setY(yPosition);

        int yOffset = 16 + 3, xOffset = 18;

        if (!collapsed)
        {
            if (LiteModFileExplorer.config.json_allowArrayCollapsing)
                DrawingHelper.drawIcon(DrawingHelper.downArrow, xPosition + 2, yPosition + 5, 6, 6, false);

            for (JsonEditorElement element : elements)
            {
                element.drawElement(xPosition + xOffset, yPosition + yOffset, maxWidth - xOffset, mouseX, mouseY, minecraft);

                element.getDeleteButton().setX(xPosition + xOffset - 18);
                element.getDeleteButton().setY(yPosition + yOffset);
                element.getDeleteButton().draw(minecraft, mouseX, mouseY);

                int height = element.getHeight();

                DrawingHelper.drawQuad(xPosition - 8, yPosition + yOffset + 1, 1, height + 3, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(xPosition - 9, yPosition + yOffset, 1, height + 3, Color.WHITE, 1F);
                DrawingHelper.drawQuad(xPosition - 7, yPosition + yOffset + 7, 5, 1, Color.DKGREY, 1F);
                DrawingHelper.drawQuad(xPosition - 8, yPosition + yOffset + 6, 5, 1, Color.WHITE, 1F);

                yOffset += element.getHeight() + 3;
            }

            DrawingHelper.drawQuad(xPosition - 8, yPosition + yOffset + 1, 1, 7, Color.DKGREY, 1F);
            DrawingHelper.drawQuad(xPosition - 9, yPosition + yOffset, 1, 7, Color.WHITE, 1F);
            DrawingHelper.drawQuad(xPosition - 7, yPosition + yOffset + 7, 5, 1, Color.DKGREY, 1F);
            DrawingHelper.drawQuad(xPosition - 8, yPosition + yOffset + 6, 5, 1, Color.WHITE, 1F);

            minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.json_editor.add_new") + ": ", xPosition, yPosition + yOffset + 3, 0xFFFFFF, true);

            int textWidth = minecraft.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.json_editor.add_new") + ": ");

            this.newString.setX(xPosition + textWidth);
            this.newString.setY(yPosition + yOffset);

            this.newBoolean.setX(xPosition + 18 + textWidth);
            this.newBoolean.setY(yPosition + yOffset);

            this.newDouble.setX(xPosition + 18 + 18 + textWidth);
            this.newDouble.setY(yPosition + yOffset);

            this.newArray.setX(xPosition + 18 + 18 + 18 + textWidth);
            this.newArray.setY(yPosition + yOffset);

            this.newMap.setX(xPosition + 18 + 18 + 18 + 18 + textWidth);
            this.newMap.setY(yPosition + yOffset);

            this.newString.draw(minecraft, mouseX, mouseY);
            this.newBoolean.draw(minecraft, mouseX, mouseY);
            this.newDouble.draw(minecraft, mouseX, mouseY);
            this.newArray.draw(minecraft, mouseX, mouseY);
            this.newMap.draw(minecraft, mouseX, mouseY);
        } else
        {
            if (LiteModFileExplorer.config.json_allowArrayCollapsing)
            {
                DrawingHelper.drawIcon(DrawingHelper.rightArrow, xPosition + 2, yPosition + 5, 6, 6, false);
                if (LiteModFileExplorer.config.json_showCollapsedArraySize)
                    minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.json_editor.array_size") + ": " + String.valueOf(elements.size()), xPosition, yPosition + 18, 0xFFFFFF, true);
            }
        }
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        boolean returns = false;
        if (this.nameField.mouseDown(mouseX, mouseY, mouseButton))
            returns = true;

        for (JsonEditorElement element : elements)
        {
            if (element.mouseDown(mouseX, mouseY, mouseButton))
                returns = true;
            else if (element.getDeleteButton().mouseDown(mouseX, mouseY, mouseButton))
            {
                elements.remove(element);
                break;
            }
        }

        if (!returns)
        {
            if (this.newDouble.mouseDown(mouseX, mouseY, mouseButton))
                this.elements.add(new JsonDoubleElement(0, 0, 0, "name", 10.0, false));
            else if (this.newBoolean.mouseDown(mouseX, mouseY, mouseButton))
                this.elements.add(new JsonBooleanElement(0, 0, 0, "name", false, false));
            else if (this.newString.mouseDown(mouseX, mouseY, mouseButton))
                this.elements.add(new JsonStringElement(0, 0, 0, "name", "value", false));
            else if (this.newArray.mouseDown(mouseX, mouseY, mouseButton))
                this.elements.add(new JsonArrayElement(0, 0, 0, "name", new ArrayList<Object>(), false));
            else if (this.newMap.mouseDown(mouseX, mouseY, mouseButton))
                this.elements.add(new JsonMapElement(0, 0, 0, "name", new LinkedTreeMap<String, Object>(), false));
            else if (this.collapse.mouseDown(mouseX, mouseY, mouseButton) && LiteModFileExplorer.config.json_allowArrayCollapsing)
                this.collapsed = !collapsed;
        }
        return returns;
    }

    @Override
    public Object getValue()
    {
        ArrayList<Object> list = new ArrayList<Object>();
        for (JsonEditorElement element : elements) list.add(element.getValue());
        return list;
    }

    @Override
    public String getName()
    {
        return this.nameField.getText();
    }

    @Override
    public void setName(String name)
    {
        this.nameField.setText(name);
    }

    @Override
    public void updateElement()
    {
        for (int i = 0; i < elements.size(); i++)
        {
            JsonEditorElement element = elements.get(i);
            element.setName(String.valueOf(i) + ": ");
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
    public void handleKeyTypes(char character, int keyCode)
    {
        if (canEditName)
            this.nameField.handleKeyTyped(keyCode, character);
        for (JsonEditorElement element : elements)
            element.handleKeyTypes(character, keyCode);
    }
}
