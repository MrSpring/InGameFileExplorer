package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.gui.GuiImageViewer;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public class EditorImage extends Editor
{
    GuiImageViewer imageViewer;
    private int x;
    private int y;
    private int width;
    private int height;
    String fileName;

    public EditorImage(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h);

        this.imageViewer = new GuiImageViewer(file, x, y, w, h).enableFullscreenButton();
        fileName = file.getName();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        this.imageViewer.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.imageViewer.setX(x);
        this.imageViewer.setY(y);
        this.imageViewer.setWidth(width);
        this.imageViewer.setHeight(height);
        this.imageViewer.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return this.imageViewer.mouseDown(mouseX, mouseY, mouseButton);
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

    }

    @Override
    public boolean hasUnsavedChanges()
    {
        return false;
    }

    @Override
    public String getOpenFileName()
    {
        return fileName;
    }

    @Override
    public void save()
    {
    }
}
