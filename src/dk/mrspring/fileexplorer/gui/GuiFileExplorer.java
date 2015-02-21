package dk.mrspring.fileexplorer.gui;

import com.mumfrey.liteloader.gl.GLClippingPlanes;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import dk.mrspring.fileexplorer.helper.FileSorter;
import dk.mrspring.fileexplorer.helper.GuiHelper;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 03-12-2014 for In-Game File Explorer.
 */
public class GuiFileExplorer implements IGui, IMouseListener
{
    int x, y, w, h, listHeight;
    boolean showControls = true;
    boolean showBackground = true;
    boolean showPath = true;
    List<GuiFileBase> guiFiles;
    String currentPath;
    IOnFileOpened onFileOpened;
    int pathX = 0, pathY = 0;

    boolean drawPath = false;
    GuiSimpleButton[] pathButtons;
    int scrollHeight = 0;

    GuiSimpleButton openFile;

    GuiSimpleButton refreshList;
    GuiSimpleButton newFolder;
    GuiSimpleButton deleteFile;
    GuiSimpleButton upOne;

    public GuiFileExplorer(int xPos, int yPos, int width, int height, String path)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        currentPath = new File(path).getAbsolutePath();

        guiFiles = new ArrayList<GuiFileBase>();

        openFile = new GuiSimpleButton(x, y, 50, 20, "gui.explorer.open").disable();
        refreshList = new GuiSimpleButton(x, y, 50, 20, "gui.explorer.refresh");
        newFolder = new GuiSimpleButton(x, y, 20, 20, "").setIcon(LiteModFileExplorer.core.getIcon("new_file"));
        deleteFile = new GuiSimpleButton(x + 30, y, 20, 20, "").setIcon(LiteModFileExplorer.core.getIcon("delete_file")).disable();
        upOne = new GuiSimpleButton(x, y, 50, 20, "gui.explorer.go_up");

        this.newFolder.setEnabled(LiteModFileExplorer.config.acceptFileManipulation);
        this.deleteFile.setEnabled(LiteModFileExplorer.config.acceptFileManipulation);

