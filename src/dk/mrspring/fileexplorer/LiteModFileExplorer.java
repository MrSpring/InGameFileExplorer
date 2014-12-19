package dk.mrspring.fileexplorer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class LiteModFileExplorer implements Tickable
{
    public static KeyBinding openExampleGui = new KeyBinding("key.file_explorer.open_gui", Keyboard.KEY_F, "key.categories.litemods");
    public static KeyBinding openFileExplorer = new KeyBinding("key.file_explorer.open_explorer", Keyboard.KEY_G, "key.categories.litemods");
    public static KeyBinding openTextEditor = new KeyBinding("key.file_explorer.open_editor", Keyboard.KEY_H, "key.categories.litemods");
    public static KeyBinding openImageViewer = new KeyBinding("ket.file_explorer.open_image", Keyboard.KEY_J, "key.categories.litemods");

    public static Config config;

    public static BufferedImage image;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        if (openExampleGui.isPressed())
            minecraft.displayGuiScreen(new GuiScreenExamplePage(minecraft.currentScreen));
        if (openFileExplorer.isPressed())
            minecraft.displayGuiScreen(new GuiScreenFileExplorer(minecraft.currentScreen, new File(config.startLocation)));
        if (openTextEditor.isPressed())
            minecraft.displayGuiScreen(new GuiScreenTextEditor("Text text text text\nMore text on a new line.\n\nEven more text.\nAnd finally, no more text! :D"));
        if (openImageViewer.isPressed())
            minecraft.displayGuiScreen(new GuiScreenImageViewer("Image Viewer", minecraft.currentScreen, "D:\\MC Modding\\In-Game File Explorer\\jars\\liteconfig\\common\\Pick A Universe Wallpaper.png"));
    }

    @Override
    public String getVersion()
    {
        return "0.1.0-BETA";
    }

    @Override
    public void init(File configPath)
    {
        LiteLoader.getInput().registerKeyBinding(openExampleGui);
        LiteLoader.getInput().registerKeyBinding(openFileExplorer);
        LiteLoader.getInput().registerKeyBinding(openTextEditor);
        LiteLoader.getInput().registerKeyBinding(openImageViewer);

        String configFilePath = configPath.getPath() + File.separator + "InGameFileExplorer.json";
        File configFile = new File(configFilePath);

        System.out.println("configFilePath = " + configFilePath);

        try
        {
            if (configFile.exists())
            {
                Gson gson = new Gson();
                FileReader reader = new FileReader(configFile);
                config = gson.fromJson(reader, Config.class);
                System.out.println("Read from config file!");
            } else
            {
                config = new Config();
                GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                String jsonCode = gson.toJson(config);
                FileWriter fileWriter = new FileWriter(configFile);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(jsonCode);
                writer.close();
                fileWriter.close();
                System.out.println("Created new config file!");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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
