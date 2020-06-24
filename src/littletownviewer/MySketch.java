package littletownviewer;

import littletownviewer.ui.Notification;
import processing.core.PApplet;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MySketch extends PApplet{
    Notification notificationTray;
    int i = 0;

    public void settings(){
        size(1200, 675);
    }

    public void setup(){
        notificationTray = new Notification(this, "ola");
    }

    public void draw(){
        background(15);
        if(notificationTray.isShowing()){
            notificationTray.draw();
        }
    }

    public void mouseClicked(){
        if(notificationTray.isShowing()){
            notificationTray.mouseClicked();
        }
    }

    public void keyTyped(){
        notificationTray = new Notification(this,
                "ola" + String.valueOf(++i));
    }

    public static void main(String[] args){
        String[] processingArgs = {"littletownviewer.MySketch"};

        Handler fh = null;
        try {
            fh = new FileHandler("littleTownViewer.log");
            Logger.getLogger("").addHandler(fh);
            PApplet.main(processingArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}