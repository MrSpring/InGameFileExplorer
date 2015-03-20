package dk.mrspring.fileexplorer.gui.editor.lang;

import dk.mrspring.fileexplorer.gui.editor.content.ContentEditorElement;
import dk.mrspring.fileexplorer.gui.editor.content.ContentHandler;
import dk.mrspring.fileexplorer.gui.editor.content.IContentType;

/**
 * Created by Konrad on 16-03-2015.
 */
public class CommentContentType implements IContentType<Comment>
{
    @Override
    public ContentEditorElement<Comment> getAsEditorElement(int x, int y, int width, String name, Comment value, boolean canEditName, ContentHandler contentHandler)
    {
        return new ContentCommentElement(x, y, width, name, value, canEditName);
    }
}
