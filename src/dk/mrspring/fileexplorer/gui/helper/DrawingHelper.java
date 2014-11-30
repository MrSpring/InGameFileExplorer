package dk.mrspring.fileexplorer.gui.helper;

import net.minecraft.client.renderer.Tessellator;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 31-07-14 for In-Game File Explorer.
 */
public class DrawingHelper
{
    IIcon crossIcon=new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 8 * tenthWidth, y + 1 * tenthHeight,
                            x + 9 * tenthWidth, y + 2 * tenthHeight,
                            x + 2 * tenthWidth, y + 9 * tenthHeight,
                            x + 1 * tenthWidth, y + 8 * tenthHeight),
                    new Quad(
                            x + 2 * tenthWidth, y + 1 * tenthHeight,
                            x + 9 * tenthWidth, y + 8 * tenthHeight,
                            x + 8 * tenthWidth, y + 9 * tenthHeight,
                            x + 1 * tenthWidth, y + 2 * tenthHeight)
            };
        }
    };
    IIcon checkMarkIcon=new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 2 * tenthWidth, y + h - 5 * tenthHeight,
                            x + 4 * tenthWidth, y + h - 3 * tenthHeight,
                            x + 4 * tenthWidth, y + h - 1 * tenthHeight,
                            x + 1 * tenthWidth, y + h - 4 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 1 * tenthHeight,
                            x + 9 * tenthWidth, y + 2 * tenthHeight,
                            x + 4 * tenthWidth, y + 9 * tenthHeight,
                            x + 3 * tenthWidth, y + 8 * tenthHeight)
            };
        }
    };
    IIcon editIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 2 * tenthWidth, y + h - 4 * tenthHeight,
                            x + 4 * tenthWidth, y + h - 2 * tenthHeight,
                            x + tenthWidth, y + h - tenthHeight,
                            x + tenthWidth, y + h - tenthHeight
                    ),
                    new Quad(
                            x + 7 * tenthWidth, y + tenthHeight,
                            x + w - tenthWidth, y + 3 * tenthHeight,
                            x + 5 * tenthWidth, y + h - 3 * tenthHeight,
                            x + 3 * tenthWidth, y + h - 5 * tenthHeight
                    )
            };
        }
    };
    IIcon playIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            return new Quad[]{new Quad(x, y, x + w, y + (h / 2), x + w, y + (h / w), x, y + h)};
        }
    };

    IIcon pauseIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            return new Quad[]{
                    new Quad(x, y, x + w / 3, y, x + w / 3, y + h, x, y + h),
                    new Quad(x + (2 * (w / 3)), y, x + w, y, x + w, y + h, x + (2 * (w / 3)), y + h)
            };
        }
    };

    IIcon imageIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{new Quad(x + 3 * tenthWidth, y + (5 * tenthHeight), x + (6 * tenthWidth), y + (9 * tenthHeight), x + tenthWidth, y + (9 * tenthHeight), x + tenthWidth, y + (8 * tenthHeight))};
        }
    };

    IIcon folderIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 7 * tenthWidth, y + 5 * tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 3 * tenthWidth, y + 9 * tenthHeight,
                            x + tenthWidth, y + 5 * tenthHeight),
                    new Quad(
                            x + 9 * tenthWidth, y + 3 * tenthHeight,
                            x + 9 * tenthWidth, y + 7 * tenthHeight,
                            x + 9 * tenthWidth, y + 7 * tenthHeight,
                            x + 7 * tenthWidth, y + 3 * tenthHeight),
                    new Quad(
                            x + 9 * tenthWidth, y + 2 * tenthHeight,
                            x + 9 * tenthWidth, y + 4 * tenthHeight,
                            x + 1 * tenthWidth, y + 4 * tenthHeight,
                            x + 1 * tenthWidth, y + 2 * tenthHeight),
                    new Quad(
                            x + 7 * tenthWidth, y + 5 * tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 3 * tenthWidth, y + 9 * tenthHeight,
                            x + 1 * tenthWidth, y + 5 * tenthHeight),
                    new Quad(
                            x + 5.5F * tenthWidth, y + 1 * tenthHeight,
                            x + 5.5F * tenthWidth, y + 2 * tenthHeight,
                            x + 2.5F * tenthWidth, y + 2 * tenthHeight,
                            x + 2.5F * tenthWidth, y + 1 * tenthHeight)
            };
        }
    };

    IIcon textFileIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 5 * tenthWidth, y + 1 * tenthHeight,
                            x + 5 * tenthWidth, y + 4 * tenthHeight,
                            x + 2 * tenthWidth, y + 4 * tenthHeight,
                            x + 2 * tenthWidth, y + 1 * tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + 1 * tenthHeight,
                            x + 8 * tenthWidth, y + 3 * tenthHeight,
                            x + 6 * tenthWidth, y + 3 * tenthHeight,
                            x + 6 * tenthWidth, y + 3 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 4 * tenthHeight,
                            x + 8 * tenthWidth, y + 5 * tenthHeight,
                            x + 2 * tenthWidth, y + 5 * tenthHeight,
                            x + 2 * tenthWidth, y + 4 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 5 * tenthHeight,
                            x + 8 * tenthWidth, y + 6 * tenthHeight,
                            x + 6 * tenthWidth, y + 6 * tenthHeight,
                            x + 6 * tenthWidth, y + 5 * tenthHeight),
                    new Quad(
                            x + 3 * tenthWidth, y + 5 * tenthHeight,
                            x + 3 * tenthWidth, y + 6 * tenthHeight,
                            x + 2 * tenthWidth, y + 6 * tenthHeight,
                            x + 2 * tenthWidth, y + 5 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 6 * tenthHeight,
                            x + 8 * tenthWidth, y + 7 * tenthHeight,
                            x + 2 * tenthWidth, y + 7 * tenthHeight,
                            x + 2 * tenthWidth, y + 6 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 7 * tenthHeight,
                            x + 8 * tenthWidth, y + 8 * tenthHeight,
                            x + 7 * tenthWidth, y + 8 * tenthHeight,
                            x + 7 * tenthWidth, y + 7 * tenthHeight),
                    new Quad(
                            x + 3 * tenthWidth, y + 7 * tenthHeight,
                            x + 3 * tenthWidth, y + 8 * tenthHeight,
                            x + 2 * tenthWidth, y + 8 * tenthHeight,
                            x + 2 * tenthWidth, y + 7 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + 8 * tenthHeight,
                            x + 8 * tenthWidth, y + 9 * tenthHeight,
                            x + 2 * tenthWidth, y + 9 * tenthHeight,
                            x + 2 * tenthWidth, y + 8 * tenthHeight)
            };
        }
    };

    public static void drawVerticalGradient(float x, float y, float w, float h, Color topColor, float topAlpha, Color bottomColor, float bottomAlpha)
    {
//        drawCustomQuad(x, y, x + w, y, x + w, y + h, x, y + h, topColor, topAlpha, topColor, topAlpha, bottomColor, bottomAlpha, bottomColor, bottomAlpha);
        drawQuad(new Quad(
                new Vector(x, y).setColor(topColor).setAlpha(topAlpha),
                new Vector(x + w, y).setColor(topColor).setAlpha(topAlpha),
                new Vector(x + w, y + h).setColor(bottomColor).setAlpha(bottomAlpha),
                new Vector(x, y + h).setColor(bottomColor).setAlpha(bottomAlpha)
        ));
    }

    public static void drawHorizontalGradient(float x, float y, float w, float h, Color leftColor, float leftAlpha, Color rightColor, float rightAlpha)
    {
//        drawCustomQuad(x, y, x + w, y, x + w, y + h, x, y + h, leftColor, leftAlpha, rightColor, rightAlpha, rightColor, rightAlpha, leftColor, leftAlpha);
        drawQuad(new Quad(
                new Vector(x, y).setColor(leftColor).setAlpha(leftAlpha),
                new Vector(x + w, y).setColor(rightColor).setAlpha(rightAlpha),
                new Vector(x + w, y + h).setColor(rightColor).setAlpha(rightAlpha),
                new Vector(x, y + h).setColor(leftColor).setAlpha(leftAlpha)
        ));
    }

    public static void drawButtonThingy(float x, float y, float w, float h, Color c1, float a1, Color c2, float a2)
    {
        drawQuad(x, y, w, h, c1, a1);
        drawOutline(x, y, w, h, c2, a2);
    }

    public static void drawOutline(float x, float y, float w, float h, Color c, float a)
    {
        drawQuad(x, y, w, 1, c, a);
        drawQuad(x, y, 1, h, c, a);
        drawQuad(x + w - 1, y, 1, h, c, a);
        drawQuad(x, y + h - 1, w, 1, c, a);
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

    public static void drawQuad(Quad quad)
    {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads();

        Vector[] vectors = quad.getVectors();

        for (Vector vector : vectors)
        {
            tessellator.getWorldRenderer().setColorRGBA_F(vector.getColor().getRed(), vector.getColor().getGreen(), vector.getColor().getBlue(), vector.getAlpha());
            tessellator.getWorldRenderer().addVertex(vector.getX(), vector.getY(), 0);
        }

        tessellator.draw();
        glShadeModel(7424);

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawQuad(float x, float y, float width, float height, Color color, float alpha)
    {
        drawQuad(new Quad(x, y, width, height).setColor(color).setAlpha(alpha));
    }
}