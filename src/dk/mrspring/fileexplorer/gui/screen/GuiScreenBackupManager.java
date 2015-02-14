package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.LiteModFileExplorer;
import dk.mrspring.fileexplorer.backup.BackupEntry;
import dk.mrspring.fileexplorer.backup.BackupManager;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.gui.interfaces.IGui;
import dk.mrspring.llcore.Color;
import dk.mrspring.llcore.DrawingHelper;
import dk.mrspring.llcore.Quad;
import dk.mrspring.llcore.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
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
        this.addGuiElement("list", new GuiBackupList(10, 10, Math.min(width - 20, 220), height - 20, LiteModFileExplorer.backupManager));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        LiteModFileExplorer.core.getDrawingHelper().drawShape(new Quad(0, 0, width, height).setColor(Color.BLACK).setAlpha(0.5F));
        super.drawScreen(mouseX, mouseY, partialTicks);
        float imageWidth = width - 220 - 30;
        if (imageWidth > 20)
        {
            mc.getTextureManager().bindTexture(new ResourceLocation("fileexplorer", "cats/cat.png"));
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();
            float maxImageHeight = height - 20 - 30;
            float imageHeight = Math.min((((float) 456 / (float) 600) * imageWidth), maxImageHeight);
            imageWidth = (600F / 456F) * imageHeight;
            helper.setAllowTextures(true);
            LiteModFileExplorer.core.getDrawingHelper().drawTexturedShape(new Quad(240, 10, imageWidth, imageHeight).setColor(Color.BLACK));
            helper.setAllowTextures(false);
        }
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

        public GuiBackupList(int x, int y, int width, int height, BackupManager manager)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            backupEntries = new ArrayList<GuiBackupEntry>();

            updateList(manager);
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

        public void updateList(BackupManager manager)
        {
            for (BackupEntry entry : manager.entries)
                backupEntries.add(new GuiBackupEntry(entry));
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
            for (Iterator<GuiBackupEntry> iterator = backupEntries.iterator(); iterator.hasNext(); )
            {
                GuiBackupEntry entry = iterator.next();
                if (entry.mouseDown(mouseX, mouseY, mouseButton))
                {
                    BackupEntry backupEntry = LiteModFileExplorer.backupManager.restoreBackup(entry.backupID);
                    if (backupEntry != null)
                    {
//                        FileLoader.restoreBackup(backupEntry);
                        iterator.remove();
                    }
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
            DrawingHelper helper = LiteModFileExplorer.core.getDrawingHelper();

            helper.drawButtonThingy(new Quad(xPosition, yPosition, width, this.height), 0, false);

            restoreButton.setX(xPosition + width - restoreButton.getWidth() - 4);
            restoreButton.setY(yPosition + 4);

            restoreButton.draw(minecraft, mouseX, mouseY);

            int nameLines = helper.drawText(name, new Vector(xPosition + 5, yPosition + 5), 0xFFFFFF, true, width - 10 - restoreButton.getWidth(), DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);
            int dateLines = helper.drawText(date, new Vector(xPosition + 5, yPosition + 5 + 3 + (nameLines * 9)), 0xFFFFFF, true, width - 10 - restoreButton.getWidth(), DrawingHelper.VerticalTextAlignment.LEFT, DrawingHelper.HorizontalTextAlignment.TOP);

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
