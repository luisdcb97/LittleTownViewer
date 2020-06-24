package littletownviewer.ui;

import com.sun.istack.internal.NotNull;
import littletownviewer.interactive.KeyboardInput;
import littletownviewer.interactive.MouseClick;
import littletownviewer.interactive.MouseDrag;
import littletownviewer.interactive.MouseScroll;
import processing.core.PApplet;

public abstract class Screen implements Drawable, KeyboardInput, MouseClick,
        MouseDrag, MouseScroll
{
    protected PApplet window;

    public void setWindow(@NotNull PApplet window){
        this.window = window;
    }
}
