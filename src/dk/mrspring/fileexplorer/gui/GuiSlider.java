package dk.mrspring.fileexplorer.gui;

import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.gui.helper.GuiHelper;
import dk.mrspring.fileexplorer.gui.interfaces.Drawable;
import dk.mrspring.fileexplorer.gui.interfaces.IDelayedDraw;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiSlider implements IGui, IDelayedDraw
{
    int x, y, width, height;
    Type type;
    int value;
    int maximum = 100, minimum = 0;
    boolean dragging = false;
    int alphaProgress = 0;
    int alphaTarget = 0;
    Drawable latestDrawable;
    DecimalFormat format = new DecimalFormat("#.##");

    public GuiSlider(int xPos, int yPos, int width, int height, Type type, int startValue)
    {
        this.x = xPos;
        this.y = yPos;

        this.width = width;
        this.height = height;

        this.value = startValue;

        this.type = type;

        if (value > maximum)
            value = maximum;
        else if (value < minimum)
            value = minimum;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public void draw(Minecraft minecraft, int mouseX, int mouseY)
    {
        DrawingHelper.drawButtonThingy(x, y + 1, width, height - 2, 0, true);
        float sliderWidth = 10F, sliderHeight = height, sliderXPos = x, sliderYPos = y;
        float progress = (((float) this.getValue()) / this.maximum);
        sliderXPos += progress * this.width - (sliderWidth * progress);
        DrawingHelper.drawButtonThingy(sliderXPos, sliderYPos, sliderWidth, sliderHeight, 0, true);
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height))
            alphaTarget = 10;
        else alphaTarget = 0;

        if (alphaProgress != 0)
            DrawingHelper.drawVerticalGradient(sliderXPos + 2, sliderYPos + 2, sliderWidth - 4, sliderHeight - 4, Color.CYAN, ((float) alphaProgress) / 10 * 0.5F, Color.BLUE, ((float) alphaProgress) / 10 * 0.7F);

//        if (dragging)
//        {
        /*switch (this.type)
        {
            case PERCENTAGE:
            {
//                    minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(this.getValue()) + "%", mouseX, mouseY - 10, 0xFFFFFF);
                List<String> lines = new ArrayList<String>();
                lines.add(String.valueOf(this.getValue()) + "%");
                this.drawHoveringText(lines, mouseX, mouseY, minecraft.fontRendererObj);
                break;
            }
            case DOUBLE:
                double dValue = ((double) this.getValue()) / 100;
                minecraft.fontRendererObj.drawStringWithShadow(format.format(dValue), mouseX, mouseY, 0xFFFFFF);
                break;
        }*/
//        }
    }

    protected void drawHoveringText(List lines, int baseX, int baseY, FontRenderer renderer)
    {
        if (!lines.isEmpty())
        {
//            GlStateManager.disableRescaleNormal();
//            RenderHelper.disableStandardItemLighting();
//            GlStateManager.disableLighting();
//            GlStateManager.disableDepth();

            int longestLineWidth = 0;
            Iterator linesIterator = lines.iterator();

            while (linesIterator.hasNext())
            {
                String currentLine = (String) linesIterator.next();
                int currentLineWidth = renderer.getStringWidth(currentLine);

                if (currentLineWidth > longestLineWidth)
                {
                    longestLineWidth = currentLineWidth;
                }
            }

            int xPosition = baseX + 12;
            int yPosition = baseY - 12;
            int height = 8;

            if (lines.size() > 1)
            {
                height += 2 + (lines.size() - 1) * 10;
            }

            /*if (xPosition + longestLineWidth > this.width)
            {
                xPosition -= 28 + longestLineWidth;
            }*/

            /*if (yPosition + height + 6 > this.height)
            {
                yPosition = this.height - height - 6;
            }*/

//            this.zLevel = 300.0F;
//            this.itemRender.zLevel = 300.0F;
            int colorCode = -267386864;
            Color color = Color.BLACK;
            float alpha = 0.75F;
            this.drawRect(xPosition - 3, yPosition - 4, xPosition + longestLineWidth + 3, yPosition - 3, color, alpha);
            this.drawRect(xPosition - 3, yPosition + height + 3, xPosition + longestLineWidth + 3, yPosition + height + 4, color, alpha);
            this.drawRect(xPosition - 3, yPosition - 3, xPosition + longestLineWidth + 3, yPosition + height + 3, color, alpha);
            this.drawRect(xPosition - 4, yPosition - 3, xPosition - 3, yPosition + height + 3, color, alpha);
            this.drawRect(xPosition + longestLineWidth + 3, yPosition - 3, xPosition + longestLineWidth + 4, yPosition + height + 3, color, alpha);
//           this.drawGradientRect(xPosition - 3, yPosition - 4, xPosition + longestLineWidth + 3, yPosition - 3, colorCode, colorCode);
//           this.drawGradientRect(xPosition - 3, yPosition + height + 3, xPosition + longestLineWidth + 3, yPosition + height + 4, colorCode, colorCode);
//           this.drawGradientRect(xPosition - 3, yPosition - 3, xPosition + longestLineWidth + 3, yPosition + height + 3, colorCode, colorCode);
//           this.drawGradientRect(xPosition - 4, yPosition - 3, xPosition - 3, yPosition + height + 3, colorCode, colorCode);
//           this.drawGradientRect(xPosition + longestLineWidth + 3, yPosition - 3, xPosition + longestLineWidth + 4, yPosition + height + 3, colorCode, colorCode);
            int start = 1347420415;
            int end = (start & 16711422) >> 1 | start & -16777216;

            Color startColor = new Color(start);
            Color endColor = new Color(end);
            float startAlpha = (float) (start >> 24 & 255) / 255.0F;
            float endAlpha = (float) (end >> 24 & 255) / 255.0F;
            this.drawRect(xPosition - 3, yPosition - 3 + 1, xPosition - 3 + 1, yPosition + height + 3 - 1, Color.WHITE, 1F);
            this.drawRect(xPosition + longestLineWidth + 2, yPosition - 3 + 1, xPosition + longestLineWidth + 3, yPosition + height + 3 - 1, Color.LTGREY, 1F);
            this.drawRect(xPosition - 3, yPosition - 3, xPosition + longestLineWidth + 3, yPosition - 3 + 1, Color.WHITE, 1F);
            this.drawRect(xPosition - 3, yPosition + height + 2, xPosition + longestLineWidth + 3, yPosition + height + 3, Color.LTGREY, 1F);
//           this.drawGradientRect(xPosition - 3, yPosition - 3 + 1, xPosition - 3 + 1, yPosition + height + 3 - 1, var10, var11);
//           this.drawGradientRect(xPosition + longestLineWidth + 2, yPosition - 3 + 1, xPosition + longestLineWidth + 3, yPosition + height + 3 - 1, var10, var11);
//           this.drawGradientRect(xPosition - 3, yPosition - 3, xPosition + longestLineWidth + 3, yPosition - 3 + 1, var10, var10);
//           this.drawGradientRect(xPosition - 3, yPosition + height + 2, xPosition + longestLineWidth + 3, yPosition + height + 3, var11, var11);

            for (int var12 = 0; var12 < lines.size(); ++var12)
            {
                String var13 = (String) lines.get(var12);
                renderer.drawStringWithShadow(var13, (float) xPosition, (float) yPosition, -1);

                if (var12 == 0)
                {
                    yPosition += 2;
                }

                yPosition += 10;
            }

//            this.zLevel = 0.0F;
//            this.itemRender.zLevel = 0.0F;
//            GlStateManager.enableLighting();
//            GlStateManager.enableDepth();
//            RenderHelper.enableStandardItemLighting();
//            GlStateManager.enableRescaleNormal();
        }
    }

    protected void drawRect(float left, float top, float right, float bottom, Color color, float alpha)
    {
        DrawingHelper.drawQuad(left, top, right - left, bottom - top, color, alpha);
    }

