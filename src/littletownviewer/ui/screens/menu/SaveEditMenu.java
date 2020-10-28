package littletownviewer.ui.screens.menu;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Font;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.Button;
import littletownviewer.ui.buttons.LWTextButton;
import littletownviewer.ui.screens.currencyedit.CurrencyEdit;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.energyedit.EnergyEdit;
import littletownviewer.ui.screens.inventoryedit.InventoryEdit;
import littletownviewer.ui.screens.objectmapedit.ObjectMapEdit;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class SaveEditMenu extends Screen {
    private final SaveFileManager saveFile;
    private final Profile playerSummary;
    private final List<Button> buttons;

    private class Profile {
        String playerName;
        String title;
        String townName;
        int dew;
        int daysPlayed;

        Profile(SaveFileManager saveFile){
            playerName = saveFile.getString("playerName");
            title = saveFile.getString("title");
            townName = saveFile.getString("townName");
            dew = saveFile.getInteger("dew");
            daysPlayed = saveFile.getInteger("daysPlayed");
        }
    }

    public SaveEditMenu(@NotNull LittleTownViewer window, SaveFileManager saveFile) {
        this.setWindow(window);
        this.saveFile = saveFile;
        this.playerSummary = new Profile(saveFile);
        this.buttons = new ArrayList<>();

        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(200, 50)
                .withTextFont("Arial")
                .withTextSize(18)
                .useDynamicWidth(true);

        LWTextButton objMapEdit = initialBuilder.positionedAt(50, 100)
                .withMessage("Edit Map")
                .onClick(() -> window.moveToScreen(
                        new LoadingScreen(this.window
                                , new ObjectMapEdit(window, saveFile)
                                , String.format("Loading map for game %d...",
                                    this.saveFile.getSaveNumber())))
                )
                .build();

        buttons.add(objMapEdit);

        LWTextButton curEdit = initialBuilder.positionedAt(50, 200)
                .withMessage("Edit Dewdrops")
                .onClick(() -> window.moveToScreen(
                        new LoadingScreen(this.window
                            , new CurrencyEdit(window, saveFile)
                            , String.format("Loading currency for game %d...",
                            this.saveFile.getSaveNumber())))
                )
                .build();

        buttons.add(curEdit);

        LWTextButton energyEdit = initialBuilder.positionedAt(50, 300)
                .withMessage("Edit Energy")
                .onClick(() -> window.moveToScreen(
                    new LoadingScreen(this.window
                            , new EnergyEdit(window, saveFile)
                            , String.format("Loading hero energy for game %d...",
                            this.saveFile.getSaveNumber())))
                )
                .build();

        buttons.add(energyEdit);

        LWTextButton itemEdit = initialBuilder.positionedAt(50, 400)
                .withMessage("Edit Inventory")
                .onClick(() -> window.moveToScreen(
                    new LoadingScreen(this.window
                            , new InventoryEdit(window, saveFile)
                            , String.format("Loading inventory for game %d...",
                            this.saveFile.getSaveNumber())))
                )
                .build();

        buttons.add(itemEdit);

        LWTextButton back = initialBuilder.positionedAt(50, 500)
                .withMessage("Return to Main Menu")
                .onClick(() -> window.moveToScreen(
                    new LoadingScreen(this.window
                            , new MainMenu(window)
                            , String.format("Loading currency for game %d...",
                            this.saveFile.getSaveNumber())))
                )
                .build();

        buttons.add(back);
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
        for (Button button: this.buttons) {
            button.setup();
        }
    }

    @Override
    public void draw() {
        window.background(50);
        window.textAlign(window.LEFT, window.CENTER);
        window.textSize(18);
        window.textFont(Font.getFont(this.window, "Arial", 18));
        window.fill(255);

        int textXCenter = 800;
        float maxDeviation = 200;
        String text = this.playerSummary.playerName;
        float textLength = window.textWidth(text);
        window.text(text, textXCenter - textLength / 2, 100);

        text = this.playerSummary.title + " of " + this.playerSummary.townName;
        textLength = window.textWidth(text);
        window.text(text, textXCenter - textLength / 2, 150);

        text = this.playerSummary.dew + " Dewdrops";
        window.text(text, textXCenter - maxDeviation, 200);

        window.textAlign(window.RIGHT, window.CENTER);
        text = "Day " + this.playerSummary.daysPlayed;
        window.text(text, textXCenter + maxDeviation, 200);

        for (Button button: this.buttons) {
            button.draw();
        }
    }
}
