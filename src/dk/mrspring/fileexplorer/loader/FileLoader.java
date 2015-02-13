package dk.mrspring.fileexplorer.loader;

import dk.mrspring.fileexplorer.LiteModFileExplorer;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Konrad on 12-02-2015.
 */
public class FileLoader extends dk.mrspring.llcore.FileLoader
{
    public static String getFileExtension(String fileName, boolean keepDot)
    {
        int lastDot = fileName.lastIndexOf('.');
        return fileName.substring(lastDot + (keepDot ? 0 : 1));
    }

    @Override
    public void addFilesToList(File folder, List<File> fileList, boolean addSubFolders)
    {
        if (LiteModFileExplorer.config.acceptFileReading)
            super.addFilesToList(folder, fileList, addSubFolders);
    }

    @Override
    public String getContentsFromFile(File file) throws IOException
    {
        if (LiteModFileExplorer.config.acceptFileReading)
            return super.getContentsFromFile(file);
        else return "";
    }

    @Override
    public boolean writeToFile(File file, String toWrite) throws IOException
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
            return super.writeToFile(file, toWrite);
        else return false;
    }

    @Override
    public boolean deleteFile(File file) throws IOException
    {
        if (LiteModFileExplorer.config.acceptFileManipulation)
            return super.deleteFile(file);
        else return false;
    }
}
