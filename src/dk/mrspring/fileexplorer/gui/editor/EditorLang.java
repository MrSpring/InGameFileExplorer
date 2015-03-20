package dk.mrspring.fileexplorer.gui.editor;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.editor.content.ContentHandler;
import dk.mrspring.fileexplorer.gui.editor.content.IContentType;
import dk.mrspring.fileexplorer.gui.editor.lang.BlankLine;
import dk.mrspring.fileexplorer.gui.editor.lang.BlankLineContentType;
import dk.mrspring.fileexplorer.gui.editor.lang.Comment;
import dk.mrspring.fileexplorer.gui.editor.lang.CommentContentType;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Konrad on 18-02-2015.
 */
public class EditorLang extends EditorContentHandler//extends Editor implements IMouseListener
{
    public EditorLang(int x, int y, int w, int h, File file)
    {
        super(x, y, w, h, file, new ContentHandler()
        {
            @Override
            public Map<String, Object> getMapFromFile(File file)
            {
                Map<String, Object> values = new LinkedHashMap<String, Object>();
                try
                {
                    String langFile = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
                    String[] lines = langFile.split("\n");
                    for (int i = 0; i < lines.length; i++)
                    {
                        String line = lines[i];
                        if (line.isEmpty())
                            values.put(String.valueOf(i), new BlankLine());
                        else if (line.startsWith("//"))
                        {
                            String commentName = String.valueOf(i);
                            String commentValue = line.substring(2).trim();
                            values.put(commentName, new Comment(commentValue));
                        } else if (line.contains("="))
                        {
                            int equalsPos = line.indexOf("=");
                            String key = line.substring(0, equalsPos);
                            String value = line.substring(equalsPos);
                            values.put(key, value);
                        }
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return values;
            }

            @Override
            public String getFileFromMap(Map<String, Object> content)
            {
                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, Object> entry : content.entrySet())
                {
                    String name = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof String)
                    {
                        String s = (String) value;
                        builder.append(name);
                        builder.append("=");
                        builder.append(s);
                    } else if (value instanceof Comment)
                    {
                        Comment comment = (Comment) value;
                        builder.append("// ");
                        builder.append(comment.comment);
                    }

                    builder.append("\n");
                }
                return builder.toString();
            }

            @Override
            public String[] getNewElementButtonIDs()
            {
                return new String[]{"S", "C", "L"};
            }

            @Override
            public IContentType getNewContentType(String buttonID)
            {
                if (buttonID.equals("C"))
                    return new CommentContentType();
                else if (buttonID.equals("L"))
                    return new BlankLineContentType();
                else if (buttonID.equals("S"))
                    return new StringContentType();
                else return null;
            }
        });
    }



    /*GuiLangViewer viewer;
    File openFile;

    public EditorLang(File file, int x, int y, int w, int h)
    {
        super(x, y, w, h);
        this.openFile = file;
        String contents;
        Lang lang = null;
        try
        {
            contents = LiteModFileExplorer.core.getFileLoader().getContentsFromFile(file);
            lang = new Lang(contents);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (lang == null)
            lang = new Lang("");

        viewer = new GuiLangViewer(x, y, w, h, lang);
    }

    @Override
    public void update(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        viewer.setX(x).setY(y).setWidth(w).setHeight(h);
        viewer.update();
    }

    @Override
    public boolean hasUnsavedChanges()
    {
        return false;
    }

    @Override
    public String getOpenFileName()
    {
        return openFile.getName();
    }

    @Override
    public void save()
    {

    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        viewer.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void handleMouseWheel(int mouseX, int mouseY, int dWheelRaw)
    {
        this.viewer.handleMouseWheel(mouseX, mouseY, dWheelRaw);
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }*/
}
