
package harkkatyo;

//Pelin kello vastaa pelin nopeudesta sekä kuluneiden kierrosten tarkkailusta
public class Clock {
    
    private float speed;
    private boolean paused;
    
    //lastTimeen tallennetaan edellinen kerta, kun kellonaika tarkistettiin
    private long lastTime;
    
    //kierrosten määrä lasketaan nykyisen ajan ja lastTimen erotuksen avulla
    private int cycles;
    
    //Kellon konstruktori 
    public Clock(float speed) {
        this.speed = speed;
        paused = false;
        this.reset();
    }
    
    //GETTERIT JA SETTERIT
    public void setSpeed(float a) {
        speed = a;
    }

    private static final long getTime() {
        return (System.nanoTime());
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    //Päivitetään kello eli haetaan nykyinen aika ja tarkistetaan, onko lastTimeen verrattuna kulunut tarpeeksi kauan
    public void update() {
        long time = getTime();
        float delta = (float) (time - lastTime);
        
        if (!paused && delta >= 1.000000F) {
            this.cycles++;
        }
        
        this.lastTime = time;
    }
    
    //Metodi, joka tarkistaa, onko kierros kulunut
    public boolean hasElapsedCycle() {
        if (cycles > 0) {
            cycles--;
            return true;
        }
        return false;
    }
    
    //Kellon resetointi uutta peliä aloitettaessa
    public void reset() {
        this.cycles = 0;
        this.lastTime = getTime();
        this.paused = false;
    }
    
}
