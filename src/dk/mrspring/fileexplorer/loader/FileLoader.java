package dk.mrspring.fileexplorer.loader;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.ModLogger;

import java.io.*;
import java.util.List;

/**
 * Created by MrSpring on 16-07-2014 for In-Game File Explorer.
 */
public class FileLoader
{
    public static void addFiles(String path, List<File> fileList, boolean addSubFiles)
    {
        if (LiteModFileExplorer.config.acceptFileReading)
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
        } else return false;
    }

    public static boolean deleteFile(File file)
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
            return file.delete();
        else return false;
    }
}