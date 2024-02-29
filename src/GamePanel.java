import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;


import java.io.InputStream;



/**
 * Created by Armin on 6/25/2016.
 */
public class GamePanel extends JLayeredPane implements MouseMotionListener {

    private static final long serialVersionUID = 1L;

    private JLabel zombiesDiedLabel;
	private Image bgImage;
    private Image peashooterImage;
    private Image freezePeashooterImage;
    private Image sunflowerImage;
    private Image peaImage;
    private Image freezePeaImage;
    private Image shortRangeImage;
    private Image axeImage;

    private Image normalZombieImage;
    private Image coneHeadZombieImage;
    private Collider[] colliders;
    

    private ArrayList<ArrayList<Zombie>> laneZombies;
    private ArrayList<ArrayList<Pea>> lanePeas;
    private ArrayList<Sun> activeSuns;

    private Timer redrawTimer;
    private Timer advancerTimer;
    private Timer sunProducer;
    private Timer zombieProducer;
    private JLabel sunScoreboard;
    private int totalZombiesKilled = 0;
    
    public void zombieDied() {
        totalZombiesKilled++;
        System.out.println("Total zombies died: " + totalZombiesKilled);
        updateZombiesDiedLabel();
    }

    // Add this method to update the label on the screen
    public void updateZombiesDiedLabel() {
        zombiesDiedLabel.setText("Zombies Died: " + totalZombiesKilled);
    }


    private GameWindow.PlantType activePlantingBrush = GameWindow.PlantType.None;

    private int mouseX, mouseY;

    private int sunScore;

    public int getSunScore() {
        return sunScore;
    }

    public void setSunScore(int sunScore) {
        this.sunScore = sunScore;
        sunScoreboard.setText(String.valueOf(sunScore));
    }

    public GamePanel(JLabel sunScoreboard) {
        setSize(1000, 752);
        setLayout(null);
        addMouseMotionListener(this);
        this.sunScoreboard = sunScoreboard;
        setSunScore(275); 
        startBackgroundMusic();

        bgImage = new ImageIcon(this.getClass().getResource("images/mainBG.png")).getImage();

      
        peashooterImage = new ImageIcon(this.getClass().getResource("images/plants/peashooter2.gif")).getImage();
        freezePeashooterImage = new ImageIcon(this.getClass().getResource("images/plants/freezepeashooter.gif")).getImage();
        sunflowerImage = new ImageIcon(this.getClass().getResource("images/plants/sunflower.gif")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("images/arrow.png")).getImage();
        freezePeaImage = new ImageIcon(this.getClass().getResource("images/freezepea.png")).getImage();
        shortRangeImage = new ImageIcon(this.getClass().getResource("images/plants/peashooter.gif")).getImage();
        axeImage = new ImageIcon(this.getClass().getResource("images/pea.png")).getImage();

        normalZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie1.png")).getImage();
        coneHeadZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie2.png")).getImage();

        laneZombies = new ArrayList<>();
        laneZombies.add(new ArrayList<>()); //line 1
        laneZombies.add(new ArrayList<>()); //line 2
        laneZombies.add(new ArrayList<>()); //line 3
        laneZombies.add(new ArrayList<>()); //line 4
        laneZombies.add(new ArrayList<>()); //line 5

        lanePeas = new ArrayList<>();
        lanePeas.add(new ArrayList<>()); //line 1
        lanePeas.add(new ArrayList<>()); //line 2
        lanePeas.add(new ArrayList<>()); //line 3
        lanePeas.add(new ArrayList<>()); //line 4
        lanePeas.add(new ArrayList<>()); //line 5

        colliders = new Collider[45];
        for (int i = 0; i < 45; i++) {
            Collider a = new Collider();
            a.setLocation(44 + (i % 9) * 100, 109 + (i / 9) * 120);
            a.setAction(new PlantActionListener((i % 9), (i / 9)));
            colliders[i] = a;
            add(a,0);
        }
     // Zombies dead label
        zombiesDiedLabel = new JLabel("Zombies Killed: " + totalZombiesKilled);
        zombiesDiedLabel.setForeground(Color.BLACK);
        zombiesDiedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        zombiesDiedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        zombiesDiedLabel.setBounds(780, 10, 200, 20);
        add(zombiesDiedLabel);
    


        activeSuns = new ArrayList<>();

        redrawTimer = new Timer(25, (ActionEvent e) -> {
            repaint();
        });
        redrawTimer.start();

        advancerTimer = new Timer(60, (ActionEvent e) -> advance());
        advancerTimer.start();

        sunProducer = new Timer(2000, (ActionEvent e) -> {
            Random rnd = new Random();
            Sun sta = new Sun(this, rnd.nextInt(800) + 100, 0, rnd.nextInt(300) + 200);
            activeSuns.add(sta);
            add(sta, 1);
        });
        sunProducer.start();

        zombieProducer = new Timer(4000, (ActionEvent e) -> {
            Random rnd = new Random();
            LevelData lvl = new LevelData();
            String[] Level = lvl.LEVEL_CONTENT[Integer.parseInt(lvl.LEVEL_NUMBER) - 1];
            int[][] LevelValue = lvl.LEVEL_VALUE[Integer.parseInt(lvl.LEVEL_NUMBER) - 1];
            int l = rnd.nextInt(5);
            int t = rnd.nextInt(100);
            Zombie z = null;
            for (int i = 0; i < LevelValue.length; i++) {
                if (t >= LevelValue[i][0] && t <= LevelValue[i][1]) {
                    z = Zombie.getZombie(Level[i], GamePanel.this, l);
                }
            }
            laneZombies.get(l).add(z);
        });
        zombieProducer.start();

    }
 
private void startBackgroundMusic() {
    // Update the path based on the location of your audio file
    String musicFilePath = "/images/creepy.wav";
    
    // Load the audio file as an InputStream
    InputStream inputStream = getClass().getResourceAsStream(musicFilePath);

    // Call the playBackgroundMusic method with the InputStream
    BackgroundMusic.playBackgroundMusic(inputStream);
}


