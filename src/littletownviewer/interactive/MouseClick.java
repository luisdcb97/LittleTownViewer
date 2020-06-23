package littletownviewer.interactive;

import processing.event.MouseEvent;

public interface MouseClick {
    void mouseClicked();
    void mouseClicked(MouseEvent event);
    void mousePressed();
    void mousePressed(MouseEvent event);
    void mouseReleased();
    void mouseReleased(MouseEvent event);
}
