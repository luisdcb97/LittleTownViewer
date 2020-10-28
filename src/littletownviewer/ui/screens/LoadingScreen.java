package littletownviewer.ui.screens;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.ui.Screen;
import littletownviewer.ui.screens.menu.MainMenu;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.logging.Logger;

public class LoadingScreen extends Screen {
    private static final Logger errorLog = Logger.getLogger(
            LoadingScreen.class.getName());
    private final Screen destination;
    private final String message;

    public boolean isReady;
    public boolean hasError;

    public LoadingScreen(@NotNull LittleTownViewer window, Screen destination){
        this(window, destination, "Loading...");
    }

    public LoadingScreen(@NotNull LittleTownViewer window, Screen destination
            , String loadingMessage){
        this.setWindow(window);
        this.destination = destination;
        this.isReady = false;
        this.hasError = false;
        this.message = loadingMessage;
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
        try{
            this.destination.setup();
            errorLog.info(
                    "Moving to screen " + destination.getClass().getSimpleName());
            window.moveToScreen(this.destination);
        }
        catch(Exception e){
            StringBuilder msg = new StringBuilder("Error while setting up '")
                    .append(this.destination.getClass().toString())
                    .append("' screen:\n")
                    .append("Error -> ")
                    .append(e.toString())
                    .append("\nTrace -> ")
                    ;
            for (StackTraceElement traceElement: e.getStackTrace()) {
                msg.append(traceElement.toString()).append("\n\t");
            }
            errorLog.severe(msg.toString());
            window.moveToScreen(new MainMenu(this.window));
        }
    }

    @Override
    public void draw() {
        window.background(50);
        window.textAlign(window.CENTER, window.CENTER);
        window.fill(0);
        window.textSize(24);
        window.text(this.message
                , window.width / 2.0f
                , window.height / 2.0f);
    }
}
