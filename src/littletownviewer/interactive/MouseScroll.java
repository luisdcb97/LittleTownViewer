package littletownviewer.interactive;

import processing.event.MouseEvent;

/**
 * <p>Interface for processing mouse scroll events</p>
 *
 * @see MouseClick
 * @see MouseDrag
 * @see KeyboardInput
 *
 * @author Luis David
 */
public interface MouseScroll {
    void MouseWheel();
    void MouseWheel(MouseEvent event);
}
