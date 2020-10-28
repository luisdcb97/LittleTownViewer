package littletownviewer.ui.screens.objectmapedit.sidebar;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.interactive.MouseClick;
import littletownviewer.interactive.MouseScroll;
import littletownviewer.ui.Drawable;
import littletownviewer.ui.screens.objectmapedit.ObjectMapTile;
import processing.event.MouseEvent;

import java.util.List;

public class ObjectMapTileSelection implements Drawable, MouseClick, MouseScroll {
    private LittleTownViewer window;
    private List<ObjectMapTile> tileList;
    private int x;
    private int y;
    private int width;
    private int height;
    private int tileSize;
    private int padding;
    private int scrollOffset;
    private int selectedTileIndex;

    public ObjectMapTileSelection(@NotNull LittleTownViewer window,
                                  int x, int y, int width, int height,
                                  int tileSize, int padding){
        this.setWindow(window);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.padding = padding;
        this.scrollOffset = 0;
        this.selectedTileIndex = 0;
    }

    //region MouseClick
    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseClicked(MouseEvent event) { }

    @Override
    public void mousePressed() {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        int tilesPerRow = LittleTownViewer.max(1,
                this.width / (this.tileSize + this.padding));
        int yEnd = this.y + this.height;
        int iterX = this.x;
        int iterY = this.y;

        for (int i = this.scrollOffset / (this.tileSize + this.padding)
             ; i < this.tileList.size(); i++)
        {
            if (i % tilesPerRow == 0 && i != 0){
                iterY += this.tileSize + this.padding;
                iterX = this.x;
            }
            if(iterY - this.scrollOffset >= this.y
                    && iterY - this.scrollOffset < yEnd)
            {
                if(this.window.mouseX >= iterX
                        && this.window.mouseX <= iterX + this.tileSize
                        && this.window.mouseY >= iterY - this.scrollOffset
                        && this.window.mouseY <= iterY +
                            this.tileSize - this.scrollOffset
                ){
                    this.selectedTileIndex = i;
                    break;
                }
            }
            iterX += this.tileSize + this.padding;
        }
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }
    //endregion

    //region Drawable
    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        int yEnd = this.y + this.height;
        int iterX = this.x;
        int iterY = this.y;
        int tilesPerRow = LittleTownViewer.max(1,
                this.width / (this.tileSize + this.padding));

        for (int i = this.scrollOffset / (this.tileSize + this.padding)
             ; i < this.tileList.size(); i++)
        {
            if (i % tilesPerRow == 0 && i != 0){
                iterY += this.tileSize + this.padding;
                iterX = this.x;
            }
            if(iterY - this.scrollOffset >= this.y
                    && iterY - this.scrollOffset < yEnd)
            {
                if(i == selectedTileIndex) {
                    this.window.fill(255, 220, 40);
                    this.window.rect(
                            iterX - 2,
                            iterY - this.scrollOffset - 2,
                            this.tileSize + 4,
                            this.tileSize + 4
                    );
                }
                this.tileList.get(i).draw(iterX, iterY - this.scrollOffset,
                        this.tileSize, false);
            }
            iterX += this.tileSize + this.padding;
        }

        //region Draw Tiles Scrollbar
        double rowsDouble = (double) this.tileList.size() / tilesPerRow;
        int rows = (int) Math.ceil( rowsDouble);
        int catLength = (this.tileSize + this.padding)
                * rows;
        float barLength = (float) this.height / catLength;

        if(barLength < 1){
            this.window.fill(100);
            this.window.rect(
                    this.x + this.width + this.padding,
                    this.y, 5, this.height
            );

            barLength *= this.height;
            float barStart = (float) ((this.scrollOffset * this.height)
                    / catLength);
            this.window.fill(250);
            this.window.rect(
                    this.x + this.width + this.padding,
                    this.y + barStart,
                    5, barLength
            );
        }
        //endregion
    }
    //endregion

    //region MouseScroll
    @Override
    public void MouseWheel() {}

    @Override
    public void MouseWheel(MouseEvent event) {
        this.scrollOffset += event.getCount() * 5;
        this.scrollOffset =
                LittleTownViewer.max(this.scrollOffset, 0);
        int tilesPerRow = LittleTownViewer.max(1,
                this.width / (this.tileSize + this.padding));
        double rowsDouble = (double) this.tileList.size() / tilesPerRow;
        int numRows = (int) Math.ceil( rowsDouble);
        int catLength = (this.tileSize + this.padding) * numRows;
        if(catLength <= this.height){
            this.scrollOffset = 0;
        }
        else{
            this.scrollOffset = LittleTownViewer.min(
                    this.scrollOffset,
                    catLength - (this.height));
        }
    }
    //endregion

    public void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setTileSize(int tileSize){
        this.tileSize = tileSize;
    }

    public void setTileList(List<ObjectMapTile> tileList) {
        this.tileList = tileList;
        this.scrollOffset = 0;
        this.selectedTileIndex = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMouseOver(int mouseX, int mouseY){
        boolean over = mouseX >= this.x;
        over &= mouseY >= this.y;
        over &= mouseX <= this.x + this.width;
        over &= mouseY <= this.y + this.height;
        return over;
    }

    public ObjectMapTile getSelectedTile(){
        return this.tileList.get(this.selectedTileIndex);
    }

}
