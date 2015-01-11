package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.GuiSimpleButton;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class GuiScreenBackupManager extends GuiScreen
{
    public GuiScreenBackupManager(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideBars().hideTitle();
        this.addGuiElement("done", new GuiSimpleButton(width - 40 - 10, height - 20 - 10, 40, 20, "Done"));
    }
}
