package littletownviewer;

import littletownviewer.ui.Notification;
import littletownviewer.ui.Screen;
import littletownviewer.ui.screens.SavesListMenu;
import processing.core.PApplet;

import java.io.IOException;
import java.util.logging.*;

public class MySketch extends PApplet{
    private Notification notificationTray;

    private Screen currentScreen, previousScreen;

    public void settings(){
        size(1200, 675);
    }

    public void setup(){
        notificationTray = new Notification(this, "", 0);
        currentScreen = new SavesListMenu(this);
        currentScreen.setup();
    }

    public void draw(){
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

    public void keyTyped(){
        currentScreen.keyTyped();
    }

    public static void main(String[] args){
        String[] processingArgs = {"littletownviewer.MySketch"};

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
}