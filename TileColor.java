package harkkatyo;

import java.awt.Color;

public enum TileColor {
    PINK(250,205,213), YELLOW(255,252,188), GREEN(217,255,227),
    BLUE(204,237,253), PURPLE(227,195,255);
    
    private int valueR;
    private int valueG;
    private int valueB;
 
    private TileColor(){}
    
    private TileColor(Color c) {
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        this.valueR = red;
        this.valueG = green;
        this.valueB = blue;
    }

    private TileColor(int a, int b, int c){
    	this.valueR = a;
        this.valueG = b;
        this.valueB = c;
    }
    
    public Color getColor() {
        Color color = new Color(valueR, valueG, valueB);
        return color;
    }
}
