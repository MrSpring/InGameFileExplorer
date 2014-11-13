package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiEditableTextField implements IGui
{
    int x, y, w, h, minW, buttonSize;
    GuiTextField textField;
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

        textField = new GuiTextField(200, fontRenderer, x + (buttonSize * 2) + (spacing * 2), y, w - (buttonSize * 2) - (spacing * 2), h);
        editButton = new GuiSimpleButton(x + buttonSize + spacing, y, buttonSize, buttonSize, "");
        cancelButton = new GuiSimpleButton(x + buttonSize + spacing, y, buttonSize, buttonSize, "");
        saveButton = new GuiSimpleButton(x, y, buttonSize, buttonSize, "");
    }

    public void setX(int newX)
    {
        x = newX;
    }

    public void setOnSave(Runnable onSave)
    {
        this.onSave = onSave;
    }

    public void startEditing()
    {
        this.editing = true;
        textField = new GuiTextField(200, renderer, x + (buttonSize * 2) + (spacing * 2), y, w - (buttonSize * 2) - (spacing * 2), h);
        textField.setText(value);
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
            this.textField.drawTextBox();
            this.saveButton.draw(minecraft, mouseX, mouseY);
            this.cancelButton.draw(minecraft, mouseX, mouseY);

            DrawingHelper.drawCheckMarkIcon(x + 1, y + 1, buttonSize - 2, buttonSize - 2, Color.WHITE, 1F);
            DrawingHelper.drawCross(x + 1 + buttonSize + spacing, y + 1, buttonSize - 2, buttonSize - 2, Color.WHITE, 1F);

        } else
        {
            this.editButton.draw(minecraft, mouseX, mouseY);

            DrawingHelper.drawEditIcon(x + 1 + buttonSize + spacing, y + 1, buttonSize - 2, buttonSize - 2, Color.WHITE, 1F);

            minecraft.fontRendererObj.drawStringWithShadow(value, x + ((buttonSize + 2) * 2), y + (buttonSize / 2) - 5, 0xFFFFFF);
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
                this.textField.mouseClicked(mouseX, mouseY, mouseButton);
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

    public boolean isEditing()
    {
        return editing;
    }

    public void keyPressed(int keyCode, char character)
    {
        if (this.editing && this.textField.isFocused())
            this.textField.textboxKeyTyped(character, keyCode);
    }
*/
}
