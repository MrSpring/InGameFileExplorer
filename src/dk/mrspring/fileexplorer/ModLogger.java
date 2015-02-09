package dk.mrspring.fileexplorer;

import org.apache.logging.log4j.Logger;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class ModLogger
{
    public static Logger logger;

    public static void print(Object... message)
    {
        logger.info(message);
    }

    public static void printDebug(Object... message)
    {
        if (LiteModFileExplorer.config.printDebug)
            print(message);
    }
}
