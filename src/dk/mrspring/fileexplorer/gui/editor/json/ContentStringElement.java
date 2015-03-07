package dk.mrspring.fileexplorer.gui.editor.json;

import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class ContentStringElement extends ContentEditorElement<String>
{
    GuiCustomTextField nameField, valueField;
    boolean canEditName;

    public ContentStringElement(int x, int y, int maxWidth, String name, String object, boolean canEditName)
    {
        super(x, y, maxWidth, name, object, canEditName);

        int width = maxWidth;
        if (width > 400)
            width = 400;

        this.nameField = new GuiCustomTextField(x, y, (width / 2) - 2, 16, name);
        this.valueField = new GuiCustomTextField(x + (width / 2) + 2, y, (width / 2) - 2, 16, object);
    }

    public ContentStringElement(int x, int y, int maxWidth, String name, String object)
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

        if (this.canEditName)
        {
            this.nameField.setX(x);
            this.nameField.setY(y);
            this.nameField.setW((width / 2) - 2);
            this.nameField.draw(minecraft, mouseX, mouseY);
            this.valueField.setX(x + (width / 2) + 2);
            this.valueField.setW((width / 2) - 2);
        } else
        {
            int xOffset = minecraft.fontRendererObj.getStringWidth(this.getName()) + 2;
            this.valueField.setX(x + xOffset);
            this.valueField.setW(width - xOffset);
            minecraft.fontRendererObj.drawString(this.getName(), x, y + 3, 0xFFFFFF, true);
        }

        this.valueField.setY(y);

        this.valueField.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        boolean clicked = false;
        if (this.nameField.mouseDown(mouseX, mouseY, mouseButton))
            clicked = true;
        if (this.valueField.mouseDown(mouseX, mouseY, mouseButton))
            clicked = true;
        return clicked;
    }

    @Override
    public void updateElement()
    {
        this.nameField.update();
        this.valueField.update();
    }

    @Override
    public void handleKeyTypes(char character, int keyCode)
    {
        if (this.canEditName)
            this.nameField.handleKeyTyped(keyCode, character);
        this.valueField.handleKeyTyped(keyCode, character);
    }

    @Override
    public Object getValue()
    {
        return this.valueField.getText();
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
}
