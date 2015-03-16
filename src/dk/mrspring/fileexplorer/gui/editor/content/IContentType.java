package dk.mrspring.fileexplorer.gui.editor.content;

/**
 * Created by Konrad on 09-03-2015.
 */
public interface IContentType<E>
{
    public ContentEditorElement<E> getAsEditorElement(int x, int y, int width, String name, E value, boolean canEditName, ContentHandler contentHandler);
}
