package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.gui.GuiCheckbox;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by MrSpring on 28-12-2014 for In-Game File Explorer.
 */
public class GuiScreenWelcome extends GuiScreen
{
    public GuiScreenWelcome(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideTitle().hideBars().hideDoneButton();

        int topHeight = 60;

        List<String> linesFromMessage = mc.fontRendererObj.listFormattedStringToWidth(StatCollector.translateToLocal("gui.screen.welcome.message").replace("\\n", "\n"), width - 30);
        topHeight += (linesFromMessage.size() * 9);

        int readLines = mc.fontRendererObj.listFormattedStringToWidth(StatCollector.translateToLocal("gui.screen.welcome.i_accept.read"), width - 50).size();
        int writeLines = mc.fontRendererObj.listFormattedStringToWidth(StatCollector.translateToLocal("gui.screen.welcome.i_accept.write"), width - 50).size();

        this.addGuiElement("terms-and-conditions", new GuiTermsAndConditions(20, topHeight, width - 40, height - topHeight - 50 - (readLines * 9) - (writeLines * 9)));
        this.addGuiElement("finish", new GuiSimpleButton(20, height - 40, 40, 20, "Done").disable());

        this.addGuiElement("accept-read", new GuiCheckbox(20, height - 65 - ((readLines - 1) * 9) - ((writeLines - 1) * 9), 10, 10, false));
        this.addGuiElement("accept-write", new GuiCheckbox(20, height - 55 - ((writeLines - 1) * 9), 10, 10, false));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (((GuiCheckbox) this.getGui("accept-write")).isChecked())
        {
            ((GuiCheckbox) this.getGui("accept-read")).setChecked(true);
            ((GuiCheckbox) this.getGui("accept-read")).setIsEnabled(false);
        } else
            ((GuiCheckbox) this.getGui("accept-read")).setIsEnabled(true);

        ((GuiSimpleButton) this.getGui("finish")).setEnabled(((GuiTermsAndConditions) this.getGui("terms-and-conditions")).loaded() && (((GuiCheckbox) this.getGui("accept-read")).isChecked() || ((GuiCheckbox) this.getGui("accept-write")).isChecked()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper.drawIcon(DrawingHelper.hoverIcon, 10, 10, width - 20, height - 20, false);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glScalef(2, 2, 2);
        DrawingHelper.drawCenteredString(mc.fontRendererObj, width / 4, 10, StatCollector.translateToLocal("gui.screen.welcome.title"), 0xFFFFFF, true);

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        DrawingHelper.drawSplitCenteredString(mc.fontRendererObj, width / 2, 50, StatCollector.translateToLocal("gui.screen.welcome.message").replace("\\n", "\n"), 0xFFFFFF, width - 30, true);
        DrawingHelper.drawSplitString(mc.fontRendererObj, 32, ((GuiCheckbox) this.getGui("accept-read")).getY() + 1, StatCollector.translateToLocal("gui.screen.welcome.i_accept.read"), 0xFFFFFF, width - 50, true);
        DrawingHelper.drawSplitString(mc.fontRendererObj, 32, ((GuiCheckbox) this.getGui("accept-write")).getY() + 1, StatCollector.translateToLocal("gui.screen.welcome.i_accept.write"), 0xFFFFFF, width - 50, true);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
        if (identifier.equals("finish"))
            this.closeAndOpenExplorer();
    }

    private void closeAndOpenExplorer()
    {
        LiteModFileExplorer.config.acceptFileReading = ((GuiCheckbox) this.getGui("accept-read")).isChecked();
        LiteModFileExplorer.config.acceptFileManipulation = ((GuiCheckbox) this.getGui("accept-write")).isChecked();
        LiteModFileExplorer.config.showWelcomeScreen = false;
        LiteModFileExplorer.saveConfig();
        mc.displayGuiScreen(new GuiScreenFileExplorer(null, new File(LiteModFileExplorer.config.startLocation)));
    }

    public class GuiTermsAndConditions implements IGui
    {
        int x, y, w, h;
        String currentText = "";

        public GuiTermsAndConditions(int xPos, int yPos, int width, int height)
        {
            this.x = xPos;
            this.y = yPos;
            this.w = width;
            this.h = height;

            try
            {
                File file = new File("igfe-terms.txt");
                if (file.exists())
                    file.delete();

                FileUtils.copyURLToFile(new URL("http://mrspring.dk/mods/igfe/conditions.txt"), file);
                if (file.exists())
                    this.currentText = FileUtils.readFileToString(file);
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public boolean loaded()
        {
            return !this.currentText.equals("");
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            DrawingHelper.drawQuad(x + 1, y, w - 2, h, Color.BLACK, 0.5F);
            DrawingHelper.drawQuad(x, y + 1, 1, h - 2, Color.BLACK, 0.5F);
            DrawingHelper.drawQuad(x + w - 1, y + 1, 1, h - 2, Color.BLACK, 0.5F);
            DrawingHelper.drawSplitString(minecraft.fontRendererObj, x + 5, y + 5, currentText, 0xFFFFFF, w - 10, true);
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

        @Override
        public void handleKeyTyped(int keyCode, char character)
        {

        }
    }
}
