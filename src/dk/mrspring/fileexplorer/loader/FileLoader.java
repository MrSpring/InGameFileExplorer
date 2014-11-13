package dk.mrspring.fileexplorer.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Created by MrSpring on 16-07-2014 for In-Game File Explorer.
 */
public class FileLoader
{
    public static void addFiles(String path, List<File> fileList,boolean addSubFiles)
    {
        try
        {
            File directory = new File(path);
            System.out.println("Loading files from: " + directory.toURI().toASCIIString());
            System.out.println("Directory list size: " + directory.list().length);
            String[] directoryContents = directory.list();

            for (String fileName : directoryContents)
            {
                File file = new File(String.valueOf(directory), fileName);
                System.out.println(" Found file: " + file.toString() + ". Adding it to the list.");
                fileList.add(file);
                if (file.isDirectory()&&addSubFiles)
                    addFiles(file.getPath(), fileList, true);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String loadFile(String path)
    {
        try
        {
            FileReader reader=new FileReader(path);
            BufferedReader bufferedReader=new BufferedReader(reader);
            StringBuilder builder=new StringBuilder();

            String line;

            while((line=bufferedReader.readLine())!=null)
            {
                System.out.println("Found line "+line+" from file @ "+path);
                builder.append(line);
                builder.append('\n');
            }

            bufferedReader.close();

            return builder.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}