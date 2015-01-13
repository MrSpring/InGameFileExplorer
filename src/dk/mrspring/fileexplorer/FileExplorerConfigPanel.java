package dk.mrspring.fileexplorer;

import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import dk.mrspring.fileexplorer.gui.GuiCheckbox;
import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class FileExplorerConfigPanel implements ConfigPanel
{
    GuiCustomTextField startPositionField;
    GuiCheckbox takeAutoBackup;
    GuiCustomTextField backupPositionField;
    GuiSimpleButton cleanBackup;

    @Override
    public void onPanelShown(ConfigPanelHost host)
    {
        Minecraft mc = Minecraft.getMinecraft();
        startPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.config_panel.file_explorer.start_folder") + ": "), 0, host.getWidth() - mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.config_panel.file_explorer.start_folder") + ": "), 16, LiteModFileExplorer.config.startLocation);
        takeAutoBackup = new GuiCheckbox(mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.config_panel.file_explorer.take_backup") + ": "), 20, 12, 12, LiteModFileExplorer.config.automaticBackup);
        backupPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.config_panel.file_explorer.backup_folder") + ": "), 36, host.getWidth() - mc.fontRendererObj.getStringWidth(StatCollector.translateToLocal("gui.config_panel.file_explorer.backup_folder") + ": "), 16, new File(LiteModFileExplorer.config.backupLocation).getAbsolutePath());
        cleanBackup = new GuiSimpleButton(0, 60, 75, 26, "gui.config_panel.file_explorer.clean_backup").setAutoHeight(true);
    }

    @Override
    public void onPanelResize(ConfigPanelHost host)
    {
        this.onPanelShown(host);
    }

    @Override
    public String getPanelTitle()
    {
        return StatCollector.translateToLocal("gui.config_panel.file_explorer.title");
    }

    @Override
    public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        startPositionField.draw(minecraft, mouseX, mouseY);
        takeAutoBackup.draw(minecraft, mouseX, mouseY);
        backupPositionField.draw(minecraft, mouseX, mouseY);
        cleanBackup.draw(minecraft, mouseX, mouseY);

        minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.config_panel.file_explorer.start_folder") + ": ", 0, 4, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.config_panel.file_explorer.take_backup") + ": ", 0, 22, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(StatCollector.translateToLocal("gui.config_panel.file_explorer.backup_folder") + ": ", 0, 40, 0xFFFFFF, true);
//        minecraft.fontRendererObj.drawString(StatCollector.translateToLocal(StatCollector.translateToLocal("gui.config_panel.file_explorer.clean_backup_warning")), cleanBackup.getWidth() + 2, cleanBackup.getY() + (cleanBackup.getHeight() / 2 - 4), 0xFFFFFF, true);
    }

    @Override
    public void onTick(ConfigPanelHost host)
    {
        this.startPositionField.update();
        this.takeAutoBackup.update();
        this.backupPositionField.setEnabled(takeAutoBackup.isChecked());
        this.backupPositionField.update();
        this.cleanBackup.update();

    }

    @Override
    public int getContentHeight()
    {
        return 100;
    }

    @Override
    public void onPanelHidden()
    {

    }

    @Override
    public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton)
    {
        this.startPositionField.mouseDown(mouseX, mouseY, mouseButton);
        this.takeAutoBackup.mouseDown(mouseX, mouseY, mouseButton);
        this.backupPositionField.mouseDown(mouseX, mouseY, mouseButton);
        if (this.cleanBackup.mouseDown(mouseX, mouseY, mouseButton))
        {
            try
            {
                FileUtils.deleteDirectory(new File(LiteModFileExplorer.config.backupLocation));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            LiteModFileExplorer.loadBackupList();
        }
    }

    @Override
    public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode)
    {
        this.startPositionField.handleKeyTyped(keyCode, keyChar);
        this.backupPositionField.handleKeyTyped(keyCode, keyChar);
    }

    @Override
    public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY)
    {

    }
}