        this.refreshList();
    }

    public GuiFileExplorer setOnFileOpened(IOnFileOpened onFileOpened)
    {
        this.onFileOpened = onFileOpened;
        return this;
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

    public GuiFileExplorer setPathEditorPosition(int x, int y)
    {
        this.pathX = x;
        this.pathY = y;
        this.drawPath = true;
        this.refreshList();
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
        listHeight = h;

        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

        if (showControls)
        {
            width -= 75;
            helper.drawVerticalLine(new Vector(x + width + 10, y + 6), h - 10, 1, true);
//                    .drawVerticalLine(new Vector(x + width + 10 + 61, y + 6), h - 10, 1, true);
//            helper.drawShape(new Quad(x + width + 5 + 6, y + 6, 1, h - 10).setColor(Color.DK_GREY));
//            helper.drawShape(new Quad(x + width + 5 + 6 + 61, y + 6, 1, h - 10).setColor(Color.DK_GREY));

//            helper.drawShape(new Quad(x + width + 5 + 5, y + 5, 1, h - 10).setColor(Color.WHITE));
//            helper.drawShape(new Quad(x + width + 5 + 5 + 61, y + 5, 1, h - 10).setColor(Color.WHITE));
            this.drawControls(minecraft, mouseX, mouseY, x + width + 5 + 11);
        }

        if (showPath)
        {
            String openFile = getCurrentAbsolutePath();
            listHeight -= (9 * LiteModFileExplorer.core.getDrawingHelper().drawText("Open directory:\n§7" + openFile, new Vector(x, y + listHeight), 0xFFFFFF, true, width, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.BOTTOM)) + 4;
        }

        if (showBackground)
            helper.drawButtonThingy(new Quad(x - 2, y - 2, width + 11, listHeight + 4), 0, true);

        int yOffset = -scrollHeight, xOffset = 5;

        GLClippingPlanes.glEnableClipping(x, x + width + 5, y, y + listHeight);
        if (guiFiles.size() > 0)
        {
            for (GuiFileBase guiFile : guiFiles)
            {
                if (!drawGuiFile(xOffset, yOffset, listHeight, guiFile))
                {
                    guiFile.setX(xOffset + x);
                    guiFile.setY(yOffset + y);
                    guiFile.setWidth(width);
                    guiFile.draw(minecraft, mouseX, mouseY);
                }
                yOffset += guiFile.getHeight() + 5;
            }
        } else
        {
            int textMaxLength = w;
            if (this.showControls)
                textMaxLength -= 75;

            int textX = x + textMaxLength / 2, textY = y + 10;
            helper.drawText(TranslateHelper.translate("gui.explorer.no_files"), new Vector(textX, textY), 0xFFFFFF, true, textMaxLength, dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment.CENTER, dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.TOP);
        }
        GLClippingPlanes.glDisableClipping();

        int totalHeight = this.getListHeight();
        if (totalHeight > listHeight)
        {
            float scrollBarYRange = (listHeight - 40);
            if (showBackground)
                scrollBarYRange -= 2;
            float maxScrollHeight = this.getMaxScrollHeight();
            float scrollProgress = (float) this.scrollHeight / maxScrollHeight;
            float scrollBarY = scrollBarYRange * scrollProgress;
            if (showBackground)
                scrollBarY += 1;
            float scrollbarX = x;
            if (showBackground)
                scrollbarX += 2;
            if (!showBackground)
                helper.drawShape(new Quad(scrollbarX, y + scrollBarY + 1, 2, 40).setColor(Color.DK_GREY));
            helper.drawShape(new Quad(scrollbarX - 1, y + scrollBarY, 2, 40).setColor(Color.WHITE));
        }

//        if (this.drawPath)
//            this.drawPath(minecraft, mouseX, mouseY);
    }

    private void drawControls(Minecraft minecraft, int mouseX, int mouseY, int xPos)
    {
        this.openFile.setX(xPos);
        this.openFile.setY(y);
        this.openFile.draw(minecraft, mouseX, mouseY);

        this.refreshList.setX(xPos);
        this.refreshList.setY(y + 25);
        this.refreshList.draw(minecraft, mouseX, mouseY);

        this.newFolder.setX(xPos);
        this.newFolder.setY(y + 50);
        this.newFolder.draw(minecraft, mouseX, mouseY);
        this.deleteFile.setX(xPos + 30);
        this.deleteFile.setY(y + 50);
        this.deleteFile.draw(minecraft, mouseX, mouseY);

        this.upOne.setX(xPos);
        this.upOne.setY(y + 75);
        this.upOne.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public void update()
    {
        if (!LiteModFileExplorer.config.acceptFileManipulation)
            this.deleteFile.disable();

        int totalHeight = this.guiFiles.size() * 35;
        if (totalHeight < this.h)
            scrollHeight = 0;

        for (GuiFileBase guiFile : this.guiFiles)
        {
            guiFile.update();
        }

        if (showControls)
        {
            this.openFile.update();
            this.refreshList.update();
            this.newFolder.update();
            this.deleteFile.update();
            this.upOne.update();
        }

        if (drawPath)
        {
            if (this.pathButtons != null)
                if (this.pathButtons.length > 0)
                    for (GuiSimpleButton button : this.pathButtons)
                        button.update();
        }
    }

    private boolean drawGuiFile(int xOffset, int yOffset, int height, GuiFileBase file)
    {
        boolean isFileTooHigh = yOffset < -70;
        boolean isFileTooLow = yOffset > height + 40;
        return (isFileTooHigh || isFileTooLow);
    }

    private void drawPath(Minecraft minecraft, int mouseX, int mouseY)
    {
        for (int i = 0; i < pathButtons.length; i++)
        {
            GuiSimpleButton button = pathButtons[i];
            button.draw(minecraft, mouseX, mouseY);
            if (i + 1 < pathButtons.length)
                minecraft.fontRendererObj.drawString(">", button.getX() + button.getWidth() + 1, button.getY() + 3, 0xFFFFFF);
        }
    }

    public GuiFileExplorer setShowPath(boolean showPath)
    {
        this.showPath = showPath;
        return this;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            if (this.openFile.mouseDown(mouseX, mouseY, mouseButton))
                return this.openSelectedFile();
            else if (this.refreshList.mouseDown(mouseX, mouseY, mouseButton))
                this.refreshList();
            else if (this.newFolder.mouseDown(mouseX, mouseY, mouseButton))
                this.createNewFile();
            else if (this.deleteFile.mouseDown(mouseX, mouseY, mouseButton))
                this.deleteSelectedFile();
            else if (this.upOne.mouseDown(mouseX, mouseY, mouseButton))
                this.goUpOne();
            else if (this.isPathClicked(mouseX, mouseY, mouseButton))
                return true;
            else
            {
                boolean returnFromHere = false;
                this.openFile.disable();
                this.deleteFile.disable();
                for (GuiFileBase guiFile : this.guiFiles)
                    if (guiFile.mouseDown(mouseX, mouseY, mouseButton) && guiFile instanceof GuiFile)
                    {
                        deleteFile.enable();
                        if (!((GuiFile) guiFile).getFileType().equals(LiteModFileExplorer.getFileType("unknown")))
                            openFile.enable();
                        returnFromHere = true;
                    }

                if (returnFromHere)
                    return true;
            }
        }
        return false;
    }

    private boolean isPathClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (this.drawPath)
            if (this.pathButtons != null)
                if (this.pathButtons.length > 0)
                {
                    String pathSoFar = "";
                    for (GuiSimpleButton button : this.pathButtons)
                    {
                        pathSoFar += button.text;
                        if (button.mouseDown(mouseX, mouseY, mouseButton))
                        {
                            this.openFile(new File(pathSoFar));
                            return true;
                        } else pathSoFar += File.separator;
                    }
                }
        return false;
    }

    private void goUpOne()
    {
        int lastIndexOfSeperator = this.currentPath.lastIndexOf(File.separator);
        if (lastIndexOfSeperator > 1)
        {
            String goTo = this.currentPath.substring(0, lastIndexOfSeperator + 1);
            this.openFile(new File(goTo));
        }
    }

    private boolean deleteSelectedFile()
    {
        for (GuiFileBase guiFile : this.guiFiles)
        {
            if (guiFile instanceof GuiFile)
                if (((GuiFile) guiFile).isSelected())
                {
                    this.deleteFile(((GuiFile) guiFile).getFile());
                    return true;
                }
        }
        return false;
    }

    private void deleteFile(File file)
    {
        try
        {
            if (file != null)
                if (file.exists())
                    if (LiteModFileExplorer.core.getFileLoader().deleteFile(file))
                        this.refreshList();
        } catch (Exception e)
        {
            e.printStackTrace();
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
        {
            if (file.exists())
            {
                if (file.isDirectory())
                {
                    this.currentPath = file.toString();
                    this.refreshList();
                    this.openFile.disable();
                    this.deleteFile.disable();
                    this.scrollHeight = 0;
                } else
                {
                    if (this.onFileOpened != null)
                    {
                        this.onFileOpened.onOpened(file);
                    }
                }
            }
        }
    }

    private void refreshList()
    {
        this.guiFiles = new ArrayList<GuiFileBase>();

        File[] filesAtCurrentPath = LiteModFileExplorer.core.getFileLoader().getFilesInFolder(new File(currentPath), false, FileLoader.DEFAULT_FILTER);

//        FileLoader.addFiles(currentPath, filesAtCurrentPath, false);

        FileSorter.sortFiles(filesAtCurrentPath, LiteModFileExplorer.getDefaultSortingType());

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

        String actualPath = new File(currentPath).getPath();
        String[] foldersToPath = actualPath.split("\\" + File.separator);

        if (foldersToPath.length > 0)
        {
            this.pathButtons = new GuiSimpleButton[foldersToPath.length];
            int xOffset = 0;
            for (int i = 0; i < foldersToPath.length; i++)
            {
                String folderName = foldersToPath[i];
                int folderWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(folderName) + 6;
                this.pathButtons[i] = new GuiSimpleButton(pathX + xOffset, pathY, folderWidth, 14, folderName);
                xOffset += folderWidth + 6;
            }
        }
    }

    private void createNewFile()
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
        {
            this.guiFiles.add(0, new GuiFileNew(0, 0, w - 10, 20, new GuiFileNew.INewFileEvents()
            {
                @Override
                public void onCreated(GuiFileNew newFile, String path)
                {
                    GuiFileExplorer.this.onFileCreated(newFile, path);
                }

                @Override
                public void onCanceled(GuiFileNew guiFileNew, String path)
                {
                    GuiFileExplorer.this.cancelNewFile(guiFileNew);
                }
            }));
            this.scrollHeight = 0;
        }
    }

    private void onFileCreated(GuiFileNew newFile, String path)
    {
        try
        {
            File file = new File(this.currentPath + File.separator + path);
            int lastDot = path.lastIndexOf('.');
            if (!file.exists())
                if (lastDot >= 0)
                {
                    if (file.createNewFile())
                        guiFiles.add(0, new GuiFile(0, 0, w - 10, 30, file, GuiFileBase.RenderType.LONG_GRID));
                } else if (file.mkdir())
                    guiFiles.add(0, new GuiFile(0, 0, w - 10, 30, file, GuiFileBase.RenderType.LONG_GRID));

            guiFiles.remove(newFile);
        } catch (IOException e)
        {
            System.err.println("Failed to create file: \"" + path + "\":");
            e.printStackTrace();
        }
    }

    private void cancelNewFile(GuiFileNew guiFileNew)
    {
        this.guiFiles.remove(guiFileNew);
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
        for (GuiFileBase guiFile : this.guiFiles)
        {
            GuiFileNew newGuiFile = guiFile instanceof GuiFileNew ? ((GuiFileNew) guiFile) : null;
            if (newGuiFile != null)
                newGuiFile.handleKeyTyped(keyCode, character);
        }
    }

    private void addScroll(int scrollHeight)
    {
        int maxScrollHeight = getMaxScrollHeight(), minScrollHeight = 0, scrollHeightAfterAddition = this.scrollHeight + scrollHeight;

        if (scrollHeightAfterAddition > maxScrollHeight)
            scrollHeightAfterAddition = maxScrollHeight;
        else if (scrollHeightAfterAddition < minScrollHeight)
            scrollHeightAfterAddition = minScrollHeight;

        this.scrollHeight = scrollHeightAfterAddition;
    }

    private int getMaxScrollHeight()
    {
        return getListHeight() - listHeight;
    }

    private int getListHeight()
    {
        int height = 0;
        if (guiFiles.size() > 0)
            for (GuiFileBase guiFile : guiFiles)
//                if (!drawGuiFile(5, height, guiFile))
                height += guiFile.getHeight() + 5;
        return height;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            int mouseWheel = dWheelRaw;
            mouseWheel /= 4;
            if (mouseWheel != 0)
                this.addScroll(-mouseWheel);
        }
    }

    public String getCurrentPath()
    {
        return currentPath;
    }

    public String getCurrentAbsolutePath()
    {
        return new File(getCurrentPath()).getAbsolutePath();
    }

    public interface IOnFileOpened
    {
        public void onOpened(File file);
    }
}
