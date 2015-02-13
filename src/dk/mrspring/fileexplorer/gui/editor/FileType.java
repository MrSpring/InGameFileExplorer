package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.llcore.Icon;

import java.util.Arrays;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public abstract class FileType implements IFileType
{
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof FileType)
        {
            return Arrays.equals(this.getSupportedTypes(), ((FileType) obj).getSupportedTypes());
        } else return super.equals(obj);
    }

    public Icon getIcon()
    {
        return LiteModFileExplorer.core.getIcon("unknown");
    }
}
