package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.IIcon;
import dk.mrspring.fileexplorer.gui.helper.Quad;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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

    public GuiFile setOnFileOpened(Runnable onFileOpened)
    {
        this.onFileOpened = onFileOpened;
        return this;
    }

    public EnumFileType getFileType()
    {
        if (filePath != null)
        {
            int lastDot = filePath.lastIndexOf('.');
            if (lastDot < 0 || new File(filePath).isDirectory())
                return EnumFileType.FOLDER;
            String extension = filePath.substring(lastDot);
            return EnumFileType.getFileTypeFor(extension);
        } else return EnumFileType.UNKNOWN;
    }

    public boolean isDirectory()
    {
        return this.getFileType().equals(EnumFileType.FOLDER);
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
        DrawingHelper.drawQuad(x, y + h - nameHeight - 1, w, 1, Color.LTGREY, 1F);
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

        DrawingHelper.drawQuad(x + iconSize - 1, y, 1, height, Color.LTGREY, 1F);
        DrawingHelper.drawQuad(x + iconSize, y, 1, height, Color.WHITE, 1F);

        DrawingHelper.drawIcon(this.getFileType().getIcon(), x + 1, y + 1, iconSize - 2, iconSize - 2);

        minecraft.fontRendererObj.drawSplitString(this.getShortFileName(), x + (int) iconSize + 3, nameY, w - (int) iconSize - 6, 0xFFFFFF);
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

    public enum EnumFileType
    {
        UNKNOWN(new IIcon()
        {
            @Override
            public Quad[] getQuads(float x, float y, float w, float h)
            {
                return new Quad[]{new Quad(x, y, w, h)};
            }
        }),
        FOLDER(DrawingHelper.folderIcon),
        TEXT_FILE(DrawingHelper.textFileIcon, ".txt"),
        IMAGE(DrawingHelper.imageIcon, ".png", ".jpg", ".jpeg"),
        JSON(DrawingHelper.textFileIcon, ".json");

        private EnumFileType(IIcon icon, String... fileTypes)
        {
            if (fileTypes == null)
                fileTypes = new String[]{""};

            this.extensions = fileTypes;
            this.icon = icon;
        }

        final String[] extensions;
        final IIcon icon;

        public List<String> getExtensions()
        {
            return Arrays.asList(this.extensions);
        }

        public IIcon getIcon()
        {
            return icon;
        }

        public static EnumFileType getFileTypeFor(String extension)
        {
            for (EnumFileType type : values())
                if (type.getExtensions().contains(extension))
                    return type;
            return UNKNOWN;
        }
    }
}
