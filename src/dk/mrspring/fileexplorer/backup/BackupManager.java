package dk.mrspring.fileexplorer.backup;

import com.google.gson.Gson;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.loader.FileLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class BackupManager
{
    public List<BackupEntry> entries = new ArrayList<BackupEntry>();

    public File registerBackup(File fileToBackup, String cause)
    {
        int id = (int) (new Date().getTime() / 1000);
        BackupEntry entry = new BackupEntry(fileToBackup, id, cause);
        entries.add(entry);
        return new File(LiteModFileExplorer.config.backupLocation + "/" + id + ".backup");
    }/*

    public void restoreBackup(int backupID)
    {
        BackupEntry entry = this.getEntryForID(backupID);
        if (entry == null)
            return;
        this.entries.remove(entry);
        FileLoader.restoreBackup(entry.getBackupFile(), entry.getOriginalFile());
    }*/

    public BackupEntry restoreBackup(int backupID)
    {
        for (BackupEntry entry : entries)
            if (entry.backup_id == backupID)
            {
                entries.remove(entry);
                return entry;
            }
        return null;
    }

    public void loadManagerFromFile(File file)
    {
        BackupManager fromFile = getFromFile(file);
        if (fromFile != null)
            this.entries = fromFile.entries;
    }

    public void saveManagerToFile(File file)
    {
        writeToFile(file, this);
    }

    public static BackupManager writeToFile(File file, BackupManager manager)
    {
        try
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
            String json = LiteModFileExplorer.core.getJsonHandler().toJson(manager);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
            return manager;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static BackupManager getFromFile(File file)
    {
        try
        {
            if (!file.exists())
                return null;
            Gson gson = new Gson();
            FileReader reader = new FileReader(file);
            return gson.fromJson(reader, BackupManager.class);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
