package littletownviewer.ui;

import littletownviewer.interactive.MouseClick;

public abstract class Tile implements Drawable, MouseClick {

    protected String label;
    protected int width;
    protected int height;

    public String label(){
        return this.label;
    }

    public int[] dimensions(){
        return new int[]{this.width, this.height};
    }

    public int width(){
        return this.width;
    }

    public int height(){
        return this.height;
    }

    public boolean isMultiTile(){
        return this.width > 1 || this.height > 1;
    }

    public abstract void draw(int x, int y, int tileSize);
}
