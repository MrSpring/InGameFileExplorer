package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.screen.GuiScreenImageViewer;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import dk.mrspring.fileexplorer.loader.ImageLoader;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiImageViewer implements IGui//, IDelayedDraw
{
    int x, y, w, h;
    File file;
    BufferedImage image;
    ByteBuffer buffer;
    int textureId = -1;
    boolean failed = false;
    boolean showFullscreenButton = false;
    boolean center = false;
    boolean showBorder = false;
    GuiSimpleButton fullscreenButton;
    int loadTime = 0, timeOut = 300;
    String caption;
    int captionLines = 0, captionExtraHeight = 0;

    public GuiImageViewer(final File file, int xPos, int yPos, int width, int height)
    {
        this.file = file;
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        fullscreenButton = new GuiSimpleButton(x, y, 20, 20, "").setIcon(LiteModFileExplorer.core.getIcon("fullscreen"));

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    BufferedImage image = ImageLoader.loadImage(file);
                    ByteBuffer buffer = ImageLoader.loadTexture1(image);
                    GuiImageViewer.this.onImageDoneLoading(image, buffer);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    GuiImageViewer.this.onImageFailedLoading();
                }
            }
        });

        thread.start();
    }

    public GuiImageViewer setCaption(String caption)
    {
        this.caption = caption;
        showBorder = true;
        return this;
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
            glPushMatrix();

            float[] imageSize = getImageSize();
            float imageX = x, imageY = y, imageWidth = imageSize[0], imageHeight = imageSize[1];

            if (center)
            {
                imageX += ((w - imageWidth) / 2);
                imageY += ((h - imageHeight) / 2);
            }

            if (showBorder)
            {
                LiteModFileExplorer.core.getDrawingHelper().drawButtonThingy(new Quad(imageX, imageY, imageWidth, imageHeight + (captionLines * 9) + captionExtraHeight), 0, false);
                imageX += 3;
                imageY += 3;
                imageWidth -= 6;
                imageHeight -= 6;
            }

            if (caption != null)
                if (!caption.equals(""))
                {
                    captionLines = LiteModFileExplorer.core.getDrawingHelper().drawText(this.caption, new Vector(imageX + (imageWidth / 2), imageY + imageHeight), 0xFFFFFF, true, (int) imageWidth, dk.mrspring.llcore.DrawingHelper.VerticalTextAlignment.CENTER, dk.mrspring.llcore.DrawingHelper.HorizontalTextAlignment.TOP);
                    captionExtraHeight = 4;
                }

            glBindTexture(GL_TEXTURE_2D, textureId);
            LiteModFileExplorer.core.getDrawingHelper().drawTexturedShape(new Quad(
                    new Vector(imageX, imageY, 0, 0),
                    new Vector(imageX + imageWidth, imageY, 512, 0),
                    new Vector(imageX + imageWidth, imageY + imageHeight, 512, 512),
                    new Vector(imageX, imageY + imageHeight, 0, 512)));
            glPopMatrix();
            minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/font/ascii.png"));
            if (showFullscreenButton)
            {
                fullscreenButton.setX(x + (int) imageWidth - 20);
                fullscreenButton.setY(y + (int) imageHeight - 20);
                fullscreenButton.draw(minecraft, mouseX, mouseY);
//                DrawingHelper.drawIcon(DrawingHelper.fullscreenIcon, x + (int) imageWidth - 19, y + (int) imageSize[1] - 19, 18, 18, false);
            }
        } else
        {
            if (this.image != null && this.buffer != null)
                this.setImage(image, buffer);
            if (failed)
                minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.image_viewer.load_failed"), x + 10, y + 10, 0xFF0000);
            else
                minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.image_viewer.loading"), x + 10, y + 10, 0x0000FF);
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenImageViewer("Fullscreen Image Viewer", Minecraft.getMinecraft().currentScreen, file));
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

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }

    public int getHeight()
    {
        return h;
    }

    public int getWidth()
    {
        return w;
    }

    public float[] getImageSize()
    {
        if (image != null)
        {
            float imageWidth = image.getWidth(), imageHeight = image.getHeight();
            float width = w;
            float height = width * (imageHeight / imageWidth);

            if (height > h)
            {
                height = h;
                width = height * (imageWidth / imageHeight);
            }

            return new float[]{width, height};
        } else return new float[]{1, 1};
    }
}
