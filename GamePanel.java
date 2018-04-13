package harkkatyo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

//Luokka, joka vastaa peliruudukosta
public class GamePanel extends JPanel {
    
    //Ruudukossa olevat sarakkeet ja rivit
    private static final int COLS = 10;
    private static final int ROWS = 20;
    //Yksittäisen tiilen sivun pituus (pikseleissä)
    private static final int TILE = 28;

    private Game game;
    //Matriisi, johon on tallennettu jo pelilaudan pohjan saavuttaneet palat
    private TileShape[][] tiles;
    
    
    public GamePanel(Game game) {
        this.game = game;
        this.tiles = new TileShape[ROWS][COLS];
        
        setBackground(new Color(197,203,206));
        setPreferredSize(new Dimension(620,260));
        
        clearBoard();
        
    }
    
    //Täyttää pelilaudan täyteen paloja (käytetty testauksessa, ei valmiissa pelissä)
    public void fillBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; i < COLS; j++) {
                tiles[i][j].setIfVisible(i, j, true);
            }
            }
        }
    
    //Tyhjentää laudan kiinteistä paloista. Kutsutaan aloitettaessa uutta peliä.
    public void clearBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                tiles[i][j] = null;
            }
        }
    }
    
    //Getterit ja setterit
    public TileShape getTile(int x, int y) {
        return tiles[x][y];
    }
    
    public void setTile(int a, int b, Tile tile) {
        tiles[b][a] = tile.getShape();
    }    
    
    public void setTile(int a, int b, TileShape tile) {
        tiles[b][a] = tile;
    } 
    
    //Metodi, jolla piirretään koko pelialusta
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //Jos peli on pausella, näytetään pelkästään PAUSED -teksti
        if (game.getPaused()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.PLAIN, 20));
            String msg = "PAUSED";
            g.drawString(msg, 10 * 28 / 2 - g.getFontMetrics().stringWidth(msg) / 2, 20 * 28 / 2);          
        }
        //Jos peli loppuu, näytetään tieto siitä ja mahdollisuus aloittaa uusi peli
        else if (game.getGameover()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.PLAIN, 20));
            String msg = "TETRIS - GAME OVER";
            g.drawString(msg, 50, 150);
            g.setFont(new Font("Calibri", Font.PLAIN, 16));
            msg = "Press Enter to Play Again";
            g.drawString(msg, 50, 200);
            
        }
        //Tavallisessa pelitilanteessa alusta piirretään tämän mukaan
        else {
            //Piirretään jo pelilaudalla kiinteät palat
            for (int i = 0; i < COLS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    TileShape tile = getTile(j,i);
                    if (tile != null) {
                        drawTile(tile, i * TILE, (j-2)* TILE, g);
                }
            }
        }
        
        //Haetaan nykyinen putoava pala sekä sen sijaintitiedot
        TileShape tile = game.getCurrentTile().getShape();
        int pieceRow = game.getRow();
        int pieceCol = game.getCol();
        int rotation = game.getRotation();
        
        //Piirretään putoava pala
        for (int i = 0; i < tile.getSize(); i++) {
            for (int j = 0; j < tile.getSize(); j++) {
                if (tile.getIfVisible(i, j, rotation) && pieceRow + j >= 2) {
                    drawTile(tile,(pieceCol + i)*28,(pieceRow + j - 2)*28, g);
                }
            }
        }
        
        //Piirretään ruudukko selkeyttämään pelialustaa
        g.setColor(new Color(246,238,238));
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS-2; j++) {
                g.drawLine(0, j * TILE, TILE * COLS, j * TILE);
                g.drawLine(i*TILE, 0, i*TILE, TILE * (ROWS-2));
            }
        }
        //Piirretään ääriviivat
        g.setColor(new Color(246,238,238));
        g.drawRect(0, 0, TILE * COLS, TILE * (ROWS - 2));
        
        Tile currentTile = game.getCurrentTile();
    }
    }
    
    public void drawTile(TileShape tile, int x, int y, Graphics g) {
        drawTile(x,y,g);
    }
    
    //Metodi, jolla piirretään yksittäinen neliö (osa josta varsinaiset palat muodostuvat)
    public void drawTile(int x, int y, Graphics g) {
        g.setColor(new Color(238,220,220));
        g.fillRect(x, y, TILE, TILE);
        
        g.setColor(new Color(231,208,208));
        g.fillRect(x, y+23, 28, 5);
        g.fillRect(x+23,y,5,28);
        
        g.setColor(new Color(242,230,230));
        g.fillRect(x, y, 5, 28);
        g.fillRect(x, y, 28, 5);
    }
    
    //Metodi, jolla tarkistetaan, onko palaa mahdollista siirtää toivottuun suuntaan
    public boolean isFree(Tile tile, int x, int y, int rotation) {
        
        //Pala on jo vasemmassa reunassa
        if (x < -tile.getShape().getLeft(rotation)) {
            return false;
        }
        else if (y < -tile.getShape().getTop(rotation)) {
            return false;
        }
        //Pala on jo oikeassa reunassa
        else if (x + tile.getShape().getSize() - tile.getShape().getRight(rotation) >= COLS) {
            return false;
        }
        //Pala on jo pohjalla
        else if (y + tile.getShape().getSize() - tile.getShape().getBottom(rotation) >= ROWS) {
            return false;
        }
        
        for (int i = 0; i < tile.getShape().getSize(); i++) {
            for (int j = 0; j < tile.getShape().getSize(); j++) {
                if (tile.tileExists(i, j, rotation) && ! isFreeTile(x+i, y+j)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isFreeTile(int x, int y) {
        boolean free = (tiles[y][x] == null);
        return free;
    }
    
    
    public void addPiece(Tile tile, int x, int y, int rotation) {
        for (int i = 0; i < tile.getShape().getSize(); i++) {
            for (int j = 0; j < tile.getShape().getSize(); j++) {
                if (tile.tileExists(i, j, rotation)) {
                    setTile(i+x, j+y, tile);
                }
            }
        }
    }
    
    //Lasketaan onko siirron päätteeksi jokin riveistä täyttynyt
    public int countFullLines() {
        int lines = 0;
        for (int i = 0; i < ROWS; i++) {
            if (isFullLine(i)) {
                game.setScore(game.getScore() + 100);
                lines++;
            }
        }
        return lines;
    }
    
    //Selvitetään onko rivi i täysi
    private boolean isFullLine(int i) {
        boolean full;
        for (int j = 0; j < COLS; j++) {
            if (isFreeTile(j, i)) {
                full = false;
                return full;
            }
        }
        for (int y = i - 1; y >= 0; y--) {
            for (int x = 0; x < COLS; x++) {
                setTile(x, y+1, getTile(y,x));
            }
        }
        return true;
    }
    
}