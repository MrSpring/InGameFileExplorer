package dk.mrspring.fileexplorer.gui.editor;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public interface IEditor
{
    public void update(int x, int y, int width, int height);

    public boolean hasUnsavedChanges();

    public String getOpenFileName();

    public void save();
}
