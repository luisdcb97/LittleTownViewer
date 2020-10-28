package littletownviewer;

import com.sun.istack.internal.NotNull;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.logging.Logger;

public class SaveFileManager {
    private static final Logger errorLog = Logger.getLogger(
            SaveFileManager.class.getName());
    private static final String APPDATA_PATH =
            System.getProperty("user.home") +
                    "\\AppData\\LocalLow\\SmashGames\\Littlewood\\";

    protected LittleTownViewer window;

    protected JSONObject json;
    protected int saveNumber;
    protected boolean fileExists;


    protected String saveFullPath;

    public SaveFileManager(@NotNull LittleTownViewer window, int saveNumber){
        this.setWindow(window);
        this.saveNumber = saveNumber;
        this.saveFullPath = String.format("%sgames%d.json",
                APPDATA_PATH, saveNumber);
        this.fileExists = false;
        this.json = null;
    }

    private void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    public boolean loadGameData(){
        try {
            this.json = window.loadJSONObject(saveFullPath);
            this.fileExists = true;
            errorLog.info(String.format("Savefile '%s' loaded.",
                    saveFullPath.substring(
                            saveFullPath.lastIndexOf("\\") + 1)));
        } catch(NullPointerException np){
            this.fileExists = false;
            this.json = null;
            errorLog.warning(
                    String.format("Savefile '%s' not found!", saveFullPath));
            this.window.notifyUser(
                    String.format(
                            "There was an error opening save game number %d"
                            , this.saveNumber
                    )
            );
        }
        return this.fileExists;
    }

    public boolean saveGameData(){
        this.window.saveJSONObject(json, saveFullPath);
        this.window.saveJSONObject(json,
                String.format("%sgames%dBACKUP.json",
                        APPDATA_PATH, this.saveNumber
                )
        );
        errorLog.info(String.format("Saved game to file %d", this.saveNumber));
        return true;
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

    public int getInteger(@NotNull String key) {
        if(fileExists){
            return this.json.getInt(key);
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

    public void setInteger(@NotNull String key, int value) {
        if(fileExists){
            this.json.setInt(key, value);
        }else {
            throw new IllegalStateException();
        }
    }

    public JSONArray getJSONArray(@NotNull String key) {
        if(fileExists){
            return this.json.getJSONArray(key);
        }else {
            throw new IllegalStateException();
        }
    }

    public void setJSONArray(@NotNull String key, JSONArray value) {
        if(fileExists){
            this.json.setJSONArray(key, value);
        }else {
            throw new IllegalStateException();
        }
    }
}
