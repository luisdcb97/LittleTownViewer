package littletownviewer.ui.screens.menu;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Font;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LWTextButton;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.SavesListMenu;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainMenu extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            MainMenu.class.getName());
    private static final String BG_PATH =
            "Resources/Textures/Screen Backgrounds/MainMenu.png";

    private final List<Button> buttons;
    PImage bgImage;

    public MainMenu(@NotNull LittleTownViewer window){
        this.setWindow(window);
        this.buttons = new ArrayList<>();

        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(200, 50)
                .withTextFont("Arial")
                .withTextSize(24)
                .useDynamicWidth(true);

        LWTextButton saveButton = initialBuilder.positionedAt(100, 490)
                .withMessage("Select Save")
                .onClick(() -> window.moveToScreen(
                        new LoadingScreen(
                                this.window, new SavesListMenu(this.window),
                                "Fetching savefile data..."
                        ))
                )
                .build();

        LWTextButton exitButton = initialBuilder.positionedAt(100, 550)
                .withMessage("Exit")
                .onClick(this.window::exit)
                .build();

        buttons.add(saveButton);
        buttons.add(exitButton);
    }

    //region Keyboard
    @Override
    public void keyTyped() {

    }

    @Override
    public void keyTyped(KeyEvent event) {

    }

    @Override
    public void keyPressed() {

    }

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void keyReleased() {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }
    //endregion

    //region Mouse
    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed() {
        this.mousePressed(null);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        for (Button button: this.buttons) {
            if (button.isMouseOver(window.mouseX, window.mouseY)) {
                button.mousePressed();
            }
        }
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void MouseDragged() {

    }

    @Override
    public void MouseDragged(MouseEvent event) {

    }

    @Override
    public void MouseWheel() {

    }

    @Override
    public void MouseWheel(MouseEvent event) {

    }
    //endregion

    @Override
    public void setup() {
        this.loadBackground();
        int centerX = window.width / 2;
        for (Button button: this.buttons) {
            button.setup();
            button.setPosition(centerX - (button.getWidth() / 2),
                    button.getY());
        }
    }

    @Override
    public void draw() {
        if (bgImage == null) {
            window.background(50);
        }
        else {
            window.background(bgImage);
        }
        window.textAlign(window.CENTER, window.CENTER);

        window.textSize(82);
        window.textFont(Font.getFont(this.window, "Arial", 82));
        window.fill(25);
        window.text("LittleTownViewer", window.width / 2.0f, 150);


        for (Button button: this.buttons) {
            button.draw();
        }
    }

    private void loadBackground(){
        bgImage = window.loadImage(BG_PATH);

        if (bgImage == null){
            errorLog.warning(
                    "Error loading background image!\n"
            );
        }
        else{
            if (bgImage.width == -1){
                bgImage = null;
                errorLog.warning(
                        "Background image path contains bad image data!\n"
                );
            }
            else{
                bgImage.resize(window.width, window.height);
            }
        }

    }
}
