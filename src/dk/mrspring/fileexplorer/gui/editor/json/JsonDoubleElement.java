package dk.mrspring.fileexplorer.gui.editor.json;

import dk.mrspring.fileexplorer.gui.GuiCustomNumberField;
import dk.mrspring.fileexplorer.gui.GuiCustomTextField;
import net.minecraft.client.Minecraft;

import java.math.BigDecimal;

/**
 * Created by MrSpring on 03-01-2015 for In-Game File Explorer.
 */
public class JsonDoubleElement extends JsonEditorElement<Double>
{
    GuiCustomNumberField numberField, decimalField;
    GuiCustomTextField nameField;
    boolean canEditName;

    public JsonDoubleElement(int x, int y, int maxWidth, String name, Double object, boolean canEditName)
    {
        super(x, y, maxWidth, name, object);

        this.canEditName = canEditName;

        int width = maxWidth;
        if (width > 400)
            width = 400;

        BigDecimal bd = new BigDecimal((Double) object);

        int number = bd.intValue();
        int decimal = bd.remainder(BigDecimal.ONE).intValue();

        this.nameField = new GuiCustomTextField(x, y, width / 3 - 2, 16, name);

        this.numberField = new GuiCustomNumberField(x, y, width / 3 - 3, 16, number);
        this.decimalField = new GuiCustomNumberField(x, y, width / 3 - 3, 16, decimal);
    }

    public JsonDoubleElement(int x, int y, int maxWidth, String name, Double object)
    {
        this(x, y, maxWidth, name, object, true);
    }

    @Override
    public int getHeight()
    {
        return 16;
    }

    @Override
    public void drawElement(int xPosition, int yPosition, int maxWidth, int mouseX, int mouseY, Minecraft minecraft)
    {
        int width = maxWidth;
        if (width > 400)
            width = 400;

        int nameWidth = (width / 3) - 2;

        if (this.canEditName)
        {
            this.nameField.setX(xPosition);
            this.nameField.setY(yPosition);
            this.nameField.setW(nameWidth);
            this.nameField.draw(minecraft, mouseX, mouseY);
        } else
        {
            nameWidth = minecraft.fontRendererObj.getStringWidth(this.getName());
            minecraft.fontRendererObj.drawString(this.getName(), xPosition, yPosition + 3, 0xFFFFFF, true);
        }

        this.numberField.setX(xPosition + nameWidth);
        this.numberField.setY(yPosition);
        this.numberField.setW((width - nameWidth) / 2 - 1);
        this.decimalField.setX(xPosition + nameWidth + ((width - nameWidth) / 2 + 1));
        this.decimalField.setY(yPosition);
        this.decimalField.setW((width - nameWidth) / 2 - 1);

        this.numberField.draw(minecraft, mouseX, mouseY);
        minecraft.fontRendererObj.drawString(".", xPosition + nameWidth + this.numberField.getW(), yPosition, 0xFFFFFF, true);
        this.decimalField.draw(minecraft, mouseX, mouseY);
    }

    @Override
    public boolean mouseDown(int relativeMouseX, int relativeMouseY, int mouseButton)
    {
        boolean returns = false;

        if (this.nameField.mouseDown(relativeMouseX, relativeMouseY, mouseButton) && this.canEditName)
            returns = true;
        if (this.numberField.mouseDown(relativeMouseX, relativeMouseY, mouseButton))
            returns = true;
        if (this.decimalField.mouseDown(relativeMouseX, relativeMouseY, mouseButton))
            returns = true;

        return returns;
    }

    @Override
    public Object getValue()
    {
        int number = this.numberField.getValue();
        int decimal = this.decimalField.getValue();

        System.out.println("number = " + number);
        System.out.println("decimal = " + decimal);

        String doubleString = number + "." + decimal;

        System.out.println("doubleString = " + doubleString);

        return Double.parseDouble(doubleString);
    }

    @Override
    public String getName()
    {
        return this.nameField.getText();
    }

    @Override
    public void setName(String name)
    {
        this.nameField.setText(name);
    }

    private double getValueAsDuble()
    {
        return (Double) this.getValue();
    }

    @Override
    public void updateElement()
    {
        System.out.println("this.getValueAsDuble() = " + this.getValueAsDuble());

        if (this.canEditName) this.nameField.update();
        this.numberField.update();
        this.decimalField.update();
    }

    @Override
    public void handleKeyTypes(char character, int keyCode)
    {
        if (this.canEditName) this.nameField.handleKeyTyped(keyCode, character);
        this.numberField.handleKeyTyped(keyCode, character);
        this.decimalField.handleKeyTyped(keyCode, character);
    }
}
