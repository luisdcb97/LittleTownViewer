package littletownviewer.ui;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.interactive.KeyboardInput;
import littletownviewer.interactive.MouseClick;
import littletownviewer.interactive.MouseDrag;
import littletownviewer.interactive.MouseScroll;

public abstract class Screen implements Drawable, KeyboardInput, MouseClick,
        MouseDrag, MouseScroll
{
    protected LittleTownViewer window;

    public void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }
}
