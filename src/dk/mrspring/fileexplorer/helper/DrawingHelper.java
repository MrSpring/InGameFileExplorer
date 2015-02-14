package dk.mrspring.fileexplorer.helper;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;

/**
 * Created by Konrad on 12-02-2015.
 */
public class DrawingHelper
{
    public static void drawButtonThingy(Quad quad, float alphaProgress, boolean enabled, Color startColor, float startAlpha, Color endColor, float endAlpha)
    {
        try
        {
            dk.mrspring.llcore.DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            float x = quad.getVector(0).getX(), y = quad.getVector(0).getY(), w = quad.getWidth(), h = quad.getHeight();

            helper.drawShape(new Quad(x + 1, y, w - 2, h).setColor(Color.BLACK).setAlpha(0.25F));
            helper.drawShape(new Quad(x, quad.getVector(0).getY() + 1, 1, quad.getHeight() - 2).setColor(Color.BLACK).setAlpha(0.25F));
            helper.drawShape(new Quad(x + w - 1, quad.getVector(0).getY() + 1, 1, quad.getHeight() - 2).setColor(Color.BLACK).setAlpha(0.25F));
            helper.drawShape(new Quad(x + 1, y + 1, w - 2, 1).setColor(Color.WHITE));
            helper.drawShape(new Quad(x + 1, y + 2, 1, h - 4).setColor(Color.WHITE));
            helper.drawShape(new Quad(x + w - 2, y + 2, 1, h - 3).setColor(Color.LT_GREY));
            helper.drawShape(new Quad(x + 1, y + h - 2, w - 3, 1).setColor(Color.LT_GREY));

            if (enabled && alphaProgress > 0)
                helper.drawShape(new Quad(
                        new Vector(x + 2, y + 2, startColor, alphaProgress * startAlpha),
                        new Vector(x + w - 2, y + 2, startColor, alphaProgress * startAlpha),
                        new Vector(x + w - 2, y + h - 2, endColor, alphaProgress * endAlpha),
                        new Vector(x + 2, y + h - 2, endColor, alphaProgress * endAlpha)));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void drawButtonThingy(Quad quad, float alphaProgress, boolean enabled)
    {
        drawButtonThingy(quad, alphaProgress, enabled, Color.CYAN, 0.25F, Color.BLUE, 0.5F);
    }
}
