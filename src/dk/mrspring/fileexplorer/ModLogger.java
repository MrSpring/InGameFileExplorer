package dk.mrspring.fileexplorer;

import org.apache.logging.log4j.Logger;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public class ModLogger
{
    public static Logger logger;

    public static void print(String message, Throwable throwable)
    {
        if (throwable != null)
            logger.info(message, throwable);
        else logger.info(message);
    }

    public static void print(String message)
    {
        print(message, null);
    }

    public static void printDebug(String message, Throwable throwable)
    {
        if (LiteModFileExplorer.config.printDebug)
            print(message, throwable);
    }

    public static void printDebug(String message)
    {
        printDebug(message, null);
    }
}
