package dk.mrspring.fileexplorer.backup;

import dk.mrspring.fileexplorer.LiteModFileExplorer;

import java.io.File;
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
        return new File(LiteModFileExplorer.config.backupLocation + "/" + id + ".backup");
    }
}
