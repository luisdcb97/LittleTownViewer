package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Color;
import littletownviewer.ui.Font;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.event.MouseEvent;

public class LWTextButton extends Button {
    public static class Builder {
        private final LittleTownViewer window;
        private final PGraphics display;

        private Runnable clickAction;

        private int x, y;
        private int width, height;

        private int innerBorderSize;
        private int outerBorderSize;

        private int textColor;
        private int outerBorderColor;
        private int innerBorderColor;
        private int backgroundColor;

        private String text;

        private int textSize;

        private String textFont;
        private boolean useDynamicWidth;

        public Builder(@NotNull LittleTownViewer window, @NotNull PGraphics display){
            this.window = window;
            this.display = display;
            this.x = 0;
            this.y = 0;
            this.outerBorderSize = 2;
            this.innerBorderSize = 3;
            this.text = "Click Here";
            this.textSize = 18;
            this.width = 100;
            this.height = 50;
            this.clickAction = () -> System.out.println(String.format(
                    "Clicked button '%s' on screen %s", this.text
                    , this.window.getScreenName()
            ));
            this.textFont = DEFAULT_FONT;
            this.textColor = Color.HexToInt(DEFAULT_TEXT_COLOR);
            this.outerBorderColor = Color.HexToInt(DEFAULT_OUTER_BORDER_COLOR);
            this.innerBorderColor = Color.HexToInt(DEFAULT_INNER_BORDER_COLOR);
            this.backgroundColor = Color.HexToInt(DEFAULT_INNER_BACKGROUND_COLOR);
            this.useDynamicWidth = true;
        }

        public Builder positionedAt(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder withDimensions(int width, int height){
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder withBorderColor(String outerColor, String innerColor){
            this.outerBorderColor = Color.HexToInt(outerColor);
            this.innerBorderColor = Color.HexToInt(innerColor);
            return this;
        }

        public Builder withBorderSize(int outerSize, int innerSize){
            if(outerSize < 0 || innerSize < 0){
                throw new IllegalArgumentException(
                        "Border size must be equal or greater than 0");
            }
            this.outerBorderSize = outerSize;
            this.innerBorderSize = innerSize;
            return this;
        }

        public Builder withTextColor(String color){
            this.textColor = Color.HexToInt(color);
            return this;
        }

        public Builder withBackgroundColor(String color){
            this.backgroundColor = Color.HexToInt(color);
            return this;
        }

        public Builder withMessage(String message){
            this.text = message;
            return this;
        }

        public Builder withTextSize(int size){
            this.textSize = size;
            return this;
        }

        public Builder onClick(Runnable action){
            this.clickAction = action;
            return this;
        }

        public Builder useDynamicWidth(boolean bool){
            this.useDynamicWidth = bool;
            return this;
        }

        public Builder withTextFont(String font){
            this.textFont = font;
            return this;
        }

        public LWTextButton build(){
            LWTextButton button = new LWTextButton();
            button.x = this.x;
            button.y = this.y;
            button.outerBorderSize = this.outerBorderSize;
            button.innerBorderSize = this.innerBorderSize;
            button.window = this.window;
            button.display = this.display;
            button.text = this.text;
            button.textSize = this.textSize;
            button.width = this.width;
            button.height = this.height;
            button.clickAction = this.clickAction;
            button.textFont = this.textFont;
            button.textColor = this.textColor;
            button.innerBorderColor = this.innerBorderColor;
            button.outerBorderColor = this.outerBorderColor;
            button.backgroundColor = this.backgroundColor;
            button.useDynamicWidth = this.useDynamicWidth;
            return button;
        }

    }

    private static final String DEFAULT_OUTER_BORDER_COLOR = "#553E34";
    private static final String DEFAULT_INNER_BORDER_COLOR = "#A46241";
    private static final String DEFAULT_INNER_BACKGROUND_COLOR = "#FEAF4E";
    private static final String DEFAULT_TEXT_COLOR = "#5E4234";
    private static final String DEFAULT_FONT = "Arial";

    private int textColor;
    private int outerBorderColor;
    private int innerBorderColor;
    private int backgroundColor;

    private String text;

    private int textSize;

    private int outerBorderSize;
    private int innerBorderSize;

    private String textFont;
    private PFont textPFont;

    private boolean isWidthUpdated;
    private boolean useDynamicWidth;

    //region Mouse
    @Override
    public boolean isMouseOver() {
        return this.isMouseOver(0, 0);
    }

    @Override
    public boolean isMouseOver(float mouseX, float mouseY) {
        return this.isMouseOverCorner(mouseX, mouseY);
    }

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

    private LWTextButton(){
        this.isWidthUpdated = false;
    }

    @Override
    public void setup() {
        this.loadFont();
        if(!isWidthUpdated && useDynamicWidth) {
            this.updateWidth();
        }
    }

    @Override
    public void draw() {
        if(!isWidthUpdated && useDynamicWidth) {
            this.updateWidth();
        }
        display.rectMode(window.CORNER);
        display.noStroke();
        display.fill(this.outerBorderColor);
        display.rect(x, y, width, height);
        display.fill(this.innerBorderColor);
        display.rect(x + outerBorderSize, y + outerBorderSize
                , width - 2 * outerBorderSize, height - 2 * outerBorderSize);
        display.fill(this.backgroundColor);
        display.rect(x + outerBorderSize + innerBorderSize
                , y + outerBorderSize + innerBorderSize
                , width - 2 * outerBorderSize - 2 * innerBorderSize
                , height - 2 * outerBorderSize - 2 * innerBorderSize);

        display.fill(this.textColor);
        display.textFont(this.textPFont);
        display.textSize(this.textSize);
        display.textAlign(window.CENTER, window.CENTER);
        display.text(this.text
                , this.x + this.width / 2.0f
                , this.y + this.height / 2.0f - 2);
    }

    public void drawHighlighted() {
        if(!isWidthUpdated && useDynamicWidth) {
            this.updateWidth();
        }
        display.rectMode(window.CORNER);
        display.noStroke();
        display.fill(this.outerBorderColor);
        display.rect(x, y, width, height);
        display.fill(this.innerBorderColor);
        display.rect(x + outerBorderSize, y + outerBorderSize
                , width - 2 * outerBorderSize, height - 2 * outerBorderSize);
        int color = Color.brighten(this.backgroundColor, 20);
        display.fill(color);
        display.rect(x + outerBorderSize + innerBorderSize
                , y + outerBorderSize + innerBorderSize
                , width - 2 * outerBorderSize - 2 * innerBorderSize
                , height - 2 * outerBorderSize - 2 * innerBorderSize);

        display.fill(this.textColor);
        display.textFont(this.textPFont);
        display.textSize(this.textSize);
        display.textAlign(window.CENTER, window.CENTER);
        display.text(this.text
                , this.x + this.width / 2.0f
                , this.y + this.height / 2.0f - 2);
    }

    private void updateWidth(){
        if (this.textPFont == null) {
            this.loadFont();
        }
        this.display.textFont(this.textPFont);
        this.display.textSize(this.textSize);
        this.width = (int) this.display.textWidth(text) + 10
                + 2 * this.innerBorderSize + 2 * this.outerBorderSize;
        this.isWidthUpdated = true;
    }

    public String getText(){
        return this.text;
    }


    private void loadFont(){
        this.textPFont = Font.getFont(this.window, this.textFont, this.textSize);
    }
}
