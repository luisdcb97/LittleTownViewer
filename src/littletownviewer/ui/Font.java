package littletownviewer.ui;

import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;
import processing.core.PFont;

import java.util.HashMap;
import java.util.Map;

public class Font {
    private static Map<String, PFont> fonts;

    private Font(){}

    public static PFont getFont(@NotNull LittleTownViewer window,
                                String typeface, int size){
        if(fonts == null){
            fonts = new HashMap<>();
        }
        String key = String.format("%s-%d", typeface, size);
        PFont font = Font.fonts.get(key);
        if (font == null) {
            font = window.createFont(typeface, size);
            Font.fonts.put(key, font);
        }
        return font;
    }

    public static void clearFonts(){
        Font.fonts.clear();
    }

    public static String[] getSystemTypefaces(){
        return PFont.list();
    }
}
