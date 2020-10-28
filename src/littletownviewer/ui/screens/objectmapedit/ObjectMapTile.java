package littletownviewer.ui.screens.objectmapedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Tile;
import littletownviewer.ui.buttons.LoadingButton;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.logging.Logger;

public final class ObjectMapTile extends Tile {
    private static final Logger errorLog = Logger.getLogger(
            ObjectMapTile.class.getName());
    private static String textureFolder;
    private static int IMAGE_GENERATION_COUNTER;

    private LittleTownViewer window;
    private String itemTexturePath;
    private String objectTexturePath;
    private PImage itemImage;
    private PImage objectImage;
    private boolean itemImageLoading;
    private boolean objectImageLoading;
    private final int tileId;
    private int displayWidth;
    private int displayHeight;


    public ObjectMapTile(@NotNull LittleTownViewer window, String label,
                         String[] categoryList)
    {
        this(window, label, categoryList, 1, 1);
    }

    public ObjectMapTile(@NotNull LittleTownViewer window, String label,
                         String[] categoryList, int width, int height)
    {
        this.setWindow(window);
        this.label = label;
        this.displayWidth = this.width = width;
        this.displayHeight = this.height = height;
        this.tileId = ObjectMapTile.IMAGE_GENERATION_COUNTER++;

        this.itemImage = null;
        this.objectImage = null;

        StringBuilder itemTexture = new StringBuilder(256)
                .append(textureFolder).append("/")
                .append("Objects").append("/")
                .append("Items").append("/")
                ;

        StringBuilder objectTexture = new StringBuilder(256)
                .append(textureFolder).append("/")
                .append("Objects").append("/")
                .append("Tiles").append("/")
                ;

        for (String category: categoryList) {
            itemTexture.append(category).append("/");
            objectTexture.append(category).append("/");
        }

        itemTexture.append(label).append("_Object_Item.png");
        objectTexture.append(label).append("_Object_Tile.png");

        this.itemTexturePath = itemTexture.toString();
        this.objectTexturePath = objectTexture.toString();
    }

    public ObjectMapTile(@NotNull LittleTownViewer window, String label,
                         String[] categoryList, int width, int height,
                         int displayWidth, int displayHeight)
    {
        this(window, label, categoryList, width, height);
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }

    public static void setTextureFolder(String textureFolder) {
        ObjectMapTile.textureFolder = textureFolder;
    }


    @Override
    public void setup() {}

    public void loadItemTexture(){
        if (this.itemImage == null){
            this.itemImage = this.window.loadImage(this.itemTexturePath);
            if (this.itemImage == null){
                errorLog.warning(
                        String.format(
                                "Error loading item texture for tile '%s' from file '%s'\n",
                                this.label, this.itemTexturePath)
                );
            }
            else if (this.itemImage.width == -1
                    || this.itemImage.height == -1)
            {
                this.itemImage = null;
                errorLog.warning(
                        String.format(
                                "Item texture file '%s' for tile '%s' contains bad image data\n",
                                this.itemTexturePath, this.label)
                );
            }
        }
    }

    public void loadTileTexture(){
        if (this.objectImage == null){
            this.objectImage = this.window.loadImage(this.objectTexturePath);
            if (this.objectImage == null){
                errorLog.warning(
                        String.format(
                                "Error loading tile texture for tile '%s' from file '%s'\n",
                                this.label, this.objectTexturePath)
                );
            }
            else if (this.objectImage.width == -1
                    || this.objectImage.height == -1)
            {
                this.objectImage = null;
                errorLog.warning(
                        String.format(
                                "Tile texture file '%s' for tile '%s' contains bad image data\n",
                                this.objectTexturePath, this.label)
                );
            }
        }
    }

    public boolean hasItemTexture(){
        return this.itemImage != null;
    }

    public boolean hasTileTexture(){
        return this.objectImage != null;
    }

