package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.FileType;
import dk.mrspring.fileexplorer.loader.FileLoader;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Date;

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
            String extension = FileLoader.getFileExtension(filePath, true);
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
        return getFile().getName();
    }

    public long getLastEdit()
    {
        return getFile().lastModified();
    }

    public Date getLastEditDate()
    {
        return new Date(getLastEdit());
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

        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
        helper.drawIcon(this.getFileType().getIcon(), new Quad(x - ((iconSize - w) / 2), y, iconSize, iconSize));

        helper.drawShape(new Quad(x, y + h - nameHeight, w, 1).setColor(Color.WHITE));
        helper.drawShape(new Quad(x, y + h - nameHeight - 1, w, 1).setColor(Color.LT_GREY));
        if (wrapName())
        {
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 5, nameY + 1, w - 8, 0x4C4C4C);
            minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + 4, nameY, w - 8, 0xFFFFFF);
        } else minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 4, nameY, 0xFFFFFF, true);
    }

    private void renderBigList(Minecraft minecraft, int mouseX, int mouseY)
    {
        float iconSize = LiteModFileExplorer.config.explorerIconSize;

        button.setWidth(w);
        button.setHeight(h);
        button.setX(x);
        button.setY(y);
        button.draw(minecraft, mouseX, mouseY);

        DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

        String rendering = this.getShortFileName();
        if (LiteModFileExplorer.config.showFileEditBelowName)
            rendering += "\n§7" + getLastEditDate().toString();
        int lines = helper.drawText(rendering, new Vector(x + (int) iconSize + 3, y + (h / 2)), 0xFFFFFF, true, w - (int) iconSize - 6, DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.CENTER);
        this.h = lines * 9 + ((int) iconSize - 9);

        helper.drawShape(new Quad(x + iconSize - 2, y + 1, 1, h - 2).setColor(Color.LT_GREY));
        helper.drawShape(new Quad(x + iconSize - 1, y + 1, 1, h - 2).setColor(Color.WHITE));

        helper.drawIcon(this.getFileType().getIcon(), new Quad(x + 2, y + 2 + (h / 2 - (iconSize / 2)), iconSize - 4, iconSize - 4));
    }

    private void renderList(Minecraft minecraft)
    {
        minecraft.fontRendererObj.drawString(this.getShortFileName(), x + 3, y + (h / 2) - 4, 0xFFFFFF, true);
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        //DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);
//        button.draw(minecraft, mouseX, mouseY);

        RenderType type = this.getRenderType();
        switch (type)
        {
            case SQUARE_GRID:
                renderSquare(minecraft);
                break;
            case LONG_GRID:
                renderBigList(minecraft, mouseX, mouseY);
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
