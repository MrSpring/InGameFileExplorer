package dk.mrspring.fileexplorer.gui.editor.json;

import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class JsonArrayElement extends JsonEditorElement<ArrayList<Object>>
{
    GuiCustomTextField nameField;
    ArrayList<JsonEditorElement> elements;
    GuiSimpleButton newBoolean, newDouble, newString, newArray, newMap;

    public JsonArrayElement(int x, int y, int maxWidth, String name, ArrayList<Object> list)
    {
        super(x, y, maxWidth, name, list);

        int width = maxWidth;
        if (width > 200)
            width = 200;

        nameField = new GuiCustomTextField(x, y, width, 16, name);
        elements = new ArrayList<JsonEditorElement>();
        newString = new GuiSimpleButton(x, y, 16, 16, "S");
        newBoolean = new GuiSimpleButton(x, y, 16, 16, "B");
        newDouble = new GuiSimpleButton(x, y, 16, 16, "D");
        newArray = new GuiSimpleButton(x, y, 16, 16, "A");
        newMap = new GuiSimpleButton(x, y, 16, 16, "M");

        for (int i = 0; i < list.size(); i++)
        {
            Object object = list.get(i);
            if (object instanceof String)
                this.elements.add(new JsonStringElement(0, 0, 0, String.valueOf(i) + ": ", (String) object, false));
            else if (object instanceof Boolean)
                this.elements.add(new JsonBooleanElement(0, 0, 0, String.valueOf(i) + ": ", (Boolean) object, false));
        }
            /*if (object instanceof String)
            {
                int height = 16;
                elements.add(new Object[]{
                        new GuiCustomTextField(x + xOffset + buttonSize, y + yOffset, width - xOffset, height, (String) object),
                        new GuiSimpleButton(x + xOffset, y + yOffset, 16, 16, "").setIcon(DrawingHelper.crossIcon)});
                yOffset += height + 3;
            } else if (object instanceof Boolean)
            {
                elements.add(new Object[]{
                        new GuiBoolean(x + xOffset + buttonSize, y + yOffset, (Boolean) object),
                        new GuiSimpleButton(x + xOffset, y + yOffset, 16, 16, "").setIcon(DrawingHelper.crossIcon)});
                yOffset += 19;
            } else if (object instanceof ArrayList)
            {
                elements.add(new Object[]{
                        new JsonArrayElement(x+xOffset,y+yOffset)
                });
            }*/
    }

    @Override
    public int getHeight()
    {
        int height = 19;
        for (JsonEditorElement element : elements) height += element.getHeight() + 3;
        return height;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        int width = maxWidth;
        if (width > 400)
            width = 400;

        nameField.setX(xPosition);
        nameField.setY(yPosition);
        nameField.setW(width);
        nameField.draw(minecraft, mouseX, mouseY);

        int yOffset = nameField.getH() + 3, xOffset = 18;

        Iterator<JsonEditorElement> iterator = elements.iterator();
        while (iterator.hasNext())
        {
            JsonEditorElement element = iterator.next();
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
    }

    @Override
    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton)
    {
        boolean returns = false;
        if (this.nameField.mouseDown(relativeMouseX, relativeMouseY, mouseButton))
            returns = true;

        for (JsonEditorElement element : elements)
        {
            if (element.mouseDown(relativeMouseX, relativeMouseY, mouseButton))
                returns = true;
        }
        return returns;
    }

    @Override
    public Object getValue()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void updateElement()
    {
        for (JsonEditorElement element : elements)
        {
            element.updateElement();
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

    }
}