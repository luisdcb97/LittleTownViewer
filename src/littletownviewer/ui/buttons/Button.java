package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;

import littletownviewer.LittleTownViewer;
import processing.core.PGraphics;

import littletownviewer.interactive.MouseClick;
import littletownviewer.ui.Drawable;

public abstract class Button implements Drawable, MouseClick {
    protected LittleTownViewer window;
    protected PGraphics display;

    protected Runnable clickAction;

    protected int x, y;
    protected int width, height;

    public abstract boolean isMouseOver();
    public abstract boolean isMouseOver(float mouseX, float mouseY);

    protected boolean isMouseOverCorner(float mouseX, float mouseY){
        boolean over = mouseX >= this.x;
        over &= mouseY >= this.y;
        over &= mouseX <= this.x + this.width;
        over &= mouseY <= this.y + this.height;
        return over;
    }

    public void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setAction(@NotNull Runnable clickAction){
        this.clickAction = clickAction;
    }
}
