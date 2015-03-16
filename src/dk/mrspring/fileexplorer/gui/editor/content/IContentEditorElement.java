package dk.mrspring.fileexplorer.gui.editor.content;

import net.minecraft.client.Minecraft;

/**
 * Created by MrSpring on 30-12-2014 for In-Game File Explorer.
 */
public interface IContentEditorElement<E>
{
    public int getHeight();

    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft);

    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton);

    public E getValue();

    public String getName();

    public void setName(String name);

    public void updateElement();

    public void handleKeyTypes(char character, int keyCode);

//    public boolean mouseDown(int mouseX, int mouseY, int mouseButton);
}
