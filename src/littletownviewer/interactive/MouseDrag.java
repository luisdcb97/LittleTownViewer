package littletownviewer.interactive;

import processing.event.MouseEvent;

/**
 * <p>Interface for processing mouse drag events</p>
 *
 * @see MouseClick
 * @see MouseScroll
 * @see KeyboardInput
 *
 * @author Luis David
 */
public interface MouseDrag {
    void MouseDragged();
    void MouseDragged(MouseEvent event);
}
