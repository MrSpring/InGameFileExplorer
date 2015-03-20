package dk.mrspring.fileexplorer.gui.editor.lang;

import dk.mrspring.fileexplorer.gui.editor.content.ContentEditorElement;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 17-03-2015.
 */
public class ContentBlankLineElement extends ContentEditorElement<BlankLine>
{
    String id;

    public ContentBlankLineElement(int x, int y, int maxWidth, String name)
    {
        super(x, y, maxWidth, name, null, false);
        this.id = name;
    }

    @Override
    public int getHeight()
    {
        return 6;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {

    }

    @Override
    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public Object getValue()
    {
        return "";
    }

    @Override
    public String getName()
    {
        return id;
    }

    @Override
    public void setName(String name)
    {
        this.id = name;
    }

    @Override
    public void updateElement()
    {

    }

    @Override
    public void handleKeyTypes(char character, int keyCode)
    {

    }
}
