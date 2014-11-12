package dk.mrspring.fileexplorer.gui.helper;

import net.minecraft.client.renderer.Tessellator;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 31-07-14 for MC Music Player.
 */
public class DrawingHelper
{
    public static void drawImageIcon(float x, float y, float w, float h, Color c, float a)
    {
        float tenthWidth = w / 10, tenthHeight = h / 10;

        drawCustomQuad(
                x + 3 * tenthWidth, y + 5 * tenthHeight,
                x + 6 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 8 * tenthHeight, c, a);

        drawCustomQuad(
                x + 7 * tenthWidth, y + 4 * tenthHeight,
                x + 9 * tenthWidth, y + 6 * tenthHeight,
                x + 9 * tenthWidth, y + 9 * tenthHeight,
                x + 2 * tenthWidth, y + 9 * tenthHeight, c, a);
    }

    public static void drawFolderIcon(float x, float y, float w, float h, Color c, float a)
    {
        float tenthWidth = w / 10, tenthHeight = h / 10;

        drawCustomQuad(
                x + 7 * tenthWidth, y + 5 * tenthHeight,
                x + 9 * tenthWidth, y + 9 * tenthHeight,
                x + 3 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 5 * tenthHeight, c, a);

        drawCustomQuad(
                x + 9 * tenthWidth, y + 3 * tenthHeight,
                x + 9 * tenthWidth, y + 7 * tenthHeight,
                x + 9 * tenthWidth, y + 7 * tenthHeight,
                x + 7 * tenthWidth, y + 3 * tenthHeight, c, a);

        drawCustomQuad(
                x + 9 * tenthWidth, y + 2 * tenthHeight,
                x + 9 * tenthWidth, y + 4 * tenthHeight,
                x + 1 * tenthWidth, y + 4 * tenthHeight,
                x + 1 * tenthWidth, y + 2 * tenthHeight, c, a);

        drawCustomQuad(
                x + 7 * tenthWidth, y + 5 * tenthHeight,
                x + 9 * tenthWidth, y + 9 * tenthHeight,
                x + 3 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 5 * tenthHeight, c, a);

        drawCustomQuad(
                x + 5.5F * tenthWidth, y + 1 * tenthHeight,
                x + 5.5F * tenthWidth, y + 2 * tenthHeight,
                x + 2.5F * tenthWidth, y + 2 * tenthHeight,
                x + 2.5F * tenthWidth, y + 1 * tenthHeight, c, a);
    }

    public static void drawTextFileIcon(float x, float y, float w, float h, Color c, float a)
    {
        float tenthWidth = w / 10, tenthHeight = h / 10;

        drawCustomQuad(
                x + 5 * tenthWidth, y + 1 * tenthHeight,
                x + 5 * tenthWidth, y + 4 * tenthHeight,
                x + 2 * tenthWidth, y + 4 * tenthHeight,
                x + 2 * tenthWidth, y + 1 * tenthHeight, c, a);

        drawCustomQuad(
                x + 6 * tenthWidth, y + 1 * tenthHeight,
                x + 8 * tenthWidth, y + 3 * tenthHeight,
                x + 6 * tenthWidth, y + 3 * tenthHeight,
                x + 6 * tenthWidth, y + 3 * tenthHeight, c, a);

        drawCustomQuad(
                x + 8 * tenthWidth, y + 4 * tenthHeight,
                x + 8 * tenthWidth, y + 5 * tenthHeight,
                x + 2 * tenthWidth, y + 5 * tenthHeight,
                x + 2 * tenthWidth, y + 4 * tenthHeight, c, a);

        drawCustomQuad(
                x + 8 * tenthWidth, y + 5 * tenthHeight,
                x + 8 * tenthWidth, y + 6 * tenthHeight,
                x + 6 * tenthWidth, y + 6 * tenthHeight,
                x + 6 * tenthWidth, y + 5 * tenthHeight, c, a);

        drawCustomQuad(
                x + 3 * tenthWidth, y + 5 * tenthHeight,
                x + 3 * tenthWidth, y + 6 * tenthHeight,
                x + 2 * tenthWidth, y + 6 * tenthHeight,
                x + 2 * tenthWidth, y + 5 * tenthHeight, c, a);

        drawCustomQuad(
                x + 8 * tenthWidth, y + 6 * tenthHeight,
                x + 8 * tenthWidth, y + 7 * tenthHeight,
                x + 2 * tenthWidth, y + 7 * tenthHeight,
                x + 2 * tenthWidth, y + 6 * tenthHeight, c, a);

        drawCustomQuad(
                x + 8 * tenthWidth, y + 7 * tenthHeight,
                x + 8 * tenthWidth, y + 8 * tenthHeight,
                x + 7 * tenthWidth, y + 8 * tenthHeight,
                x + 7 * tenthWidth, y + 7 * tenthHeight, c, a);

        drawCustomQuad(
                x + 3 * tenthWidth, y + 7 * tenthHeight,
                x + 3 * tenthWidth, y + 8 * tenthHeight,
                x + 2 * tenthWidth, y + 8 * tenthHeight,
                x + 2 * tenthWidth, y + 7 * tenthHeight, c, a);

        drawCustomQuad(
                x + 8 * tenthWidth, y + 8 * tenthHeight,
                x + 8 * tenthWidth, y + 9 * tenthHeight,
                x + 2 * tenthWidth, y + 9 * tenthHeight,
                x + 2 * tenthWidth, y + 8 * tenthHeight, c, a);
    }

