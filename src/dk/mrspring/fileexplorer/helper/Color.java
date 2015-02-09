package dk.mrspring.fileexplorer.helper;

/**
 * Created by MrSpring on 31-07-14 for In-Game File Explorer.
 */
public class Color
{
    float r, g, b;
    String name;

    public static final Color BLACK = new Color("black", 0F, 0F, 0F);
    public static final Color WHITE = new Color("white", 1F, 1F, 1F);

    public static final Color RED = new Color("red", 1F, 0F, 0F);
    public static final Color LT_RED = new Color("light_red", 1F, .75F, .75F);
    public static final Color GREEN = new Color("green", 0F, 1F, 0F);
    public static final Color BLUE = new Color("blue", 0F, 0F, 1F);
    public static final Color LT_BLUE = new Color("light_blue", .75F, .75F, 1F);

    public static final Color ORANGE = new Color("orange", 1F, .5F, 0F);
    public static final Color YELLOW = new Color("yellow", 1F, 1F, 0F);
    public static final Color CYAN = new Color("cyan", 0F, 1F, 1F);
    public static final Color PURPLE = new Color("purple", 1F, 0, 1F);

    public static final Color LT_GREY = new Color("light_grey", .75F, .75F, .75F);
    public static final Color DK_GREY = new Color("dark_grey", .25F, .25F, .25F);
    public static final Color GREY = new Color("grey", .5F, .5F, .5F);

    public Color(String name, float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
    }

    public Color(int colorCode)
    {
        float var7 = (float)(colorCode >> 24 & 255) / 255.0F;
        float var8 = (float)(colorCode >> 16 & 255) / 255.0F;
        float var9 = (float)(colorCode >> 8 & 255) / 255.0F;
    }

    public float getRed()
    {
        return this.r;
    }

    public float getGreen()
    {
        return this.g;
    }

    public float getBlue()
    {
        return this.b;
    }

    public String getName()
    {
        return name;
    }
}