//    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
//    {
//        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
//        float startR = (float)(startColor >> 16 & 255) / 255.0F;
//        float startG = (float)(startColor >> 8 & 255) / 255.0F;
//        float startB = (float)(startColor & 255) / 255.0F;
//        float var11 = (float)(endColor >> 24 & 255) / 255.0F;
//        float var12 = (float)(endColor >> 16 & 255) / 255.0F;
//        float var13 = (float)(endColor >> 8 & 255) / 255.0F;
//        float var14 = (float)(endColor & 255) / 255.0F;
//        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        GlStateManager.shadeModel(7425);
//        Tessellator var15 = Tessellator.getInstance();
//        WorldRenderer var16 = var15.getWorldRenderer();
//        var16.startDrawingQuads();
//        var16.setColorRGBA_F(startR, startG, startB, startAlpha);
//        var16.addVertex((double)right, (double)top, 0);
//        var16.addVertex((double)left, (double)top, 0);
//        var16.setColorRGBA_F(var12, var13, var14, var11);
//        var16.addVertex((double)left, (double)bottom, );
//        var16.addVertex((double)right, (double)bottom, (double)this.zLevel);
//        var15.draw();
//        GlStateManager.shadeModel(7424);
//        GlStateManager.disableBlend();
//        GlStateManager.enableAlpha();
//        GlStateManager.enableTexture2D();
//    }

    @Override
    public void update()
    {
        if (dragging)
        {
            alphaTarget = 15;
        }

        if (alphaProgress < alphaTarget)
            alphaProgress += 5;
        else if (alphaProgress > alphaTarget)
            alphaProgress -= 1;
    }

    @Override
    public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
    {
        if (GuiHelper.isMouseInBounds(mouseX, mouseY, x, y, width, height) && mouseButton == 0)
        {
            dragging = true;
            return true;
        } else return false;
    }

    @Override
    public void mouseUp(int mouseX, int mouseY, int mouseButton)
    {
        dragging = false;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {
        if (mouseButton == 0 && dragging)
        {
            float progress = (((float) mouseX) - x) / width;
            value = (int) (progress * maximum);

            if (value > maximum)
                value = maximum;
            else if (value < minimum)
                value = minimum;
        }
    }

    @Override
    public void handleKeyTyped(int keyCode, char character)
    {

    }

    @Override
    public Drawable getDelayedDrawable()
    {
        return new Drawable()
        {
            @Override
            public void draw(Minecraft minecraft, int mouseX, int mouseY)
            {
                if (GuiSlider.this.dragging)
                {
                    List<String> lines = new ArrayList<String>();
                    lines.add(String.valueOf(GuiSlider.this.getValue()) + "%");
                    GuiSlider.this.drawHoveringText(lines, mouseX, mouseY, minecraft.fontRendererObj);
                }
            }
        };
    }

    public enum Type
    {
        PERCENTAGE,
        DOUBLE
    }
}
