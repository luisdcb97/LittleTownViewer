package littletownviewer.ui.screens.objectmapedit.sidebar;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import littletownviewer.interactive.MouseClick;
import littletownviewer.interactive.MouseScroll;
import littletownviewer.ui.Drawable;
import littletownviewer.ui.screens.objectmapedit.ObjectMapTile;
import littletownviewer.ui.screens.objectmapedit.ObjectMapTileDictionary;
import processing.event.MouseEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ObjectMapSidebar implements Drawable, MouseClick, MouseScroll {
    private LittleTownViewer window;
    private final ObjectMapTileDictionary tileDictionary;
    private final ObjectMapTileSelection tileSelection;
    private String selectedCategory;
    private String selectedSubCategory;
    private int categoryScrollOffset;
    private int subCategoryScrollOffset;

    private final List<ObjectMapTileCategoryButton> categoryButtons;
    private final Map<String, ObjectMapTileCategoryButton> subCategoryButtons;
    private final int maxTileRows = 5;

    private int width;
    private int height;
    private int tileSize;
    private int categorySize;
    private int horizontalPadding;
    private int verticalPadding;

    public ObjectMapSidebar(@NotNull LittleTownViewer window
            , ObjectMapTileDictionary tileDictionary) {
        this.setWindow(window);
        this.tileDictionary = tileDictionary;
        this.width = (int) (this.window.width / 3.0f);
        this.height = this.window.height;
        this.tileSize = 48;
        this.categorySize = 48;
        this.horizontalPadding = 20;
        this.verticalPadding = 5;
        this.categoryScrollOffset = this.subCategoryScrollOffset = 0;
        this.tileSelection = new ObjectMapTileSelection(
                this.window, this.horizontalPadding, 140,
                this.width - 2 * this.horizontalPadding,
                (this.verticalPadding + this.tileSize) * this.maxTileRows,
                this.tileSize, this.verticalPadding
        );

        ObjectMapTileCategoryButton.setTextureFolder("Resources/Textures");
        categoryButtons = new ArrayList<>();
        subCategoryButtons = new HashMap<>();
        for (int i = 0; i < this.tileDictionary.getCategories().size(); i++) {
            String cat = this.tileDictionary.getCategories().get(i);
            categoryButtons.add(new ObjectMapTileCategoryButton(
                    this.window,
                    this.horizontalPadding + (this.categorySize + this.verticalPadding) * i,
                    20,
                    cat,
                    this.categorySize,
                    () -> selectCategory(cat)
            ));
        }
    }

    @Override
    public void setup() {
        this.categoryButtons.forEach(ObjectMapTileCategoryButton::setup);
        this.selectedCategory = tileDictionary.getCategories().get(0);
        loadSubCategoryButtons(this.selectedCategory);
        this.tileDictionary.getSubcategoriesOf(selectedCategory).forEach(
                subCat -> {
                    this.subCategoryButtons.get(subCat).setup();
                });
        this.selectedSubCategory = tileDictionary
                .getSubcategoriesOf(selectedCategory)
                .get(0);

        List<ObjectMapTile> tiles = tileDictionary
                .getTilesInSubcategory(this.selectedSubCategory);

        for (ObjectMapTile tile: tiles) {
            if(!tile.hasItemTexture()) {
                tile.loadItemTexture();
            }
        }

        this.tileSelection.setup();
        this.tileSelection.setTileList(tiles);
    }

    @Override
    public void draw() {
        window.rectMode(window.CORNER);
        window.strokeWeight(1);
        window.stroke(0);
        window.fill(190, 20, 20);
        window.rect(0, 0, this.width, this.window.height);

        this.window.noStroke();
        ListIterator<ObjectMapTileCategoryButton> iterCat =
                this.categoryButtons.listIterator(this.categoryButtons.size());
        Stream.generate(iterCat::previous)
                .limit(this.categoryButtons.size())
                .forEach(categoryButton -> {
                    if(categoryButton.getX() - this.categoryScrollOffset >=
                            this.horizontalPadding
                        && categoryButton.getX() + categoryButton.getWidth()
                            - this.categoryScrollOffset <=
                            this.width - this.horizontalPadding
                    )
                    {
                        if (categoryButton.getCategory().equals(
                                this.selectedCategory))
                        {
                            this.window.fill(255, 220, 40);
                            this.window.rect(
                                    categoryButton.getX() - 2
                                            - categoryScrollOffset,
                                    categoryButton.getY() - 2,
                                    categoryButton.getWidth() + 4,
                                    categoryButton.getHeight() + 4
                            );
                        }
                        categoryButton.setHorizontalScroll(
                                this.categoryScrollOffset);
                        categoryButton.draw();
                    }
                }
                );
        int catLength;
        float barLength;
        float barStart;

        //region Draw Category Scrollbar
        catLength = (this.categorySize + this.verticalPadding)
                * this.categoryButtons.size();
        barLength = (this.width - 2.0f * this.horizontalPadding)
                / catLength;

        if(barLength < 1){

            this.window.fill(100);

            this.window.rect(
                    this.horizontalPadding,
                    20 + 3 + this.categorySize,
                    this.width - 2 * this.horizontalPadding,
                    5
            );

            barStart = (float) this.categoryScrollOffset / catLength
                    * (this.width - 2.0f * this.horizontalPadding);
            barLength *= (this.width - 2.0f * this.horizontalPadding);
            this.window.fill(250);
            this.window.rect(
                    this.horizontalPadding + barStart,
                    20 + 3 + this.categorySize,
                    barLength,
                    5
            );
        }
        //endregion


        List<String> subCats = this.tileDictionary
                .getSubcategoriesOf(this.selectedCategory);

        ListIterator<String> iter = subCats.listIterator(subCats.size());
        Stream.generate(iter::previous).limit(subCats.size()).forEach(
                subCat -> {
                    ObjectMapTileCategoryButton subCatButton =
                            this.subCategoryButtons.get(subCat);
                    if(subCatButton.getX() - this.subCategoryScrollOffset >=
                            this.horizontalPadding
                            && subCatButton.getX() + subCatButton.getWidth()
                            - this.subCategoryScrollOffset <=
                            this.width - this.horizontalPadding
                    ) {
                        if (subCat.equals(this.selectedSubCategory)) {
                            this.window.fill(255, 220, 40);
                            this.window.rect(
                                    subCatButton.getX() - 2
                                            - this.subCategoryScrollOffset,
                                    subCatButton.getY() - 2,
                                    subCatButton.getWidth() + 4,
                                    subCatButton.getHeight() + 4);
                        }
                        subCatButton.setHorizontalScroll(
                                this.subCategoryScrollOffset);
                        subCatButton.draw();
                    }
                }
                );

        //region Draw SubCategory Scrollbar
        catLength = (this.categorySize + this.verticalPadding)
                * this.subCategoryButtons.size();
        barLength = (this.width - 2.0f * this.horizontalPadding)
                / catLength;

        if(barLength < 1){

            this.window.fill(100);

            this.window.rect(
                    this.horizontalPadding,
                    80 + 3 + this.categorySize,
                    this.width - 2 * this.horizontalPadding,
                    5
            );

            barStart = (float) this.subCategoryScrollOffset / catLength
                    * (this.width - 2.0f * this.horizontalPadding);
            barLength *= (this.width - 2.0f * this.horizontalPadding);
            this.window.fill(250);
            this.window.rect(
                    this.horizontalPadding + barStart,
                    80 + 3 + this.categorySize,
                    barLength,
                    5
            );
        }
        //endregion

        this.tileSelection.draw();
    }

    private void loadSubCategoryButtons(String category){
        List<String> subCats = this.tileDictionary.getSubcategoriesOf(category);
        int i = 0;
        this.subCategoryButtons.clear();
        for (String subcat:
             subCats) {
            if(this.subCategoryButtons.get(subcat) == null){
                this.subCategoryButtons.put(subcat,
                        new ObjectMapTileCategoryButton(
                                this.window,
                                this.horizontalPadding + (this.categorySize
                                        + this.verticalPadding) * i,
                                80,
                                subcat,
                                this.categorySize,
                                () -> this.selectSubCategory(subcat),
                                true
                        )
                );

            }
            i++;
        }

    }

    private void selectCategory(String cat){
        this.selectedCategory = cat;
        loadSubCategoryButtons(cat);
        this.selectedSubCategory = this.tileDictionary
                .getSubcategoriesOf(cat).get(0);
        this.subCategoryScrollOffset = 0;
        this.tileSelection.setTileList(
                this.tileDictionary.getTilesInSubcategory(
                        this.selectedSubCategory
                )
        );
    }

    private void selectSubCategory(String subCat){
        this.selectedSubCategory = subCat;
        this.tileSelection.setTileList(
                this.tileDictionary.getTilesInSubcategory(subCat)
        );
    }

    @Override
    public void mouseClicked() {

    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed() {
        this.mousePressed(null);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        for (ObjectMapTileCategoryButton button:
             this.categoryButtons) {
            if(button.getX() >= this.horizontalPadding
                    && button.getX() <= this.width - this.horizontalPadding
                    && button.isMouseOver(
                            this.window.mouseX + this.categoryScrollOffset,
                            this.window.mouseY)
            ) {
                button.mousePressed(event);
            }
        }

        for (ObjectMapTileCategoryButton button:
                this.tileDictionary
                        .getSubcategoriesOf(this.selectedCategory)
                        .stream().map(this.subCategoryButtons::get)
                        .collect(Collectors.toList())
        ) {
            if(button.getX() >= this.horizontalPadding
                    && button.getX() <= this.width - this.horizontalPadding
                    && button.isMouseOver(
                            this.window.mouseX + this.subCategoryScrollOffset,
                            this.window.mouseY)
            ) {
                button.mousePressed(event);
            }
        }

        if(this.tileSelection.isMouseOver(
                this.window.mouseX, this.window.mouseY)
        ){
            this.tileSelection.mousePressed(event);
        }
    }

    @Override
    public void mouseReleased() {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void MouseWheel() {
        this.MouseWheel(null);
    }

    @Override
    public void MouseWheel(MouseEvent event) {
        if (this.window.mouseY >= 20
                && this.window.mouseY <= 20 + this.categorySize){
            this.categoryScrollOffset += event.getCount() * 5;
            this.categoryScrollOffset =
                    LittleTownViewer.max(this.categoryScrollOffset, 0);
            int catLength = (this.categorySize + this.verticalPadding)
                    * this.categoryButtons.size();
            if(catLength <= this.width - 2 * this.horizontalPadding){
                this.categoryScrollOffset = 0;
            }
            else{
                this.categoryScrollOffset = LittleTownViewer.min(
                        this.categoryScrollOffset,
                        catLength
                                - (this.width - 2 * this.horizontalPadding));
            }
        }
        else if (this.window.mouseY >= 80
                && this.window.mouseY <= 80 + this.categorySize){
            this.subCategoryScrollOffset += event.getCount() * 5;
            this.subCategoryScrollOffset =
                    LittleTownViewer.max(this.subCategoryScrollOffset, 0);
            int catLength = (this.categorySize + this.verticalPadding)
                    * this.subCategoryButtons.size();
            if(catLength <= this.width - 2 * this.horizontalPadding){
                this.subCategoryScrollOffset = 0;
            }
            else{
                this.subCategoryScrollOffset = LittleTownViewer.min(
                        this.subCategoryScrollOffset,
                        catLength
                                - (this.width - 2 * this.horizontalPadding));
            }
        }
        else if(this.tileSelection.isMouseOver(
                this.window.mouseX, this.window.mouseY)
        ){
            this.tileSelection.MouseWheel(event);
        }

    }

    public void setWindow(@NotNull LittleTownViewer window){
        this.window = window;
    }

    public void resize(int width, int height){
        this.width = (int) (width / 3.0f);
        this.height = height;
    }

    public void setTileSize(int tileSize){
        this.tileSize = tileSize;
    }

    public ObjectMapTile getSelectedTile(){
        return this.tileSelection.getSelectedTile();
    }

    public int getWidth() {
        return width;
    }

    public int[] dimensions(){
        return new int[]{this.width, this.height};
    }

    public int[] padding(){
        return new int[]{this.horizontalPadding, this.verticalPadding};
    }

    public int tileEndHeight(){
        return this.tileSelection.getY() + this.tileSelection.getHeight();
    }
}
