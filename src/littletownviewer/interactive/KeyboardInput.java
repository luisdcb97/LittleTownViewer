package littletownviewer.interactive;

import processing.event.KeyEvent;

public interface KeyboardInput {
    void keyTyped();
    void keyTyped(KeyEvent event);
    void keyPressed();
    void keyPressed(KeyEvent event);
    void keyReleased();
    void keyReleased(KeyEvent event);
}
