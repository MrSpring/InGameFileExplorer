package dk.mrspring.fileexplorer;

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

    public boolean json_usePrettyPrinting = true;

    public String number_format = "00000.00";
}
