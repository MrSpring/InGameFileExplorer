package dk.mrspring.fileexplorer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import dk.mrspring.fileexplorer.backup.BackupManager;
import dk.mrspring.fileexplorer.gui.editor.*;
import dk.mrspring.fileexplorer.gui.editor.content.ContentHandler;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenBackupManager;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenFileExplorer;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenWelcome;
import dk.mrspring.fileexplorer.helper.FileSorter;
import dk.mrspring.fileexplorer.loader.FileLoader;
import dk.mrspring.llcore.Icon;
import dk.mrspring.llcore.LLCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class LiteModFileExplorer implements Tickable, Configurable
{
    public static KeyBinding openBackupManager = new KeyBinding("key.file_explorer.open_backup_manager", Keyboard.KEY_F, "key.categories.litemods");
    public static KeyBinding openFileExplorer = new KeyBinding("key.file_explorer.open_file_explorer", Keyboard.KEY_G, "key.categories.litemods");

    public static Config config;
    public static File configFile;

    public static BackupManager backupManager;

    public static BufferedImage image;

    private static Map<String, FileType> supportedFileTypes;

    public static LLCore core;

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        if (openBackupManager.isPressed())
            minecraft.displayGuiScreen(new GuiScreenBackupManager("Backup Manager", minecraft.currentScreen));
        if (openFileExplorer.isPressed())
        {
            if (config.showWelcomeScreen)
                minecraft.displayGuiScreen(new GuiScreenWelcome("Welcome", minecraft.currentScreen));
            else
                minecraft.displayGuiScreen(new GuiScreenFileExplorer(minecraft.currentScreen, new File(config.startLocation)));
        }
    }

    public static boolean canEditFile(String extension)
    {
        return supportedFileTypes.containsKey(extension);
    }

    public static FileType getFileType(String extension)
    {
        if (extension == null)
            return supportedFileTypes.get("unknown");
        else if (extension.isEmpty())
            return supportedFileTypes.get("directory");
        FileType type = supportedFileTypes.get(extension);
        if (type == null)
            type = supportedFileTypes.get("unknown");
        return type;
    }

    public static FileSorter.SortingType getDefaultSortingType()
    {
        return config.fileSortingType;
    }

    @Override
    public void init(File configPath)
    {
        core = new LLCore("fileexplorer");
        core.setFileLoader(new FileLoader());
        core.registerIcon(HardcodedAssets.unknownIcon, "unknown");
        core.registerIcon(HardcodedAssets.jsonIcon, "json");
        core.registerIcon(HardcodedAssets.txtIcon, "txt");
        core.registerIcon(HardcodedAssets.imageIcon, "image");
        core.registerIcon(HardcodedAssets.folderIcon, "folder");
        core.registerIcon(HardcodedAssets.arrowUpIcon, "arrow_up");
        core.registerIcon(HardcodedAssets.arrowDownIcon, "arrow_down");
        core.registerIcon(HardcodedAssets.arrowLeftIcon, "arrow_left");
        core.registerIcon(HardcodedAssets.arrowRightIcon, "arrow_right");
        core.registerIcon(HardcodedAssets.crossIcon, "cross");
        core.registerIcon(HardcodedAssets.checkMarkIcon, "check_mark");
        core.registerIcon(HardcodedAssets.newFileIcon, "new_file");
        core.registerIcon(HardcodedAssets.fullscreenIcon, "fullscreen");
//        core.loadIcon(new ResourceLocation("fileexplorer", "unknown"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "json"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "txt"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "image"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "folder"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "arrow_up"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "arrow_down"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "arrow_left"));
//        core.loadIcon(new ResourceLocation("fileexplorer", "arrow_right"));

        LiteLoader.getInput().registerKeyBinding(openBackupManager);
        LiteLoader.getInput().registerKeyBinding(openFileExplorer);

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
                config.validateValues();
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

        loadBackupList();

//        FileLoader.takeBackup(configFile);
//        FileLoader.takeBackup(new File("crash-reports"));

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
                return new EditorContentHandler(x, y, width, height, file, new ContentHandler());
            }

            @Override
            public Icon getIcon()
            {
                return core.getIcon("json");
            }

            @Override
            public String getName()
            {
                return "JSON";
            }
        });
        supportedFileTypes.put(".yml", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".yml", ".yaml"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorYml(x, y, width, height, file);
            }

            @Override
            public String getName()
            {
                return "YAML";
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
            public Icon getIcon()
            {
                return core.getIcon("folder");
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
            public Icon getIcon()
            {
                return core.getIcon("unknown");
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
            public Icon getIcon()
            {
                return core.getIcon("image");
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

        tempType = new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".txt", ".rtf"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorText(x, y, width, height, file);
            }

            @Override
            public Icon getIcon()
            {
                return core.getIcon("txt");
            }

            @Override
            public String getName()
            {
                return "TEXT";
            }
        };

        supportedFileTypes.put(".txt", tempType);
        supportedFileTypes.put(".rtf", tempType);

        supportedFileTypes.put(".lang", new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".lang"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorLang(x, y, width, height, file);
            }

            @Override
            public String getName()
            {
                return "LANG";
            }
        });

        /*tempType = new FileType()
        {
            @Override
            public String[] getSupportedTypes()
            {
                return new String[]{".yml", ".yaml"};
            }

            @Override
            public Editor getNewEditor(int x, int y, int width, int height, File file)
            {
                return new EditorYml(x, y, width, height, file);
            }

            @Override
            public String getName()
            {
                return "YAML";
            }
        };
        supportedFileTypes.put(".yml", tempType);
        supportedFileTypes.put(".yaml", tempType);*/

        FileSorter.load();
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

    public static File getBackupFile()
    {
        return new File(config.backupLocation + "/backup_index.json");
    }

    public static File getBackupDirectory()
    {
        return new File(LiteModFileExplorer.config.backupLocation);
    }

    public static void saveBackupList()
    {
        System.out.println("Saving list.");
        File backupListFile = getBackupFile();
        backupManager.saveManagerToFile(backupListFile);
    }

    public static void loadBackupList()
    {
        File backupListFile = getBackupFile();
        backupManager = BackupManager.getFromFile(backupListFile);
        if (backupManager == null)
            backupManager = new BackupManager();
        saveBackupList();
    }

    public static void resetBackupList()
    {
        try
        {
            FileUtils.deleteDirectory(getBackupDirectory());
            backupManager = new BackupManager();
            saveBackupList();
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
    public void upgradeSettings(String version, File configPath, File oldConfigPath)
    {

    }

    @Override
    public String getName()
    {
        return "In-Game File Explorer";
    }

    @Override
    public Class<? extends ConfigPanel> getConfigPanelClass()
    {
        return FileExplorerConfigPanel.class;
    }
}
