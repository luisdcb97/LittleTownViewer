package littletownviewer.ui.screens.inventoryedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LWTextButton;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.menu.SaveEditMenu;
import processing.data.JSONArray;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class InventoryEdit extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            InventoryEdit.class.getName());
    public static String itemDictionaryFilename = "Resources/itemList.json";

    private static final int ITEMS_PER_ROW = 7;
    private static final int SIDEBAR_WIDTH = 400;

    private InventoryItemDictionary itemDictionary;
    private String selectedCategory;
    private InventoryItem selectedItem;
    private final SaveFileManager saveFile;

    private final List<Button> buttons;
    private final List<Button> tabButtons;
    private final List<InventoryItem> itemButtons;

    private int tabScrollOffset;
    private int itemScrollOffset;

    public InventoryEdit(@NotNull LittleTownViewer window, SaveFileManager saveFile){
        this.setWindow(window);
        this.saveFile = saveFile;
        this.itemDictionary = null;
        this.selectedItem = null;

        this.tabScrollOffset = 0;
        this.itemScrollOffset = 0;

        buttons = new ArrayList<>();
        tabButtons = new ArrayList<>();
        itemButtons = new ArrayList<>();
    }


    //region Keyboard
    @Override
    public void keyTyped() {

    }

    @Override
    public void keyTyped(KeyEvent event) {

    }

    @Override
    public void keyPressed() {

    }

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void keyReleased() {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }
    //endregion

    //region Mouse
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
        for (Button button: this.buttons) {
            if (button.isMouseOver(window.mouseX, window.mouseY)) {
                button.mousePressed();
            }
        }
        for (Button button: this.tabButtons) {
            if (button.isMouseOver(window.mouseX, window.mouseY)) {
                button.mousePressed();
            }
        }
        for (Button button: this.itemButtons) {
            if (button.isMouseOver(window.mouseX, window.mouseY)) {
                button.mousePressed();
            }
        }
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void MouseDragged() {

    }

    @Override
    public void MouseDragged(MouseEvent event) {

    }

    @Override
    public void MouseWheel() {
        this.MouseWheel(null);
    }

    @Override
    public void MouseWheel(MouseEvent event) {
        if (this.window.mouseX >= SIDEBAR_WIDTH && this.window.mouseY <= 100){
            this.tabScrollOffset += event.getCount() * 10;
            int min = 0;
            int max = 0;
            int bgWidth = window.width - (20 + SIDEBAR_WIDTH);
            int contentWidth = (this.tabButtons.size() - 1) * 10;
            for (Button tab: this.tabButtons) {
                contentWidth += tab.getWidth();
            }
            if (contentWidth > bgWidth){
                max = contentWidth - bgWidth;
            }
            this.tabScrollOffset = LittleTownViewer.constrain(this.tabScrollOffset,
                    min, max);
        }
        else if(this.window.mouseX >= SIDEBAR_WIDTH){
            this.itemScrollOffset += event.getCount() * 10;
            int min = 0;
            int max = 0;
            int paneHeight = window.height - 120;
            int rows = LittleTownViewer.ceil(
                    (float) this.itemButtons.size() / ITEMS_PER_ROW);
            int itemHeight = InventoryItem.getDimensions()[1];
            int contentHeight = rows * itemHeight + (rows - 1) * 10;
            if(contentHeight > paneHeight){
                max = contentHeight - paneHeight;
            }
            this.itemScrollOffset = LittleTownViewer.constrain(this.itemScrollOffset,
                    min, max);
        }
    }
    //endregion

    @Override
    public void setup() {
        InventoryItem.setTextureFolder("Resources/Textures");
        InventoryItem.setDimensions(100, 150);

        this.itemDictionary = new InventoryItemDictionary(window,
                InventoryEdit.itemDictionaryFilename);
        JSONArray inventoryQuantities = saveFile.getJSONArray("inventory");
        this.setItemQuantities(inventoryQuantities);

        this.addTabButtons();
        this.addItemQuantityButtons();
        this.tabButtons.get(0).mousePressed();
        this.itemButtons.get(0).mousePressed();

        this.addActionButtons();

        for (Button button: this.buttons) {
            button.setup();
        }
    }

    @Override
    public void draw() {
        window.background(50);
        this.drawCategories();
        this.drawItems();
        window.fill(120, 0, 0);
        window.rect(0, 0, SIDEBAR_WIDTH, window.height);
        this.selectedItem.draw();
        for (Button but: this.buttons) {
            but.draw();
        }
    }

    private void addTabButtons(){
        this.tabButtons.clear();
        int x = InventoryEdit.SIDEBAR_WIDTH + 10;
        int y = 10;
        int width = 200;
        int height = 80;
        for (String tabName: this.itemDictionary.getCategories()){
            LWTextButton.Builder builder = new LWTextButton.Builder(
                    this.window, this.window.getGraphics());
            builder.positionedAt(x, y)
                    .withTextSize(18)
                    .withDimensions(width, height)
                    .withMessage(tabName)
                    .onClick(() -> {
                        this.selectedCategory = tabName;
                        this.setItemButtons(
                                this.itemDictionary.getCategoryItems(tabName));
                        this.itemButtons.get(0).mousePressed();
                    });
            Button but = builder.build();
            but.setup();
            this.tabButtons.add(but);
            x += but.getWidth() + 10;
        }
    }

    private void setItemButtons(List<InventoryItem> items){
        this.itemButtons.clear();
        this.itemScrollOffset = 0;
        int startX = SIDEBAR_WIDTH + 10;
        int startY = 110;
        int x = startX;
        int y = startY;
        int[] cardDimensions = InventoryItem.getDimensions();
        final int xPos = (int)
                (SIDEBAR_WIDTH / 2.0f - (cardDimensions[0] / 2.0f));
        for (int i = 0; i < items.size(); i++) {
            InventoryItem item = items.get(i);
            if(i % InventoryEdit.ITEMS_PER_ROW == 0 && i > 0){
                x = startX;
                y += cardDimensions[1] + 10;
            }
            item.setPosition(x, y);
            int finalI = i;
            item.setAction(() -> {
                try {
                    this.selectedItem = this.itemButtons.get(finalI)
                            .clone();
                    this.selectedItem.setPosition(xPos, 10);
                    this.selectedItem.setAction(() -> {
                    });
                } catch (CloneNotSupportedException e) {
                    errorLog.warning(
                            String.format(
                                    "Error creating display copy for item '%s'",
                                    this.itemButtons.get(0).label()));
                }
            });
            this.itemButtons.add(item);
            x += cardDimensions[0] + 10;
        }
    }

    private void addItemQuantityButtons(){
        int buttonWidth = 100;
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(buttonWidth, 60)
                .withTextSize(24)
                .useDynamicWidth(false)
                .withBorderSize(3, 4);

        int sidebarHalfWidth = SIDEBAR_WIDTH / 2;
        int cardHalfWidth = InventoryItem.getDimensions()[0] / 2;

        int minusCenter = (sidebarHalfWidth - cardHalfWidth) / 2;
        int xMinus = minusCenter - (buttonWidth / 2);

        int plusCenter = (sidebarHalfWidth - cardHalfWidth) / 2
                + sidebarHalfWidth + cardHalfWidth;
        int xPlus = plusCenter - (buttonWidth / 2);

        LWTextButton sub1Builder = initialBuilder.positionedAt(xMinus, 10)
                .withMessage("-1")
                .onClick(() -> {
                    this.selectedItem.addQuantity(-1);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(-1));
                })
                .build();

        LWTextButton add1Builder = initialBuilder.positionedAt(xPlus, 10)
                .withMessage("+1")
                .onClick(() -> {
                    this.selectedItem.addQuantity(1);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(1));
                })
                .build();

        LWTextButton sub10Builder = initialBuilder.positionedAt(xMinus, 80)
                .withMessage("-10")
                .onClick(() -> {
                    this.selectedItem.addQuantity(-10);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(-10));
                })
                .build();

        LWTextButton add10Builder = initialBuilder.positionedAt(xPlus, 80)
                .withMessage("+10")
                .onClick(() -> {
                    this.selectedItem.addQuantity(10);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(10));
                })
                .build();

        LWTextButton sub100Builder = initialBuilder.positionedAt(xMinus, 150)
                .withMessage("-100")
                .onClick(() -> {
                    this.selectedItem.addQuantity(-100);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(-100));
                })
                .build();

        LWTextButton add100Builder = initialBuilder.positionedAt(xPlus, 150)
                .withMessage("+100")
                .onClick(() -> {
                    this.selectedItem.addQuantity(100);
                    Optional<InventoryItem> origItem = this.itemDictionary
                            .getItemById(this.selectedItem.id());
                    origItem.ifPresent((inventoryItem) ->
                            inventoryItem.addQuantity(100));
                })
                .build();

        this.buttons.add(sub1Builder);
        this.buttons.add(add1Builder);
        this.buttons.add(sub10Builder);
        this.buttons.add(add10Builder);
        this.buttons.add(sub100Builder);
        this.buttons.add(add100Builder);
    }

    private void addActionButtons(){
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(200, 80)
                .withTextSize(24)
                .useDynamicWidth(false)
                .withBorderSize(3, 4);

        int xPos = SIDEBAR_WIDTH / 2 - 100;
        int yPos = 400;

        LWTextButton saveButton = initialBuilder.positionedAt(xPos, yPos)
                .withMessage("Save")
                .onClick(() -> {
                    Map<Integer, Integer> map =
                            this.itemDictionary.getInventoryIdMap();
                    JSONArray inv = this.saveFile.getJSONArray("inventory");
                    for (int i = 0; i < inv.size(); i++) {
                        if (map.containsKey(i)){
                            inv.setInt(i, map.get(i));
                        }
                    }
                    this.saveFile.setJSONArray("inventory", inv);
                    this.saveFile.saveGameData();
                })
                .build();

        yPos += 80 + 20;

        LWTextButton backButton = initialBuilder.positionedAt(xPos, yPos)
                .withMessage("< Back")
                .onClick(() -> {
                    this.window.moveToScreen(
                            new LoadingScreen(this.window,
                                    new SaveEditMenu(this.window, this.saveFile)
                                    , String.format("Return to savefile %d"
                                    , this.saveFile.getSaveNumber()))
                    );
                })
                .build();

        this.buttons.add(saveButton);
        this.buttons.add(backButton);
    }

    private void drawCategories(){
        int contentWidth = (this.tabButtons.size() - 1) * 10;
        int iterX = SIDEBAR_WIDTH + 10;
        for (Button tab: this.tabButtons) {
            tab.setPosition(iterX - this.tabScrollOffset, tab.getY());
            if (tab.getX() >= SIDEBAR_WIDTH + 10) {
                LWTextButton inventoryTab = (LWTextButton) tab;
                if(inventoryTab.getText().equals(selectedCategory)){
                    inventoryTab.drawHighlighted();
                }
                else {
                    tab.draw();
                }
            }
            iterX += tab.getWidth() + 10;
            contentWidth += tab.getWidth();
        }

        int bgWidth = window.width - (20 + SIDEBAR_WIDTH);
        float barLength = (float) bgWidth / contentWidth;

        if(barLength < 1){
            window.rectMode(window.CORNER);
            window.fill(100);
            window.rect(SIDEBAR_WIDTH + 10,95, bgWidth, 5);

            float barStart = (float) this.tabScrollOffset / contentWidth * bgWidth;
            barLength *= bgWidth;
            window.fill(255);
            window.rect(SIDEBAR_WIDTH + 10 + barStart,
                    95, barLength, 5);
        }

    }

    private void drawItems(){
        int itemHeight = InventoryItem.getDimensions()[1];
        int rows = LittleTownViewer.ceil(
                (float) this.itemButtons.size() / ITEMS_PER_ROW);
        int contentHeight = rows * itemHeight + (rows - 1) * 10;
        int iterY = 110;
        int i = 0;
        for (InventoryItem item: this.itemButtons) {
            if(i % InventoryEdit.ITEMS_PER_ROW == 0 && i > 0){
                iterY += itemHeight + 10;
            }
            item.setPosition(item.getX(), iterY - itemScrollOffset);
            if (item.getY() >= 100) {
                item.draw();
            }
            i++;
        }

        int paneHeight = window.height - 120;
        float barLength = (float) paneHeight / contentHeight;

        if(barLength < 1){
            window.rectMode(window.CORNER);
            window.fill(100);
            window.rect(window.width - 10,110, 5, paneHeight);

            float barStart = (float) this.itemScrollOffset / contentHeight *
                    paneHeight;
            barLength *= paneHeight;
            window.fill(255);
            window.rect(window.width - 10, 110 + barStart,
                    5, barLength);
        }
    }

    private void setItemQuantities(JSONArray inventory){
        for (String category: this.itemDictionary.getCategories()) {
            for (InventoryItem item:
                    this.itemDictionary.getCategoryItems(category)) {
                item.addQuantity(inventory.getInt(item.id()));
            }
        }
    }
}
