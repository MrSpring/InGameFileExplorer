package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.IIcon;
import dk.mrspring.fileexplorer.gui.helper.Quad;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MrSpring on 30-11-2014 for In-Game File Explorer.
 */
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
    IMAGE(DrawingHelper.imageIcon, ".png", ".jpg", ".jpeg");

    private EnumFileType(IIcon icon, String... fileTypes)
    {
        if (fileTypes == null)
            fileTypes = new String[]{""};

        this.extensions = fileTypes;
        this.icon=icon;
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
