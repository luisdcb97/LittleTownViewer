package littletownviewer.interactive;

import processing.event.MouseEvent;

/**
 * <p>Interface for processing mouse click events</p>
 *
 * @see MouseDrag
 * @see MouseScroll
 * @see KeyboardInput
 *
 * @author Luis David
 */
public interface MouseClick {
    void mouseClicked();
    void mouseClicked(MouseEvent event);
    void mousePressed();
    void mousePressed(MouseEvent event);
    void mouseReleased();
    void mouseReleased(MouseEvent event);
}
