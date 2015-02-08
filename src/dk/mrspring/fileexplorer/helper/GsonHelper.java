package dk.mrspring.fileexplorer.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Konrad on 08-02-2015.
 */
public class GsonHelper
{
    public static Gson getPrettyPrintingGson()
    {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
