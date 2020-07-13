package littletownviewer.ui.buttons;

import com.sun.istack.internal.NotNull;

import littletownviewer.MySketch;
import littletownviewer.ui.Color;
import processing.core.PGraphics;

import processing.event.MouseEvent;

public class CloseButton extends Button {
    protected static final String DEFAULT_FILL_COLOR = "#FF0000";
    protected static final String DEFAULT_STROKE_COLOR = "#000000";

    protected String fillColor;
    protected String strokeColor;

    public CloseButton(@NotNull MySketch window, int x, int y,
                       int length, @NotNull Runnable clickAction)
    {
        this(window, window.g, x, y, length, clickAction);
    }

    public CloseButton(@NotNull MySketch window, @NotNull PGraphics display,
                       int x, int y, int length, @NotNull Runnable clickAction)
    {
        this.setWindow(window);
        this.display = display;
        this.x = x;
        this.y = y;
        this.width = length;
        this.height = length;
        this.fillColor = DEFAULT_FILL_COLOR;
        this.strokeColor = DEFAULT_STROKE_COLOR;
        this.clickAction = clickAction;
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
        this.mouseClicked(null);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        this.clickAction.run();
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
    public void mouseReleased() {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void setup() {}

    @Override
    public void draw() {
        display.rectMode(window.CORNER);
        int[] color = Color.HexToRGB(strokeColor);
        display.stroke(color[0], color[1], color[2]);
        color = Color.HexToRGB(fillColor);
        display.fill(color[0], color[1], color[2]);
        display.strokeWeight(2);
        display.rect(x, y, width, height);

        display.line(x + 5, y + 5,
                x + width - 5, y + height - 5);
        display.line(x + 5, y + height - 5,
                x + width - 5, y + 5);
    }
}
