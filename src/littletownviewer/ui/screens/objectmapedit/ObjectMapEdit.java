package littletownviewer.ui.screens.objectmapedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.menu.SaveEditMenu;
import littletownviewer.ui.screens.objectmapedit.sidebar.ObjectMapSidebar;
import littletownviewer.ui.screens.objectmapedit.sidebar.ObjectMapSidebarButton;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ObjectMapEdit extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            ObjectMapEdit.class.getName());
    public static String tileDictionaryFilename =
            "Resources/ObjectTileList.json";

    private ObjectMapGrid mapGrid;
    private ObjectMapSidebar sidebar;
    private ObjectMapTileDictionary tileDictionary;
    private ObjectMapTile selectedTile;
    private final SaveFileManager saveFile;

    private boolean highlightTile;

    private List<Button> buttons;

    public ObjectMapEdit(@NotNull LittleTownViewer window, SaveFileManager saveFile) {
        this.setWindow(window);
        sidebar = null;
        tileDictionary = null;
        selectedTile = null;
        mapGrid = null;
        this.saveFile = saveFile;
        highlightTile = false;

        //region Buttons
        buttons = new ArrayList<>();
        ObjectMapSidebarButton.Builder backBuilder =
                new ObjectMapSidebarButton.Builder(this.window,
                        this.window.getGraphics());
        backBuilder.withTextSize(20)
                .withDimensions(100, 50)
                .withBorderSize(2)
                .withMessage("< Back")
                .onClick(() -> {
                    this.window.moveToScreen(
                            new LoadingScreen(this.window,
                                    new SaveEditMenu(this.window, this.saveFile)
                                    , String.format("Return to savefile %d"
                                        , this.saveFile.getSaveNumber()))
                    );
                });
        ObjectMapSidebarButton backButton = backBuilder.build();
        buttons.add(backButton);

        ObjectMapSidebarButton.Builder saveBuilder =
                new ObjectMapSidebarButton.Builder(this.window,
                        this.window.getGraphics());
        saveBuilder.withTextSize(20)
                .withDimensions(100, 50)
                .withBorderSize(2)
                .withMessage("Save")
                .onClick(() -> {
                    this.saveFile.setString("mapObjectString", mapGrid.toString());
                    this.saveFile.saveGameData();
                    errorLog.info("Map saved");
                });
        ObjectMapSidebarButton saveButton = saveBuilder.build();
        buttons.add(saveButton);

        //endregion
    }


    //region Drawable
    @Override
    public void setup() {
        ObjectMapTile.setTextureFolder("Resources/Textures");
        tileDictionary = new ObjectMapTileDictionary(this.window,
                ObjectMapEdit.tileDictionaryFilename);
        this.sidebar = new ObjectMapSidebar(this.window, this.tileDictionary);
        String mapString = saveFile.getString("mapObjectString");
        this.mapGrid = new ObjectMapGrid(this.window, mapString, tileDictionary);
        mapGrid.setup();
        sidebar.setup();
        mapGrid.move(this.sidebar.getWidth() + this.mapGrid.getTileSize(),
                this.mapGrid.getTileSize());
        for (Button button: this.buttons) {
            button.setup();
        }
    }

    @Override
    public void draw() {
        window.background(50);
        if(this.highlightTile){
            mapGrid.draw(0, 0, this.selectedTile);
        }
        else{
            mapGrid.draw(0, 0, null);
        }
        window.noTint();
        sidebar.draw();
        int[] pad = this.sidebar.padding();
        int tileEnd = this.sidebar.tileEndHeight();
        window.rectMode(window.CORNER);
        window.fill(110, 20, 20);
        window.rect(pad[0], tileEnd + pad[1]
                , this.sidebar.getWidth() - 2 * pad[0], 5);
        this.selectedTile = sidebar.getSelectedTile();
        int tileSize = 48;
        if(this.selectedTile != null){
            this.mapGrid.setSelectedTile(this.selectedTile);
            int labelY = tileEnd + 5 + 2 * pad[1];
            selectedTile.draw(pad[0], labelY
                    , tileSize, false);
            int space = 5;
            int labelLength = this.sidebar.getWidth() - 2 * pad[0] - tileSize - space;
            window.fill(64, 87, 116);
            window.rect(pad[0] + tileSize + space, labelY, labelLength, tileSize);
            int border = 3;
            window.fill(78, 146, 191);
            window.rect(pad[0] + tileSize + space + border, labelY + border
                    , labelLength - 2 * border, tileSize - 2 * border);
            window.fill(95, 191, 217);
            window.rect(pad[0] + tileSize + space + border, labelY + border
                    , labelLength - 2 * border
                    , (tileSize - 2 * border) / 2.0f);
            int textX = pad[0] + tileSize + space + 2 * border;
            window.textAlign(window.LEFT, window.CENTER);
            window.fill(50, 73, 102);
            window.textSize(20);
            window.text(
                    String.format("%dx%d - %s", this.selectedTile.width()
                            , this.selectedTile.height()
                            , this.selectedTile.label())
                        , textX, labelY + (tileSize / 2.0f) - border
            );
        }

        int xPos = pad[0];
        int yPos = tileEnd + 5 + 3 * pad[1] + tileSize;
        for (int i = 0; i < buttons.size(); i++) {
            Button button = this.buttons.get(i);
            button.setPosition(
                    xPos
                    , yPos
            );
            button.draw();
            if(i % 2 == 1){
                xPos = pad[0];
                yPos += button.getHeight() + pad[1];
            }
            else {
                xPos += button.getWidth() + pad[0];
            }
        }
    }
    //endregion

    //region KeyboardInput
    @Override
    public void keyTyped() {}

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed() {}

    @Override
    public void keyPressed(KeyEvent event) {
        int offset = this.mapGrid.getTileSize() * 3;
        if (event.getKey() == 'w'){
            this.mapGrid.move(0, offset);
        }
        else if (event.getKey() == 'a'){
            this.mapGrid.move(offset, 0);
        }
        else if (event.getKey() == 's'){
            this.mapGrid.move(0, -offset);
        }
        else if (event.getKey() == 'd'){
            this.mapGrid.move(-offset, 0);
        }

        if(event.getKey() == 'h'){
            this.highlightTile = !this.highlightTile;
        }
    }

    @Override
    public void keyReleased() {}

    @Override
    public void keyReleased(KeyEvent event) {}
    //endregion

    //region MouseClick
    @Override
    public void mouseClicked() {
        this.mouseClicked(null);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        sidebar.mouseClicked(event);
    }

    @Override
    public void mousePressed() {
        this.mousePressed(null);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (this.window.mouseX <= this.sidebar.getWidth()) {
            sidebar.mousePressed(event);
            for (Button button: this.buttons) {
                if (button.isMouseOver(window.mouseX, window.mouseY)) {
                    button.mousePressed();
                }
            }
        }
        else{
            this.mapGrid.mousePressed(event);
        }
    }

    @Override
    public void mouseReleased() {
        this.mouseReleased(null);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        sidebar.mouseReleased(event);
    }
    //endregion

    //region MouseDrag
    @Override
    public void MouseDragged() {

    }

    @Override
    public void MouseDragged(MouseEvent event) {

    }
    //endregion

    //region MouseScroll
    @Override
    public void MouseWheel() {
        this.MouseWheel(null);
    }

    @Override
    public void MouseWheel(MouseEvent event) {
        if(this.window.mouseX <= this.sidebar.getWidth()) {
            this.sidebar.MouseWheel(event);
        }
        else{
            this.mapGrid.MouseWheel(event);
        }
    }
    //endregion
}
