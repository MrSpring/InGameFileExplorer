package dk.mrspring.fileexplorer.loader;

import dk.mrspring.fileexplorer.LiteModFileExplorer;

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
                String[] directoryContents = directory.list();

                for (String fileName : directoryContents)
                {
                    File file = new File(String.valueOf(directory), fileName);
                    fileList.add(file);
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
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    builder.append(line);
                    builder.append('\n');
                }

                bufferedReader.close();

                return builder.toString();
            } catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        else return "";
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
}