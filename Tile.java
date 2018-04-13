package harkkatyo;

import java.awt.Color;

//Luokka, jossa kuvataan yksittäistä palaa, sen muotoa, väriä yms
public class Tile {
    
    //Palan attribuutit
    private Color color;
    private TileShape shape;
    private int rotation;
    private float x;
    private float y;
    private float width;
    private float height;
            
    //Palan konstruktorit, joista jälkimmäinen asettaa halutut arvot attribuutteihin
    public Tile() {
    }
    
    public Tile(Color color, TileShape shape, float x, float y, float width, float height) {
        this.color = color;
        this.shape = shape;
        rotation = 0;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
    }
    
    //Getterit ja setterit
    public TileShape getShape() {
        return shape;
    }
    
    public float getWidth() {
        return width;
    }
    
    /*Metodi, joka tarkistaa onko kyseinen pala olemassa kyseisessä ruudussa:
    
      1 2 3              1 2 3
    1 *                1
    2 *                2     *
    3 * *              3 * * *
    
    Esimerkiksi ylle piirretty sama pala palauttaa metodissa eri tuloksen rotaatiosta riippuen
    
    tileExists(1,1,1) -> true
    tileExists(1,1,2) -> false
    
    */
    public boolean tileExists (int i, int j, int rotation) {
        return getShape().getIfVisible(i, j, rotation);
    }
}
