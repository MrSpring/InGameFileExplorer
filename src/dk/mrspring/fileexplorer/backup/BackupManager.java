package dk.mrspring.fileexplorer.backup;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.ModLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class BackupManager
{
    public List<BackupEntry> entries = new ArrayList<BackupEntry>();

    public File registerBackup(File fileToBackup)
    {
        int id = (int) (new Date().getTime() / 1000);
        BackupEntry entry = new BackupEntry(fileToBackup, id);
        entries.add(entry);
        LiteModFileExplorer.saveBackupList();
        return new File(LiteModFileExplorer.config.backupLocation + "/" + id + ".backup");
    }

    public void restoreBackup(int backupID)
    {
        for (BackupEntry entry : entries)
        {
            if (entry.backup_id == backupID)
            {
                File backup = entry.getBackupFile();
                File original = entry.getOriginalFile();

                original.delete();
                boolean restored = false;
                if (backup.isDirectory())
                    try
                    {
                        ModLogger.printDebug("Copying directory: " + backup.getPath() + ", to: " + original.getPath());
                        FileUtils.copyDirectory(backup, original);
                        restored = true;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                else
                    try
                    {
                        ModLogger.printDebug("Copying file: " + backup.getPath() + ", to: " + original.getPath());
                        FileUtils.copyFile(backup, original);
                        restored = true;
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                if (restored)
                {
                    backup.delete();
                    entries.remove(entry);
                    return;
                }
            }
        }
    }
}
