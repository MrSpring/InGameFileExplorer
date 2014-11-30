package dk.mrspring.fileexplorer.gui.helper;

/**
 * Created by MrSpring on 30-11-2014 for In-Game File Explorer.
 */
public class Vector
{
    float x, y;
    Color color = Color.WHITE;
    float alpha = 1F;

    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector(float[] pos)
    {
        this(pos[0], pos[1]);
    }

    public Vector setColor(Color color)
    {
        this.color = color;
        return this;
    }

    public Vector setAlpha(float alpha)
    {
        this.alpha = alpha;
        return this;
    }

    public Color getColor()
    {
        return color;
    }

    public float getAlpha()
    {
        return alpha;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public Vector setY(float y)
    {
        this.y = y;
        return this;
    }

    public Vector setX(float x)
    {
        this.x = x;
        return this;
    }
}
