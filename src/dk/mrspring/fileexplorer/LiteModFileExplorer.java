package dk.mrspring.fileexplorer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import dk.mrspring.fileexplorer.gui.editor.*;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.IIcon;
import dk.mrspring.fileexplorer.gui.helper.Quad;
import dk.mrspring.fileexplorer.gui.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
    public static File configFile;

    public static BufferedImage image;

    public static Map<String, FileType> supportedFileTypes;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        if (openExampleGui.isPressed())
            minecraft.displayGuiScreen(new GuiScreenExamplePage(minecraft.currentScreen));
        if (openFileExplorer.isPressed())
        {
            if (config.showWelcomeScreen)
                minecraft.displayGuiScreen(new GuiScreenWelcome("Welcome", minecraft.currentScreen));
            else
                minecraft.displayGuiScreen(new GuiScreenFileExplorer(minecraft.currentScreen, new File(config.startLocation)));
        }
        if (openTextEditor.isPressed())
            minecraft.displayGuiScreen(new GuiScreenTextEditor("Text text text text\nMore text on a new line.\n\nEven more text.\nAnd finally, no more text! :D"));
        if (openImageViewer.isPressed())
            minecraft.displayGuiScreen(new GuiScreenImageViewer("Image Viewer", minecraft.currentScreen, new File("D:\\MC Modding\\In-Game File Explorer\\jars\\liteconfig\\common\\Pick A Universe Wallpaper.png")));
    }

    public static void saveConfig()
    {
        try
        {
            if (!configFile.exists())
                configFile.createNewFile();
            GsonBuilder gsonBuilder = new GsonBuilder();
            if (config.json_usePrettyPrinting)
                gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String jsonCode = gson.toJson(config);
            FileWriter fileWriter = new FileWriter(configFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonCode);
            writer.close();
            fileWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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
        configFile = new File(configFilePath);

        ModLogger.logger = LogManager.getLogger("InGameFileExplorer");

        try
        {
            if (configFile.exists())
            {
                Gson gson = new Gson();
                FileReader reader = new FileReader(configFile);
                config = gson.fromJson(reader, Config.class);
                saveConfig();
                ModLogger.printDebug("Loaded config from file: " + configFile.getPath() + ", debug messages are enabled! Get ready for spam!");
            } else
            {
                config = new Config();
                saveConfig();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            if (config == null)
                config = new Config();
        }

        supportedFileTypes = new HashMap<String, FileType>();

        supportedFileTypes.put(".json", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".json"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorJson(x, y, width, height, file);
            }

            @Override
            public IIcon getIcon()
            {
                return DrawingHelper.textFileIcon;
            }

            @Override
            public String getName()
            {
                return "JSON";
            }
        });
        supportedFileTypes.put("directory", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{"directory"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return null;
            }

            @Override
            public IIcon getIcon()
            {
                return DrawingHelper.folderIcon;
            }

            @Override
            public String getName()
            {
                return "FOLDER";
            }
        });
        supportedFileTypes.put("unknown", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{"unknown"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return null;
            }

            @Override
            public String getName()
            {
                return "UNKNOWN";
            }

            @Override
            public IIcon getIcon()
            {
                return new IIcon()
                {
                    @Override
                    public Quad[] getQuads(float x, float y, float w, float h, float alpha)
                    {
                        return new Quad[]{
                                new Quad(x, y, w, h)
                        };
                    }
                };
            }
        });

        FileType tempType = new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".png", ".jpeg", ".jpg"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorImage(x, y, width, height, file);
            }

            @Override
            public IIcon getIcon()
            {
                return DrawingHelper.imageIcon;
            }

            @Override
            public String getName()
            {
                return "IMAGE_VIEWER";
            }
        };
        supportedFileTypes.put(".png", tempType);
        supportedFileTypes.put(".jpg", tempType);
        supportedFileTypes.put(".jpeg", tempType);

        supportedFileTypes.put(".txt", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".txt"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorText(x, y, width, height, file);
            }

            @Override
            public IIcon getIcon()
            {
                return DrawingHelper.textFileIcon;
            }

            @Override
            public String getName()
            {
                return "TEXT";
            }
        });
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
