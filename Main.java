import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;




public class Main {

    public static Game game;
    public static JFrame frame;
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static DrawPanel drawPanel = new DrawPanel();
    public static final Vec2 TILE_SIZE = new Vec2(80, 80);
    public static final Color TILE_COLOR = new Color(230, 200, 150);
    public static HashMap<Vec2, Body> placedTiles = new HashMap<>();
    public static boolean isLMousePressed = false;
    public static boolean isRMousePressed = false;

    public static void placeBlock(Vec2 position) {
        Vec2 tilePos = (position.divide(TILE_SIZE).floor().mul(TILE_SIZE));
        for (Vec2 key : placedTiles.keySet()) {
            if (key.equals(tilePos)) return;
        }
        Body block = new Body(
            new RectDisplay(TILE_SIZE, TILE_COLOR),
            new RectShape(TILE_SIZE),
            true
        );
        block.setGlobalPos(tilePos);
        block.addToScene();
        placedTiles.put(tilePos, block);
    }

    public static void removeBlock(Vec2 position) {
        Vec2 tilePos = (position.divide(TILE_SIZE).floor().mul(TILE_SIZE));
        
        for (Vec2 key : placedTiles.keySet()) {
            if (key.equals(tilePos)) {
                Body block;
                (block = placedTiles.get(key)).destroy();
                placedTiles.remove(key);
                break;
            }
        }
        
        
        
        
        
    }

    public static void addObject(GameObject obj) {
        gameObjects.add(obj);
    }
    public static void removeObject(GameObject obj) {
        gameObjects.remove(obj);
    }

    public static void main(String[] args) {

        // replace with variables
        game = new Game(120, 120);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(((int)1200), ((int)800));
        frame.getContentPane().setBackground(new Color(20, 20, 20));
        frame.setTitle("My Platformer!");

        frame.add(drawPanel);
        
        drawPanel.bgColor = new Color(20, 20, 20);
        
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isLMousePressed = true;
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    isRMousePressed = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isLMousePressed = false;
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    isRMousePressed = false;
                }
            }
        };
        
        frame.addMouseListener(mouseListener);

        game.setTickFunction((d) -> {
            if (isLMousePressed) {
                placeBlock(new Vec2(MouseInfo.getPointerInfo().getLocation()).add(new Vec2(0, TILE_SIZE.y/-2)));
            } else if (isRMousePressed) {
                removeBlock(new Vec2(MouseInfo.getPointerInfo().getLocation()).add(new Vec2(0, TILE_SIZE.y/-2)));
            }
            for(GameObject obj : gameObjects) {
                obj.frameUpdate(d);
            }
        });

        game.setDrawFunction((d) -> {
            
            for(GameObject obj : gameObjects) {
                if (obj instanceof Display) 
                    drawPanel.addShape(((Display)obj).getDisplayShape());
                
            }
            frame.revalidate();
            drawPanel.repaint();
            

        });

        // add stuff

        RectDisplay testDisplay = new RectDisplay(new Vec2(50, 50), Color.WHITE);
        testDisplay.setColor(Color.RED);

        RectShape testShape = new RectShape(new Vec2(50, 50));

        Body testBody = new Player(testDisplay, testShape);

        Body testStaticBody = new Body(new RectDisplay(new Vec2(500,50), Color.BLUE)
        , new RectShape(new Vec2(500, 50)), true);
        testStaticBody.setGlobalPos(new Vec2(120, 300));

        testBody.velocity = new Vec2(50, 50);

        testBody.addToScene();

        // add stuff

        frame.setVisible(true);
        
        Thread loopThread = new Thread(() -> {
            game.startLoops();
        });
        
        loopThread.start();
    }
}