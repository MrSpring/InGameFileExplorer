package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenImageViewer;
import dk.mrspring.fileexplorer.loader.ImageLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiImageViewer implements IGui//, IDelayedDraw
{
    int x, y, w, h;
    String path;
    BufferedImage image;
    ByteBuffer buffer;
    int textureId = -1;
    boolean failed = false;
    boolean showFullscreenButton = false;
    boolean center = false;
    GuiSimpleButton fullscreenButton;
    int loadTime = 0, timeOut = 300;

    public GuiImageViewer(final String path, int xPos, int yPos, int width, int height)
    {
        this.path = path;
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        fullscreenButton = new GuiSimpleButton(x, y, 20, 20, "");

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("Loading image at: " + path);
                    BufferedImage image = ImageLoader.loadImage(path);
                    ByteBuffer buffer = ImageLoader.loadTexture1(image);
                    GuiImageViewer.this.onImageDoneLoading(image, buffer);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    System.out.println("Reporting failed...");
                    GuiImageViewer.this.onImageFailedLoading();
                }
            }
        });

        thread.start();

        /*try
        {
            image = ImageLoader.loadImage(path);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        this.setImage(image);*/
        //textureId=ImageLoader.loadTexture(this.image);
    }

    public GuiImageViewer centerImage()
    {
        this.center = true;
        return this;
    }

    public GuiImageViewer enableFullscreenButton()
    {
        this.showFullscreenButton = true;
        return this;
    }

    private void onImageDoneLoading(BufferedImage image, ByteBuffer buffer)
    {
        this.image = image;
        this.buffer = buffer;
    }

    private void onImageFailedLoading()
    {
        this.failed = true;
    }

    public void setImage(final BufferedImage image, ByteBuffer buffer)
    {
        this.image = image;

        int textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        setTextureId(textureId);
    }

    public int getTextureId()
    {
        return textureId;
    }

    public void setTextureId(int textureId)
    {
        this.textureId = textureId;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (textureId != -1 && !failed)
        {
            glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            float imageWidth = image.getWidth(), imageHeight = image.getHeight();
            float width = w;
            float height = width * (imageHeight / imageWidth);

            if (height > h)
            {
                height = h;
                width = height * (imageWidth / imageHeight);
            }
            if (center)
                DrawingHelper.drawTexturedRect(x + ((w - width) / 2), y + ((h - height) / 2), width, height, 0, 0, 512, 512, 1F);
            else DrawingHelper.drawTexturedRect(x, y, width, height, 0, 0, 512, 512, 1F);
            if (showFullscreenButton)
            {
                fullscreenButton.setX(x + (int) width - 20);
                fullscreenButton.setY(y + (int) height - 20);
                fullscreenButton.draw(minecraft, mouseX, mouseY);
                DrawingHelper.drawIcon(DrawingHelper.fullscreenIcon, x + (int) width - 19, y + (int) height - 19, 18, 18, false);
            }
        } else
        {
            if (this.image != null && this.buffer != null)
                this.setImage(image, buffer);
            if (failed)
                minecraft.fontRendererObj.drawString("Failed loading... :(", x + 10, y + 10, 0xFF0000);
            else minecraft.fontRendererObj.drawString("Loading...", x + 10, y + 10, 0x0000FF);
        }
    }

    public void setHeight(int h)
    {
        this.h = h;
    }

    public void setWidth(int w)
    {
        this.w = w;
    }

    @Override
    public void update()
    {
        if (showFullscreenButton)
            fullscreenButton.update();

        if (textureId == -1 && loadTime <= timeOut)
            loadTime++;
        if (loadTime > timeOut)
            this.failed = true;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (showFullscreenButton)
            if (fullscreenButton.mouseDown(mouseX, mouseY, mouseButton))
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenImageViewer("Fullscreen Image Viewer", Minecraft.getMinecraft().currentScreen, path));
        return false;
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

    }

    /*@Override
    public Drawable getDelayedDrawable()
    {
        return new Drawable()
        {
            @Override
            public void draw(Minecraft minecraft, int mouseX, int mouseY)
            {
                if (textureId != -1)
                {
                    GL11.glPushMatrix();

                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

                    float imageWidth = image.getWidth(), imageHeight = image.getHeight();

                    float width = w;
                    float height = width * (imageHeight / imageWidth);

                    if (height > h)
                    {
                        height = h;
                        //System.out.println("Height is: "+height +" width is "+imageHeight);
                        width = height * (imageWidth / imageHeight);
                    }
                    DrawingHelper.drawTexturedRect(x, y, width, height, 0, 0, 512, 512, 1F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);

                    GL11.glPopMatrix();
                } else
                {
                    if (image != null && buffer != null)
                        GuiImageViewer.this.setImage(image, buffer);
                    minecraft.fontRendererObj.drawString("Loading...", x + 10, y + 10, 0xfff);
                }
            }
        };
    }*/
}
