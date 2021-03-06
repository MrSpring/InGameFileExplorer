package dk.mrspring.fileexplorer;

import dk.mrspring.fileexplorer.helper.FileSorter;

import java.io.File;

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
    public FileSorter.SortingType fileSortingType = FileSorter.SortingType.NAME;
    public boolean sortFolders = false;
    public boolean hideNonEditableFiles = false;
    public int explorerIconSize = 20;
    public boolean showArrowOnDropDowns = true;
    public boolean showFileEditBelowName = true;
    public boolean showFileSizeBelowName = true;
    public boolean showOpenDirectory = true;
    public String[] textFileTypes = new String[]{".txt", ".rtf"};

    public boolean json_usePrettyPrinting = true;
    public boolean json_allowArrayCollapsing = true;
    public boolean json_allowMapCollapsing = true;
    public boolean json_showCollapsedArraySize = true;
    public boolean json_showCollapsedMapSize = true;

    public String numberFormat = "00000.00";
    public boolean showExplorerBackground = true;

    public void validateValues()
    {
        {
            File tempStartFile = new File(startLocation);
            if (!tempStartFile.isDirectory())
                startLocation = System.getProperty("user.home");
        }

        {
            if (!acceptFileReading)
                showWelcomeScreen = true;
        }

        {
            File tempBackupFile = new File(backupLocation);
            if (!tempBackupFile.isDirectory())
                backupLocation = "igfe-backup";
        }

        {
            if (explorerIconSize < 15)
                explorerIconSize = 15;
        }

        {
            if (fileSortingType == null)
                fileSortingType = FileSorter.SortingType.NAME;
        }

        {
            if (textFileTypes.length == 0)
                textFileTypes = new String[]{".txt", ".rtf"};
        }
    }

    public String getTextFileTypes()
    {
        StringBuilder builder = new StringBuilder(textFileTypes[0]);
        for (String type : textFileTypes)
        {
            builder.append(';');
            builder.append(type);
        }
        return builder.toString();
    }

    public void setTextFileTypes(String string)
    {
        String[] types;
        if (string.contains(";"))
            types = string.split(";");
        else types = new String[]{string};
        this.textFileTypes = types;
    }
}
