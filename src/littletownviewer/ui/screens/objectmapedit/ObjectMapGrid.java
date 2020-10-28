package littletownviewer.ui.screens.objectmapedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Grid;
import littletownviewer.ui.Tile;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ObjectMapGrid extends Grid {
    private static final Logger errorLog = Logger.getLogger(
            ObjectMapGrid.class.getName());

    private static class BuildingBox {
        private ObjectMapTile tile;
        private int x;
        private int y;
        private int width;
        private int height;

        public BuildingBox(ObjectMapTile tile, int x, int y,
                           int width, int height) {
            this.tile = tile;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(int x, int y){
            return x >= this.x && x < this.x + width
                    && y <= this.y && y > this.y - height;
        }

        public boolean intersects(BuildingBox building){

            if (this.y <= building.y - building.height
                    || building.y <= this.y - this.height) {
                return false;
            }
            if (this.x + this.width <= building.x
                    || building.x + building.width <= this.x) {
                return false;
            }
            return true;
        }
    }

    private static final int ANIMAL_LIMIT = 30;

    private LittleTownViewer window;
    private String mapString;
    private ObjectMapTileDictionary tileDictionary;
    private List<BuildingBox> buildings;
    private ObjectMapTile[] backgroundTiles;
    private ObjectMapTile selectedTile;

    public ObjectMapGrid(@NotNull LittleTownViewer window, String mapString,
                         ObjectMapTileDictionary tileDictionary){
        this.setWindow(window);
        this.mapString = mapString;
        this.tileDictionary = tileDictionary;
        this.tileSize = 12;
        this.offsetX = 0;
        this.offsetY = 0;
        this.buildings = new ArrayList<>();
        this.backgroundTiles = new ObjectMapTile[2];
        String[] categories = new String[]{"Custom", "Custom"};
        this.backgroundTiles[0] = new ObjectMapTile(this.window,
                "Background", categories);
        this.backgroundTiles[1] = new ObjectMapTile(this.window,
                "Background Alternate", categories);
        this.selectedTile = null;
    }

    @Override
    public void setup() {
        this.gridHeight = 50;
        String[] objectList = mapString.split("/");
        this.gridWidth = objectList.length / this.gridHeight;
        this.grid = new ObjectMapTile[this.gridHeight][this.gridWidth];

        int rowIndex = this.gridHeight - 1;
        int i = 0;
        ObjectMapTile tile;
        Optional<ObjectMapTile> optionalTile;
        for (String tileLabel: objectList) {
            if(tileLabel.equals("0")){
                tileLabel = "Empty";
            }
            optionalTile = tileDictionary.getTileByLabel(tileLabel);
            if(!optionalTile.isPresent()){
                errorLog.warning("Object Map Tile '" + tileLabel + "' does not exist");
                this.window.notifyUser(
                        "There is an unrecognized tile in the map: " + tileLabel);
                this.addCustomTile(tileLabel);
                tile = tileDictionary.getTileByLabel(tileLabel).get();
            }
            else{
                tile = optionalTile.get();
            }
            int j = i % this.gridWidth;
            if(j == 0 && i != 0){
                rowIndex--;
            }

            this.grid[rowIndex][j] = tile;
            if (tile.isMultiTile()){
                BuildingBox building = new BuildingBox(tile,
                        j, rowIndex, tile.width(), tile.height());
                this.buildings.add(building);
            }
            i++;
        }
    }

    @Override
    public void draw(int x, int y){
        draw(x, y, null);
    }

    public void draw(int x, int y, ObjectMapTile selectedTile) {
        window.pushMatrix();
        window.translate(x, y);
        window.translate(this.offsetX, this.offsetY);

        if(selectedTile == null){
            window.noTint();
        }
        else{
            window.tint(100, 255);
        }

        int counter = 0;
        for(int i = 0; i < this.gridHeight; i++) {
            int tileY = i * this.tileSize;
            for (int j = this.gridWidth - 1; j >= 0; j--) {
                int tileX = j * this.tileSize;
                if (counter % 2 == 0){
                    this.backgroundTiles[0].draw(tileX, tileY, this.tileSize);
                }
                else{
                    this.backgroundTiles[1].draw(tileX, tileY, this.tileSize);
                }
                counter++;
            }
            counter++;
        }


        for(int i = 0; i < this.gridHeight; i++){
            int tileY = i * this.tileSize;
            for(int j = this.gridWidth - 1; j >= 0; j--){
                int tileX = j * this.tileSize;
                this.grid[i][j].draw(tileX, tileY, this.tileSize);
            }
        }
        if(selectedTile != null) {
            // Do a second pass to draw selected tiles highlighted and in foreground
            window.noTint();
            for (int i = 0; i < this.gridHeight; i++) {
                int tileY = i * this.tileSize;
                for (int j = this.gridWidth - 1; j >= 0; j--) {
                    int tileX = j * this.tileSize;
                    if (selectedTile == this.grid[i][j]) {
                        this.grid[i][j].draw(tileX, tileY, this.tileSize);
                    }
                }
            }
        }
        window.popMatrix();
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
        int[] posClicked = this.getGridPositionClicked(
                this.window.mouseX, this.window.mouseY);
        if (posClicked[0] < 0 || posClicked[0] >= this.gridWidth
            || posClicked[1] < 0 || posClicked[1] >= this.gridHeight){
            errorLog.warning(String.format(
                    "Cannot place tile in position (%d, %d)"
                    , posClicked[0], posClicked[1]));
            return;
        }

        boolean occupied = false;
        boolean canPlace = false;
        BuildingBox newBuilding = null;
        if (this.selectedTile.isMultiTile()){
            if (posClicked[0] + this.selectedTile.width() - 1 >= this.gridWidth
                || posClicked[1] - this.selectedTile.height() + 1 < 0){
                this.window.notifyUser(String.format(
                        "Not enough space to place building '%s'"
                        , this.selectedTile.label()));
                errorLog.warning(String.format(
                        "Building '%s' with size (%d, %d) cannot be placed in position (%d, %d)"
                        , this.selectedTile.label()
                        , this.selectedTile.width(), this.selectedTile.height()
                        , posClicked[0], posClicked[1]
                        ));
                return;
            }

            newBuilding = new BuildingBox(this.selectedTile
                    , posClicked[0], posClicked[1]
                    , this.selectedTile.width(), this.selectedTile.height());
            for (BuildingBox building: this.buildings) {
                occupied = building.intersects(newBuilding)
                        && (building.x != posClicked[0]
                        || building.y != posClicked[1]);
                if (occupied){
                    this.window.notifyUser(String.format(
                            "New building intersects building '%s'"
                            , building.tile.label()));
                    errorLog.warning(String.format(
                            "New building in position (%d, %d) intersects building '%s'"
                            , posClicked[0], posClicked[1]
                            , building.tile.label()));
                    break;
                }
            }
            ObjectMapTile emptyTile =
                    this.tileDictionary.getTileByLabel("Empty").get();
            for (int i = newBuilding.x; i < newBuilding.x + newBuilding.width; i++) {
                for (int j = newBuilding.y - newBuilding.height + 1; j <= newBuilding.y; j++) {
                    if (i == newBuilding.x && j == newBuilding.y){
                        continue;
                    }
                    if (!this.grid[j][i].equals(emptyTile)){
                        occupied = true;
                        this.window.notifyUser(String.format(
                                "Clear space for building '%s'"
                                , newBuilding.tile.label()));
                        errorLog.warning(String.format(
                                "Clear space for new building '%s' in position (%d, %d)"
                                , newBuilding.tile.label()
                                , posClicked[0], posClicked[1]));
                        break;
                    }
                }
            }
        }
        else{
            for (BuildingBox building: this.buildings) {
                occupied = building.contains(posClicked[0], posClicked[1])
                        && (building.x != posClicked[0]
                        || building.y != posClicked[1]);
                if (occupied){
                    this.window.notifyUser(String.format(
                            "That position is occupied by building '%s'"
                            , building.tile.label()));
                    errorLog.warning(String.format(
                            "Position (%d, %d) is occupied by building '%s'"
                            , posClicked[0], posClicked[1]
                            , building.tile.label()));
                    break;
                }
            }
        }

        canPlace = !occupied;
        if(countAnimals() >= ANIMAL_LIMIT && this.isSelectedTileAnimal()){
            canPlace = false;
            this.window.notifyUser(String.format(
                    "There is a %d limit for animals"
                    , ANIMAL_LIMIT));
        }

        try {
            if (canPlace) {
                if (this.grid[posClicked[1]][posClicked[0]].isMultiTile()) {
                    this.removeBuilding(
                            (ObjectMapTile) this.grid[posClicked[1]][posClicked[0]]);
                }
                this.grid[posClicked[1]][posClicked[0]] = this.selectedTile;
                if (newBuilding != null) {
                    this.buildings.add(newBuilding);
                }
            }
        }
        catch (Exception e){
            errorLog.severe(String.format(
                    "Error placing tile '%s' in position (%d, %d)"
                    , this.selectedTile.label()
                    , posClicked[0], posClicked[1]));
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

    }

    @Override
    public void MouseWheel(MouseEvent event) {
        int tileOffsetX = this.offsetX / this.tileSize;
        int tileOffsetY = this.offsetY / this.tileSize;
        this.tileSize -= event.getCount();
        this.tileSize = LittleTownViewer.max(this.tileSize, 12);
        this.tileSize = LittleTownViewer.min(this.tileSize, 64);
        this.offsetX = tileOffsetX * this.tileSize;
        this.offsetY = tileOffsetY * this.tileSize;
    }

    @Override
    public void draw() {
        this.draw(0, 0);
    }

    public void setWindow(@NotNull LittleTownViewer window) {
        this.window = window;
    }

    private int countAnimals(){
        int count = 0;

        List<ObjectMapTile> animalTiles =
                tileDictionary.getTilesInSubcategory("Animals");

        for(int i = 0; i < this.gridHeight; i++){
            for(int j = 0; j < this.gridWidth; j++){
                if (animalTiles.contains((ObjectMapTile) grid[i][j])){
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isSelectedTileAnimal(){
        List<ObjectMapTile> animalTiles =
                tileDictionary.getTilesInSubcategory("Animals");
        return animalTiles.contains(this.selectedTile);
    }

    private void addCustomTile(String name){
        ObjectMapTile newTile = new ObjectMapTile(this.window, name,
                new String[] {"Custom", "Custom"});
        this.tileDictionary.addCustomTile(newTile);

    }

    public void setSelectedTile(ObjectMapTile selectedTile) {
        this.selectedTile = selectedTile;
    }

    private void removeBuilding(ObjectMapTile tile){
        for (int i = 0; i < this.buildings.size(); i++) {
            if (this.buildings.get(i).tile.equals(tile)){
                this.buildings.remove(i);
                break;
            }
        }
    }

    @Override
    public String toString(){
        String[] mapArr = new String[this.gridWidth * this.gridHeight];
        int index = 0;
        for(int i = this.gridHeight - 1; i >= 0; i--){
            for(int j = 0; j < this.gridWidth; j++){
                Tile tile = this.grid[i][j];
                if (tile.label().equals("Empty")){
                    mapArr[index++] = "0";
                }
                else{
                    mapArr[index++] = tile.label();
                }
            }
        }
        return String.join("/", mapArr) + "/";
    }
}
