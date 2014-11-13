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

    private enum FileType
    {
        UNKNOWN,
        FOLDER,
        TEXT_FILE,
        IMAGE
    }
}
