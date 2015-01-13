package dk.mrspring.fileexplorer.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.ModLogger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrSpring on 16-07-2014 for In-Game File Explorer.
 */
public class FileLoader
{
    public static void addFiles(String path, List<File> fileList, boolean addSubFiles)
    {
        if (LiteModFileExplorer.config.acceptFileReading)
        {
            try
            {
                File directory = new File(path);
                ModLogger.printDebug("Adding files from folder: " + directory.getPath() + ", add sub files: " + addSubFiles);
                String[] directoryContents = directory.list();

                for (String fileName : directoryContents)
                {
                    File file = new File(String.valueOf(directory), fileName);
                    fileList.add(file);
                    ModLogger.printDebug("Found file: " + file.getPath() + ", adding it to the list.");
                    if (file.isDirectory() && addSubFiles)
                        addFiles(file.getPath(), fileList, true);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            ModLogger.printDebug("Failed to load from folder: " + new File(path).getPath() + ", user has not accepted file reading!");
        }
    }

    public static String readFile(File file)
    {
        if (LiteModFileExplorer.config.acceptFileReading)
            try
            {
                ModLogger.printDebug("Reading from file: " + file.getPath() + "...");
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    builder.append(line);
                    builder.append('\n');
                    ModLogger.printDebug("Found line: " + line + " in file: " + file.getPath());
                }

                bufferedReader.close();

                return builder.toString();
            } catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        else
        {
            ModLogger.printDebug("Failed to load from file: " + file.getPath() + ", user has not accepted file reading!");
            return "";
        }
    }

    public static boolean writeToFile(File file, String toWrite)
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
        {
            takeBackup(file);
            try
            {
                FileWriter writer = new FileWriter(file);
                writer.write(toWrite);
                writer.close();
                return true;
            } catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        } else
        {
            ModLogger.printDebug("Failed to write to file: " + file.getPath() + ", user has not accepted file manipulation!");
            return false;
        }
    }

    public static boolean deleteFile(File file)
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
        {
            takeBackup(file);
            return file.delete();
        } else
        {
            ModLogger.printDebug("Failed to delete file: " + file.getPath() + ", user has not accepted file manipulation!");
            return false;
        }
    }

    public static Map<String, Object> readJsonFile(File jsonFile)
    {
        String fromFile = readFile(jsonFile);
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (!fromFile.equals(""))
        {
            try
            {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<Map<String, Object>>()
                {
                }.getType();
                jsonObject = gson.fromJson(fromFile, stringStringMap);
            } catch (Exception e)
            {
                ModLogger.printDebug("Failed reading from JSON file: " + jsonFile.getPath());
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static void takeBackup(File toBackup)
    {
        File backupDestFile = LiteModFileExplorer.backupManager.registerBackup(toBackup);
        if (!toBackup.isDirectory())
            try
            {
                backupDestFile.createNewFile();
                FileUtils.copyFile(toBackup, backupDestFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        else
            try
            {
                backupDestFile.mkdir();
                FileUtils.copyDirectory(toBackup, backupDestFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
    }
}