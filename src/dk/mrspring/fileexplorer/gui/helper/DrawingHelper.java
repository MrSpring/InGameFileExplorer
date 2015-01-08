package dk.mrspring.fileexplorer.gui.helper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by MrSpring on 31-07-14 for In-Game File Explorer.
 */
public class DrawingHelper
{
    public static IIcon newFileIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 6 * tenthWidth, y + 6 * tenthHeight,
                            3 * tenthWidth, tenthHeight),
                    new Quad(
                            x + 7 * tenthWidth, y + 5 * tenthHeight,
                            tenthWidth, 3 * tenthHeight),
                    new Quad(
                            x + 2 * tenthWidth, y + tenthHeight,
                            3 * tenthWidth, 8 * tenthHeight),
                    new Quad(
                            x + 5 * tenthWidth, y + tenthHeight,
                            3 * tenthWidth, 3 * tenthHeight),
                    new Quad(
                            x + 5 * tenthWidth, y + 4 * tenthHeight,
                            tenthWidth, tenthHeight),
                    new Quad(
                            x + 5 * tenthWidth, y + 8 * tenthHeight,
                            tenthWidth, tenthHeight)};
        }
    };

    public static IIcon deleteIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 2 * tenthWidth, y + 8 * tenthHeight,
                            6 * tenthWidth, tenthHeight),
                    new Quad(
                            x + 2 * tenthWidth, y + 4 * tenthHeight,
                            tenthWidth, 4 * tenthHeight),
                    new Quad(
                            x + 4.5F * tenthWidth, y + 4 * tenthHeight,
                            tenthWidth, 4 * tenthHeight),
                    new Quad(
                            x + 7 * tenthWidth, y + 4 * tenthHeight,
                            tenthWidth, 4 * tenthHeight),
                    new Quad(
                            x + tenthWidth, y + 2 * tenthHeight,
                            8 * tenthWidth, tenthHeight),
                    new Quad(
                            x + 4.5F * tenthWidth, y + tenthHeight,
                            tenthWidth, tenthHeight)};
        }
    };

    public static IIcon downArrow = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            return new Quad[]{
                    new Quad(
                            w / 2 + x, y + h,
                            x + w, y,
                            x, y,
                            w / 2 + x, y + h)};
        }
    };

    public static IIcon upArrow = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            return new Quad[]{
                    new Quad(
                            w / 2 + x, y,
                            x + w, y + h,
                            x, y + h,
                            w / 2 + x, y)};
        }
    };

    public static IIcon rightArrow = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            return new Quad[]{
                    new Quad(
                            x + w, h / 2 + y,
                            x, y + h,
                            x, y,
                            x + w, h / 2 + y)
            };
        }
    };

    public static IIcon fullscreenIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + tenthWidth, y + tenthHeight,
                            x + 4 * tenthWidth, y + tenthWidth,
                            x + tenthWidth, y + 4 * tenthHeight,
                            x + tenthWidth, y + tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + 4 * tenthHeight,
                            x + 6 * tenthWidth, y + tenthHeight),
                    new Quad(
                            x + tenthWidth, y + 6 * tenthHeight,
                            x + tenthWidth, y + 9 * tenthHeight,
                            x + 4 * tenthWidth, y + 9 * tenthHeight,
                            x + tenthWidth, y + 6 * tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + 9 * tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 9 * tenthWidth, y + 6 * tenthHeight,
                            x + 6 * tenthWidth, y + 9 * tenthHeight),
                    new Quad(
                            x + 2 * tenthWidth, y + 3 * tenthHeight,
                            x + 3 * tenthWidth, y + 2 * tenthHeight,
                            x + 4 * tenthWidth, y + 3 * tenthHeight,
                            x + 3 * tenthWidth, y + 4 * tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + 3 * tenthHeight,
                            x + 7 * tenthWidth, y + 2 * tenthHeight,
                            x + 8 * tenthWidth, y + 3 * tenthHeight,
                            x + 7 * tenthWidth, y + 4 * tenthHeight),
                    new Quad(
                            x + 2 * tenthWidth, y + 7 * tenthHeight,
                            x + 3 * tenthWidth, y + 8 * tenthHeight,
                            x + 4 * tenthWidth, y + 7 * tenthHeight,
                            x + 3 * tenthWidth, y + 6 * tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + 7 * tenthHeight,
                            x + 7 * tenthWidth, y + 8 * tenthHeight,
                            x + 8 * tenthWidth, y + 7 * tenthHeight,
                            x + 7 * tenthWidth, y + 6 * tenthHeight)
            };
        }
    };
    public static IIcon hoverIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            return new Quad[]{
                    new Quad(x + 1, y, w - 2, h, Color.BLACK, alpha),
                    new Quad(x, y + 1, 1, h - 2, Color.BLACK, alpha),
                    new Quad(x + w - 1, y + 1, 1, h - 2, Color.BLACK, alpha),
                    new Quad(x + 1, y + 1, w - 2, 1, Color.WHITE, 1F),
                    new Quad(x + 1, y + 2, 1, h - 4, Color.WHITE, 1F),
                    new Quad(x + w - 2, y + 2, 1, h - 3, Color.LTGREY, 1F),
                    new Quad(x + 1, y + h - 2, w - 3, 1, Color.LTGREY, 1F)
            };
        }
    };
    public static IIcon crossIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
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
    public static IIcon checkMarkIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
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
    public static IIcon editIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
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

    public static IIcon playIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{new Quad(
                    x + tenthWidth, y + tenthHeight,
                    x + 9 * tenthWidth, y + (h / 2),
                    x + 9 * tenthWidth, y + (h / 2),
                    x + tenthWidth, y + 9 * tenthHeight)
            };
        }
    };

    public static IIcon pauseIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + tenthWidth, y + tenthHeight,
                            x + 4 * tenthWidth, y + tenthHeight,
                            x + 4 * tenthWidth, y + h - tenthHeight,
                            x + tenthWidth, y + h - tenthHeight),
                    new Quad(
                            x + 6 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 6 * tenthWidth, y + 9 * tenthHeight)
            };
        }
    };

    public static IIcon imageIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tenthWidth = w / 10, tenthHeight = h / 10;
            return new Quad[]{
                    new Quad(
                            x + 3.5F * tenthWidth, y + 5 * tenthHeight,
                            x + 6 * tenthWidth, y + 9 * tenthHeight,
                            x + tenthWidth, y + 9 * tenthHeight,
                            x + tenthWidth, y + 8 * tenthHeight),
                    new Quad(
                            x + 6.5F * tenthWidth, y + 4 * tenthHeight,
                            x + 9 * tenthWidth, y + 6 * tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 2 * tenthWidth, y + 9 * tenthHeight),
                    new Quad(
                            x + tenthWidth, y + tenthWidth,
                            x + 2 * tenthWidth, y + tenthHeight,
                            x + 2 * tenthWidth, y + 9 * tenthHeight,
                            x + tenthWidth, y + 9 * tenthHeight),
                    new Quad(
                            x + 8 * tenthWidth, y + tenthWidth,
                            x + 9 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + 9 * tenthHeight,
                            x + 8 * tenthWidth, y + 9 * tenthHeight),
                    new Quad(
                            x + tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + tenthHeight,
                            x + 9 * tenthWidth, y + 2 * tenthHeight,
                            x + tenthWidth, y + 2 * tenthHeight)
            };
        }
    };

    public static IIcon folderIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
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
    public static IIcon textFileIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
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
    public static IIcon jsonFileIcon = new IIcon()
    {
        @Override
        public Quad[] getQuads(float x, float y, float w, float h, float alpha)
        {
            float tw = w / 10, th = h / 10;
            float tww = w / 20, twh = h / 20;
            return new Quad[]{
                    new Quad(
                            x + 2 * tw, y + th,
                            tw, 8 * th),
                    new Quad(
                            x + 3 * tw, y + th,
                            2 * tw, 3 * th),
                    new Quad(
                            x + 6 * tw, y + th,
                            x + 8 * tw, y + 3 * th,
                            x + 6 * tw, y + 3 * th,
                            x + 6 * tw, y + th),
                    new Quad(
                            x + 3 * tw, y + 4 * th,
                            5 * tw, th),
                    new Quad(
                            x + 7 * tw, y + 4 * th,
                            tw, 5 * th),
                    new Quad(
                            x + 3 * tw, y + 8 * th + twh,
                            4 * tw, twh),
                    new Quad(
                            x + 3 * tw, y + 5 * th,
                            tww, th + twh),
                    new Quad(
                            x + 3 * tw, y + 7 * th,
                            tww, th + twh),
                    new Quad(
                            x + 5 * tw - tww, y + 5 * th,
                            tw, 4 * th - twh),
                    new Quad(
                            x + 4 * tw, y + 5 * th + twh,
                            tww, 3 * th - twh),
                    new Quad(
                            x + 5 * tw + tww, y + 5 * th + twh,
                            tww, 3 * th - twh),
                    new Quad(
                            x + 6 * tw + tww, y + 5 * th,
                            tww, th + twh),
                    new Quad(
                            x + 6 * tw + tww, y + 7 * th,
                            tww, th + twh)
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

    public static void drawButtonThingy(float x, float y, float w, float h, float alphaProgress, boolean enabled, Color startColor, float startAlphaMultiplier, Color endColor, float endAlphaMultiplier)
    {
        drawIcon(hoverIcon, x, y, w, h, 0.25F, false);
        if (enabled)
            drawVerticalGradient(x + 2, y + 2, w - 4, h - 4, startColor, alphaProgress * startAlphaMultiplier, endColor, alphaProgress * endAlphaMultiplier);
        else DrawingHelper.drawVerticalGradient(x + 2, y + 2, w - 4, h - 4, Color.LTGREY, 0.5F, Color.DKGREY, 0.8F);
//        drawQuad(x, y, w, h, c1, a1);
//        drawOutline(x, y, w, h, c2, a2);
    }

    public static void drawButtonThingy(float x, float y, float w, float h, float alphaProgress, boolean enabled, Color startColor, Color endColor)
    {
        drawButtonThingy(x, y, w, h, alphaProgress, enabled, startColor, 0.25F, endColor, 0.5F);
    }

    public static void drawButtonThingy(float x, float y, float w, float h, float alphaProgress, boolean enabled)
    {
        drawButtonThingy(x, y, w, h, alphaProgress, enabled, Color.CYAN, Color.BLUE);
    }

    public static void drawOutline(float x, float y, float w, float h, Color c, float a)
    {
        drawQuad(x, y, w, 1, Color.WHITE, a);
        drawQuad(x, y, 1, h, Color.WHITE, a);
        drawQuad(x + w - 1, y, 1, h, Color.LTGREY, a);
        drawQuad(x, y + h - 1, w, 1, Color.LTGREY, a);
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

    public static void drawIcon(IIcon icon, float x, float y, float w, float h, float alpha)
    {
        drawIcon(icon, x, y, w, h, alpha, true);
    }

    public static void drawIcon(IIcon icon, float x, float y, float w, float h, boolean shadow)
    {
        drawIcon(icon, x, y, w, h, 0.85F, shadow);
    }

    public static void drawIcon(IIcon icon, float x, float y, float w, float h, float alpha, boolean shadow)
    {
        float twentiethWidth = w / 20, twentiethHeight = h / 20;
        Quad[] quads = icon.getQuads(x, y, w, h, alpha);
        if (shadow)
            for (Quad quad : quads)
            {
                quad.setColor(Color.DKGREY);
                quad.translate(twentiethWidth / 2, twentiethHeight / 2);
                drawQuad(quad);
                quad.setColor(Color.WHITE);
                quad.translate(-(twentiethWidth), -(twentiethHeight));
            }
        for (Quad quad : quads)
            drawQuad(quad);
    }

    public static void drawIcon(IIcon icon, float x, float y, float w, float h)
    {
        drawIcon(icon, x, y, w, h, true);
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

    public static void drawCenteredString(FontRenderer renderer, float x, float y, String s, int color, boolean shadow)
    {
        renderer.drawString(s, x - (renderer.getStringWidth(s) / 2), y, color, shadow);
    }

    public static void drawCenteredString(FontRenderer renderer, float x, float y, String s, int color)
    {
        drawCenteredString(renderer, x, y, s, color, true);
    }

    public static int drawSplitCenteredString(FontRenderer renderer, float x, float y, String s, int color, int maxLength, boolean shadow)
    {
        List<String> lines = renderer.listFormattedStringToWidth(s, maxLength);
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            drawCenteredString(renderer, x, y + (i * 9), line, color, shadow);
        }
        return lines.size();
    }

    public static int drawSplitCenteredString(FontRenderer renderer, float x, float y, String s, int colot, int maxLength)
    {
        return drawSplitCenteredString(renderer, x, y, s, colot, maxLength, true);
    }

    /**
     * @param renderer
     * @param x
     * @param y
     * @param s
     * @param color
     * @param maxLength
     * @param shadow
     * @return Returns how many lines the string has been wrapped to.
     */
    public static int drawSplitString(FontRenderer renderer, int x, int y, String s, int color, int maxLength, boolean shadow)
    {
        if (shadow)
            renderer.drawSplitString(s, x + 1, y + 1, maxLength, getShadowColor(color));
        renderer.drawSplitString(s, x, y, maxLength, color);
        return renderer.listFormattedStringToWidth(s, maxLength).size();
    }

    public static int getShadowColor(int color)
    {
        return (color & 16579836) >> 2 | color & -16777216;
    }
}