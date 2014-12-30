package dk.mrspring.fileexplorer.gui.screen;

import dk.mrspring.fileexplorer.gui.*;
import dk.mrspring.fileexplorer.gui.helper.Color;
import dk.mrspring.fileexplorer.gui.helper.DrawingHelper;
import dk.mrspring.fileexplorer.loader.FileLoader;

import java.io.File;

/**
 * Created by MrSpring on 09-11-2014 for In-Game File Explorer.
 */
public class GuiScreenFileExplorer extends GuiScreen
{
    String startFrom;
    String openFileType = "";
    boolean initialized = false;

    public GuiScreenFileExplorer(net.minecraft.client.gui.GuiScreen previousScreen, File path)
    {
        super("File Explorer", previousScreen);
        startFrom = path.getPath();
    }

    @Override
    public void initGui()
    {
        if (!initialized)
        {
            super.initGui();

            this.addGuiElement("explorer", new GuiFileExplorer(5, 5, 250, height - 10, startFrom).setShowBackground(false).setOnFileOpened(new GuiFileExplorer.IOnFileOpened()
            {
                @Override
                public void onOpened(File file)
                {
                    System.out.println("Opening file!");
                    GuiScreenFileExplorer.this.openFile(file);
                }
            })/*.setPathEditorPosition(8, height - getBarHeight() - 22)*/);

//        this.setSubtitle("Explore your files on your local hard-drive!");
            this.hideBars();
            this.hideTitle();
            initialized = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        DrawingHelper.drawQuad(0, 0, width, height, Color.BLACK, 0.5F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void openFile(File file)
    {
        System.out.println("Opening file!");
        int lastDot = file.getPath().lastIndexOf(".");
        String extension = file.getPath().substring(lastDot);
        GuiFile.EnumFileType fileType = GuiFile.EnumFileType.getFileTypeFor(extension);

        if (!this.openFileType.equals(""))
            this.removeElement(this.openFileType);

        switch (fileType)
        {
            case TEXT_FILE:
            {
                String fileContents = FileLoader.readFile(file);
                this.openFileType = "text_editor";
                this.addGuiElement(this.openFileType, new GuiMultiLineTextField(260, 10, fileContents));
                break;
            }
            case IMAGE:
            {
                this.openFileType = "image_viewer";
                this.addGuiElement(this.openFileType, new GuiImageViewer(file.getPath(), 258, 10, width - 243 - 25, height - 20).enableFullscreenButton());
//                this.mc.displayGuiScreen(new GuiScreenImageViewer("image_viewer", this, file.getPath()));
                break;
            }
            case JSON:
            {
                this.openFileType = "json_editor";
                this.addGuiElement(this.openFileType, new GuiJsonViewer(258, 10, width - 243 - 25, height - 20, file));
                break;
            }
            default:
                break;
        }
    }

    @Override
    public boolean updateElement(String identifier, IGui gui)
    {
        if (identifier.equals("explorer"))
            ((GuiFileExplorer) gui).setHeight(height - 10);
        else if (identifier.equals("image_viewer"))
            ((GuiImageViewer) gui).setWidth(width - 243 - 25);
        else if (identifier.equals("json_editor"))
        {
            ((GuiJsonViewer) gui).setWidth(width - 243 - 25);
            ((GuiJsonViewer) gui).setHeight(height - 10);
        }
        return true;
    }

    /*
    GuiFileExplorer explorer;
    //    GuiSimpleButton reload;
    GuiMultiLineTextField textEditor;
    GuiImageViewer imageViewer;
    GuiSimpleButton saveOpenFile;
    GuiSimpleButton closeOpenFile;
    GuiEditableTextField locationChanger;

    boolean createNewFileExplorer = false;
    String location = "C:\\Users\\Konrad\\OneDrive\\YouTube";
    String openFile = location;
    int changerX = 10, changerXTarget;

    @Override
    public void initGui()
    {
        super.initGui();

        locationChanger = new GuiEditableTextField(10, 10, 240, 20, location, mc.fontRendererObj);
        locationChanger.setOnSave(new Runnable()
        {
            @Override
            public void run()
            {
                GuiScreenFileExplorer.this.openFile(new GuiFile(0, 0, 0, 0, new File(GuiScreenFileExplorer.this.locationChanger.getValue())));
            }
        });

        explorer = new GuiFileExplorer(10, 40, 240, this.height - 40, location);
        //reload = new GuiSimpleButton(200, height - 30, 50, 20, "Refresh");

        saveOpenFile = new GuiSimpleButton(320, 40, 40, 20, "Save");
        closeOpenFile = new GuiSimpleButton(270, 40, 40, 20, "Close");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        explorer.draw(mc, mouseX, mouseY);
        locationChanger.draw(mc, mouseX, mouseY);
//        reload.draw(mc, mouseX, mouseY);

        if (textEditor != null)
        {
            textEditor.draw(mc, mouseX, mouseY);
            saveOpenFile.draw(mc, mouseX, mouseY);
            closeOpenFile.draw(mc, mouseX, mouseY);
            DrawingHelper.drawRect(260, 40, 1, height - 50, Color.WHITE, 1F);
        } else if (imageViewer != null)
        {
            closeOpenFile.draw(mc, mouseX, mouseY);
            imageViewer.draw(mc, mouseX, mouseY);
            DrawingHelper.drawRect(260, 40, 1, height - 50, Color.WHITE, 1F);
        }
    }

    @Override
    public void updateScreen()
    {
        explorer.update();

        if (explorer.getScrollHeight() > 0)
            changerXTarget = 175 + 25;
        else changerXTarget = 10;

        if (changerX < changerXTarget)
            changerX += 19;
        else if (changerX > changerXTarget)
            changerX -= 19;

        System.out.println("Scroll height: " + explorer.getScrollHeight() + ", changerX: " + changerX + ", target: " + changerXTarget);

        locationChanger.setX(changerX);

        locationChanger.update();
        //reload.update();
        if (textEditor != null)
        {
            textEditor.update();

            saveOpenFile.update();
            closeOpenFile.update();
        } else if (imageViewer != null)
        {
            imageViewer.setWidth((width - 10) - 270);
            imageViewer.setHeight(height - 80);
//            this.imageViewer = new GuiImageViewer(path, 270, 40, (width-10)-270, height-50);

            imageViewer.update();

            closeOpenFile.update();
        }

        if (createNewFileExplorer)
        {
            createNewFileExplorer = false;
            this.explorer = new GuiFileExplorer(10, 40, 240, this.height - 80, this.location);
            this.locationChanger.setValue(this.location);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        if (locationChanger.isEditing())
            locationChanger.keyPressed(keyCode, typedChar);
        else if (textEditor != null)
            textEditor.handleKeystroke(keyCode, typedChar);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        explorer.mouseDown(mouseX, mouseY, mouseButton);
        //if (reload.mouseDown(mouseX, mouseY, mouseButton))
//            explorer = new GuiFileExplorer("C:\\Users\\Konrad\\Documents\\Minecraftig");
            //createNewFileExplorer = true;
        */
/*else*//*
 if (textEditor != null && saveOpenFile.mouseDown(mouseX, mouseY, mouseButton))
            explorer.saveTextFile(textEditor.getText(), openFile);
        else if (closeOpenFile.mouseDown(mouseX, mouseY, mouseButton))
            closeOpenFile();
        else if (locationChanger.mouseDown(mouseX, mouseY, mouseButton))
        {
        }
    }

    public boolean openFile(GuiFile file)
    {
        if (file.isDirectory())
            scheduleFileReload(file.getPath());
        else if (file.isTextFile())
            openTextFile(file.getPath());
        else if (file.isImage())
            openImageFile(file.getPath());
        else
            return false;
        return true;
    }

    public void closeOpenFile()
    {
        this.imageViewer = null;
        this.textEditor = null;
    }

    public void scheduleFileReload(String newPath)
    {
        this.createNewFileExplorer = true;
        this.location = newPath;
    }

    public void openTextFile(String path)
    {
        openFile = path;
        this.textEditor = new GuiMultiLineTextField(270, 40, FileLoader.loadFile(openFile));
    }

    public void openImageFile(String path)
    {
        openFile = path;
        this.imageViewer = new GuiImageViewer(path, 270, 70, (width - 10) - 270, height - 70);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.explorer.handleMouseInput();
    }

    public class GuiFileExplorer implements IGui
    {
        int x = 0, y = 0, w, h;
        int scrollHeight = 0, selected = -1;
        String currentPath;
        List<GuiFile> guis = new ArrayList<GuiFile>();
        GuiSimpleButton back;
        GuiSimpleButton reload;
        long timeAtLastClick = 0;

        public GuiFileExplorer(int xPos, int yPos, int width, int height, String path)
        {
            this.x = xPos;
            this.y = yPos;
            this.w = width;
            this.h = height;
            System.out.println(h);
            this.currentPath = path;
            back = new GuiSimpleButton(x + w - 50, y, 50, 20, "Go Up");
            reload = new GuiSimpleButton(x + w - 50, y + h - 20, 50, 20, "Reload");
            List<File> files = new ArrayList<File>();
            FileLoader.addFiles(currentPath, files, false);
            int fileY = y;
            for (File file : files)
            {
                if (file != null)
                {
                    guis.add(new GuiFile(x, fileY, w - 70, 25, file));
                    fileY += 30;
                }
            }
        }

        public int getScrollHeight()
        {
            return scrollHeight;
        }

        public void saveTextFile(String text, String path)
        {
            try
            {
                FileWriter writer = new FileWriter(path);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
//                bufferedWriter.write(text);
                String[] lines = text.split("\n");
                for (String line : lines)
                {
                    System.out.println("Writing line: " + line + " to file: " + path);
                    bufferedWriter.append(line).append(System.getProperty("line.separator"));
                }
                bufferedWriter.close();
                writer.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void draw(Minecraft minecraft, int mouseX, int mouseY)
        {
            boolean drawScrollbar = this.getMaxOnScreenFiles() < guis.size();

            int fileY = y;
            for (int i = 0; i < guis.size(); i++)
            {
                GuiFile gui = guis.get(i);
                gui.setY(fileY - scrollHeight);
                if (i == selected)
                    gui.setHightlighted();
                else gui.stopBeingHighlighted();
                gui.draw(minecraft, mouseX, mouseY);
                fileY += 30;
            }

            if (drawScrollbar)
                DrawingHelper.drawRect(x + 177.5F, y + getScrollbarY(), 5, 40, Color.WHITE, 1F);

            back.draw(minecraft, mouseX, mouseY);

//        DrawingHelper.drawOutline(x + 10, y + 10 + 22, size, size, Color.WHITE, 1F);
//        DrawingHelper.drawTextFileIcon(x + 11, y + 11+22, size-2, size-2, Color.WHITE, 1F);
//
//        minecraft.fontRendererObj.drawStringWithShadow("Folder",x+10+size+3,y+10+7,0xFFFFFF);
        }

        private int getMaxOnScreenFiles()
        {
            int result = (h - 10) / 30;
            if (result > guis.size())
                return guis.size();
            else return result;
        }

        private float getScrollbarY()
        {
            int maxScrollHeight = (this.guis.size() - getMaxOnScreenFiles()) * 30;
            float scrollProgress = 0;
            if (scrollHeight > 0)
                scrollProgress = ((float) scrollHeight) / ((float) maxScrollHeight);
            float maxYPosition = y + h - 50;

//            System.out.println("Max Scroll Height: " + maxScrollHeight + ", Scroll progress: " + scrollProgress + ", Mac Y Position: " + maxYPosition+", returning: "+maxYPosition*scrollProgress);

            return maxYPosition * scrollProgress;
//            float range = h - (((h - 10) / 100) * 30) - 10;
//            float fileHeights = (this.guis.size() - getMaxOnScreenFiles()) * 30;
//            float decimal = ((float) this.scrollHeight) / fileHeights;
//            return (decimal * range) + 5;
        }

        @Override
        public void update()
        {
            for (GuiFile gui : guis)
            {
//                if (gui.isHighlighted() && !GuiScreenFileExplorer.this.openFile.equalsIgnoreCase(gui.getPath()))
//                    gui.stopBeingHighlighted();

                gui.update();
            }

            back.update();
        }

        @Override
        public boolean mouseDown(int mouseX, int mouseY, int mouseButton)
        {
            for (int i = 0; i < guis.size(); i++)
            {
                GuiFile gui = guis.get(i);
                if (gui.mouseDown(mouseX, mouseY, mouseButton))
                {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - timeAtLastClick < 500 && i == selected)
                        GuiScreenFileExplorer.this.openFile(gui);

                    this.selected = i;
                    timeAtLastClick = currentTime;
//                    if (GuiScreenFileExplorer.this.openFile(gui))
//                        gui.setHightlighted();
                    return true;
                }
            }
            this.selected = -1;

            if (back.mouseDown(mouseX, mouseY, mouseButton))
            {
                int lastSlash = currentPath.lastIndexOf("\\");
                String toRemove = currentPath.substring(lastSlash);
                String newPath = currentPath.replace(toRemove, "");
                GuiScreenFileExplorer.this.scheduleFileReload(newPath);
                return true;
            }
            return false;
        }

        public void handleMouseInput()
        {
            int mouseWheel = Mouse.getDWheel();

            int height = (-mouseWheel / 4) + scrollHeight;

            if (height < 0)
                height = 0;
            else if (height > (this.guis.size() - getMaxOnScreenFiles()) * 30)
                height = (this.guis.size() - getMaxOnScreenFiles()) * 30;
            scrollHeight = height;
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
*/

}
