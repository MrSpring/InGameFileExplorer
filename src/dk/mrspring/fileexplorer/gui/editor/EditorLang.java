package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiLangViewer;
import dk.mrspring.fileexplorer.gui.editor.lang.Lang;
import dk.mrspring.fileexplorer.gui.interfaces.IMouseListener;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

/**
 * Created by Konrad on 18-02-2015.
 */
public class EditorLang extends Editor implements IMouseListener
{
    GuiLangViewer viewer;
    File openFile;

    public EditorLang(File file, int x, int y, int w, int h)
    {
        super(x, y, w, h);
        this.openFile = file;
        String contents;
        Lang lang = null;
        try
        {
            contents = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
            lang = new Lang(contents);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (lang == null)
            lang = new Lang("");

        viewer = new GuiLangViewer(x, y, w, h, lang);
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        viewer.setX(x).setY(y).setWidth(w).setHeight(h);
        viewer.update();
    }

    @Override
    public boolean hasUnsavedChanges()
    {
        return false;
    }

    @Override
    public String getOpenFileName()
    {
        return openFile.getName();
    }

    @Override
    public void save()
    {

    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        viewer.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        this.viewer.handleMouseWheel(mouseX, mouseY, dWheelRaw);
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }
}
