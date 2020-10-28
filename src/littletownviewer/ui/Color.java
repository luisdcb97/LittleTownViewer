package littletownviewer.ui;
import com.sun.istack.internal.NotNull;
import littletownviewer.LittleTownViewer;

/**
 *
 */
public class Color {
    private Color(){}

    public static int[] HexToRGBA(@NotNull String hexString){
        String[] rgba = Color.splitHex(hexString);
        String r = rgba[0];
        String g = rgba[1];
        String b = rgba[2];
        String a = rgba[3];
        int intR = Integer.parseInt(r, 16);
        int intG = Integer.parseInt(g, 16);
        int intB = Integer.parseInt(b, 16);
        int intA = Integer.parseInt(a, 16);
        return new int[] {intR, intG, intB, intA};
    }

    public static int HexToInt(@NotNull String hexString){
        int[] rgb = Color.HexToRGBA(hexString);
        int a = rgb[3] << 24;
        int r = rgb[0] << 16;
        int g = rgb[1] << 8;
        int b = rgb[2];
        return a | r | g | b;
    }

    public static int brighten(int color, int percentage){
        if (percentage < 0 || percentage > 100){
            throw new IllegalArgumentException();
        }
        float factor = percentage / 100.0f + 1;
        int rgb, r, g, b;
        rgb = 0x00FFFFFF & color;
        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = rgb & 0xFF;

        r = LittleTownViewer.min((int) (r * factor), 255) << 16;
        g = LittleTownViewer.min((int) (g * factor), 255) << 8;
        b = LittleTownViewer.min((int) (b * factor), 255);
        return 0xFF000000 | r | g | b;
    }

    public static String[] splitHex(@NotNull String hexColor){
        if (hexColor.startsWith("#")){
            hexColor = hexColor.substring(1);
        }
        else if (hexColor.startsWith("0x")){
            hexColor = hexColor.substring(2);
        }

        String[] colors = new String[4];
        int rgbStart = 0;
        if (hexColor.length() == 8){
            colors[3] = hexColor.substring(0, 2);
            rgbStart = 2;
        }
        else {
            colors[3] = "FF";
        }
        colors[0] = hexColor.substring(rgbStart, rgbStart + 2);
        colors[1] = hexColor.substring(rgbStart + 2, rgbStart + 4);
        colors[2] = hexColor.substring(rgbStart + 4, rgbStart + 6);

        return colors;
    }
}
