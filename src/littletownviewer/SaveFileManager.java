package littletownviewer;

import com.sun.istack.internal.NotNull;
import littletownviewer.ui.Notification;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.util.logging.Logger;

public class SaveFileManager {
    private static Logger errorLog = Logger.getLogger(
            SaveFileManager.class.getName());
    private static final String APPDATA_PATH =
            System.getProperty("user.home") +
                    "\\AppData\\LocalLow\\SmashGames\\Littlewood\\";

    protected PApplet window;

    protected JSONObject json;
    protected int saveNumber;
    protected boolean fileExists;


    protected String saveFullPath;

    public SaveFileManager(@NotNull PApplet window, int saveNumber){
        this.setWindow(window);
        this.saveNumber = saveNumber;
        this.saveFullPath = APPDATA_PATH + "games"
                + String.valueOf(saveNumber) + ".json";
        this.fileExists = false;
        this.json = null;
    }

    private void setWindow(@NotNull PApplet window){
        this.window = window;
    }

    public boolean loadGameData(){
        try {
            this.json = window.loadJSONObject(saveFullPath);
            this.fileExists = true;
        } catch(NullPointerException np){
            this.fileExists = false;
            this.json = null;
            errorLog.warning(
                    String.format("Savefile '%s' not found!", saveFullPath));
            // TODO: find a better way to add notifications
            ((MySketch) this.window).notificationTray = new Notification(
                    this.window,
                    "There was an error opening save game number " +
                            String.valueOf(this.saveNumber));
        }
        return this.fileExists;
    }

    public void saveGameData(){
        this.window.saveJSONObject(json, saveFullPath);
        this.window.saveJSONObject(json,
                APPDATA_PATH + "games" +
                        String.valueOf(this.saveNumber) + "BACKUP.json");
        errorLog.info(String.format("Saved game to file %d", this.saveNumber));
    }

    public boolean isFileReady() {
        return fileExists;
    }

    public int getSaveNumber(){
        return this.saveNumber;
    }

    public String getString(@NotNull String key) {
        if(fileExists){
            return this.json.getString(key);
        }else {
            throw new IllegalStateException();
        }
    }

    public void setString(@NotNull String key, String value) {
        if(fileExists){
            this.json.setString(key, value);
        }else {
            throw new IllegalStateException();
        }
    }
}
