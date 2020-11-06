package littletownviewer;

import com.sun.istack.internal.NotNull;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.logging.Logger;

public class SaveFileManager {
    private static final Logger errorLog = Logger.getLogger(
            SaveFileManager.class.getName());
    private static final String saveFolderPath = getSaveFolderPath();

    protected LittleTownViewer window;

    protected JSONObject json;
    protected int saveNumber;
    protected boolean fileExists;


    protected String saveFullPath;

    private static String getSaveFolderPath(){
        String osName = System.getProperty("os.name", "Windows")
                .toLowerCase();
        String sep = System.getProperty("file.separator", "/");

        StringBuilder builder =
                new StringBuilder(System.getProperty("user.home"));

        if(osName.contains("window")){
            builder.append(sep)
                    .append("AppData")
                    .append(sep)
                    .append("LocalLow")
                    .append(sep)
                    .append("SmashGames")
                    .append(sep)
                    .append("Littlewood")
                    .append(sep);
        }
        else if(osName.contains("mac")){
            builder.append(sep)
                    .append("Library")
                    .append(sep)
                    .append("Application Support")
                    .append(sep)
                    .append("SmashGames")
                    .append(sep)
                    .append("Littlewood")
                    .append(sep);
        }
        else{
            builder.append(sep)
                    .append(".config")
                    .append(sep)
                    .append("unity3d")
                    .append(sep)
                    .append("SmashGames")
                    .append(sep)
                    .append("Littlewood")
                    .append(sep);
        }

        return builder.toString();
    }

    public SaveFileManager(@NotNull LittleTownViewer window, int saveNumber){
        this.setWindow(window);
        this.saveNumber = saveNumber;
        this.saveFullPath = String.format("%sgames%d.json",
                saveFolderPath, saveNumber);
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
            errorLog.info(String.format("Savefile '%s' loaded.", saveNumber));
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
                        saveFolderPath, this.saveNumber
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
