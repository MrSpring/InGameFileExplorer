package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiFile implements IGui
{
    int x, y, w, h;
    String filePath;
    GuiSimpleButton button;
    RenderType renderType;
    long timeAtLastClick = 0;

    public GuiFile(int xPos, int yPos, int width, int height, String path, RenderType renderType)
    {
        this.x = xPos;
        this.y = yPos;
        this.w = width;
        this.h = height;
        this.filePath = path;
        button = new GuiSimpleButton(x, y, w, h, "");
        this.renderType = renderType;
    }

    public GuiFile(int xPos, int yPos, int width, int height, File path, RenderType type)
    {
        this(xPos, yPos, width, height, path.getPath(), type);
    }

    public EnumFileType getFileType()
    {
        if (filePath != null)
        {
            int lastDot = filePath.lastIndexOf('.');
            String extension = filePath.substring(lastDot);
            return EnumFileType.getFileTypeFor(extension);
        } else return EnumFileType.UNKNOWN;
    }

    public RenderType getRenderType()
    {
        return renderType;
    }

    public void setRenderType(RenderType renderType)
    {
        this.renderType = renderType;
    }

    public File getFile()
    {
        return new File(filePath);
    }

    public String getShortFileName()
    {
        int lastFileSeperator = this.filePath.lastIndexOf('\\') + 1;
        return this.filePath.substring(lastFileSeperator);
    }

    private void renderSquare(Minecraft minecraft)
    {
        float nameHeight = 30;
        float iconSize = Math.min(w, h - nameHeight);
        DrawingHelper.drawIcon(this.getFileType().getIcon(), x - ((iconSize - w) / 2), y, iconSize, iconSize);
        DrawingHelper.drawQuad(x, y + iconSize, w, 1, Color.WHITE, 1F);
        minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 4, y + iconSize + (nameHeight / 2) - 4, 0xFFFFFF, true);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        //DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);
        button.draw(minecraft, mouseX, mouseY);

        switch (this.getRenderType())
        {
            case SQUARE_GRID:
                renderSquare(minecraft);
                break;
        }
    }

    @Override
    public void update()
    {
        button.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (this.button.mouseDown(mouseX, mouseY, mouseButton))
        {
            long systemTime = System.currentTimeMillis();
            System.out.println(systemTime + ", " + timeAtLastClick + ", " + (systemTime - timeAtLastClick));
            if (systemTime - timeAtLastClick < 250)
                this.button.makeHighlighted();
            else this.button.stopBeingHighlighted();
            this.timeAtLastClick = System.currentTimeMillis();
            return true;
        } else
        {
            this.button.stopBeingHighlighted();
            return false;
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

    }

    public enum RenderType
    {
        LIST,
        SQUARE_GRID,
        LONG_GRID
    }
/*
    int x, y, w, h;
    File file;
    GuiSimpleButton button;
    FileType type;

    public GuiFile(int xPos, int yPos, int width, int height, File file)
    {
        this.x = xPos;
        this.y = yPos;

        this.w = width;
        this.h = height;

        this.file = file;
        button = new GuiSimpleButton(x, y, width, height, "");

        if (file.isDirectory())
            type = FileType.FOLDER;
        else
        {
            int lastDot = file.toURI().toASCIIString().lastIndexOf(".");
            if (lastDot > 0)
            {
                String extension = file.toURI().toASCIIString().substring(lastDot);

                if (extension.equalsIgnoreCase(".txt"))
                    type = FileType.TEXT_FILE;
                else if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg"))
                    type = FileType.IMAGE;
                else type = FileType.UNKNOWN;
            } else type = FileType.UNKNOWN;
        }
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setHightlighted()
    {
        this.button.makeHighlighted();
    }

    public void stopBeingHighlighted()
    {
        this.button.stopBeingHighlighted();
    }

    public boolean isHighlighted()
    {
        return this.button.isHighlighted();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        button.draw(minecraft, mouseX, mouseY);
        float size = this.h;
        DrawingHelper.drawOutline(x, y, size, size, Color.WHITE, 1F);
        switch (type)
        {
            case FOLDER:
                DrawingHelper.drawFolderIcon(x + 1, y + 1, size - 2, size - 2, Color.WHITE, 1F);
                break;
            case TEXT_FILE:
                DrawingHelper.drawTextFileIcon(x + 1, y + 1, size - 2, size - 2, Color.WHITE, 1F);
                break;
            case UNKNOWN:
                //DrawingHelper.drawRect(x + 2, y + 2, size - 4, size - 4, Color.WHITE, 1F);
                minecraft.fontRendererObj.drawStringWithShadow("?", x + (size / 2) - 2, y + (size / 2) - 4, 0xFFFFFF);
                break;
            case IMAGE:
                //minecraft.fontRendererObj.drawStringWithShadow("M", x + (size / 2) - 2, y + (size / 2) - 4, 0xFFFFFF);
                DrawingHelper.drawImageIcon(x + 1, y + 1, size - 2, size - 2, Color.WHITE, 1F);
                break;
        }
        String fileName = file.getName();

        int textY = y + (h / 2) - 4;
        int maxTextWidth=w - (int) size - 4;

        if (minecraft.fontRendererObj.getStringWidth(fileName) > maxTextWidth)
            textY -= 4;

        minecraft.fontRendererObj.drawSplitString(file.getName(), x + (int) size + 3+1, textY+1, maxTextWidth, 0x4C4C4C);
        minecraft.fontRendererObj.drawSplitString(file.getName(), x + (int) size + 3, textY, maxTextWidth, 0xFFFFFF);
    }

    public boolean isDirectory()
    {
        return this.file.isDirectory();
    }

    public boolean isTextFile()
    {
        return this.type == FileType.TEXT_FILE;
    }

    public boolean isImage()
    {
        return this.type == FileType.IMAGE;
    }

    public String getPath()
    {
        return this.file.getPath();
    }

    @Override
    public void update()
    {
        button.setY(y);
        button.setX(x);
        button.update();
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return button.mouseDown(mouseX, mouseY, mouseButton);
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

    private enum FileType
    {
        UNKNOWN,
        FOLDER,
        TEXT_FILE,
        IMAGE
    }
*/
}
