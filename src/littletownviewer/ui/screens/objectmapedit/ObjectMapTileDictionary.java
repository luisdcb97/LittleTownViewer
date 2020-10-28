package littletownviewer.ui.screens.objectmapedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.*;

public final class ObjectMapTileDictionary {

    private LittleTownViewer window;
    private Map<String, List<String>> categories;
    private Map<String, List<ObjectMapTile>> subcategories;
    private Map<String, ObjectMapTile> tileDictionary;

    public ObjectMapTileDictionary(@NotNull LittleTownViewer window, String tileListFilename) {
        this.setWindow(window);

        categories = new LinkedHashMap<>();
        subcategories = new LinkedHashMap<>();
        tileDictionary = new LinkedHashMap<>(100);

        JSONArray tileJSON = window.loadJSONArray(
                tileListFilename);

        for (int i = 0; i < tileJSON.size(); i++) {
            JSONObject cat = tileJSON.getJSONObject(i);
            String catName = cat.getString("Name");
            JSONArray catSubcats = cat.getJSONArray("SubCategory");
            List<String> subcatsList = new ArrayList<>();

            for (int j = 0; j < catSubcats.size(); j++) {
                JSONObject subcat = catSubcats.getJSONObject(j);
                String subcatName = subcat.getString("Name");
                subcatsList.add(subcatName);

                JSONArray subcatTiles = subcat.getJSONArray("Tiles");
                List<ObjectMapTile> tileList = new ArrayList<>();

                for (int k = 0; k < subcatTiles.size(); k++) {
                    JSONObject tile = subcatTiles.getJSONObject(k);
                    int width = tile.getInt("Width", 1);
                    int height = tile.getInt("Height", 1);
                    int displayWidth =
                            tile.getInt("DisplayWidth", 1);
                    int displayHeight =
                            tile.getInt("DisplayHeight", 1);
                    ObjectMapTile mapTile = new ObjectMapTile(
                            this.window
                            , tile.getString("Name")
                            , new String[] {catName, subcatName}
                            , width, height, displayWidth, displayHeight
                    );
                    tileList.add(mapTile);
                    tileDictionary.put(mapTile.label(), mapTile);
                }

                subcategories.put(subcatName
                        , Collections.unmodifiableList(tileList));
            }

            categories.put(catName, Collections.unmodifiableList(subcatsList));
        }

        categories = Collections.unmodifiableMap(categories);
        subcategories = Collections.unmodifiableMap(subcategories);
        tileDictionary = Collections.unmodifiableMap(tileDictionary);
    }

    private void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    public Optional<ObjectMapTile> getTileByLabel(String label){
        ObjectMapTile tile = tileDictionary.get(label);
        return Optional.ofNullable(tile);
    }

    public List<String> getCategories(){
        List<String> cat = new ArrayList<>(categories.keySet());
        return Collections.unmodifiableList(cat);
    }

    public List<String> getSubcategoriesOf(String category){
        List<String> subcat = categories.getOrDefault(
                category, Collections.emptyList());
        return Collections.unmodifiableList(subcat);
    }

    public List<ObjectMapTile> getTilesInSubcategory(String name){
        List<ObjectMapTile> tiles = subcategories.getOrDefault(
                name, Collections.emptyList());
        return Collections.unmodifiableList(tiles);
    }

    public void addCustomTile(ObjectMapTile tile){
        List<ObjectMapTile> customTiles = subcategories.get("Custom");
        List<ObjectMapTile> newList = new ArrayList<>(customTiles);
        newList.add(tile);
        Map<String, List<ObjectMapTile>> newSubcatMap =
                new LinkedHashMap<>(subcategories);
        newSubcatMap.put("Custom", Collections.unmodifiableList(newList));
        subcategories = Collections.unmodifiableMap(newSubcatMap);
        Map<String, ObjectMapTile> newMap = new LinkedHashMap<>(tileDictionary);
        newMap.put(tile.label(), tile);
        tileDictionary = Collections.unmodifiableMap(newMap);
    }
}
