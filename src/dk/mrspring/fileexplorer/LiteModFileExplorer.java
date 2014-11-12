package dk.mrspring.fileexplorer;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenExamplePage;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenFileExplorer;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenImageViewer;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenTextEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for MC Music Player.
 */
public class LiteModFileExplorer implements Tickable
{
    public static KeyBinding openExampleGui = new KeyBinding("key.file_explorer.open_gui", Keyboard.KEY_F, "key.categories.litemods");
    public static KeyBinding openFileExplorer = new KeyBinding("key.file_explorer.open_explorer", Keyboard.KEY_G, "key.categories.litemods");
    public static KeyBinding openTextEditor = new KeyBinding("key.file_explorer.open_editor", Keyboard.KEY_H, "key.categories.litemods");
    public static KeyBinding openImageViewer =new KeyBinding("ket.file_explorer.open_image",Keyboard.KEY_J, "key.categories.litemods");

    public static BufferedImage image;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        if (openExampleGui.isPressed())
            minecraft.displayGuiScreen(new GuiScreenExamplePage());
        if (openFileExplorer.isPressed())
            minecraft.displayGuiScreen(new GuiScreenFileExplorer());
        if (openTextEditor.isPressed())
            minecraft.displayGuiScreen(new GuiScreenTextEditor("Text text text text\nMore text on a new line.\n\nEven more text.\nAnd finally, no more text! :D"));
        if (openImageViewer.isPressed())
            minecraft.displayGuiScreen(new GuiScreenImageViewer());
    }

    @Override
    public String getVersion()
    {
        return "0001-0.1.0-BETA";
    }

    @Override
    public void init(File configPath)
    {
        LiteLoader.getInput().registerKeyBinding(openExampleGui);
        LiteLoader.getInput().registerKeyBinding(openFileExplorer);
        LiteLoader.getInput().registerKeyBinding(openTextEditor);
        LiteLoader.getInput().registerKeyBinding(openImageViewer);

        /*try
        {
            image = ImageLoader.loadImage("C:\\Users\\Konrad\\Documents\\Minecraftig\\More Folder\\Minecraft_grass_block.png");
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath)
    {

    }

    @Override
    public String getName()
    {
        return "In-Game File Explorer";
    }
}
