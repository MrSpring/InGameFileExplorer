package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiMultiLineTextField;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public class EditorText extends Editor implements IMouseListener
{
    String lastSave;
    GuiMultiLineTextField textField;

    GuiSimpleButton saveButton, restoreButton;
    File file;

    public EditorText(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h);

        this.file = file;

        try
        {
            lastSave = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        textField = new GuiMultiLineTextField(x, y, w, h, lastSave).hideBackground();

        saveButton = new GuiSimpleButton(x - 62, y + h - 15, 50, 20, "gui.text_editor.save");
        saveButton.setEnabled(LiteModFileExplorer.config.acceptFileManipulation);
        restoreButton = new GuiSimpleButton(x - 62, y + h - 45, 50, 20, "gui.text_editor.reload");
    }

    @Override
    public boolean hasUnsavedChanges()
    {
        return !textField.getText().equals(lastSave);
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;

        this.textField.setWidth(w);
        this.textField.setHeight(h);
        this.textField.update();

        this.saveButton.setX(x - 62);
        this.saveButton.setY(y + h - 20);
        this.saveButton.update();

        this.restoreButton.setX(x - 62);
        this.restoreButton.setY(y + h - 45);
        this.restoreButton.update();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        this.textField.draw(minecraft, mouseX, mouseY);
        this.saveButton.draw(minecraft, mouseX, mouseY);
        this.restoreButton.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.saveButton.mouseDown(mouseX, mouseY, mouseButton))
            this.save();
        else if (this.restoreButton.mouseDown(mouseX, mouseY, mouseButton))
            this.restoreFromFile();

        return this.textField.mouseDown(mouseX, mouseY, mouseButton);
    }

    private void restoreFromFile()
    {
        try
        {
            String fromFile = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(this.file);
            this.textField.setText(fromFile, true);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void save()
    {
        try
        {
            LiteModFileExplorer.core.getFileLoader().writeToFile(this.file, this.textField.getText());
            this.lastSave = this.textField.getText();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getOpenFileName()
    {
        return file.getName();
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
        this.textField.handleKeyTyped(keyCode, character);
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        this.textField.handleMouseWheel(mouseX, mouseY, dWheelRaw);
    }
}
