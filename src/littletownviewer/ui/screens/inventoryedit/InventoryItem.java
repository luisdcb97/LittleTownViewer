package littletownviewer.ui.screens.inventoryedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Color;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LoadingButton;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.logging.Logger;

public class InventoryItem extends Button implements Cloneable{
    private static final Logger errorLog = Logger.getLogger(
            InventoryItem.class.getName());

    private static final String BORDER_COLOR = "#7E5640";
    private static final String ITEM_BG_COLOR = "#D3AB7A";
    private static final String TEXT_BORDER_COLOR = "#C26D36";
    private static final String TEXT_BG_COLOR = "#F2B55A";
    private static final String TEXT_COLOR = "#7A5748";

    private static final int CARD_BORDER_SIZE = 4;
    private static final int TEXT_BORDER_SIZE = 4;
    private static final int TEXT_PANE_PERCENTAGE = 30;


    private static String textureFolder;
    private static int cardWidth;
    private static int cardHeight;

    private final int id;
    private final String label;

    private boolean itemImageLoading;
    private final String texturePath;
    private PImage itemImage;
    private int quantity;

    public static void setTextureFolder(String textureFolder) {
        InventoryItem.textureFolder = textureFolder;
    }

    public static void setDimensions(int width, int height) {
        InventoryItem.cardWidth = width;
        InventoryItem.cardHeight = height;
    }

    public static int[] getDimensions() {
        return new int[]{InventoryItem.cardWidth, InventoryItem.cardHeight};
    }

    public InventoryItem(@NotNull LittleTownViewer window, int id
            , String label, String category, int quantity) {
        this.id = id;
        this.window = window;
        this.label = label;
        this.quantity = quantity;
        this.itemImage = null;
        this.x = 0;
        this.y = 0;
        this.width = InventoryItem.cardWidth;
        this.height = InventoryItem.cardHeight;

        this.texturePath = textureFolder + "/Items/" +
                category + "/" +
                label + "_Item.png";
    }

    //region Mouse
    @Override
    public boolean isMouseOver() {
        return this.isMouseOver(0, 0);
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        return this.isMouseOverCorner(mouseX, mouseY);
    }

    @Override
    public void mouseClicked() {

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
    //endregion

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        int iconSize = 64;
        window.rectMode(window.CORNER);
        window.fill(Color.HexToInt(InventoryItem.BORDER_COLOR));
        window.rect(x, y, cardWidth, cardHeight);

        window.fill(Color.HexToInt(InventoryItem.ITEM_BG_COLOR));
        window.rect(x + InventoryItem.CARD_BORDER_SIZE,
                y + InventoryItem.CARD_BORDER_SIZE,
                cardWidth - 2 * InventoryItem.CARD_BORDER_SIZE,
                cardHeight - 2 * InventoryItem.CARD_BORDER_SIZE);

        this.drawItemIcon(x + cardWidth / 2 - iconSize / 2
                , y + iconSize / 4, iconSize);

        int textPaneSize = (InventoryItem.cardHeight * TEXT_PANE_PERCENTAGE) /
                100;
        int textPaneStart = y + (InventoryItem.cardHeight - textPaneSize
                - InventoryItem.CARD_BORDER_SIZE);

        window.fill(Color.HexToInt(InventoryItem.TEXT_BORDER_COLOR));
        window.rect(x + InventoryItem.CARD_BORDER_SIZE,
                textPaneStart,
                cardWidth - 2 * InventoryItem.CARD_BORDER_SIZE,
                textPaneSize);

        window.fill(Color.HexToInt(InventoryItem.TEXT_BG_COLOR));
        window.rect(x + InventoryItem.CARD_BORDER_SIZE,
                textPaneStart + InventoryItem.TEXT_BORDER_SIZE,
                cardWidth - 2 * InventoryItem.CARD_BORDER_SIZE,
                textPaneSize - 2 * InventoryItem.TEXT_BORDER_SIZE);

        window.fill(Color.HexToInt(InventoryItem.TEXT_COLOR));
        window.textAlign(window.CENTER, window.CENTER);
        window.textSize(18);
        window.text(this.quantity, x + cardWidth * 2 / 3.0f,
                textPaneStart + textPaneSize / 2.0f);
    }

    private void drawItemIcon(int x, int y, int size){
        if (this.itemImage == null) {
            this.itemImage = this.window.requestImage(this.texturePath);
            this.itemImageLoading = true;
        }

        if (this.itemImageLoading) {
            if(this.itemImage.width == 0){
                LoadingButton.getButton(this.window, size)
                        .draw(x, y);
            }
            else {
                this.itemImageLoading = false;
                if(this.itemImage.width == -1){
                    errorLog.warning(
                            String.format(
                                    "Texture file '%s' for item '%s' contains bad image data\n",
                                    this.texturePath, this.label)
                    );
                    this.itemImage = this.generateItemImage(
                            size, String.format("I%d", this.id));
                }
            }
        } else {
            window.image(this.itemImage, x, y, size, size);
        }
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

    public void addQuantity(int amount){
        this.quantity += amount;
        this.quantity = LittleTownViewer.max(0, this.quantity);
        this.quantity = LittleTownViewer.min(999, this.quantity);
    }

    public int id(){
        return this.id;
    }

    public int quantity(){
        return this.quantity;
    }

    public String label(){
        return this.label;
    }

    @Override
    public InventoryItem clone() throws CloneNotSupportedException {
        return (InventoryItem) super.clone();
    }
}
