package dk.mrspring.fileexplorer.gui.helper;

/**
 * Created by MrSpring on 30-11-2014 for In-Game File Explorer.
 */
public class Quad
{
    Vector[] vectors = new Vector[4];

    public Quad(Vector[] vectors)
    {
        this.vectors = vectors;
    }

    public Quad(Vector one, Vector two, Vector three, Vector four)
    {
        this(new Vector[]{one, two, three, four});
    }

    public Quad(float[] one, float[] two, float[] three, float[] four)
    {
        this(new Vector(one), new Vector(two), new Vector(three), new Vector(four));
    }

    public Quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
    {
        this(new float[]{x1, y1}, new float[]{x2, y2}, new float[]{x3, y3}, new float[]{x4, y4});
    }

    public Quad(float x1, float y1, Color c1, float a1, float x2, float y2, Color c2, float a2, float x3, float y3, Color c3, float a3, float x4, float y4, Color c4, float a4)
    {
        this(new Vector(x1, y1).setColor(c1).setAlpha(a1), new Vector(x2, y2).setColor(c2).setAlpha(a2), new Vector(x3, y3).setColor(c3).setAlpha(a3), new Vector(x4, y4).setColor(c4).setAlpha(a4));
    }

    public Quad(float x, float y, float width, float height)
    {
        this(x, y, x + width, y, x + width, y + height, x, y + height);
    }

    public Quad(float x, float y, float w, float h, Color c, float a)
    {
        this(x, y, c, a, x + w, y, c, a, x + w, y + h, c, a, x, y + h, c, a);
    }

    public void translate(float x, float y)
    {
        for (Vector vector : vectors)
        {
            vector.setX(vector.getX() + x);
            vector.setY(vector.getY() + y);
        }
    }

    public Vector[] getVectors()
    {
        return vectors;
    }

    public Vector getVector(int id)
    {
        return vectors[id];
    }

    public Quad setColor(Color color)
    {
        for (Vector vector:vectors)
            vector.setColor(color);
        return this;
    }

    public Quad setAlpha(float alpha)
    {
        for (Vector vector:vectors)
            vector.setAlpha(alpha);
        return this;
    }
}
