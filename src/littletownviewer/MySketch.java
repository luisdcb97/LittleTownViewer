package littletownviewer;

import littletownviewer.ui.Notification;
import processing.core.PApplet;

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
        PApplet.main(processingArgs);
    }
}