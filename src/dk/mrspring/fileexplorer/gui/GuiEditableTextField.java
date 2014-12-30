package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiEditableTextField implements IGui
{
    int x, y, w, h, minW, buttonSize;
    GuiCustomTextField textField;
    GuiSimpleButton editButton, saveButton, cancelButton;
    String value;
    boolean editing = false;
    Runnable onSave;
    FontRenderer renderer;
    int spacing = 5;

    public GuiEditableTextField(int xPos, int yPos, int width, int height, String startValue, FontRenderer fontRenderer)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        value = startValue;
        if (w < minW)
            w = minW;

        renderer = fontRenderer;

        buttonSize = height;

//        textField = new GuiTextField(200, fontRenderer, x + (buttonSize * 2) + (spacing * 2), y, w - (buttonSize * 2) - (spacing * 2), h);
        textField = new GuiCustomTextField(x + (buttonSize * 2) + (spacing * 2), y, w - (buttonSize * 2) - (spacing * 2), h, startValue);
        editButton = new GuiSimpleButton(x + buttonSize + spacing, y, buttonSize, buttonSize, "");
        cancelButton = new GuiSimpleButton(x + buttonSize + spacing, y, buttonSize, buttonSize, "");
        saveButton = new GuiSimpleButton(x, y, buttonSize, buttonSize, "");
    }

    public void setX(int newX)
    {
        x = newX;
        this.textField.setX(newX);
    }

    public void setOnSave(Runnable onSave)
    {
        this.onSave = onSave;
    }

    public void startEditing()
    {
        this.editing = true;
        textField.setText(value);
        textField.setCursorPos(0, false);
    }

    public void cancelEditing()
    {
        this.editing = false;
    }

    public void saveEditing()
    {
        this.editing = false;
        this.value = this.textField.getText();
        if (onSave != null)
            onSave.run();
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (editing)
        {
            this.textField.draw(minecraft, mouseX, mouseY);
            this.saveButton.draw(minecraft, mouseX, mouseY);
            this.cancelButton.draw(minecraft, mouseX, mouseY);

            DrawingHelper.drawIcon(DrawingHelper.checkMarkIcon, x + 1, y + 1, buttonSize - 2, buttonSize - 2);
            DrawingHelper.drawIcon(DrawingHelper.crossIcon, x + 1 + buttonSize + spacing, y + 1, buttonSize - 2, buttonSize - 2);

        } else
        {
            this.editButton.draw(minecraft, mouseX, mouseY);

            DrawingHelper.drawIcon(DrawingHelper.editIcon, x + 1 + buttonSize + spacing, y + 1, buttonSize - 2, buttonSize - 2);

            minecraft.fontRendererObj.drawStringWithShadow(value, x + ((buttonSize + 2) * 2) + 5, y + (buttonSize / 2) - 5, 0xFFFFFF);
        }
    }

    @Override
    public void update()
    {
        this.editButton.setX(x + buttonSize + spacing);
        this.cancelButton.setX(x + buttonSize + spacing);
        this.saveButton.setX(x);

        this.editButton.update();
        this.cancelButton.update();
        this.saveButton.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (editing)
        {
            if (this.saveButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.saveEditing();
                return true;
            } else if (this.cancelButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.cancelEditing();
                return true;
            } else
            {
                this.textField.mouseDown(mouseX, mouseY, mouseButton);
                return false;
            }
        } else
        {
            if (this.editButton.mouseDown(mouseX, mouseY, mouseButton))
            {
                this.startEditing();
                return true;
            } else return false;
        }
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
        if (this.editing && this.textField.isFocused())
            this.textField.handleKeyTyped(keyCode, character);
    }

    public boolean isEditing()
    {
        return editing;
    }
}
