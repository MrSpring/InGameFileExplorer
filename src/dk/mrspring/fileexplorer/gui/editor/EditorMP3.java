package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.sound.Player;
import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * Created by MrSpring on 09-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class EditorMP3 extends Editor
{
    File musicFile;
    Player player;

    GuiSimpleButton stopPlaying;

    public EditorMP3(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h);

        this.musicFile = file;
        stopPlaying = new GuiSimpleButton(x + 5, y + 5, 10, 10, "PP");
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        if (player == null)
        {
            player = new Player(musicFile);
            player.start();
        }

        stopPlaying.update();
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        stopPlaying.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (stopPlaying.mouseDown(mouseX, mouseY, mouseButton))
        {
            this.player.togglePlayback();
            return true;
        } else return false;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }
}
