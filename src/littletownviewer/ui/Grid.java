package littletownviewer.ui;

import littletownviewer.interactive.MouseClick;
import littletownviewer.interactive.MouseDrag;
import littletownviewer.interactive.MouseScroll;

public abstract class Grid implements Drawable, MouseClick, MouseDrag,
        MouseScroll {

    protected int gridWidth;
    protected int gridHeight;
    protected int tileSize;

    protected Tile[][] grid;

    protected int offsetX;
    protected int offsetY;

    public abstract void draw(int x, int y);

    public void move(int x, int y){
        this.offsetX += x;
        this.offsetY += y;
    }

    public void resetTranslation(){
        this.offsetX = 0;
        this.offsetY = 0;
    }

    protected int[] getGridPositionClicked(double x, double y){
        x = x - this.offsetX;
        y = y - this.offsetY;
        int i = (int) Math.floor(x / this.tileSize);
        int j = (int) Math.floor(y / this.tileSize);

        return new int[]{i, j};
    }

    public Tile getTileClicked(double x, double y){
        int[] gridPos = getGridPositionClicked(x, y);

        if(gridPos[0] < 0 || gridPos[0] > this.gridWidth - 1
                || gridPos[1] < 0 || gridPos[1] > this.gridHeight - 1
        ){
            String msg = String.format("(%d, %d)", gridPos[0], gridPos[1]);

            throw new IndexOutOfBoundsException(msg);
        }
        else{
            return grid[gridPos[0]][gridPos[1]];
        }
    }

    public int getTileSize(){
        return this.tileSize;
    }

    public int[] offsets(){
        return new int[]{this.offsetX, this.offsetY};
    }
}
