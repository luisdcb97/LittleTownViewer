package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;
import littletownviewer.SaveFileManager;
import littletownviewer.ui.Color;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class SaveFileButton extends Button{
    protected static final String DEFAULT_FILL_COLOR = "#96FA32";
    protected static final String DEFAULT_HOVER_FILL_COLOR = "#6ED20A";
    protected static final String DEFAULT_STROKE_COLOR = "#000000";

    private SaveFileManager saveFile;
    private PApplet window;

    public SaveFileButton(@NotNull PApplet window, SaveFileManager saveFile,
                          int x, int y, int width, int height)
    {
        this.setWindow(window);
        this.saveFile = saveFile;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setWindow(@NotNull PApplet window){
        this.window = window;
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw(){
        window.rectMode(window.CORNER);
        int color[] = Color.HexToRGB(DEFAULT_STROKE_COLOR);
        window.stroke(color[0], color[1], color[2]);
        window.strokeWeight(2);
        if(this.isMouseOver(window.mouseX - this.x,
                window.mouseY - this.y)){
            color = Color.HexToRGB(DEFAULT_HOVER_FILL_COLOR);
            window.fill(color[0], color[1], color[2]);
        }
        else{
            color = Color.HexToRGB(DEFAULT_FILL_COLOR);
            window.fill(color[0], color[1], color[2]);
        }

        window.rect(this.x, this.y, this.width, this.height);
        window.textAlign(window.CENTER, window.CENTER);
        color = Color.HexToRGB(DEFAULT_STROKE_COLOR);
        window.fill(color[0], color[1], color[2]);

        if(this.saveFile.isFileReady()){
            window.textSize(32);
            window.text(this.saveFile.getString("playerName"),
                    this.x + (this.width / 2.0f),
                    this.y + (this.height / 3.0f));

            String title = this.saveFile.getString("title") + " of " +
                    this.saveFile.getString("townName");
            window.textSize(24);
            window.text(title, this.x + (this.width / 2.0f),
                    this.y + (2 * this.height / 3.0f));
        }
    }

    @Override
    public boolean isMouseOver() {
        return isMouseOver(0, 0);
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        boolean over = mouseX >= this.x;
        over &= mouseY >= this.y;
        over &= mouseX <= this.x + this.width;
        over &= mouseY <= this.y + this.height;
        return over;
    }

    @Override
    public void mouseClicked() {
        System.out.printf(
                "Clicked on save %d\n", this.saveFile.getSaveNumber());
        //moveToScreen(new LoadingScreen(new ObjectMapEdit(instance, this.saveFile)));
    }

    @Override
    public void mouseClicked(MouseEvent event) {}

    @Override
    public void mousePressed() {}

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased() {}

    @Override
    public void mouseReleased(MouseEvent event) {}
}
