package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import processing.event.MouseEvent;

import java.util.HashMap;
import java.util.Map;

public class LoadingButton extends Button{
    private static Map<Integer, LoadingButton> buttons;

    private int angle;
    private int increment;
    private boolean graphicUpdated;


    private LoadingButton(@NotNull LittleTownViewer window, int size){
        this.setWindow(window);
        this.width = this.height = size;
        this.angle = 0;
        this.increment = 1;

        this.display = this.window.createGraphics(this.width, this.height);
        this.graphicUpdated = false;
    }

    public static LoadingButton getButton(@NotNull LittleTownViewer window, int size){
        if(buttons == null){
            buttons = new HashMap<>();
        }
        LoadingButton loadingButton = LoadingButton.buttons.get(size);
        if(loadingButton == null){
            loadingButton = new LoadingButton(window, size);
            LoadingButton.buttons.put(size, loadingButton);
        }
        return loadingButton;
    }

    public static void ageButtons(){
        if(buttons != null){
            for (LoadingButton button : buttons.values()){
                button.graphicUpdated = false;
            }
        }
    }

    @Override
    public void draw() {
        this.draw(0, 0);
    }

    public void draw(float x, float y) {
        if(!this.graphicUpdated){
            this.updateGraphic();
        }
        this.window.image(this.display, x, y);
    }

    private void updateGraphic(){
        display.beginDraw();
        this.angle = (this.angle + this.increment) % 360;
        display.background(50);
        display.noFill();
        display.strokeWeight(4);
        display.stroke(150);
        float center = this.width/2.0f;
        float diameter = this.width * 3/5.0f;
        display.arc(center, center, diameter, diameter, 0, LittleTownViewer.TWO_PI);
        display.stroke(255);
        float start = LittleTownViewer.radians(this.angle);
        float stop = start + 0.1f;
        display.arc(center, center, diameter, diameter, start, stop);
        display.endDraw();
        this.graphicUpdated = true;
    }

    @Override
    public boolean isMouseOver() {
        return false;
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        return false;
    }

    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed() {

    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void setup() {

    }

}
