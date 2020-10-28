package littletownviewer.ui.screens.objectmapedit.sidebar;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Color;
import littletownviewer.ui.buttons.Button;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.event.MouseEvent;

public class ObjectMapSidebarButton extends Button {
    public static class Builder {
        private final LittleTownViewer window;
        private final PGraphics display;

        private Runnable clickAction;

        private int x, y;
        private int width, height;

        private int borderSize;

        private String textColor;
        private String borderColor;
        private String topColor;
        private String bottomColor;

        private String text;

        private int textSize;

        private String textFont;

        public Builder(@NotNull LittleTownViewer window, @NotNull PGraphics display){
            this.window = window;
            this.display = display;
            this.x = 0;
            this.y = 0;
            this.borderSize = 2;
            this.text = "Click Here";
            this.textSize = 18;
            this.width = 100;
            this.height = 50;
            this.clickAction = () -> System.out.println(String.format(
                    "Clicked button '%s' on screen %s", this.text
                    , this.window.getScreenName()
            ));
            this.textFont = DEFAULT_FONT;
            this.textColor = DEFAULT_TEXT_COLOR;
            this.borderColor = DEFAULT_BORDER_COLOR;
            this.topColor = DEFAULT_TOP_COLOR;
            this.bottomColor = DEFAULT_BOTTOM_COLOR;
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

        public Builder withBorderColor(String color){
            this.borderColor = color;
            return this;
        }

        public Builder withBorderSize(int size){
            this.borderSize = size;
            return this;
        }

        public Builder withTopColor(String color){
            this.topColor = color;
            return this;
        }

        public Builder withBottomColor(String color) {
            this.bottomColor = color;
            return this;
        }

        public Builder withTextColor(String color){
            this.textColor = color;
            return this;
        }

        public Builder withTextFont(String font){
            this.textFont = font;
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

        public ObjectMapSidebarButton build(){
            ObjectMapSidebarButton button = new ObjectMapSidebarButton();
            button.x = this.x;
            button.y = this.y;
            button.borderSize = this.borderSize;
            button.window = this.window;
            button.display = this.display;
            button.text = this.text;
            button.textSize = this.textSize;
            button.width = this.width;
            button.height = this.height;
            button.clickAction = this.clickAction;
            button.textFont = this.textFont;
            button.textColor = this.textColor;
            button.borderColor = this.borderColor;
            button.topColor = this.topColor;
            button.bottomColor = this.bottomColor;
            return button;
        }
    }

    private static final String DEFAULT_BORDER_COLOR = "#405774";
    private static final String DEFAULT_TOP_COLOR = "#5FBFD9";
    private static final String DEFAULT_BOTTOM_COLOR = "#4E92BF";
    private static final String DEFAULT_TEXT_COLOR = "#324966";
    private static final String DEFAULT_FONT = "Arial";

    private String textColor;
    private String borderColor;
    private String topColor;
    private String bottomColor;

    private String text;

    private int textSize;

    private int borderSize;

    private String textFont;
    private PFont textPFont;

    private boolean isWidthUpdated;

    private ObjectMapSidebarButton(){
        this.isWidthUpdated = false;
    }

    //region Mouse
    @Override
    public boolean isMouseOver() {
        return isMouseOver(0, 0);
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

    @Override
    public void setup() {
        this.textPFont = this.window.createFont(this.textFont, this.textSize);
    }

    @Override
    public void draw() {
        if(!isWidthUpdated) {
            this.updateWidth();
        }
        display.rectMode(window.CORNER);
        display.noStroke();
        int[] color = Color.HexToRGBA(borderColor);
        display.fill(color[0], color[1], color[2]);
        display.rect(x, y, width, height);
        color = Color.HexToRGBA(topColor);
        display.fill(color[0], color[1], color[2]);
        display.rect(x + borderSize, y + borderSize
                , width - 2 * borderSize, (height- 2 * borderSize) / 2.0f);
        color = Color.HexToRGBA(bottomColor);
        display.fill(color[0], color[1], color[2]);
        display.rect(x + borderSize, y + height / 2.0f
                , width - 2 * borderSize, (height- 2 * borderSize) / 2.0f);

        color = Color.HexToRGBA(textColor);
        display.fill(color[0], color[1], color[2]);
        display.textFont(this.textPFont);
        display.textSize(this.textSize);
        display.textAlign(window.CENTER, window.CENTER);
        display.text(this.text
                , this.x + this.width / 2.0f
                , this.y + this.height / 2.0f - borderSize);

    }

    private void updateWidth(){
        this.display.textFont(this.textPFont);
        this.display.textSize(this.textSize);
        this.width = (int) this.display.textWidth(text) + 10
                + 2 * this.borderSize;
        this.isWidthUpdated = true;
    }
}
