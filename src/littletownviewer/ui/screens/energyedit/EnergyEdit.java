package littletownviewer.ui.screens.energyedit;

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

public class EnergyEdit extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            EnergyEdit.class.getName());
    private static int MIN_TOTAL_ENERGY = 100;
    private static int MAX_TOTAL_ENERGY = 5000;
    private static int MIN_CURRENT_ENERGY = 0;

    private int currentEnergy;
    private int totalEnergy;
    private List<Button> buttons;
    private SaveFileManager saveFile;
    private boolean changeTotalEnergy;
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

    public EnergyEdit(@NotNull LittleTownViewer window, SaveFileManager saveFile) {
        this.setWindow(window);
        this.currentEnergy = saveFile.getInteger("dayEXP");
        this.totalEnergy = saveFile.getInteger("maxDayEXP");
        this.saveFile = saveFile;
        this.changeTotalEnergy = true;
        this.buttons = new ArrayList<>();
        this.addEditButtons();

        int buttonWidth = 200;
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(buttonWidth, 60)
                .withTextSize(24)
                .useDynamicWidth(false);

        int buttonX = this.window.width / 2 - buttonWidth / 2;
        LWTextButton switchButton = initialBuilder
                .positionedAt(buttonX, 330)
                .withMessage("Switch")
                .onClick(() -> {
                    this.changeTotalEnergy = !this.changeTotalEnergy;
                })
                .build();

        LWTextButton saveButton = initialBuilder
                .positionedAt(buttonX, 400)
                .withMessage("Save")
                .onClick(() -> {
                    this.saveFile.setInteger("dayEXP", this.currentEnergy);
                    this.saveFile.setInteger("maxDayEXP", this.totalEnergy);
                    this.saveFile.saveGameData();
                    errorLog.info("Energy saved");
                })
                .build();

        LWTextButton backButton = initialBuilder
                .positionedAt(buttonX, 470)
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

        this.buttons.add(switchButton);
        this.buttons.add(saveButton);
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

        int totalEnergyWidth = 500;
        int currentEnergyWidth = (int )(((float) currentEnergy / totalEnergy) * totalEnergyWidth);
        window.rectMode(window.CORNER);
        window.noStroke();
        window.fill(140);
        window.rect(350, 100, totalEnergyWidth, 50);
        window.fill(210);
        window.rect(350, 100, currentEnergyWidth, 50);

        window.textSize(32);
        window.textAlign(window.CENTER, window.CENTER);
        window.fill(255);
        if (changeTotalEnergy){
            window.text(String.format("Total energy: %d", this.totalEnergy)
                    , window.width / 2.0f, 200);
        }
        else{
            window.text(String.format("Energy spent: %d", this.currentEnergy)
                    , window.width / 2.0f, 200);
        }
    }

    private void clampCurrentEnergy(){
        this.currentEnergy = LittleTownViewer.max(MIN_CURRENT_ENERGY, this.currentEnergy);
        this.currentEnergy = LittleTownViewer.min(this.totalEnergy, this.currentEnergy);
    }

    private void clampTotalEnergy(){
        this.totalEnergy = LittleTownViewer.max(MIN_TOTAL_ENERGY, this.totalEnergy);
        this.totalEnergy = LittleTownViewer.max(this.currentEnergy, this.totalEnergy);
        this.totalEnergy = LittleTownViewer.min(MAX_TOTAL_ENERGY, this.totalEnergy);
    }

    private void addEditButtons() {
        LWTextButton.Builder initialBuilder = new
                LWTextButton.Builder(this.window, this.window.getGraphics())
                .withDimensions(200, 60)
                .withTextSize(24)
                .useDynamicWidth(false);

        LWTextButton sub10Button = createEditButton(
                initialBuilder.positionedAt(100,100)
                , false, 10);
        LWTextButton sub100Button = createEditButton(
                initialBuilder.positionedAt(100,170)
                , false, 100);
        LWTextButton sub1000Button = createEditButton(
                initialBuilder.positionedAt(100,240)
                , false, 1000);

        LWTextButton add10Button = createEditButton(
                initialBuilder.positionedAt(900,100)
                , true, 10);
        LWTextButton add100Button = createEditButton(
                initialBuilder.positionedAt(900,170)
                , true, 100);
        LWTextButton add1000Button = createEditButton(
                initialBuilder.positionedAt(900,240)
                , true, 1000);

        this.buttons.add(sub10Button);
        this.buttons.add(add10Button);
        this.buttons.add(sub100Button);
        this.buttons.add(add100Button);
        this.buttons.add(sub1000Button);
        this.buttons.add(add1000Button);
    }

    private LWTextButton createEditButton(LWTextButton.Builder builder
            , boolean isAddition, int quantity){
        if(isAddition){
            return builder
                    .withMessage(String.format("+%d", quantity))
                    .onClick(() -> {
                        if(changeTotalEnergy){
                            this.totalEnergy += quantity;
                            this.clampTotalEnergy();
                        }
                        else{
                            this.currentEnergy += quantity;
                            this.clampCurrentEnergy();
                        }
                    })
                    .build();
        }
        else {
            return builder
                    .withMessage(String.format("-%d", quantity))
                    .onClick(() -> {
                        if(changeTotalEnergy){
                            this.totalEnergy -= quantity;
                            this.clampTotalEnergy();
                        }
                        else{
                            this.currentEnergy -= quantity;
                            this.clampCurrentEnergy();
                        }
                    })
                    .build();
        }
    }
}
