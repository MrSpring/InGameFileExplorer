package dk.mrspring.fileexplorer.gui.editor.lang;

import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.editor.content.ContentEditorElement;
import net.minecraft.client.Minecraft;

/**
 * Created by Konrad on 16-03-2015.
 */
public class ContentCommentElement extends ContentEditorElement<Comment>
{
    GuiCustomTextField valueField;
    String id;

    public ContentCommentElement(int x, int y, int maxWidth, String name, Comment object, boolean canEditName)
    {
        super(x, y, maxWidth, name, object, canEditName);

        valueField = new GuiCustomTextField(x + 10, y, maxWidth - 10, 10, object.comment);
        this.id = name;
    }

    @Override
    public int getHeight()
    {
        return 20;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        minecraft.fontRendererObj.drawString(" - ", xPosition, 4, 0xFFFFFF, true);
        valueField.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public Object getValue()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void setName(String name)
    {

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
