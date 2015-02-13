package dk.mrspring.fileexplorer.gui.editor.json;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public abstract class JsonEditorElement<E> implements IJsonEditorElement
{
    GuiSimpleButton deleteButton;

    public JsonEditorElement(int x, int y, int maxWidth, String name, E object)
    {
        this.deleteButton = new GuiSimpleButton(x, y, 16, 16, "").setIcon(LiteModFileExplorer.core.getIcon("cross"));
    }

    public GuiSimpleButton getDeleteButton()
    {
        return deleteButton;
    }
}
