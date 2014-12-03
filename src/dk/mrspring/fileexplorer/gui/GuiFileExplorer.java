package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 03-12-2014 for In-Game File Explorer.
 */
public class GuiFileExplorer implements IGui, IMouseListener
{
    int x, y, w, h;
    boolean showControls = true;
    boolean showFileDetails = true;
    boolean showBackground = true;
    List<GuiFile> guiFiles;
    String currentPath;

    int scrollHeight = 0;

    GuiSimpleButton openFile;
    GuiSimpleButton refreshList;
    GuiSimpleButton newFolder;

    public GuiFileExplorer(int xPos, int yPos, int width, int height, String path)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        currentPath = path;

        guiFiles = new ArrayList<GuiFile>();

        List<File> filesAtCurrentPath = new ArrayList<File>();

        FileLoader.addFiles(currentPath, filesAtCurrentPath, false);

        int yOffset = 0;

        openFile = new GuiSimpleButton(x, y, 50, 20, "Open");
        refreshList = new GuiSimpleButton(x, y, 50, 20, "Refresh");
        newFolder = new GuiSimpleButton(x, y, 50, 20, "New Folder");

        for (File file : filesAtCurrentPath)
        {
            guiFiles.add(new GuiFile(0, yOffset, w - 10, 30, file, GuiFile.RenderType.LONG_GRID));
        }
    }

    public GuiFileExplorer setShowBackground(boolean showBackground)
    {
        this.showBackground = showBackground;
        return this;
    }

    public GuiFileExplorer setShowFileDetails(boolean showFileDetails)
    {
        this.showFileDetails = showFileDetails;
        return this;
    }

    public GuiFileExplorer setShowControls(boolean showControls)
    {
        this.showControls = showControls;
        return this;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        int width = w;

        if (showControls)
        {
            width -= 75;
            DrawingHelper.drawQuad(x + width + 5 + 5, y + 5, 1, h - 10, Color.WHITE, 1F);
            DrawingHelper.drawQuad(x + width + 5 + 5 + 61, y + 5, 1, h - 10, Color.WHITE, 1F);
            this.drawControls(minecraft, mouseX, mouseY, x + width + 5 + 11);
        }

        if (showBackground)
            DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);

        int yOffset = 0 - scrollHeight, xOffset = 5;

        for (GuiFile guiFile : guiFiles)
        {
            if (!drawGuiFile(xOffset, yOffset, guiFile))
            {
                guiFile.setX(xOffset + x);
                guiFile.setY(yOffset + y);
                guiFile.setWidth(width);
                guiFile.draw(minecraft, mouseX, mouseY);
            }

            yOffset += 35;
        }

        float scrollBarYRange = (h - 50);

        float maxScrollHeight = (this.guiFiles.size() * 35) - 40;

        float scrollProgress = (float) this.scrollHeight / maxScrollHeight;

        float scrollBarY = scrollBarYRange * scrollProgress;

        DrawingHelper.drawQuad(x-1, y + scrollBarY+6, 2, 40, Color.WHITE, 1F);
    }

    private boolean drawGuiFile(int xOffset, int yOffset, GuiFile file)
    {
        boolean isFileTooHigh = yOffset < -70;
        boolean isFileTooLow = yOffset > this.h + 40;
        return isFileTooHigh || isFileTooLow;
    }

    private void drawControls(Minecraft minecraft, int mouseX, int mouseY, int xPos)
    {
        this.openFile.setX(xPos);
        this.openFile.setY(y + 5);
        this.openFile.draw(minecraft, mouseX, mouseY);

        this.refreshList.setX(xPos);
        this.refreshList.setY(y + 5 + 25);
        this.refreshList.draw(minecraft, mouseX, mouseY);

        this.newFolder.setX(xPos);
        this.newFolder.setY(y + 5 + 50);
        this.newFolder.draw(minecraft, mouseX, mouseY);
    }

    private void drawFileDetails()
    {

    }

    @Override
    public void update()
    {
        for (GuiFile guiFile : this.guiFiles)
        {
            guiFile.update();
        }

        if (showControls)
        {
            this.openFile.update();
            this.refreshList.update();
            this.newFolder.update();
        }
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        for (GuiFile guiFile : this.guiFiles)
            if (guiFile.mouseDown(mouseX, mouseY, mouseButton))
                return true;
        if (this.openFile.mouseDown(mouseX, mouseY, mouseButton))
            ; // TODO: Open file
        else if (this.refreshList.mouseDown(mouseX, mouseY, mouseButton))
            ; // TODO: this.refreshFileList();
        else if (this.newFolder.mouseDown(mouseX, mouseY, mouseButton))
            ; // TODO: this.createNewFile();
        return false;
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

    private void addScroll(int scrollHeight)
    {
        int maxScrollHeight, minScrollHeight = -5, scrollHeightAfterAddition = this.scrollHeight + scrollHeight;

        maxScrollHeight = (this.guiFiles.size() * 35) - 40;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scrollHeight = scrollHeightAfterAddition;

        /*if (scrollHeightAfterAddition < maxScrollHeight && scrollHeightAfterAddition > minScrollHeight)
            this.scrollHeight = scrollHeightAfterAddition;*/
    }

    @Override
    public void handleMouseInput()
    {
        int mouseWheel = Mouse.getDWheel();
        mouseWheel /= 4;
        this.addScroll(mouseWheel);
    }
}
