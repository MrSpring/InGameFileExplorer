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
    boolean showBackground = true;
    List<GuiFileBase> guiFiles;
    String currentPath;

    int scrollHeight = 0;

    GuiSimpleButton openFile;
    GuiSimpleButton refreshList;
    GuiSimpleButton newFolder;
    GuiSimpleButton upOne;

    public GuiFileExplorer(int xPos, int yPos, int width, int height, String path)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        currentPath = path;

        guiFiles = new ArrayList<GuiFileBase>();

        openFile = new GuiSimpleButton(x, y, 50, 20, "Open").disable();
        refreshList = new GuiSimpleButton(x, y, 50, 20, "Refresh");
        newFolder = new GuiSimpleButton(x, y, 50, 20, "New File");
        upOne = new GuiSimpleButton(x, y, 50, 20, "Go Up");

        this.refreshList();
    }

    public GuiFileExplorer setShowBackground(boolean showBackground)
    {
        this.showBackground = showBackground;
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

        for (GuiFileBase guiFile : guiFiles)
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

        int totalHeight = this.guiFiles.size() * 35;
        if (totalHeight > this.h)
        {
            float scrollBarYRange = (h - 50);
            float maxScrollHeight = (this.guiFiles.size() * 35) - 40;
            float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
            float scrollBarY = scrollBarYRange * scrollProgress;
            DrawingHelper.drawQuad(x - 1, y + scrollBarY + 6, 2, 40, Color.WHITE, 1F);
        }
    }

    @Override
    public void update()
    {
        int totalHeight = this.guiFiles.size() * 35;
        if (totalHeight < this.h)
            scrollHeight = -5;

        for (GuiFileBase guiFile : this.guiFiles)
        {
            guiFile.update();
        }

        if (showControls)
        {
            this.openFile.update();
            this.refreshList.update();
            this.newFolder.update();
            this.upOne.update();
        }
    }

    private boolean drawGuiFile(int xOffset, int yOffset, GuiFileBase file)
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

        this.upOne.setX(xPos);
        this.upOne.setY(y + 5 + 75);
        this.upOne.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.openFile.mouseDown(mouseX, mouseY, mouseButton))
            return this.openSelectedFile();
        else if (this.refreshList.mouseDown(mouseX, mouseY, mouseButton))
            this.refreshList();
        else if (this.newFolder.mouseDown(mouseX, mouseY, mouseButton))
            ; // TODO: this.createNewFile();
        else if (this.upOne.mouseDown(mouseX, mouseY, mouseButton))
            this.goUpOne();
        else
        {
            boolean returnFromHere = false;
            this.openFile.disable();
            for (GuiFileBase guiFile : this.guiFiles)
                if (guiFile.mouseDown(mouseX, mouseY, mouseButton) && guiFile instanceof GuiFile)
                {
                    if (((GuiFile)guiFile).isDirectory())
                        openFile.enable();
                    returnFromHere = true;
                }

            if (returnFromHere)
                return true;
        }
        return false;
    }

    private void goUpOne()
    {
        int lastIndexOfSeperator = this.currentPath.lastIndexOf(File.separator);
        System.out.println("seperator = " + File.separator);
        if (lastIndexOfSeperator > 1)
        {
            String goTo = this.currentPath.substring(0, lastIndexOfSeperator + 1);
            System.out.println("lastIndexOfSeperator = " + lastIndexOfSeperator);
            System.out.println("goTo = " + goTo);
            this.openFile(new File(goTo));
        }
    }

    private boolean openSelectedFile()
    {
        for (GuiFileBase guiFile : this.guiFiles)
        {
            if (guiFile instanceof GuiFile)
                if (((GuiFile) guiFile).isSelected())
                {
                    this.openFile(((GuiFile) guiFile).getFile());
                    return true;
                }
        }
        return false;
    }

    private void openFile(File file)
    {
        if (file != null)
            if (file.exists())
                if (file.isDirectory())
                {
                    System.out.println("Opening directory at: " + file.toString());
                    this.currentPath = file.toString();
                    this.refreshList();
                    this.openFile.disable();
                    this.scrollHeight = -5;
                } else
                {
                    // TODO: Start a Runnable that tells the open Screen that a non-directory file has been opened
                }
    }

    private void refreshList()
    {
        this.guiFiles = new ArrayList<GuiFileBase>();

        List<File> filesAtCurrentPath = new ArrayList<File>();

        FileLoader.addFiles(currentPath, filesAtCurrentPath, false);

        for (File file : filesAtCurrentPath)
        {
            guiFiles.add(new GuiFile(0, 0, w - 10, 30, file, GuiFile.RenderType.LONG_GRID).setOnFileOpened(new Runnable()
            {
                @Override
                public void run()
                {
                    GuiFileExplorer.this.openSelectedFile();
                }
            }));
        }
    }

    private void createNewFile()
    {

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
        this.addScroll(-mouseWheel);
    }
}
