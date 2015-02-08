package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.Editor;
import dk.mrspring.fileexplorer.gui.editor.FileType;
import dk.mrspring.fileexplorer.helper.Color;
import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;

import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiScreenFileExplorer extends GuiScreen
{
    String startFrom;
    String openFileType = "";
    boolean initialized = false;

    public GuiScreenFileExplorer(net.minecraft.client.gui.GuiScreen previousScreen, File path)
    {
        super("File Explorer", previousScreen);
        startFrom = path.getPath();
    }

    @Override
    public void initGui()
    {
        if (!initialized)
        {
            super.initGui();

            this.addGuiElement("explorer", new GuiFileExplorer(5, 5, 250, height - 10, startFrom).setShowBackground(false).setOnFileOpened(new GuiFileExplorer.IOnFileOpened()
            {
                @Override
                public void onOpened(File file)
                {
                    GuiScreenFileExplorer.this.openFile(file);
                }
            }));

            this.hideBars();
            this.hideTitle();
            initialized = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper.drawQuad(0, 0, width, height, Color.BLACK, 0.5F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void openFile(File file)
    {
        int lastDot = file.getPath().lastIndexOf(".");
        String extension = file.getPath().substring(lastDot);
        FileType fileType = LiteModFileExplorer.getFileType(extension);

        Editor editor = fileType.getNewEditor(258, 10, width - 243 - 25, height - 20, file);
        String name = fileType.getName();

        if (editor != null)
        {
            if (!this.openFileType.equals(""))
                this.removeElement("editor");
            this.openFileType = name;
            this.addGuiElement("editor", editor);
        }
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("editor"))
        {
            Editor editor = (Editor) gui;
            editor.update(258, 10, width - 243 - 25, height - 20);
        }

        if (identifier.equals("explorer") && gui instanceof GuiFileExplorer)
            ((GuiFileExplorer) gui).setHeight(height - 10);

        return true;
    }
}
