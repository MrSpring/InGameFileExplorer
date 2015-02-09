package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.FileType;
import dk.mrspring.fileexplorer.helper.Color;
import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiFile extends GuiFileBase
{
    GuiSimpleButton button;
    long timeAtLastClick = 0;
    private Runnable onFileOpened = new Runnable()
    {
        @Override
        public void run()
        {

        }
    };
    boolean wrapName = true;

    public GuiFile(int xPos, int yPos, int width, int height, String path, RenderType renderType)
    {
        super(xPos, yPos, width, height);

        this.filePath = path;
        button = new GuiSimpleButton(x, y, w, h, "");
        this.renderType = renderType;
    }

    public GuiFile(int xPos, int yPos, int width, int height, File path, RenderType type)
    {
        this(xPos, yPos, width, height, path.getPath(), type);
    }

    public GuiFile setOnFileOpened(Runnable onFileOpened)
    {
        this.onFileOpened = onFileOpened;
        return this;
    }

    public FileType getFileType()
    {
        if (filePath != null)
        {
            String extension = FileLoader.getFileExtension(new File(filePath), true);
            if (extension.equals("") || new File(filePath).isDirectory())
                return LiteModFileExplorer.getFileType("directory");
            else return LiteModFileExplorer.getFileType(extension);
        } else return LiteModFileExplorer.getFileType("unknown");
    }

    public boolean isDirectory()
    {
        return new File(filePath).isDirectory();
    }

    public void setX(int x)
    {
        super.setX(x);
        this.button.setX(x);
    }

    public void setY(int y)
    {
        super.setY(y);
        this.button.setY(y);
    }

    public void setWidth(int w)
    {
        super.setWidth(w);
        this.button.setWidth(w);
    }

    public void setHeight(int h)
    {
        super.setHeight(h);
        this.button.setHeight(h);
    }

    public boolean isSelected()
    {
        return this.button.isHighlighted();
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

    public void setWrapName(boolean wrapName)
    {
        this.wrapName = wrapName;
    }

    public boolean wrapName()
    {
        return wrapName;
    }

    private void renderSquare(Minecraft minecraft)
    {
        int nameHeight = 16;
        int nameWidth = minecraft.fontRendererObj.getStringWidth(this.getShortFileName());
        float iconSize = Math.min(w, h - nameHeight);
        int nameY = y + h - nameHeight + (nameHeight / 2) - 4;
        if (nameWidth > w - 8 && wrapName())
        {
            nameHeight += 9;
            nameY -= 9;
        }
        DrawingHelper.drawIcon(this.getFileType().getIcon(), x - ((iconSize - w) / 2), y, iconSize, iconSize);

        DrawingHelper.drawQuad(x, y + h - nameHeight, w, 1, Color.WHITE, 1F);
        DrawingHelper.drawQuad(x, y + h - nameHeight - 1, w, 1, Color.LT_GREY, 1F);
        if (wrapName())
        {
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 5, nameY + 1, w - 8, 0x4C4C4C);
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 4, nameY, w - 8, 0xFFFFFF);
        } else minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 4, nameY, 0xFFFFFF, true);
    }

    private void renderBigList(Minecraft minecraft)
    {
        float height = h;
        float minimumHeightFor2Lines = 20;

        float iconSize = height;

        int nameWidth = minecraft.fontRendererObj.getStringWidth(this.getShortFileName());
        int nameY = y + h / 2 - 4;

        int maxNameWidth = w - (int) iconSize - 6;

        if (nameWidth > maxNameWidth)
        {
            if (height < minimumHeightFor2Lines)
                height = minimumHeightFor2Lines;

            nameY -= 4;
            button.setHeight((int) height);
        }

        iconSize = height;

        DrawingHelper.drawQuad(x + iconSize - 2, y + 1, 1, height - 2, Color.LT_GREY, 1F);
        DrawingHelper.drawQuad(x + iconSize - 1, y + 1, 1, height - 2, Color.WHITE, 1F);

        DrawingHelper.drawIcon(this.getFileType().getIcon(), x, y, iconSize, iconSize, false);

//        minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + (int) iconSize + 3, nameY, w - (int) iconSize - 6, 0xFFFFFF);
        DrawingHelper.drawSplitString(minecraft.fontRendererObj, x + (int) iconSize + 3, nameY, this.getShortFileName(), 0xFFFFFF, w - (int) iconSize - 6, true);
    }

    private void renderList(Minecraft minecraft)
    {
        minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 3, y + (h / 2) - 4, 0xFFFFFF, true);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        //DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);
        button.draw(minecraft, mouseX, mouseY);

        RenderType type = this.getRenderType();
        switch (type)
        {
            case SQUARE_GRID:
                renderSquare(minecraft);
                break;
            case LONG_GRID:
                renderBigList(minecraft);
                break;
            case LIST:
                renderList(minecraft);
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
            this.button.makeHighlighted();
            long systemTime = System.currentTimeMillis();
            if (systemTime - timeAtLastClick < 250)
            {
                this.onFileOpened.run();
            }
            this.timeAtLastClick = System.currentTimeMillis();
            return true;
        } else
        {
            this.button.stopBeingHighlighted();
            return false;
        }
    }
}
