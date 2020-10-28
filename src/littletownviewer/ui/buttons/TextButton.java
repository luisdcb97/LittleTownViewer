package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Color;
import littletownviewer.ui.Font;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.event.MouseEvent;

public class TextButton extends Button{
    protected static final String DEFAULT_FILL_COLOR = "#FF0000";
    protected static final String DEFAULT_STROKE_COLOR = "#000000";
    protected static final String DEFAULT_FONT = "Arial";

    protected String fillColor;
    protected String strokeColor;

    private String text;
    private int textSize;

    private String textFont;
    private PFont textPFont;

    private boolean isWidthUpdated;

    public TextButton(@NotNull LittleTownViewer window, @NotNull PGraphics display
            , int x, int y, String text, int height
            , @NotNull Runnable clickAction)
    {
        this.setWindow(window);
        this.display = display;
        this.x = x;
        this.y = y;
        this.text = text;
        this.width = (int) window.textWidth(text) + 10;
        this.height = height;
        this.fillColor = DEFAULT_FILL_COLOR;
        this.strokeColor = DEFAULT_STROKE_COLOR;
        this.textFont = DEFAULT_FONT;
        this.clickAction = clickAction;
        this.isWidthUpdated = false;
    }

    @Override
    public boolean isMouseOver() {
        return isMouseOver(0, 0);
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        return this.isMouseOverCorner(mouseX, mouseY);
    }

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
        this.clickAction.run();
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }
    //endregion

    @Override
    public void setup() {
        this.loadFont();
    }

    @Override
    public void draw() {
        if(!isWidthUpdated) {
            this.updateWidth();
        }
        display.rectMode(window.CORNER);
        int[] color = Color.HexToRGBA(fillColor);
        display.fill(color[0], color[1], color[2]);
        color = Color.HexToRGBA(strokeColor);
        display.stroke(color[0], color[1], color[2]);
        display.strokeWeight(2);
        display.rect(x, y, width, height);
        display.textFont(this.textPFont);
        display.textSize(this.textSize);
        display.fill(color[0], color[1], color[2]);
        display.textAlign(window.CENTER, window.CENTER);
        display.text(this.text
                , this.x + this.width / 2.0f
                , this.y + this.height / 2.0f);
    }

    public void setText(String text) {
        this.text = text;
        this.isWidthUpdated = false;
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
        this.textPFont = null;
        this.isWidthUpdated = false;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.textPFont = null;
        this.isWidthUpdated = false;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    private void updateWidth(){
        if (textPFont == null) {
            this.loadFont();
        }
        this.display.textFont(this.textPFont);
        this.display.textSize(this.textSize);
        this.width = (int) this.display.textWidth(text) + 10;
        this.isWidthUpdated = true;
    }

    private void loadFont(){
        this.textPFont = Font.getFont(this.window, this.textFont, this.textSize);
    }
}
