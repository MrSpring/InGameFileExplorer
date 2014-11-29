package dk.mrspring.fileexplorer.gui.helper;

import java.util.ArrayList;

import static org.lwjgl.input.Keyboard.*;

/**
 * Created by MrSpring on 29-11-2014 for In-Game File Explorer.
 */
public class TextHelper
{
    private static ArrayList<Integer> keyCodeBlacklist;

    public static void initializeBlacklist()
    {
        keyCodeBlacklist = new ArrayList<Integer>();

        keyCodeBlacklist.add(KEY_LSHIFT);
        keyCodeBlacklist.add(KEY_RSHIFT);

        keyCodeBlacklist.add(KEY_LCONTROL);
        keyCodeBlacklist.add(KEY_RCONTROL);

        keyCodeBlacklist.add(KEY_LMENU);
        keyCodeBlacklist.add(KEY_RMENU);
    }

    public static boolean isKeyWritable(int keyCode)
    {
        if (keyCodeBlacklist == null)
            initializeBlacklist();
        return !keyCodeBlacklist.contains(keyCode);
    }
}
