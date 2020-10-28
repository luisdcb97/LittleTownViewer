package littletownviewer.ui.screens.inventoryedit;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.*;

public class InventoryItemDictionary {
    private Map<String, List<InventoryItem>> categories;

    public InventoryItemDictionary(@NotNull LittleTownViewer window
            , String itemListFilename) {
        categories = new LinkedHashMap<>();

        JSONArray itemJSON = window.loadJSONArray(
                itemListFilename);

        for (int i = 0; i < itemJSON.size(); i++) {
            JSONObject cat = itemJSON.getJSONObject(i);
            String catName = cat.getString("Name");
            JSONArray catItems = cat.getJSONArray("Items");
            List<InventoryItem> itemList = new ArrayList<>();

            for (int j = 0; j < catItems.size(); j++) {
                JSONObject itemObj = catItems.getJSONObject(j);
                int id = itemObj.getInt("Id");
                String name = itemObj.getString("Name");
                InventoryItem item =
                        new InventoryItem(window, id, name, catName, 0);
                itemList.add(item);
            }
            categories.put(catName, Collections.unmodifiableList(itemList));

        }
        this.categories = Collections.unmodifiableMap(this.categories);
    }

    public List<String> getCategories(){
        List<String> cat = new ArrayList<>(categories.keySet());
        return Collections.unmodifiableList(cat);
    }

    public List<InventoryItem> getCategoryItems(String category){
        List<InventoryItem> list = categories.getOrDefault(category
                , Collections.emptyList());
        return Collections.unmodifiableList(list);
    }

    public Map<Integer, Integer> getInventoryIdMap(){
        Map<Integer, Integer> map = new HashMap<>();

        for (List<InventoryItem> itemList: categories.values()) {
            for (InventoryItem item : itemList) {
                map.put(item.id(), item.quantity());
            }
        }
        return Collections.unmodifiableMap(map);
    }

    public Optional<InventoryItem> getItemById(int id){
        for (List<InventoryItem> itemList: categories.values()){
            for (InventoryItem item: itemList){
                if (id == item.id()){
                    return Optional.of(item);
                }
            }
        }
        return Optional.empty();
    }
}
