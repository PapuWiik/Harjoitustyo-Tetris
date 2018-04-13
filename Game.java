package harkkatyo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.JFrame;

// Game-luokka vastaa varsinaisesta pelin kulusta sekä sisältää graafisen liittymän

public class Game extends JFrame {
    
    //Pelilaudalla näkyvät kaksi erillistä paneelia
    private GamePanel gamepanel;
    private SidePanel side;
    
    //Nykyinen sekä seuraava tiili
    private Tile nextTile;
    private Tile currentTile;
    
    //Nykyisen tiilen rivi, sarake ja rotaatio tällä hetkellä
    private int currentCol;
    private int currentRow;
    private int currentRotation;
    
    //Attribuutteja liittyen pelin nykyiseen tilaan, pistemäärään, tasoon
    private boolean paused;
    private boolean gameover;
    private int score;
    private int currentLevel;
    
    //Attribuutit liittyen pelikelloon ja pelin nopeuteen
    private Clock clock;
    private static long time1 = 1500L;
    private float gameSpeed;
    private float oldSpeed;
    
    //Palikoiden mahdolliset muodot ja värit
    ArrayList<TileColor> colors = new ArrayList<>();
    ArrayList<TileShape> shapes = new ArrayList<>();
    
    //Pelilaudan konstruktori
    public Game() {
        super();
        
        //Asetetaan pelin käyttöliittymään liittyviä ulkoasuasetuksia
        setLayout(new BorderLayout());
        setResizable(false);
        setVisible(true);
        setSize(new Dimension(580,530));
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Luodaan uudet paneelit ja asetetaan ne pelilaudalle
        gamepanel = new GamePanel(this);
        side = new SidePanel(this);
        
        add(gamepanel, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);
        
        paused = false;
        gameover = false;
        
        //Lisätään kaikki mahdolliset värit ArrayListiin siltä varalta, jos halutaankin palojen vaihtavan väriä
        colors.add(TileColor.BLUE);
        colors.add(TileColor.GREEN);
        colors.add(TileColor.PINK);
        colors.add(TileColor.PURPLE);
        colors.add(TileColor.YELLOW);
        
        //Lisätään kaikki mahdolliset palojen muodot ArrayListiin
        shapes.add(TileShape.ISHAPE);
        shapes.add(TileShape.LSHAPE);
        shapes.add(TileShape.JSHAPE);
        shapes.add(TileShape.SSHAPE);
        shapes.add(TileShape.TSHAPE);
        shapes.add(TileShape.ZSHAPE);
        shapes.add(TileShape.OSHAPE);
        
        oldSpeed = 1500;

        //Arvotaan kahdesti uusi pala, jotta saadaan tallennettua pala sekä muuttujaan nextTile että currentTile
        setNextTile();
        setNextTile();
        
        //Lisätään taustamusiikki
        music();
        
        //Lisätään käyttäjän toimintoja kuunteleva KeyListener
        addKeyListener(new KeyListener() {
        
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                
                //Kun painetaan S-kirjainta, palojen putoamisnopeus kasvaa tai palaa ennalleen
                case KeyEvent.VK_S:    
                    if (!paused && time1 != 70) {
                        oldSpeed = time1;
                        time1 = 70;
                    }
                    else if (!paused) {
                        time1 = (long) oldSpeed;
                    }
                    break;
                    
                //Kun painetaan A-kirjainta, siirretään palan currentColumnia yhden pienemmäksi (mikäli mahdollista)    
                case KeyEvent.VK_A:
                    if (!paused && (currentRow + currentTile.getShape().getSize() != 20)) {
                        if (gamepanel.isFree(currentTile, currentCol - 1, currentRow, currentRotation)) {
                            currentCol--;
                        }
                    }
                    break;
                    
                //Kun painetaan D-kirjainta, siirretään palan currentColumnia yhden suuremmaksi (mikäli mahdollista)    
                case KeyEvent.VK_D:
                    if (!paused && (currentRow + currentTile.getShape().getSize() != 20)) {
                        if (gamepanel.isFree(currentTile, currentCol + 1, currentRow, currentRotation)) {
                            currentCol++;
                    }
                }
                    break;
                    
                //Kun painetaan W-kirjainta, käännetään nykyistä palaa myötäpäivään  
                case KeyEvent.VK_W:
                    if (!paused) {
                        int newRotation;
                        if (currentRotation < 3) {
                            newRotation = currentRotation + 1;
                        }
                        else {
                            newRotation = 0;
                        }
                        rotatePiece(newRotation);
                    }
                    break;
                    
                //Kun painetaan enteriä, peli alkaa alusta (tai jatkuu pausetuksen jälkeen)    
                case KeyEvent.VK_ENTER:
                    if (paused == true) {
                        paused = false;
                    }
                    else if (gameover == true) {
                        gameover = false;
                        time1 = (long) oldSpeed;
                        resetGame();
                    }
                    break;
                }
            }
    public void keyReleased(KeyEvent e) {
        }
    public void keyTyped(KeyEvent e) {
        }   
    });
        
    }
    
    /*Main-metodissa luodaan ensiksi uusi Game-olio (piirretään pelilauta)
    ja tämän jälkeen kutsutaan startGame() metodia, joka vastaa pelin kulusta
    */
    public static void main(String[] args) {
        Game theGame = new Game();
        theGame.startGame();
    }
    
    //GETTERIT JA SETTERIT    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getRotation() {
        return currentRotation;
    }
    
    public int getRow() {
        return currentRow;
    }
    
    public int getCol() {
        return currentCol;
    }
    
    public boolean getGameover() {
        return gameover;
    }
    
    public boolean getPaused() {
        return paused;
    }
    
    public void setLevel(int i) {
        currentLevel = i;
    }
    
    public void setScore(int i) {
        score = i;
    }
    
    public void setGameover(boolean b) {
        gameover = b;
    }
    
    public void setNextTile() {
        
        currentTile = nextTile;
        
        Random rnd = new Random();
        int a = rnd.nextInt(colors.size());
        int b = rnd.nextInt(shapes.size());
        
        nextTile = new Tile(shapes.get(b).getColor(),shapes.get(b), 10, 10, 100, 100);
        
    }
    
    public Tile getNextTile() {
        return nextTile;
    }
    
    public Tile getCurrentTile() {
        return currentTile;
    }
    
    // Pelin kulusta vastaava startGame()
    public void startGame() {
        
        //asetetaan pelin aloitusnopeus
        gameSpeed = 1.0f;
        this.clock = new Clock((1/gameSpeed)*1000);
        
        while(true) {
            long time = System.nanoTime();
            clock.update();
            
            //Kello päivittää itseään jatkuvasti. Kun yksi kierros on kellon mukaan kulunut, päivitetään pelilauta
            if (clock.hasElapsedCycle()) {
                updateGame();
            }
            
            //Piirretään pelilaudan paneelit uudelleen (muutoksien varalta)
            renderGame();
            
            //Peli odottaa hetken ennen kuin palaa suorittamaan looppia uudelleen
            long delta = (System.nanoTime() - time) / 1000000L;
            if (delta < time1) {
                try {
                    Thread.sleep(time1-delta);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //updateGame()-metodia kutsutaan aina kellon edettyä kierroksen verran eteenpäin
    public void updateGame() {
        //Mikäli mahdollista, siirretään nykyistä palaa rivi alemmas
        if (gamepanel.isFree(currentTile, currentCol, currentRow + 1, currentRotation)) {
            currentRow++;
        }
        /*Jos rivin alaspäin siirtäminen ei onnistu, lisätään pala "osaksi pelilautaa".
        Tällöin täytyy myös tarkistaa, onko pelilaudalle muodostunut kyseisen siirron yhteydessä
        täysinäisiä rivejä. Mikäli on, kasvatetaan pelaajan scorea ja päivitetään sivupaneeli.
        */
        else {
            gamepanel.addPiece(currentTile, currentCol, currentRow, currentRotation);
            int fullRows = gamepanel.countFullLines();
            if (fullRows > 0) {
                score = score + (100*fullRows);
                fullRows = 0;
                side.repaint();
            }
            
            //Nopeutetaan kellon toimintaa hiukan
            gameSpeed += 0.035f;
            time1 += 0.035f;
            clock.setSpeed(gameSpeed);
            
            //Päivitetään tämän hetkinen taso pelinopeuden mukaisesti
            currentLevel = (int)(gameSpeed * 1.70f);
            
            //Arvotaan seuraava, uusi pala
            createNewPiece();
        }
    }
    
    //Piirtää pelilaudan paneelit uudelleen
    public void renderGame() {
        gamepanel.repaint();
        side.repaint();
    }
    
    //Vastaa taustamusiikista, joka löytyy tiedostosta music.wav. Tätä toistetaan loputtomasti pelin ollessa käynnissä
    public static void music() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }   catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
    
    //Metodi, jolla luodaan uusi pala edellisen saavuttua pohjalle
    private void createNewPiece() {
        this.currentTile = nextTile;
	this.currentCol = currentTile.getShape().getStartingCol();
	this.currentRow = currentTile.getShape().getStartingRow();
	this.currentRotation = 0;
	setNextTile();
        //Mikäli uutta palaa ei pystytä asettamaan alustalle, loppuu peli ja pysähtyy kello
	if(gamepanel.isFree(currentTile, currentCol, currentRow, currentRotation) == false) {
            setGameover(true);
            clock.setPaused(true);
        }
    }
    
    /*Metodi, jonka avulla paloja käännetään. Mikäli isFree()-metodi myöntää luvan,
    muutetaan kääntämiseen tarpeellisia currentRotation, currentRow ja currentCol
    muuttujia.
    */
    private void rotatePiece(int rotation) {
        int col = currentCol;
        int row = currentRow;
        
        if (gamepanel.isFree(currentTile, col, row, rotation)) {
            currentRotation = rotation;
            currentRow = row;
            currentCol = col;
        }
    }
    
    // Metodi, joka aloittaa pelin alusta. Tätä kutsutaan tilanteessa, jossa peli loppuu ja pelaaja haluaa aloittaa uuden.
    public void resetGame() {
        currentLevel = 1;
        score = 0;
        gameSpeed = 1.0f;
        setNextTile();
        setNextTile();
        gamepanel.clearBoard();
        clock.reset();
        clock.setSpeed(gameSpeed);
        createNewPiece();
    }
}
