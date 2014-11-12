package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.GuiImageViewer;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiScreenImageViewer extends GuiScreen
{
    GuiImageViewer imageViewer;

    @Override
    public void initGui()
    {
        imageViewer = new GuiImageViewer("C:\\Users\\Konrad\\Documents\\Minecraftig\\More Folder\\Minecraft_grass_block.png", 10, 10, 10, 10);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        imageViewer.draw(mc, mouseX, mouseY);
    }

    @Override
    public void updateScreen()
    {
        imageViewer.update();
    }
}
