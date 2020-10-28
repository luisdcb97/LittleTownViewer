package littletownviewer.ui;

import com.sun.istack.internal.NotNull;

import littletownviewer.LittleTownViewer;
import littletownviewer.ui.buttons.CloseButton;
import processing.core.PGraphics;
import processing.event.MouseEvent;

import littletownviewer.interactive.MouseClick;

public class Notification implements Drawable, MouseClick {
    protected static final int MIN_WIDTH = 200;
    protected static final int MIN_HEIGHT = 100;
    protected static final int BUTTON_LENGTH = 30;
    protected static final int BUTTON_PADDING = 10;
    protected static final int DEFAULT_INTERVAL = 3000;

    protected LittleTownViewer window;
    protected PGraphics display;
    protected CloseButton closeButton;

    protected String message;
    protected int popUpStart;
    protected int interval;
    protected boolean showing;

    protected int x, y;
    protected int width, height;

    public Notification(@NotNull LittleTownViewer window, @NotNull String message){
        this(window, message, DEFAULT_INTERVAL);
    }

    public Notification(@NotNull LittleTownViewer window, @NotNull String message,
                        int interval)
    {
        this.setWindow(window);
        this.width = LittleTownViewer.max(
                window.width / 3, MIN_WIDTH);
        this.height = LittleTownViewer.max(
                window.height / 5, MIN_HEIGHT);
        this.display = window.createGraphics(this.width, this.height);
        this.message = message;
        this.interval = interval;
        this.showing = true;
        this.popUpStart = window.millis();
        this.closeButton = new CloseButton(window, display,
                this.width - BUTTON_LENGTH - BUTTON_PADDING,
                BUTTON_PADDING, BUTTON_LENGTH, this::close);
        this.x = this.window.width - this.width - BUTTON_PADDING;
        this.y = this.window.height - this.height - BUTTON_PADDING;
    }

    public void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    @Override
    public void setup(){

    }

    @Override
    public void draw() {
        display.beginDraw();
        display.background(50);
        this.closeButton.draw();
        display.textAlign(window.LEFT, window.TOP);
        display.textSize(16);
        display.fill(0);
        display.rectMode(window.CORNERS);
        display.text(this.message, BUTTON_PADDING, BUTTON_PADDING,
                this.width - 2 * BUTTON_PADDING - BUTTON_LENGTH,
                this.height - BUTTON_PADDING);
        display.endDraw();
        window.image(this.display, this.x, this.y);
        this.showing = window.millis() <= this.popUpStart + this.interval;
    }

    public boolean isShowing(){
        return this.showing;
    }

    public void close(){
        this.showing = false;
        this.interval = 0;
    }

    @Override
    public void mouseClicked() {
        this.mouseClicked(null);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (this.showing && this.closeButton.isMouseOver(
                window.mouseX - this.x, window.mouseY - this.y))
        {
            this.closeButton.mouseClicked();
        }
    }

    @Override
    public void mousePressed() {
        this.mousePressed(null);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (this.showing && this.closeButton.isMouseOver(
                window.mouseX - this.x, window.mouseY - this.y))
        {
            this.closeButton.mousePressed();
        }
    }

    @Override
    public void mouseReleased() {
        this.mouseReleased(null);
    }

    @Override
    public void mouseReleased(MouseEvent event) {}
}
