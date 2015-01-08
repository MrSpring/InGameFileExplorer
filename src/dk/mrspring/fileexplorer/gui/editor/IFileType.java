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
}
