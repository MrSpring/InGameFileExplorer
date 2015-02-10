package dk.mrspring.fileexplorer.helper;

import dk.mrspring.fileexplorer.ModLogger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by Konrad on 10-02-2015.
 */
public class ClipboardHelper
{
    public static String paste()
    {
        try
        {
            Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
            return (String) board.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e)
        {
            ModLogger.printDebug("Failed to paste from clipboard:");
            e.printStackTrace();
        } catch (IOException e)
        {
            ModLogger.printDebug("Failed to paste from clipboard:");
            e.printStackTrace();
        } catch (ClassCastException e)
        {
            ModLogger.printDebug("Failed to paste from clipboard:");
            e.printStackTrace();
        }

        return "";
    }

    public static void copy(String string)
    {
        try
        {
            StringSelection selection = new StringSelection(string);
            Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
            board.setContents(selection, selection);
        } catch (Exception e)
        {
            ModLogger.printDebug("Failed to copy string: " + string);
            e.printStackTrace();
        }
    }
}
