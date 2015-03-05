package dk.mrspring.fileexplorer.gui.editor;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Konrad on 05-03-2015.
 */
public class EditorYml extends EditorJson
{
    public EditorYml(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h, file);
    }

    @Override
    public Map<String, Object> getMapFromFile(File file)
    {
        try
        {
            YamlReader yamlReader = new YamlReader(new FileReader(file));
            Map<String, Object> read = (Map<String, Object>) yamlReader.read();
            for (Map.Entry<String, Object> entry : read.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue().getClass());
            }
            return read;
        } catch (Exception e)
        {
            e.printStackTrace();
            return new LinkedTreeMap<String, Object>();
        }
//        return super.getMapFromFile(file);
    }

    @Override
    public String getFileFromMap(Map<String, Object> content)
    {
        try
        {
            StringWriter stringWriter = new StringWriter();
            YamlWriter yamlWriter = new YamlWriter(stringWriter);
            yamlWriter.write(content);
            yamlWriter.close();
            return stringWriter.getBuffer().toString();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}
