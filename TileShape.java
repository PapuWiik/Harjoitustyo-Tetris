package harkkatyo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

//Enumeraatio, jossa kuvataan kaikki mahdolliset erilaiset palojen muodot sekä niiden "piirto-ohjeet" rotaatiosta riippuen
public enum TileShape {
    
    /* L-muoto ja sen rotaatiot
    
          * 
          *
          * *
    
    */
    LSHAPE(3, new boolean[][] {
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,	
			true,	false,	false,	
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	true,
			true,	true,	true,	
			false,	false,	false,	
		}
	}), 
    
        /* I-muoto ja sen rotaatiot
    
          * 
          *
          * 
          *
    
    */
    ISHAPE(4, new boolean[][] {
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		},
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		}
	}), 
    
    /* J-muoto ja sen rotaatiot
    
          * 
          *
        * * 
    
    */    
    
    JSHAPE(3, new boolean[][] {
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	true,	
			false,	false,	false,	
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,	
			false,	false,	true,	
		}
	}),
    
    /* T-muoto ja sen rotaatiot
    
        * * * 
          *
          * 
    
    */    
    
    TSHAPE(3, new boolean[][] {
		{
			true,	true,	true,
			false,	true,	false,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,	
			false,	false,	true,	
		},
		{
			false,	false,	false,
			false,	true,	false,
			true,	true,	true,
		},
		{
			true,	false,	false,
			true,	true,	false,	
			true,	false,	false,	
		}
	}),
    
    /* Z-muoto ja sen rotaatiot
    
        * * 
          * *
    
    */    
    
    ZSHAPE(3, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,	
			false,	true,	false,	
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,	
			true,	false,	false,	
		}
	}),
    
    /* O-muoto ja sen rotaatiot
     
        * *
        * * 
    
    */    
    
    OSHAPE(2, new boolean[][] {
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
    
    /* S-muoto ja sen rotaatiot
    
          * *
        * * 
    
    */    
    
    SSHAPE(3, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,	
			false,	false,	true,	
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,	
			false,	true,	false,	
		}
	});
    
    /*Yksittäiseen palaan liitettävät attribuutit:
    int size = palan leveys/korkeus (kaikki palat neliöitä, joten esim 3x3 palan size on 3)
    boolean[][] tile = palan erilaiset rotaatiot ja säännöt palan piirtämiseen kyseisessä asennossa
    int startingRow, startingCol = rivi ja sarake, jolle peli piirtää kyseisen palan sen ilmestyessä pelilaudalle
    */
    private int size;
    private boolean[][] tile;
    private int startingRow;
    private int startingCol;
    private Color color;
    ArrayList<Color> colors = new ArrayList<>();
    
    //Uuden tiilimuodon luomiseen käytettävä konstruktori
    private TileShape(int size, boolean[][] tile) {
		this.size = size;
		this.tile = tile;
                
                colors.add(Color.BLUE);
                colors.add(Color.GREEN);
                colors.add(Color.RED);
                colors.add(Color.YELLOW);
                colors.add(Color.PINK);
                colors.add(Color.ORANGE);
                
                startingRow = getTop(0);
                startingCol = 5 - this.size;
                
                Random rnd = new Random();
                int a = rnd.nextInt(colors.size());
                
                this.color = colors.get(a);
	}
    
    //GETTERIT JA SETTERIT
    public boolean getIfVisible(int i, int j, int rotation) {
        return tile[rotation][j*size+i];
    }
    
    public void setIfVisible(int i, int j, boolean b) {
        tile[i][j] = b;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getStartingRow() {
        return startingRow;
    }
    
    public int getStartingCol() {
        return startingCol;
    }
    
    public int getTop(int rotation) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getIfVisible(j,i,rotation)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int getBottom(int rotation) {
        for (int i = size - 1; i >= 0; i--) {
            for (int j = 0; j < size; j++) {
                if (getIfVisible(j,i,rotation)) {
                    return size - i;
                }
            }
        }
        return -1;
    }
    
    public int getRight(int rotation) {
        for (int i = size - 1; i >= 0;  i--) {
            for (int j = 0; j < size; j++) {
                if (getIfVisible(i,j,rotation)) {
                    return size - i;
                }
            }
        }
        return -1;
    }
    
    public int getLeft(int rotation) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getIfVisible(i, j, rotation)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public Color getColor() {
        return color;
    }
}
