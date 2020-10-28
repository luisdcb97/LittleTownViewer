package littletownviewer;

import littletownviewer.ui.Notification;
import littletownviewer.ui.Screen;
import littletownviewer.ui.buttons.LoadingButton;
import littletownviewer.ui.screens.LoadingScreen;
import littletownviewer.ui.screens.menu.MainMenu;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.IOException;
import java.util.logging.*;

public class LittleTownViewer extends PApplet{
    private Notification notificationTray;

    private Screen currentScreen;

    public void settings(){
        size(1200, 675);
    }

    public void setup(){
        notificationTray = new Notification(this, "", 0);
        this.moveToScreen(
                new LoadingScreen(this, new MainMenu(this)
                        , "Loading application...")
        );
    }

    public void draw(){
        LoadingButton.ageButtons();

        currentScreen.draw();

        if(notificationTray.isShowing()){
            notificationTray.draw();
        }

    }

    @Override
    public void mousePressed(){
        if(notificationTray.isShowing()){
            notificationTray.mousePressed();
        }
        else{
            currentScreen.mousePressed();
        }
    }

    @Override
    public void keyTyped(){
        currentScreen.keyTyped();
    }

    @Override
    public void keyPressed(KeyEvent event){
        currentScreen.keyPressed(event);
    }

    @Override
    public void mouseWheel(MouseEvent event){
        currentScreen.MouseWheel(event);
    }

    public static void main(String[] args){
        String[] processingArgs = {"littletownviewer.LittleTownViewer"};

        Handler fh;
        try {
            fh = new FileHandler("littleTownViewer.log");
            fh.setLevel(Level.INFO);
            Formatter fmt = Logger.getLogger("").getHandlers()[0].getFormatter();
            fh.setFormatter(fmt);
            Logger.getLogger("").addHandler(fh);
            PApplet.main(processingArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(String message){
        this.notificationTray = new Notification(this, message);
    }

    public void moveToScreen(Screen destination){
        currentScreen = destination;
        this.getSurface().setTitle(
                "LittleTownViewer - " + this.getSimpleScreenName());
        if(destination instanceof LoadingScreen){
            thread("loadingSetup");
        }
    }

    public void loadingSetup(){
        if(!(currentScreen instanceof LoadingScreen)){
            return;
        }
        currentScreen.setup();
    }

    public String getScreenName(){
        return this.currentScreen.getClass().getCanonicalName();
    }

    public String getSimpleScreenName(){
        return this.currentScreen.getClass().getSimpleName();
    }

}