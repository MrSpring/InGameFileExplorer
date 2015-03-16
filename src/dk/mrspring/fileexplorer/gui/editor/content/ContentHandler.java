package dk.mrspring.fileexplorer.gui.editor.content;

import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.fileexplorer.LiteModFileExplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Konrad on 09-03-2015.
 */
public class ContentHandler
{
    public Map<String, Object> getMapFromFile(File file)
    {
        Map<String, Object> json = new LinkedHashMap<String, Object>();
        try
        {
            String jsonCode = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
            json = LiteModFileExplorer.core.getJsonHandler().loadFromJson(jsonCode, LinkedTreeMap.class);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (json == null)
            json = new LinkedTreeMap<String, Object>();
        return json;
    }

    public String getFileFromMap(Map<String, Object> content)
    {
        return LiteModFileExplorer.core.getJsonHandler().toJson(content);
    }

    public IContentType getContentType(String name, Object value, boolean canEditName)
    {
        if (value instanceof Boolean)
            return new BooleanContentType();
        else if (value instanceof String)
            return new StringContentType();
        else if (value instanceof Number)
            return new NumberContentType();
        else if (value instanceof ArrayList)
            return new ListContentType();
        else if (value instanceof Map)
            return new MapContentType();
        else return null;
    }

    public class BooleanContentType implements IContentType<Boolean>
    {
        @Override
        public ContentEditorElement<Boolean> getAsEditorElement(int x, int y, int width, String name, Boolean value, boolean canEditName, ContentHandler contentHandler)
        {
            return new ContentBooleanElement(x, y, width, name, value, canEditName);
        }
    }

    public class StringContentType implements IContentType<String>
    {
        @Override
        public ContentEditorElement<String> getAsEditorElement(int x, int y, int width, String name, String value, boolean canEditName, ContentHandler contentHandler)
        {
            return new ContentStringElement(x, y, width, name, value, canEditName);
        }
    }

    public class NumberContentType implements IContentType<Double>
    {
        @Override
        public ContentEditorElement<Double> getAsEditorElement(int x, int y, int width, String name, Double value, boolean canEditName, ContentHandler contentHandler)
        {
            return new ContentDoubleElement(x, y, width, name, value, canEditName);
        }
    }

    public class ListContentType implements IContentType<List>
    {
        @Override
        public ContentEditorElement<List> getAsEditorElement(int x, int y, int width, String name, List value, boolean canEditName, ContentHandler contentHandler)
        {
            return new ContentArrayElement(x, y, width, name, value, canEditName, contentHandler);
        }
    }

    public class MapContentType implements IContentType<Map>
    {
        @Override
        public ContentEditorElement<Map> getAsEditorElement(int x, int y, int width, String name, Map value, boolean canEditName, ContentHandler contentHandler)
        {
            return new ContentMapElement(x, y, width, name, value, canEditName, contentHandler);
        }
    }
}
