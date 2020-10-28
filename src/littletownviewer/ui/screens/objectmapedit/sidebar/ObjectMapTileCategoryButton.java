package littletownviewer.ui.screens.objectmapedit.sidebar;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LoadingButton;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.logging.Logger;

public class ObjectMapTileCategoryButton extends Button {
    private static final Logger errorLog = Logger.getLogger(
            ObjectMapTileCategoryButton.class.getName());
    private static String textureFolder;
    private static int counter = 0;

    private PImage buttonImage;
    private final String category;
    private final String texturePath;
    private boolean imageLoading;
    private int horizontalScroll;
    private int instanceCounter;

    public ObjectMapTileCategoryButton(
            @NotNull LittleTownViewer window,
            int x, int y,
            String category,
            int buttonSize,
            @NotNull Runnable clickAction)
    {
        this(window, x, y, category, buttonSize, clickAction, false);
    }

    public ObjectMapTileCategoryButton(
            @NotNull LittleTownViewer window,
            int x, int y,
            String category,
            int buttonSize,
            @NotNull Runnable clickAction,
            boolean isSubCategory)
    {
        this.setWindow(window);
        this.x = x;
        this.y = y;
        this.clickAction = clickAction;
        this.category = category;
        this.width = this.height = buttonSize;
        this.horizontalScroll = 0;

        StringBuilder imageTexture = new StringBuilder(256)
                .append(textureFolder).append("/")
                .append("Objects").append("/")
                ;
        if(isSubCategory){
            imageTexture.append("SubCategories");
        }
        else {
            imageTexture.append("Categories");
        }
        imageTexture.append("/").append(category).append(".png");
        this.texturePath = imageTexture.toString();
        this.imageLoading = false;
        this.instanceCounter = counter++;
    }

    @Override
    public boolean isMouseOver() {
        return this.isMouseOverCorner(0, 0);
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        return this.isMouseOverCorner(mouseX, mouseY);
    }

    @Override
    public void mouseClicked() {
        this.mouseClicked(null);
    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed() {
        this.mousePressed(null);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.clickAction.run();
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void setup() {
        loadImage();
    }

    @Override
    public void draw() {
        if (this.buttonImage == null){
            this.buttonImage = this.window.requestImage(this.texturePath);
            this.imageLoading = true;
        }
        if (this.imageLoading){
            if(this.buttonImage.width == 0){
                LoadingButton.getButton(this.window, this.width)
                        .draw(this.x - this.horizontalScroll, this.y);
            }
            else {
                this.imageLoading = false;
                if(this.buttonImage.width == -1){
                    errorLog.warning(
                            String.format(
                                    "Error loading texture for category '%s' from file '%s'\n",
                                    this.category, this.texturePath)
                    );
                    this.generateCategoryImage();
                }
            }
        }
        else {
            this.window.image(this.buttonImage,
                    this.x - this.horizontalScroll, this.y,
                    this.width, this.height);
            if(this.isMouseOver(
                    this.window.mouseX - this.horizontalScroll,
                    this.window.mouseY))
            {
                this.drawTooltip();
            }
        }
    }

    private void drawTooltip(){
        window.rectMode(LittleTownViewer.CORNER);
        window.fill(50);
        float length = window.textWidth(this.category) + 6;
        window.rect(this.window.mouseX + 20 - this.horizontalScroll,
                this.window.mouseY - 10,
                length + 6,
                20);
        window.fill(255);
        window.textSize(14);
        window.textAlign(LittleTownViewer.CENTER, LittleTownViewer.CENTER);
        window.text(this.category,
                this.window.mouseX + 23 + length / 2 - this.horizontalScroll,
                this.window.mouseY - 2);
    }

    private void generateCategoryImage(){
        this.display = this.window.createGraphics(
                this.width, this.height);
        display.beginDraw();
        display.background(100);
        display.fill(200);
        display.textAlign(LittleTownViewer.CENTER, LittleTownViewer.CENTER);
        display.textSize(18);
        display.text("?" + String.valueOf(instanceCounter),
                this.width/2.0f, this.height/2.0f);
        display.endDraw();
        this.buttonImage = this.display;
    }

    public void loadImage(){
        if (this.buttonImage == null){
            this.buttonImage = this.window.loadImage(this.texturePath);
            if (this.buttonImage == null){
                errorLog.warning(
                        String.format(
                                "Error loading texture for category '%s' from file '%s'\n",
                                this.category, this.texturePath)
                );
                this.generateCategoryImage();
            }
            else if (this.buttonImage.width == -1
                    || this.buttonImage.height == -1)
            {
                this.buttonImage = null;
                errorLog.warning(
                        String.format(
                                "Texture file '%s' for category '%s' contains bad image data\n",
                                this.texturePath, this.category)
                );
                this.generateCategoryImage();
            }
        }
    }

    public static void setTextureFolder(String textureFolder) {
        ObjectMapTileCategoryButton.textureFolder = textureFolder;
    }

    public String getCategory(){
        return this.category;
    }

    public void setHorizontalScroll(int horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
    }
}
