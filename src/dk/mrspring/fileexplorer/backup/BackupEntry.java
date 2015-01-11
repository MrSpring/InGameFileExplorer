package dk.mrspring.fileexplorer.backup;

import java.io.File;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class BackupEntry
{
    String original_location = "";
    int backup_id = 0;

    public BackupEntry(String backedUpFrom, int backupID)
    {
        this.original_location = backedUpFrom;
        this.backup_id = backupID;
    }

    public BackupEntry(File backedUpFrom, int backupID)
    {
        this(backedUpFrom.getAbsolutePath(), backupID);
    }

    public File getOriginalFile()
    {
        return new File(original_location);
    }
}