package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.helper.TextHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

/**
 * Created by MrSpring on 14-11-2014 for In-Game File Explorer.
 */
public class GuiCustomTextField implements IGui
{
    int x, y, w, h;
    String text = "";
    boolean focused = false;
    int editPosition = 0;
    float editX = 0, editY = 0;
    int editCount = 0;
    int maxEditCount = 20;

    public GuiCustomTextField(int xPos, int yPos, int width, int height)
    {
        this.x = xPos;
        this.y = yPos;
        this.w = width;
        this.h = height;
    }

    public GuiCustomTextField setText(String text)
    {
        this.text = text;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    public void toggleFocus()
    {
        this.focused = !this.focused;
    }

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
//        DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, 0.25F, Color.WHITE, 1F);
        float backgroundAlpha = 0.25F;
        if (this.focused)
            backgroundAlpha = 0.5F;
        DrawingHelper.drawButtonThingy(x, y, w, h, Color.BLACK, backgroundAlpha, Color.WHITE, 1F);
        minecraft.fontRendererObj.drawString(this.getText(), x + 5, ((float) y) + ((float) h) / 2 - 4, 0xFFFFFF, true);
        if (this.focused)
            this.drawEditThingy(minecraft);
    }

    private void drawEditThingy(Minecraft mc)
    {
        if (editCount < maxEditCount / 2)
            DrawingHelper.drawRect((editX + x + 5) - 0.5F, editY + y + 2.5F, 1, 10, Color.LTGREY, 1F);
    }

    private void recalculateEditRenderPosition(Minecraft mc)
    {
        char[] textAsCharacters = this.getText().toCharArray();

        int editingXPos = 0;
        for (int i = 0; i < textAsCharacters.length; i++)
        {
            if (i == this.editPosition)
                break;
            char character = textAsCharacters[i];
            int characterWidth = mc.fontRendererObj.getCharWidth(character);
            editingXPos += characterWidth;
        }
        this.editX = editingXPos;
    }

    public void setEditPosition(int editPosition)
    {
        this.editPosition = editPosition;
        this.editCount = 0;
    }

    public int getEditPosition()
    {
        return editPosition;
    }

    private void calculateEditRenderPositionFromMouse(int mouseX, int mouseY)
    {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int localMouseX = mouseX - x - 5;
        int textWidth = renderer.getStringWidth(this.getText());

        String trimmedText = renderer.trimStringToWidth(this.getText(), localMouseX);

        char[] textAsCharacters = this.getText().toCharArray();
        char[] trimmedTextAsCharacters = trimmedText.toCharArray();

        this.setEditPosition(trimmedText.length());

        float throughLastLetter;

        if (textAsCharacters.length != trimmedTextAsCharacters.length)
        {
            int trimmedTextWidth = renderer.getStringWidth(trimmedText);

            char clickedChar = textAsCharacters[trimmedTextAsCharacters.length];

            throughLastLetter = ((float) (localMouseX - trimmedTextWidth + 1)) / ((float) renderer.getCharWidth(clickedChar));

            editX = trimmedTextWidth;

            if (throughLastLetter >= 0.5F)
            {
                editX += renderer.getCharWidth(clickedChar);
                this.setEditPosition(this.getEditPosition() + 1);
            }

            System.out.println("\"" + trimmedText + "\", " + localMouseX + ", " + throughLastLetter + ", " + textAsCharacters[trimmedTextAsCharacters.length]);
        } else editX = textWidth;


        /*if (localMouseX < textWidth)
            editX = renderer.splitStringWidth(this.getText(), localMouseX);
        else editX = 0;

        int localMouseY = mouseY - y;

        int editingXPos = x + 5;
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        for (char character : textAsCharacters)
        {
            int characterWidth=renderer.getCharWidth(character);
            renderer.splitStringWidth()
        }*/
    }

    public void writeCharacter(char character)
    {
        StringBuilder builder = new StringBuilder(this.getText());
        builder.insert(this.getEditPosition(), character);
        this.setText(builder.toString());
        this.setEditPosition(this.getEditPosition() + 1);
        this.recalculateEditRenderPosition(Minecraft.getMinecraft());
    }

    public void writeString(String string)
    {
        StringBuilder builder = new StringBuilder(this.getText());
        builder.insert(this.getEditPosition(), string);
        this.setText(builder.toString());
        this.setEditPosition(this.getEditPosition() + string.length());
        this.recalculateEditRenderPosition(Minecraft.getMinecraft());
    }

    public void delete(int charAmount)
    {
        System.out.println(this.getEditPosition());
        StringBuilder builder = new StringBuilder(this.getText());
        if (charAmount > 0 && this.getEditPosition() != this.getText().length())
        {
            builder.delete(this.getEditPosition(), this.getEditPosition() + charAmount);
        } else if (charAmount < 0 && this.getEditPosition() != 0)
        {
            builder.delete(this.getEditPosition() + charAmount, this.getEditPosition());
            this.setEditPosition(this.getEditPosition() + charAmount);
        }

        this.setText(builder.toString());
        this.recalculateEditRenderPosition(Minecraft.getMinecraft());
    }

    @Override
    public void update()
    {
        if (focused)
        {
            editCount++;
            if (editCount > maxEditCount)
                editCount = 0;
        }
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, w, h))
        {
            this.setFocused(true);
            this.calculateEditRenderPositionFromMouse(mouseX, mouseY);
            return true;
        } else
        {
            this.setFocused(false);
            return false;
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {

    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {
        if (keyCode == Keyboard.KEY_LEFT)
        {
            if (this.getEditPosition() > 0)
                this.setEditPosition(this.getEditPosition() - 1);
            this.recalculateEditRenderPosition(Minecraft.getMinecraft());
        } else if (keyCode == Keyboard.KEY_RIGHT)
        {
            if (this.getEditPosition() < this.getText().length())
                this.setEditPosition(this.getEditPosition() + 1);
            this.recalculateEditRenderPosition(Minecraft.getMinecraft());
        } else if (keyCode == Keyboard.KEY_BACK)
            this.delete(-1);
        else if (keyCode == Keyboard.KEY_DELETE)
            this.delete(1);
        else if (TextHelper.isKeyWritable(keyCode))
            this.writeCharacter(character);
    }
}