    public static void drawVerticalGradient(float x, float y, float w, float h, Color topColor, float topAlpha, Color bottomColor, float bottomAlpha)
    {
        drawCustomQuad(x, y, x + w, y, x + w, y + h, x, y + h, topColor, topAlpha, topColor, topAlpha, bottomColor, bottomAlpha, bottomColor, bottomAlpha);
    }

    public static void drawButtonThingy(float x, float y, float w, float h, Color c1, float a1, Color c2, float a2)
    {
        drawRect(x, y, w, h, c1, a1);
        drawOutline(x, y, w, h, c2, a2);
    }

    public static void drawOutline(float x, float y, float w, float h, Color c, float a)
    {
        drawRect(x, y, w, 1, c, a);
        drawRect(x, y, 1, h, c, a);
        drawRect(x + w - 1, y, 1, h, c, a);
        drawRect(x, y + h - 1, w, 1, c, a);
    }

    public static void drawEditIcon(float x, float y, float width, float height, Color color, float alpha)
    {
//        drawOutline(x, y, width, height, color, alpha);

//        drawRect(x, y, width, 1, color, alpha);
//        drawRect(x, y + height - 1, width, 1, color, alpha);
//
//        drawRect(x, y, 1, height, color, alpha);
//        drawRect(x + width - 1, y, 1, height, color, alpha);

        float tenthWidth = width / 10, tenthHeight = height / 10;

        drawCustomQuad(
                x + 2 * tenthWidth, y + height - 4 * tenthHeight,
                x + 4 * tenthWidth, y + height - 2 * tenthHeight,
                x + tenthWidth, y + height - tenthHeight,
                x + tenthWidth, y + height - tenthHeight,
                color, alpha
        );

        drawCustomQuad(
                x + 7 * tenthWidth, y + tenthHeight,
                x + width - tenthWidth, y + 3 * tenthHeight,
                x + 5 * tenthWidth, y + height - 3 * tenthHeight,
                x + 3 * tenthWidth, y + height - 5 * tenthHeight,
                color, alpha
        );
    }

    public static void drawCheckMarkIcon(float x, float y, float width, float height, Color color, float alpha)
    {
//        drawRect(x, y, width, 1, color, alpha);
//        drawRect(x, y + height - 1, width, 1, color, alpha);
//
//        drawRect(x, y, 1, height, color, alpha);
//        drawRect(x + width - 1, y, 1, height, color, alpha);

        float tenthWidth = width / 10, tenthHeight = height / 10;

        drawCustomQuad(
                x + 2 * tenthWidth, y + height - 5 * tenthHeight,
                x + 4 * tenthWidth, y + height - 3 * tenthHeight,
                x + 4 * tenthWidth, y + height - 1 * tenthHeight,
                x + 1 * tenthWidth, y + height - 4 * tenthHeight,
                color, alpha
        );

        drawCustomQuad(
                x + 8 * tenthWidth, y + 1 * tenthHeight,
                x + 9 * tenthWidth, y + 2 * tenthHeight,
                x + 4 * tenthWidth, y + 9 * tenthHeight,
                x + 3 * tenthWidth, y + 8 * tenthHeight,
                color, alpha
        );
    }

