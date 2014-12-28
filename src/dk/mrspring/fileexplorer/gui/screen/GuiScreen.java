package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.*;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MrSpring on 27-11-2014 for In-Game File Explorer.
 */
public class GuiScreen extends net.minecraft.client.gui.GuiScreen
{
    String title = "untitled", subtitle = "";
    private HashMap<String, IGui> guiHashMap;
    private boolean showBars = true;
    private boolean drawCenteredTitle = true;
    private boolean useDefaultDoneButton = true;
    private int barHeight = 30;
    protected net.minecraft.client.gui.GuiScreen previousScreen;
    private int mouseXAtLastFrame = 0, mouseYAtLastFrame = 0;

    public GuiScreen(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        this.title = title;
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.guiHashMap = new HashMap<String, IGui>();

        this.addGuiElement("done_button", new GuiSimpleButton(0, 0, 40, 20, "Done").hideBackground());
    }

    public void addGuiElement(String identifier, IGui gui)
    {
        if (!this.guiHashMap.containsKey(identifier))
            this.guiHashMap.put(identifier, gui);
    }

    public void removeElement(String identifier)
    {
        if (this.guiHashMap.containsKey(identifier))
            this.guiHashMap.remove(identifier);
    }

    public void drawCenteredTitle()
    {
        String translatedTitle = StatCollector.translateToLocal(this.title);

        int textPosY = this.barHeight / 2 - 4;

        if (this.drawSubTitle())
            textPosY -= 6;

        float titleWidth = mc.fontRendererObj.getStringWidth("§l" + translatedTitle);
        float underlineOverflow = 5F;
        float underlinePosX = (width / 2) - (titleWidth / 2) - underlineOverflow;

        this.drawCenteredString(mc.fontRendererObj, "§l" + translatedTitle, this.width / 2, textPosY, 0xFFFFFF);

        if (this.drawSubTitle())
            this.drawCenteredSubTitle();

        DrawingHelper.drawQuad(underlinePosX + 1, textPosY + 9, titleWidth + (underlineOverflow * 2), 1, Color.DKGREY, 1F);
        DrawingHelper.drawQuad(underlinePosX, textPosY + 8, titleWidth + (underlineOverflow * 2), 1, Color.WHITE, 1F);
    }

    public void drawCenteredSubTitle()
    {
        String translatedSubTitle = StatCollector.translateToLocal(this.subtitle);
        int textPosY = this.barHeight / 2 - 10;
        this.drawCenteredString(mc.fontRendererObj, translatedSubTitle, this.width / 2, textPosY + 10, 0xFFFFFF);
    }

    public boolean drawSubTitle()
    {
        return !this.subtitle.equals("");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // TODO: Fix Done button

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.showBars)
        {
            DrawingHelper.drawQuad(0, 0, width, barHeight - 1, Color.BLACK, 0.75F);
            DrawingHelper.drawQuad(0, height - barHeight + 1, width, barHeight - 1, Color.BLACK, 0.75F);

            DrawingHelper.drawQuad(0, barHeight - 1, width, 1, Color.WHITE, 1F);
            DrawingHelper.drawQuad(0, height - barHeight, width, 1, Color.WHITE, 1F);

            if (this.drawCenteredTitle)
                this.drawCenteredTitle();

            GL11.glTranslatef(0, barHeight, 0);
        } else if (this.drawCenteredTitle)
            this.drawCenteredTitle();

        /*if (this.useDefaultDoneButton)
        {
            ((GuiSimpleButton) this.getGui("done_button")).setY(height - barHeight + (barHeight / 2) - 9);
            ((GuiSimpleButton) this.getGui("done_button")).setX((barHeight / 2) - 10);
            this.getGui("done_button").draw(mc, mouseX, actualMouseY);
        }*/

        List<Drawable> drawables = new ArrayList<Drawable>();

        for (Map.Entry<String, IGui> entry : guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui element = entry.getValue();

            if (this.drawGui(identifier, element) && !identifier.equals("done_button"))
            {
                element.draw(mc, mouseX, mouseY);
                if (element instanceof IDelayedDraw)
                {
                    drawables.add(((IDelayedDraw) element).getDelayedDrawable());
                }
            }
        }

        if (drawables.size() > 0)
        {
            for (Drawable drawable : drawables)
                drawable.draw(mc, mouseX, mouseY);
        }

        this.mouseXAtLastFrame = mouseX;
        this.mouseYAtLastFrame = mouseY;
    }

    public int getBarHeight()
    {
        return barHeight;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        for (Map.Entry<String, IGui> entry : guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui element = entry.getValue();

            if (identifier.equals("done_button"))
                element.update();
            else if (this.updateElement(identifier, element))
                element.update();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            String identifier = entry.getKey();
            IGui gui = entry.getValue();

            if (this.listensForKey(identifier, gui))
                gui.handleKeyTyped(keyCode, typedChar);
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int dWheel = Mouse.getDWheel();
        for (IGui iGui : this.guiHashMap.values())
            if (iGui instanceof IMouseListener)
                ((IMouseListener) iGui).handleMouseWheel(mouseXAtLastFrame, mouseYAtLastFrame, dWheel);
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        try
        {
            for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
            {
                String identifier = entry.getKey();
                IGui gui = entry.getValue();

                if (identifier.equals("done_button") && gui.mouseDown(mouseX, mouseY, mouseButton))
                    mc.displayGuiScreen(this.previousScreen);
                else if (gui.mouseDown(mouseX, mouseY, mouseButton))
                    this.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            IGui gui = entry.getValue();
            gui.mouseUp(mouseX, mouseY, state);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        for (Map.Entry<String, IGui> entry : this.guiHashMap.entrySet())
        {
            IGui gui = entry.getValue();
            gui.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {

    }

    public GuiScreen hideDoneButton()
    {
        this.useDefaultDoneButton = false;
        return this;
    }

    public GuiScreen setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
        if (this.barHeight < 30)
            this.barHeight = 30;
        return this;
    }

    public void replaceGui(String identifier, IGui gui)
    {
        this.guiHashMap.put(identifier, gui);
    }

    public boolean listensForKey(String identifier, IGui gui)
    {
        return true;
    }

    public void setBarHeight(int barHeight)
    {
        this.barHeight = barHeight;
    }

    public GuiScreen hideBars()
    {
        this.showBars = false;
        return this;
    }

    public GuiScreen hideTitle()
    {
        this.drawCenteredTitle = false;
        return this;
    }

    public IGui getGui(String identifier)
    {
        if (this.guiHashMap.containsKey(identifier))
            return this.guiHashMap.get(identifier);
        else return null;
    }

    public boolean drawGui(String identifier, IGui gui)
    {
        return true;
    }

    public boolean updateElement(String identifier, IGui gui)
    {
        return true;
    }
}
