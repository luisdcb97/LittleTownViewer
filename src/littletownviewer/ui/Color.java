package littletownviewer.ui;


import com.sun.istack.internal.NotNull;

public class Color {
    private Color(){}

    public static int[] HexToRGB(@NotNull String hexString){
        String r = hexString.substring(1, 3);
        String g = hexString.substring(3, 5);
        String b = hexString.substring(5, 7);
        int intR = Integer.parseInt(r, 16);
        int intG = Integer.parseInt(g, 16);
        int intB = Integer.parseInt(b, 16);
        return new int[] {intR, intG, intB};
    }
}