    private void advance() {
        for (int i = 0; i < 5; i++) {
            ArrayList<Zombie> zombiesToRemove = new ArrayList<>();
            for (int j = 0; j < laneZombies.get(i).size(); j++) {
                Zombie zombie = laneZombies.get(i).get(j);
                zombie.advance();
                if (!zombie.isMoving()) {
                    zombiesToRemove.add(zombie);
                }
            }
           
            for (Zombie zombie : zombiesToRemove) {
                laneZombies.get(i).remove(zombie);
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                p.advance();
            }
        }

        ArrayList<Sun> sunsToRemove = new ArrayList<>();
        for (int i = 0; i < activeSuns.size(); i++) {
            Sun sun = activeSuns.get(i);
            sun.advance();
            if (!sun.isVisible()) {
                sunsToRemove.add(sun);
            }
        }
        for (Sun sun : sunsToRemove) {
            activeSuns.remove(sun);
        }
    }




    public void removeSunflower(int x, int y) {
        for (int i = 0; i < laneZombies.size(); i++) {
            ArrayList<Zombie> zombiesInLane = laneZombies.get(i);
            for (int j = 0; j < zombiesInLane.size(); j++) {
                Zombie zombie = zombiesInLane.get(j);
                if (zombie.getPosX() < 20 + (x * 100)) { 
                    zombiesInLane.remove(j);
                    j--; 
                }
            }
        }

        for (Collider collider : colliders) {
            Plant plant = collider.getAssignedPlant();
            if (plant instanceof Sunflower && collider.getX() == x && collider.getY() == y) {
                Sunflower sunflower = (Sunflower) plant;
                sunflower.stop(); 
                sunflower.explode();
                collider.setPlant(null); 
                
                break; 
            }
        }
    }

  
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);

        //Draw Plants
        for (int i = 0; i < 45; i++) {
            Collider c = colliders[i];
            if (c.assignedPlant != null) {
                Plant p = c.assignedPlant;
                if (p instanceof Peashooter) {
                    g.drawImage(peashooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof FreezePeashooter) {
                    g.drawImage(freezePeashooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof ShortRange) {
                    g.drawImage(shortRangeImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof Sunflower) {
                    g.drawImage(sunflowerImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (Zombie z : laneZombies.get(i)) {
                if (z instanceof NormalZombie) {
                    g.drawImage(normalZombieImage, z.getPosX(), 109 + (i * 120), null);
                } else if (z instanceof ConeHeadZombie) {
                    g.drawImage(coneHeadZombieImage, z.getPosX(), 109 + (i * 120), null);
                }
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea pea = lanePeas.get(i).get(j);
                if (pea instanceof FreezePea) {
                    g.drawImage(freezePeaImage, pea.getPosX(), 130 + (i * 120), null);
                }
                if (pea instanceof Axe) {
                     g.drawImage(axeImage, pea.getPosX(), 130 + (i * 120), null);
                    
                    } else {
                    g.drawImage(peaImage, pea.getPosX(), 130 + (i * 120), null);
                }
            }

        }
        


    }

    private class PlantActionListener implements ActionListener {

        int x, y;

        public PlantActionListener(int x, int y) {
            this.x = x;
            this.y = y;
         
        }
        

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activePlantingBrush == GameWindow.PlantType.Sunflower) {
                if (getSunScore() >= 50) {
                    colliders[x + y * 9].setPlant(new Sunflower(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 50);
                }
            }
            if (activePlantingBrush == GameWindow.PlantType.Peashooter) {
                if (getSunScore() >= 100) {
                    colliders[x + y * 9].setPlant(new Peashooter(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 100);
                }
            }

            if (activePlantingBrush == GameWindow.PlantType.Freezepeashooter) {
                if (getSunScore() >= 175) {
                    colliders[x + y * 9].setPlant(new FreezePeashooter(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 175);
                }
            }
            if (activePlantingBrush == GameWindow.PlantType.ShortRange) {
                if (getSunScore() >= 200) {
                    colliders[x + y * 9].setPlant(new ShortRange(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 200);
                }
            }
            activePlantingBrush = GameWindow.PlantType.None;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMouseX(e.getX());
        setMouseY(e.getY());
    }

    static int progress = 0;

    public static void setProgress(int num) {
        progress = progress + num;
        System.out.println(progress);
        if (progress >= 150) {
            if ("1".equals(LevelData.LEVEL_NUMBER)) {
                JOptionPane.showMessageDialog(null, "LEVEL_CONTENT Completed !!!" + '\n' + "Starting next LEVEL_CONTENT");
                GameWindow.gw.dispose();
                LevelData.write("2");
                GameWindow.gw = new GameWindow();
            } else {
                JOptionPane.showMessageDialog(null, "LEVEL_CONTENT Completed !!!" + '\n' + "More Levels will come soon !!!" + '\n' + "Resetting data");
                LevelData.write("1");
                System.exit(0);
            }
            progress = 0;
        }
    }

    public GameWindow.PlantType getActivePlantingBrush() {
        return activePlantingBrush;
    }

    public void setActivePlantingBrush(GameWindow.PlantType activePlantingBrush) {
        this.activePlantingBrush = activePlantingBrush;
    }

    public ArrayList<ArrayList<Zombie>> getLaneZombies() {
        return laneZombies;
    }

    public void setLaneZombies(ArrayList<ArrayList<Zombie>> laneZombies) {
        this.laneZombies = laneZombies;
    }

    public ArrayList<ArrayList<Pea>> getLanePeas() {
        return lanePeas;
    }

    public void setLanePeas(ArrayList<ArrayList<Pea>> lanePeas) {
        this.lanePeas = lanePeas;
    }

    public ArrayList<Sun> getActiveSuns() {
        return activeSuns;
    }

    public void setActiveSuns(ArrayList<Sun> activeSuns) {
        this.activeSuns = activeSuns;
    }

    public Collider[] getColliders() {
        return colliders;
    }

    public void setColliders(Collider[] colliders) {
        this.colliders = colliders;
    }

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}



}
