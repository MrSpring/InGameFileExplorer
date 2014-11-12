package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.loader.ImageLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by MrSpring on 11-11-2014 for In-Game File Explorer.
 */
public class GuiImageViewer implements IGui
{
    int x, y, w, h;
    BufferedImage image;
    int textureId = -1;

    public GuiImageViewer(final String path, int xPos, int yPos, int width, int height)
    {
        x = xPos;
        y = yPos;
        w = width;
        h = height;

        BufferedImage image = null;
        try
        {
            image = ImageLoader.loadImage(path);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        this.setImage(image);
        //textureId=ImageLoader.loadTexture(this.image);
    }

    public void setImage(final BufferedImage image)
    {
        this.image = image;
        int id = ImageLoader.loadTexture(image);
        setTextureId(id);
    }

    public void setTextureId(int textureId)
    {
        this.textureId = textureId;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (textureId != -1)
        {

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
//        DrawingHelper.drawRect(x,y,image.getWidth(),image.getHeight());

//        int imageWidth = image.getWidth(), imageHeight = image.getHeight();
//
//        int max = (int) MathHelper.abs_max((double) imageWidth, (double) imageHeight);


//            float width = image.getWidth(), height = image.getHeight();
//            float iwidthToHeightRatio = width / height, iheightToWidthRatio = height / width;
//            float vwidthToHeightRatio = w / h, vheightToWidthRatio = h / w;
//
//            if (width > height)
//            {
//                // Draw with width being W
//            } else
//            {
//                // Draw with height being H
//            }


//            if (image.getWidth()>width)
//                width=image.getWidth();

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

    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
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
}
