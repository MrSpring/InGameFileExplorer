package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.backup.BackupEntry;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.fileexplorer.helper.DrawingHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class GuiScreenBackupManager extends GuiScreen
{
    int imageTextLines = 0;

    public GuiScreenBackupManager(String title, net.minecraft.client.gui.GuiScreen previousScreen)
    {
        super(title, previousScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.hideBars().hideTitle();
        this.addGuiElement("done", new GuiSimpleButton(width - 40 - 10, height - 20 - 10, 40, 20, "Done"));
        this.addGuiElement("list", new GuiBackupList(10, 10, Math.min(width - 20, 220), height - 20));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void guiClicked(String identifier, IGui gui, int mouseX, int mouseY, int mouseButton)
    {
        if (identifier.equals("done"))
        {
            mc.displayGuiScreen(this.previousScreen);
        } else super.guiClicked(identifier, gui, mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean drawGui(String identifier, IGui gui)
    {
        return super.drawGui(identifier, gui);
    }

    public class GuiBackupList implements IGui
    {
        int x, y, width, height;
        List<GuiBackupEntry> backupEntries;

        public GuiBackupList(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            backupEntries = new ArrayList<GuiBackupEntry>();

            for (BackupEntry entry : LiteModFileExplorer.backupManager.entries)
                backupEntries.add(new GuiBackupEntry(entry));
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            int yOffset = 0;
            for (GuiBackupEntry entry : backupEntries)
            {
                int height = entry.draw(minecraft, mouseX, mouseY, x, y + yOffset, width);
                yOffset += height + 5;
            }
        }

        @Override
        public void update()
        {
            for (GuiBackupEntry entry : backupEntries)
                entry.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            for (GuiBackupEntry entry : backupEntries)
            {
                if (entry.mouseDown(mouseX, mouseY, mouseButton))
                {
                    BackupEntry backupEntry = LiteModFileExplorer.backupManager.restoreBackup(entry.backupID);
                    if (backupEntry != null)
                        FileLoader.restoreBackup(backupEntry);
                }
            }
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

    public class GuiBackupEntry
    {
        GuiSimpleButton restoreButton;
        String name;
        String date;
        int backupID;
        int height = 0;

        public GuiBackupEntry(BackupEntry entry)
        {
            name = entry.getOriginalFile().getName();
            date = entry.getBackupDate().toString();
            backupID = entry.getBackupID();
            restoreButton = new GuiSimpleButton(0, 0, 50, 0, "Restore");
        }

        public int draw(Minecraft minecraft, int mouseX, int mouseY, int xPosition, int yPosition, int width)
        {
            DrawingHelper.drawIcon(DrawingHelper.hoverIcon, xPosition, yPosition, width, this.height, false);

            restoreButton.setX(xPosition + width - restoreButton.getWidth() - 4);
            restoreButton.setY(yPosition + 4);

            restoreButton.draw(minecraft, mouseX, mouseY);

            int nameLines = DrawingHelper.drawSplitString(minecraft.fontRendererObj, xPosition + 5, yPosition + 5, name, 0xFFFFFF, width - 10 - restoreButton.getWidth());
            int dateLines = DrawingHelper.drawSplitString(minecraft.fontRendererObj, xPosition + 5, yPosition + 5 + 3 + (nameLines * 9), date, 0xFFFFFF, width - 10 - restoreButton.getWidth());

            height = (nameLines * 9) + (dateLines * 9) + 12;
            restoreButton.setHeight(height - 8);

            return this.height;
        }

        public void update()
        {
            restoreButton.update();
        }

        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            return restoreButton.mouseDown(mouseX, mouseY, mouseButton);
        }
    }
}
