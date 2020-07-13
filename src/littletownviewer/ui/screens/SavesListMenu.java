package littletownviewer.ui.screens;

import littletownviewer.MySketch;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.SaveFileButton;
import org.jetbrains.annotations.NotNull;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class SavesListMenu extends Screen {
    private static final int NUM_SAVES = 3;


    private SaveFileButton[] saveButtons;

    public SavesListMenu(@NotNull MySketch window){
        this.setWindow(window);
        this.saveButtons = new SaveFileButton[NUM_SAVES];
        for (int i = 0; i < NUM_SAVES; i++){
            SaveFileManager save = new SaveFileManager(window, i);
            this.saveButtons[i] =
                    new SaveFileButton(window, save, 200, 50 + i * 200,
                            800, 150);
        }
    }

    @Override
    public void setup() {
        for (SaveFileButton saveButton : this.saveButtons){
            saveButton.setup();
        }
    }

    @Override
    public void draw() {
        window.background(50);

        for (SaveFileButton saveButton : this.saveButtons){
            saveButton.draw();
        }
    }

    @Override
    public void keyTyped() {}

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed() {}

    @Override
    public void keyPressed(KeyEvent event) {}

    @Override
    public void keyReleased() {}

    @Override
    public void keyReleased(KeyEvent event) {}

    @Override
    public void mouseClicked() {}

    @Override
    public void mouseClicked(MouseEvent event) {}

    @Override
    public void mousePressed() {
        for (SaveFileButton saveButton : this.saveButtons){
            if(saveButton.isMouseOver(window.mouseX, window.mouseY))
            {
                saveButton.mouseClicked();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased() {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void MouseDragged() {}

    @Override
    public void MouseDragged(MouseEvent event) {}

    @Override
    public void MouseWheel() {}

    @Override
    public void MouseWheel(MouseEvent event) {}

}
