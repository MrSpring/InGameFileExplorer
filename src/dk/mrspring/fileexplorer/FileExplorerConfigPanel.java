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
    GuiCheckbox hideNonEditableFiles;
    GuiCheckbox showEditDate;
    GuiCheckbox showFileSize;
    GuiCheckbox showOpenDirectory;
    GuiCustomTextField textFileTypes;

    @Override
    public void onPanelShown(ConfigPanelHost host)
    {
        Config config = LiteModFileExplorer.config;
        Minecraft mc = Minecraft.getMinecraft();
        startPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": "), 0, host.getWidth() - mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": "), 16, config.startLocation);
        takeAutoBackup = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.take_backup") + ": "), 20, 12, 12, config.automaticBackup);
        backupPositionField = new GuiCustomTextField(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": "), 36, host.getWidth() - mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": "), 16, new File(config.backupLocation).getAbsolutePath());
        cleanBackup = new GuiSimpleButton(0, 60, 75, 26, "gui.config_panel.file_explorer.clean_backup").setAutoHeight(true);
        FileSorter.SortingType[] types = FileSorter.SortingType.values();
        int current = ArrayUtils.indexOf(types, LiteModFileExplorer.config.fileSortingType);
        GuiDropDownList.ListElement[] elements = new GuiDropDownList.ListElement[types.length];
        for (int i = 0; i < types.length; i++)
            elements[i] = new GuiDropDownList.ListElement(
                    TranslateHelper.translate("sorting_type." + types[i].toString().toLowerCase() + ".name") + "\n\u00a77" +
                            TranslateHelper.translate("sorting_type." + types[i].toString().toLowerCase() + ".desc"), types[i]);
        sortingType = new GuiDropDownList(0, 105, 150, 30, current, elements);
        hideNonEditableFiles = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.hide_non_editable_files") + ": "), 130, 12, 12, config.hideNonEditableFiles);
        showEditDate = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.show_file_edit_date") + ": "), 150, 12, 12, config.showFileEditBelowName);
        showFileSize = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.show_file_size") + ": "), 180, 12, 12, config.showFileSizeBelowName);
        showOpenDirectory = new GuiCheckbox(mc.fontRendererObj.getStringWidth(TranslateHelper.translate("gui.config_panel.file_explorer.show_open_directory") + ": "), 200, 12, 12, config.showOpenDirectory);
        textFileTypes = new GuiCustomTextField(0, 250, host.getWidth(), 16, config.getTextFileTypes());
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
        hideNonEditableFiles.setY(90 + cleanBackup.getHeight() + sortingType.getSelectedElement().getHeight());
        hideNonEditableFiles.draw(minecraft, mouseX, mouseY);
        showEditDate.setY(hideNonEditableFiles.getY() + 12 + 5);
        showEditDate.draw(minecraft, mouseX, mouseY);
        showFileSize.setY(showEditDate.getY() + 12 + 5);
        showFileSize.draw(minecraft, mouseX, mouseY);
        showOpenDirectory.setY(showFileSize.getY() + 12 + 5);
        showOpenDirectory.draw(minecraft, mouseX, mouseY);
        textFileTypes.setY(showOpenDirectory.getY() + 12 + 5 + 10);
        textFileTypes.draw(minecraft, mouseX, mouseY);

        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.start_folder") + ": ", 0, 4, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.take_backup") + ": ", 0, 22, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.backup_folder") + ": ", 0, 40, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.file_sorting") + ": ", 0, sortingType.getY() - 12, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.hide_non_editable_files") + ": ", 0, hideNonEditableFiles.getY() + 2, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.show_file_edit_date") + ": ", 0, showEditDate.getY() + 2, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.show_file_size") + ": ", 0, showFileSize.getY() + 2, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.show_open_directory") + ": ", 0, showOpenDirectory.getY() + 2, 0xFFFFFF, true);
        minecraft.fontRendererObj.drawString(TranslateHelper.translate("gui.config_panel.file_explorer.text_file_types") + ": ", 0, textFileTypes.getY() - 10, 0xFFFFFF, true);
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
        this.hideNonEditableFiles.update();
        this.showEditDate.update();
        this.showFileSize.update();
        this.showOpenDirectory.update();
        this.textFileTypes.update();
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
        LiteModFileExplorer.config.hideNonEditableFiles = hideNonEditableFiles.isChecked();
        LiteModFileExplorer.config.showFileEditBelowName = showEditDate.isChecked();
        LiteModFileExplorer.config.showFileSizeBelowName = showFileSize.isChecked();
        LiteModFileExplorer.config.showOpenDirectory = showOpenDirectory.isChecked();
        LiteModFileExplorer.config.setTextFileTypes(textFileTypes.getText());
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
        this.hideNonEditableFiles.mouseDown(mouseX, mouseY, mouseButton);
        this.showEditDate.mouseDown(mouseX, mouseY, mouseButton);
        this.showFileSize.mouseDown(mouseX, mouseY, mouseButton);
        this.showOpenDirectory.mouseDown(mouseX, mouseY, mouseButton);
        this.textFileTypes.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode)
    {
        this.startPositionField.handleKeyTyped(keyCode, keyChar);
        this.backupPositionField.handleKeyTyped(keyCode, keyChar);
        this.textFileTypes.handleKeyTyped(keyCode, keyChar);
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
