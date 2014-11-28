package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.*;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiScreenExamplePage extends GuiScreen
{
    private String buttonID = "button";
    private String numberID = "number_field";
    private String sliderID = "slider";
    private String tFieldID = "text_field";
    private String checkBID = "check_box";

    public GuiScreenExamplePage(net.minecraft.client.gui.GuiScreen currentScreen)
    {
        super("Example Page", currentScreen);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.addGuiElement(buttonID, new GuiSimpleButton(10, 10, 40, 40, "Button"));
        this.addGuiElement(numberID, new GuiNumberField(60, 10, 20, GuiNumberField.Type.PERCENTAGE, mc.fontRendererObj));
        this.addGuiElement(sliderID, new GuiSlider(60, 40, 120, 30, GuiSlider.Type.PERCENTAGE, 50));
        this.addGuiElement(tFieldID, new GuiEditableTextField(60 + 75 + 10, 10, 240, 20, "This is Edible!", mc.fontRendererObj));
        this.addGuiElement(checkBID, new GuiCheckbox(10, 10 + 40 + 10, 10, 10, false));

        this.setBarHeight(50);

        this.setSubtitle("Play around with some cool Gui features!");
    }

    /*
    GuiSimpleButton button;
    GuiNumberField numberField;
    GuiSlider slider;
    GuiEditableTextField field;
    GuiCheckbox checkbox;

    @Override
    public void initGui()
    {
        button = new GuiSimpleButton(10, 10, 40, 40, "Button");
        numberField = new GuiNumberField(60, 10, 20, GuiNumberField.Type.PERCENTAGE, Minecraft.getMinecraft().fontRendererObj);
        slider = new GuiSlider(60, 40, 120, 30, GuiSlider.Type.PERCENTAGE, 50);
        field = new GuiEditableTextField(60 + 75 + 10, 10, 240, 20, "This is Edible!", Minecraft.getMinecraft().fontRendererObj);
        checkbox = new GuiCheckbox(10, 10 + 40 + 10, 10, 10, false);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        button.update();
        numberField.update();
        slider.update();
        field.update();
        checkbox.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Minecraft minecraft = Minecraft.getMinecraft();

        button.draw(minecraft, mouseX, mouseY);
        numberField.draw(minecraft, mouseX, mouseY);
        slider.draw(minecraft, mouseX, mouseY);
        field.draw(minecraft, mouseX, mouseY);
        checkbox.draw(minecraft, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        button.mouseDown(mouseX, mouseY, mouseButton);
        numberField.mouseDown(mouseX, mouseY, mouseButton);
        slider.mouseDown(mouseX, mouseY, mouseButton);
        field.mouseDown(mouseX, mouseY, mouseButton);
        checkbox.mouseDown(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        slider.mouseUp(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick)
    {
        super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);

        slider.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        this.field.keyPressed(keyCode, typedChar);
    }
*/
}
