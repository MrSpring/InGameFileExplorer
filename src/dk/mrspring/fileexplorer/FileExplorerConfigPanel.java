package dk.mrspring.fileexplorer;

import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import dk.mrspring.fileexplorer.gui.GuiCheckbox;
import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import dk.mrspring.fileexplorer.gui.GuiDropDownList;
import dk.mrspring.fileexplorer.gui.GuiSimpleButton;
import dk.mrspring.fileexplorer.helper.FileSorter;
import dk.mrspring.fileexplorer.helper.TranslateHelper;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

/**
 * Created by MrSpring on 11-01-2015 for In-Game File Explorer - 1.8.0.
 */
public class FileExplorerConfigPanel implements ConfigPanel
{
    GuiCustomTextField startPositionField;
    GuiCheckbox takeAutoBackup;
    GuiCustomTextField backupPositionField;
    GuiSimpleButton cleanBackup;
    GuiDropDownList sortingType;

    @Override
    public void onPanelShown(ConfigPanelHost host)
    {
        Config startConfig = LiteModFileExplorer.config;
        Minecraft mc = Minecraft.getMinecraft();
        startPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": "), 0, host.getWidth() - mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": "), 16, startConfig.startLocation);
        takeAutoBackup = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.take_backup") + ": "), 20, 12, 12, startConfig.automaticBackup);
        backupPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": "), 36, host.getWidth() - mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": "), 16, new File(startConfig.backupLocation).getAbsolutePath());
        cleanBackup = new GuiSimpleButton(0, 60, 75, 26, "gui.config_panel.file_explorer.clean_backup").setAutoHeight(true);
        FileSorter.SortingType[] types = FileSorter.SortingType.values();
        int current = ArrayUtils.indexOf(types, LiteModFileExplorer.config.fileSortingType);
        GuiDropDownList.ListElement[] elements = new GuiDropDownList.ListElement[types.length];
        for (int i = 0; i < types.length; i++)
            elements[i] = new GuiDropDownList.ListElement(
                    TranslateHelper.translate("sorting_type." + types[i].toString().toLowerCase() + ".name") + "\n§7" +
                            TranslateHelper.translate("sorting_type." + types[i].toString().toLowerCase() + ".desc"), types[i]);
        sortingType = new GuiDropDownList(0, 105, 150, 30, current, elements);
    }

    @Override
    public void onPanelResize(ConfigPanelHost host)
    {
        this.onPanelShown(host);
    }

    @Override
    public String getPanelTitle()
    {
        return TranslateHelper.translate("gui.config_panel.file_explorer.title");
    }

    @Override
    public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        startPositionField.draw(minecraft, mouseX, mouseY);
        takeAutoBackup.draw(minecraft, mouseX, mouseY);
        backupPositionField.draw(minecraft, mouseX, mouseY);
        cleanBackup.draw(minecraft, mouseX, mouseY);
        sortingType.setY(81 + cleanBackup.getHeight());
        sortingType.draw(minecraft, mouseX, mouseY);

        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": ", 0, 4, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.take_backup") + ": ", 0, 22, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": ", 0, 40, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.file_sorting") + ": ", 0, sortingType.getY()-12, 0xFFFFFF, true);
//        minecraft.fontRendererObj.drawString(TranslateHelper.translate(TranslateHelper.translate("gui.config_panel.file_explorer.clean_backup_warning")), cleanBackup.getWidth() + 2, cleanBackup.getY() + (cleanBackup.getHeight() / 2 - 4), 0xFFFFFF, true);
    }

    @Override
    public void onTick(ConfigPanelHost host)
    {
        this.startPositionField.update();
        this.takeAutoBackup.update();
        this.backupPositionField.setEnabled(takeAutoBackup.isChecked());
        this.backupPositionField.update();
        this.cleanBackup.update();
        this.sortingType.update();
    }

    @Override
    public int getContentHeight()
    {
        return 300;
    }

    @Override
    public void onPanelHidden()
    {
        LiteModFileExplorer.config.startLocation = startPositionField.getText();
        LiteModFileExplorer.config.automaticBackup = takeAutoBackup.isChecked();
        File newFile = new File(backupPositionField.getText());
        File oldFile = new File(LiteModFileExplorer.config.backupLocation);
        if (!newFile.getAbsolutePath().equals(oldFile.getAbsolutePath()))
            LiteModFileExplorer.config.backupLocation = backupPositionField.getText();
        LiteModFileExplorer.config.fileSortingType = sortingType.getSelectedElement().getIdentifier();
        LiteModFileExplorer.config.validateValues();
        LiteModFileExplorer.saveConfig();
    }

    @Override
    public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton)
    {
        this.startPositionField.mouseDown(mouseX, mouseY, mouseButton);
        this.takeAutoBackup.mouseDown(mouseX, mouseY, mouseButton);
        this.backupPositionField.mouseDown(mouseX, mouseY, mouseButton);
        if (this.cleanBackup.mouseDown(mouseX, mouseY, mouseButton))
        {
            LiteModFileExplorer.resetBackupList();
        }
        this.sortingType.mouseDown(mouseX, mouseY, mouseButton);
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
