package littletownviewer.ui.screens.currencyedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LWTextButton;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.menu.SaveEditMenu;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CurrencyEdit extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            CurrencyEdit.class.getName());
    private static int MAX_DEWDROPS = 999999;

    private int dewdrops;
    private int dewdropsPrev;
    private List<Button> buttons;
    private SaveFileManager saveFile;

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

    public CurrencyEdit(@NotNull LittleTownViewer window, SaveFileManager saveFile) {
        this.setWindow(window);
        this.dewdrops = saveFile.getInteger("dew");
        this.dewdropsPrev = this.dewdrops;
        this.saveFile = saveFile;
        this.buttons = new ArrayList<>();

        this.addEditButtons();


        int buttonWidth = 200;
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(buttonWidth, 60)
                .withTextSize(24)
                .useDynamicWidth(false);

        int buttonX = this.window.width / 2 - buttonWidth / 2;
        LWTextButton saveButton = initialBuilder
                .positionedAt(buttonX, 300)
                .withMessage("Save")
                .onClick(() -> {
                    this.saveFile.setInteger("dew", this.dewdrops);
                    this.saveFile.saveGameData();
                    errorLog.info("Dewdrops saved");
                    this.dewdropsPrev = this.dewdrops;
                })
                .build();

        LWTextButton resetButton = initialBuilder
                .positionedAt(buttonX, 370)
                .withMessage("Reset")
                .onClick(() -> {
                    this.dewdrops = this.dewdropsPrev;
                })
                .build();

        LWTextButton backButton = initialBuilder
                .positionedAt(buttonX, 440)
                .withMessage("Back")
                .onClick(() -> {
                    this.window.moveToScreen(
                            new LoadingScreen(this.window,
                                    new SaveEditMenu(this.window, this.saveFile)
                                    , String.format("Return to savefile %d"
                                    , this.saveFile.getSaveNumber()))
                    );
                })
                .build();

        this.buttons.add(saveButton);
        this.buttons.add(resetButton);
        this.buttons.add(backButton);
    }

    @Override
    public void setup() {
        for (Button button: this.buttons) {
            button.setup();
        }
    }

    @Override
    public void draw() {
        window.background(50);
        for (Button button: this.buttons) {
            button.draw();
        }
        window.textAlign(window.CENTER, window.CENTER);
        window.fill(255);
        window.textSize(48);
        window.text(this.dewdrops, window.width / 2.0f, 100);
        window.fill(180);
        window.textSize(32);
        window.text(this.dewdropsPrev, window.width / 2.0f, 140);
    }

    private void clampDewdrops(){
        this.dewdrops = LittleTownViewer.max(0, this.dewdrops);
        this.dewdrops = LittleTownViewer.min(MAX_DEWDROPS, this.dewdrops);
    }

    private void addEditButtons(){
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(200, 60)
                .withTextSize(24)
                .useDynamicWidth(false);

        LWTextButton sub1Builder = initialBuilder.positionedAt(100, 50)
                .withMessage("-1")
                .onClick(() -> {
                    this.dewdrops -= 1;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton add1Builder = initialBuilder.positionedAt(900, 50)
                .withMessage("+1")
                .onClick(() -> {
                    this.dewdrops += 1;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton sub10Builder = initialBuilder.positionedAt(100, 120)
                .withMessage("-10")
                .onClick(() -> {
                    this.dewdrops -= 10;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton add10Builder = initialBuilder.positionedAt(900, 120)
                .withMessage("+10")
                .onClick(() -> {
                    this.dewdrops += 10;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton sub100Builder = initialBuilder.positionedAt(100, 190)
                .withMessage("-100")
                .onClick(() -> {
                    this.dewdrops -= 100;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton add100Builder = initialBuilder.positionedAt(900, 190)
                .withMessage("+100")
                .onClick(() -> {
                    this.dewdrops += 100;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton sub1000Builder = initialBuilder.positionedAt(100, 260)
                .withMessage("-1000")
                .onClick(() -> {
                    this.dewdrops -= 1000;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton add1000Builder = initialBuilder.positionedAt(900, 260)
                .withMessage("+1000")
                .onClick(() -> {
                    this.dewdrops += 1000;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton sub10000Builder = initialBuilder.positionedAt(100, 330)
                .withMessage("-10000")
                .onClick(() -> {
                    this.dewdrops -= 10000;
                    this.clampDewdrops();
                })
                .build();

        LWTextButton add10000Builder = initialBuilder.positionedAt(900, 330)
                .withMessage("+10000")
                .onClick(() -> {
                    this.dewdrops += 10000;
                    this.clampDewdrops();
                })
                .build();

        buttons.add(add1Builder);
        buttons.add(sub1Builder);
        buttons.add(add10Builder);
        buttons.add(sub10Builder);
        buttons.add(add100Builder);
        buttons.add(sub100Builder);
        buttons.add(add1000Builder);
        buttons.add(sub1000Builder);
        buttons.add(add10000Builder);
        buttons.add(sub10000Builder);
    }
}