    private PGraphics generateItemImage(int tileSize, String id){
        PGraphics display = window.createGraphics(tileSize, tileSize);

        display.beginDraw();
        display.rectMode(PConstants.CORNER);
        display.stroke(0);
        display.strokeWeight(1);
        display.fill(0, 170, 0);
        display.rect(0, 0, tileSize, tileSize);
        display.textAlign(PConstants.CENTER, PConstants.CENTER);
        display.fill(170, 0, 85);
        display.textSize(16);
        display.text(
                id
                , (float) (tileSize / 2)
                , (float) (tileSize / 2)
        );
        display.endDraw();
        return display;
    }

    private PGraphics generateObjectImage(int tileSize, String id){
        PGraphics display = window.createGraphics(
                this.displayWidth * tileSize,
                this.displayHeight * tileSize);

        display.beginDraw();
        display.rectMode(PConstants.CORNER);
        display.stroke(0);
        display.strokeWeight(1);
        display.fill(0, 170, 0);
        display.rect(0, 0,
                tileSize * this.displayWidth,
                tileSize * this.displayHeight);
        display.textAlign(PConstants.CENTER, PConstants.CENTER);
        display.fill(170, 0, 85);
        display.textSize(16);
        display.text(
                id
                , (float) (tileSize * this.displayWidth / 2)
                , (float) (tileSize * this.displayHeight / 2)
        );
        display.endDraw();
        return display;
    }

    @Override
    public void draw() {
        this.draw(0, 0, 24);
    }

    @Override
    public void draw(int x, int y, int tileSize) {
        this.draw(x, y, tileSize, true);
    }

    public void draw(int x, int y, int tileSize, boolean asObject){
        if (asObject) {
            if (this.objectImage == null) {
                this.objectImage = this.window.requestImage(
                        this.objectTexturePath);
                this.objectImageLoading = true;
            }

            if (this.objectImageLoading) {
                if(this.objectImage.width == 0){
                    LoadingButton.getButton(this.window,
                            tileSize * LittleTownViewer.max(this.height, this.width))
                            .draw(x, y);
                }
                else {
                    this.objectImageLoading = false;
                    if(this.objectImage.width == -1){
                        errorLog.warning(
                                String.format(
                                        "Tile texture file '%s' for tile '%s' contains bad image data\n",
                                        this.objectTexturePath, this.label)
                        );
                        this.objectImage = this.generateObjectImage(
                                48, String.format("O%d", this.tileId));
                    }
                }
            } else {
                window.image(this.objectImage, x
                        , y + tileSize * (1 - this.displayHeight)
                        , tileSize * this.displayWidth
                        , tileSize * this.displayHeight
                );
            }
        } else {
            if (this.itemImage == null) {
                this.itemImage = this.window.requestImage(this.itemTexturePath);
                this.itemImageLoading = true;
            }

            if (this.itemImageLoading) {
                if(this.itemImage.width == 0){
                    LoadingButton.getButton(this.window, tileSize)
                            .draw(x, y);
                }
                else {
                    this.itemImageLoading = false;
                    if(this.itemImage.width == -1){
                        errorLog.warning(
                                String.format(
                                        "Item texture file '%s' for tile '%s' contains bad image data\n",
                                        this.itemTexturePath, this.label)
                        );
                        this.itemImage = this.generateItemImage(
                                tileSize, String.format("I%d", this.tileId));
                    }
                }
            } else {
                window.image(this.itemImage, x, y, tileSize, tileSize);
            }
        }
    }

    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed() {

    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    public void setDisplayHeight(int displayHeight) {
        if (displayHeight < 1) {
            throw new IllegalArgumentException();
        }
        this.displayHeight = displayHeight;
    }

    public void setDisplayWidth(int displayWidth) {
        if (displayWidth < 1) {
            throw new IllegalArgumentException();
        }
        this.displayWidth = displayWidth;
    }

    public void setWindow(@NotNull LittleTownViewer window) {
        this.window = window;
    }
}
