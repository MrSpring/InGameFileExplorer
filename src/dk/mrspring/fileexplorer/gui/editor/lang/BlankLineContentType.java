package dk.mrspring.fileexplorer.gui.editor.lang;

import dk.mrspring.fileexplorer.gui.editor.content.ContentEditorElement;
import dk.mrspring.fileexplorer.gui.editor.content.ContentHandler;
import dk.mrspring.fileexplorer.gui.editor.content.IContentType;

/**
 * Created by Konrad on 17-03-2015.
 */
public class BlankLineContentType implements IContentType<BlankLine>
{
    @Override
    public ContentEditorElement<BlankLine> getAsEditorElement(int x, int y, int width, String name, BlankLine value, boolean canEditName, ContentHandler contentHandler)
    {
        return new ContentBlankLineElement(x, y, width, name);
    }
}
