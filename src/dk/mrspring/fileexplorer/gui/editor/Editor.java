package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.gui.interfaces.IGui;

/**
 * Created by MrSpring on 07-01-2015 for In-Game File Explorer.
 */
public abstract class Editor implements IGui, IEditor
{
    int x;
    int y;
    int w;
    int h;

    public Editor(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update()
    {

    }
}