    public static void drawCross(float x, float y, float width, float height, Color color, float alpha)
    {
        float tenthWidth = width / 10, tenthHeight = height / 10;

        drawCustomQuad(
                x + 8 * tenthWidth, y + 1 * tenthHeight,
                x + 9 * tenthWidth, y + 2 * tenthHeight,
                x + 2 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 8 * tenthHeight,
                color, alpha
        );

        drawCustomQuad(
                x + 2 * tenthWidth, y + 1 * tenthHeight,
                x + 9 * tenthWidth, y + 8 * tenthHeight,
                x + 8 * tenthWidth, y + 9 * tenthHeight,
                x + 1 * tenthWidth, y + 2 * tenthHeight,
                color, alpha
        );
    }

    public static void drawRect(float x, float y, float width, float height, Color color, float alpha)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        drawCustomQuad(x, y, x + width, y, x + width, y + height, x, y + height, color, alpha);

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, int u, int v, int u2, int v2, float alpha)
    {
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.01F);
        glEnable(GL_TEXTURE_2D);
        glColor4f(1.0F, 1.0F, 1.0F, alpha);

        float texMapScale = 0.001953125F;

        Tessellator tessellator = Tessellator.getInstance();

        tessellator.getWorldRenderer().startDrawingQuads();

        tessellator.getWorldRenderer().addVertexWithUV(x, y + height, 0, u * texMapScale, v2 * texMapScale);
        tessellator.getWorldRenderer().addVertexWithUV(x + width, y + height, 0, u2 * texMapScale, v2 * texMapScale);
        tessellator.getWorldRenderer().addVertexWithUV(x + width, y, 0, u2 * texMapScale, v * texMapScale);
        tessellator.getWorldRenderer().addVertexWithUV(x, y, 0, u * texMapScale, v * texMapScale);

        tessellator.draw();

        glDisable(GL_BLEND);
    }

    public static void drawPlayIcon(float x, float y, float width, float height, Color color, float alpha)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        drawCustomQuad(x, y, x + width, y + (height / 2), x + width, y + (height / 2), x, y + height, color, alpha);

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawPauseIcon(float xPos, float yPos, float width, float height, Color color, float alpha)
    {
        drawRect(xPos, yPos, width / 3, height, color, alpha);
        drawRect(xPos + ((width / 3) * 2), yPos, width / 3, height, color, alpha);
    }

    public static void drawCustomQuad(float v1PosX, float v1PosY, float v2PosX, float v2PosY, float v3PosX, float v3PosY, float v4PosX, float v4PosY, Color c1, float a1, Color c2, float a2, Color c3, float a3, Color c4, float a4)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(7425);
//        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();

//        glColor4f(c1.getRed(), c1.getGreen(), c1.getBlue(), a1);
        tessellator.getWorldRenderer().setColorRGBA_F(c1.getRed(), c1.getGreen(), c1.getBlue(), a1);
        tessellator.getWorldRenderer().addVertex(v1PosX, v1PosY, 0);
        tessellator.getWorldRenderer().setColorRGBA_F(c2.getRed(), c2.getGreen(), c2.getBlue(), a2);
        tessellator.getWorldRenderer().addVertex(v2PosX, v2PosY, 0);
        tessellator.getWorldRenderer().setColorRGBA_F(c3.getRed(), c3.getGreen(), c3.getBlue(), a3);
        tessellator.getWorldRenderer().addVertex(v3PosX, v3PosY, 0);
        tessellator.getWorldRenderer().setColorRGBA_F(c4.getRed(), c4.getGreen(), c4.getBlue(), a4);
        tessellator.getWorldRenderer().addVertex(v4PosX, v4PosY, 0);

        tessellator.draw();
        glShadeModel(7424);

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawCustomQuad(float v1PosX, float v1PosY, float v2PosX, float v2PosY, float v3PosX, float v3PosY, float v4PosX, float v4PosY, Color color, float alpha)
    {
        drawCustomQuad(v1PosX, v1PosY, v2PosX, v2PosY, v3PosX, v3PosY, v4PosX, v4PosY, color, alpha, color, alpha, color, alpha, color, alpha);
    }
}