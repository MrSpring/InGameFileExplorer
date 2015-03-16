package dk.mrspring.fileexplorer.gui.editor.content;

import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.GuiNumberField;
import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 03-01-2015 for In-Game File Explorer.
 */
public class ContentDoubleElement extends ContentEditorElement<Double>
{
    GuiNumberField numberField;
    GuiCustomTextField nameField;

    public ContentDoubleElement(int x, int y, int maxWidth, String name, Double object, boolean canEditName)
    {
        super(x, y, maxWidth, name, object, canEditName);

        int width = maxWidth;
        if (width > 400)
            width = 400;

        this.nameField = new GuiCustomTextField(x, y, width / 2 - 2, 16, name);
        this.numberField = new GuiNumberField(x, y, width / 2 - 2, 32, object);
    }

    public ContentDoubleElement(int x, int y, int maxWidth, String name, Double object)
    {
        this(x, y, maxWidth, name, object, true);
    }

    @Override
    public int getHeight()
    {
        return this.numberField.isFocused() ? 32 : 16;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        int width = maxWidth;
        if (width > 400)
            width = 400;

        if (this.canEditName)
        {
            this.nameField.setX(xPosition);
            this.nameField.setY(yPosition);
            this.nameField.setW(width / 2 - 2);
            this.nameField.draw(minecraft, mouseX, mouseY);

            this.numberField.setX(width / 2 + 2 + xPosition);
            this.numberField.setY(yPosition);
            this.numberField.setWidth(width / 2 - 2);
        } else
        {
            minecraft.fontRendererObj.drawString(this.getName(), xPosition, yPosition + 3, 0xFFFFFF, true);

            this.numberField.setX(xPosition + minecraft.fontRendererObj.getStringWidth(this.getName()) + 2);
            this.numberField.setY(yPosition);
            this.numberField.setWidth(width - minecraft.fontRendererObj.getStringWidth(this.getName()) - 2);
        }

        this.numberField.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton)
    {
        boolean returns = false;

        if (this.nameField.mouseDown(relativeMouseX, relativeMouseY, mouseButton) && this.canEditName)
            returns = true;
        if (this.numberField.mouseDown(relativeMouseX, relativeMouseY, mouseButton))
            returns = true;

        return returns;
    }

    @Override
    public Object getValue()
    {
        return numberField.getValue();
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
        if (this.canEditName) this.nameField.update();
        if (this.numberField.isFocused())
            this.numberField.setHeight(32);
        else this.numberField.setHeight(16);
        this.numberField.update();
    }

    @Override
    public void handleKeyTypes(char character, int keyCode)
    {
        if (this.canEditName) this.nameField.handleKeyTyped(keyCode, character);
        this.numberField.handleKeyTyped(keyCode, character);
    }
}
