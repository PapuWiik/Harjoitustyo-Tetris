package harkkatyo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Pelilaudan oikea puoli, "infopalkki"
public class SidePanel extends JPanel {
    
    private Game game;
    
    //Luodaan paneelissa käytettävät fontit
    Font font = new Font("Calibri", Font.PLAIN, 35);
    Font font2 = new Font("Calibri", Font.PLAIN, 20);
    Font font3 = new Font("Calibri", Font.PLAIN, 16);
    JLabel text = new JLabel("T E T R I S               ");
    JLabel emptyText = new JLabel("   ");
    
    public SidePanel(Game game) {
        
        this.game = game;
        
        setBackground(new Color(197,203,206));
        setPreferredSize(new Dimension(250, 400));
    }
    
    //Piirretään paneelin tekstit. Nämä tehtävä paintComponentilla(), koska level ja score muuttuvat pelin edetessä.
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("T E T R I S",0,80);
        
        g.setFont(font2);
        g.drawString("Level: "+game.getCurrentLevel(), 0, 110);
        g.drawString("Score: "+game.getScore(), 0, 130);
        
        g.setFont(font3);
        g.drawString("CONTROLS", 0, 180);
        g.drawString("A - Move Left", 0, 200);
        g.drawString("D - Move Right", 0, 215);
        g.drawString("W - Turn Tile", 0, 230);
        g.drawString("S - Speed Up or Slow Down", 0, 245);
    }
}
