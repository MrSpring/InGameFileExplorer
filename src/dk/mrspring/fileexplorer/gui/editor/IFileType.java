package dk.mrspring.fileexplorer.gui.editor;

import java.io.File;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public interface IFileType
{
    public String[] getSupportedTypes();

    public Editor getNewEditor(int x, int y, int width, int height, File file);

    public String getName();

/*
    UNKNOWN(new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            return new Quad[]{new Quad(x, y, w, h)};
        }
    }),
    FOLDER(DrawingHelper.folderIcon),
    TEXT_FILE(DrawingHelper.textFileIcon, ".txt"),
    IMAGE(DrawingHelper.imageIcon, ".png", ".jpg", ".jpeg"),
    JSON(DrawingHelper.textFileIcon, ".json");
    private IGui newEditor;

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
*/
}
