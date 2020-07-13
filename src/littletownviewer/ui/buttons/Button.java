package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;

import littletownviewer.MySketch;
import processing.core.PGraphics;

import littletownviewer.interactive.MouseClick;
import littletownviewer.ui.Drawable;

public abstract class Button implements Drawable, MouseClick {
    protected MySketch window;
    protected PGraphics display;

    protected Runnable clickAction;

    protected int x, y;
    protected int width, height;

    public abstract boolean isMouseOver();
    public abstract boolean isMouseOver(float mouseX, float mouseY);

    public void setWindow(@NotNull MySketch window){
        this.window = window;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
