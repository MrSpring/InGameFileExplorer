package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.GuiImageViewer;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiScreenImageViewer extends GuiScreen
{
    final File image;
    float closeMessageOpacity = 1F;

    public GuiScreenImageViewer(String title, net.minecraft.client.gui.GuiScreen previousScreen, File imagePath)
    {
        super(title, previousScreen);
        this.image = imagePath;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.addGuiElement("image_viewer", new GuiImageViewer(image, 0, 0, width, height).centerImage());
        this.hideTitle().hideBars().hideDoneButton();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper.drawQuad(0, 0, width, height, Color.BLACK, 1F);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPushMatrix();
        GL11.glScalef(2, 2, 1);
        float fullHeight = 30;
        float yOffset = 16 - (int) (closeMessageOpacity * fullHeight);
        if (yOffset < 0)
            yOffset = 0;
        DrawingHelper.drawIcon(DrawingHelper.hoverIcon, width / 4 - (mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.screen.image_viewer.exit_fullscreen")) / 2) - 5, -yOffset, mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.screen.image_viewer.exit_fullscreen")) + 10, 16, false);
        DrawingHelper.drawCenteredString(mc.fontRendererObj, (float) width / 4, 4 - yOffset, StatCollector.translateToLocal("gui.screen.image_viewer.exit_fullscreen"), 0xFFFFFF);
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
            mc.displayGuiScreen(previousScreen);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
//        closeMessageOpacity = 0.75F;
        if (closeMessageOpacity > 0)
            closeMessageOpacity -= 0.025;
        else closeMessageOpacity = 0;
    }
}
