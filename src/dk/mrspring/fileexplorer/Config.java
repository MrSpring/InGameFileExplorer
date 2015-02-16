package dk.mrspring.fileexplorer;

import dk.mrspring.fileexplorer.helper.FileSorter;

/**
 * Created by MrSpring on 08-12-2014 for In-Game File Explorer.
 */
public class Config
{
    public String startLocation = System.getProperty("user.home");
    public boolean showWelcomeScreen = true;
    public boolean acceptFileManipulation = false;
    public boolean acceptFileReading = false;
    public boolean printDebug = true;
    public boolean automaticBackup = true;
    public String backupLocation = "igfe-backup";
    public FileSorter.SortingType file_sorting_type = FileSorter.SortingType.NAME;
    public boolean sortFolders = false;
    public boolean hideNonEditableFiles = false;

    public boolean json_usePrettyPrinting = true;
    public boolean json_allowArrayCollapsing = true;
    public boolean json_allowMapCollapsing = true;
    public boolean json_showCollapsedArraySize = true;
    public boolean json_showCollapsedMapSize = true;

    public String number_format = "00000.00";
}
