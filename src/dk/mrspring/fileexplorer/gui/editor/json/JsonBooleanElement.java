package dk.mrspring.fileexplorer.gui.editor.json;

import dk.mrspring.fileexplorer.gui.GuiBoolean;
import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class JsonBooleanElement extends JsonEditorElement<Boolean>
{
    boolean value;
    boolean canEditName;

    GuiCustomTextField nameField;
    GuiBoolean booleanGui;

    public JsonBooleanElement(int x, int y, int maxWidth, String name, boolean object, boolean canEditName)
    {
        super(x, y, maxWidth, name, object);

        this.canEditName = canEditName;

        this.value = object;

        int width = maxWidth;

        if (width > 400)
            width = 400;

        this.nameField = new GuiCustomTextField(x, y, width - 36, 16, name);

        this.booleanGui = new GuiBoolean(x + width - 32, y, object);
    }

    public JsonBooleanElement(int x, int y, int maxWidth, String name, boolean object)
    {
        this(x, y, maxWidth, name, object, true);
    }

    @Override
    public int getHeight()
    {
        return 16;
    }

    @Override
    public void drawElement(int x, int y, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        int width = maxWidth;
        if (width > 400)
            width = 400;

        this.nameField.setX(x);
        this.nameField.setY(y);
        this.nameField.setW(width - 36);

        if (canEditName)
        {
            this.booleanGui.setX(x + width - 35);
            this.nameField.draw(minecraft, mouseX, mouseY);
        } else
        {
            this.booleanGui.setX(x + minecraft.fontRendererObj.getStringWidth(this.getName()) + 2);
            minecraft.fontRendererObj.drawString(this.getName(), x, y, 0xFFFFFF);
        }

        this.booleanGui.setY(y);
        this.booleanGui.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void updateElement()
    {
        this.booleanGui.update();
        this.nameField.update();
    }

    @Override
    public void handleKeyTypes(char character, int keyCode)
    {
        this.nameField.handleKeyTyped(keyCode, character);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.booleanGui.mouseDown(mouseX, mouseY, mouseButton))
            return true;
        else return this.nameField.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public Boolean getValue()
    {
        return booleanGui.getValue();
    }

    @Override
    public String getName()
    {
        return nameField.getText();
    }
}
