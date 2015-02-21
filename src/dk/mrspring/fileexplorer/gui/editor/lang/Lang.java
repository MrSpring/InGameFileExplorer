package dk.mrspring.fileexplorer.gui.editor.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 18-02-2015.
 */
public class Lang
{
    List<ILangElement> lang;

    public Lang(String raw)
    {
        List<ILangElement> temp = new ArrayList<ILangElement>();
        if (!raw.isEmpty())
        {
            String[] lines = new String[]{raw};
            if (raw.contains("\n"))
                lines = raw.split("\n");
            for (String line : lines)
            {
                if (line.startsWith("//"))
                {
                    temp.add(new CommentElement(line.replace("//", "").trim()));
                } else if (line.contains("="))
                {
                    String[] entry = line.split("=");
                    if (entry.length >= 2)
                        temp.add(new LangElement(entry[0], entry[1]));
                } else if (line.isEmpty() || line.equals("\n"))
                    temp.add(new SpacerElement());
            }
        }
        this.lang = temp;
    }

    public List<ILangElement> getElements()
    {
        return lang;
    }

    public void addElement(ILangElement element)
    {
        if (element != null)
            this.lang.add(element);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (ILangElement element : lang)
        {
            builder.append(element.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static interface ILangElement
    {
        public String toString();
    }

    public static class SpacerElement implements ILangElement
    {
        @Override
        public String toString()
        {
            return "";
        }
    }

    public static class CommentElement implements ILangElement
    {
        String comment;

        public CommentElement(String comment)
        {
            this.comment = comment;
        }

        public CommentElement setComment(String comment)
        {
            this.comment = comment;
            return this;
        }

        public String getComment()
        {
            return comment;
        }

        @Override
        public String toString()
        {
            return "// " + getComment();
        }
    }

    public static class LangElement implements ILangElement
    {
        String key, value;

        public LangElement(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public String getValue()
        {
            return value;
        }

        public LangElement setKey(String key)
        {
            this.key = key;
            return this;
        }

        public LangElement setValue(String value)
        {
            this.value = value;
            return this;
        }

        @Override
        public String toString()
        {
            return getKey() + "=" + getValue();
        }
    }
}
