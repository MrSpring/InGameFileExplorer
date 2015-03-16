package dk.mrspring.fileexplorer.gui.editor;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.google.gson.internal.LinkedTreeMap;
import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.content.ContentHandler;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Konrad on 09-03-2015.
 */
public class EditorYml extends EditorContentHandler
{
    public EditorYml(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h, file, new ContentHandler()
        {
            @Override
            public Map<String, Object> getMapFromFile(File file)
            {
                Map<String, Object> yml = new LinkedTreeMap<String, Object>();
                try
                {
                    String fromFile = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
                    StringReader stringReader = new StringReader(fromFile);
                    YamlReader reader = new YamlReader(stringReader);
                    reader.getConfig().setClassTag("tag:yaml.org,2002:map", LinkedHashMap.class);
                    yml = reader.read(LinkedHashMap.class);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (yml == null)
                    yml = new HashMap<String, Object>();
                return yml;
            }

            @Override
            public String getFileFromMap(Map<String, Object> content)
            {
                try
                {
                    StringWriter stringWriter = new StringWriter();
                    YamlWriter yamlWriter = new YamlWriter(stringWriter);
                    yamlWriter.getConfig().setClassTag("tag:yaml.org,2002:map", LinkedHashMap.class);
                    yamlWriter.write(content);
                    yamlWriter.close();
                    return stringWriter.getBuffer().toString();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    return "";
                }
            }
        });
    }
}
