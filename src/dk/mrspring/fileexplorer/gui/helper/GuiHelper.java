package dk.mrspring.fileexplorer.gui.helper;

/**
 * Created by MrSpring on 09-11-2014 for MC Music Player.
 */
public class GuiHelper
{
    public static boolean isMouseInBounds(int mouseX, int mouseY, int posX, int posY, int width, int height)
    {
        return mouseX >= posX && mouseY >= posY && mouseX < posX + width && mouseY < posY + height;
    }
}
