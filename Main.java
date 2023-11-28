import java.awt.Color;

import java.util.ArrayList;

import javax.swing.JFrame;




public class Main {

    public static Game game;
    public static JFrame frame;
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static DrawPanel drawPanel = new DrawPanel();
    public static final Vec2 TILE_SIZE = new Vec2(80, 80);
    public static final Color TILE_COLOR = new Color(230, 200, 150);
    public static BlockPlacer blockPlacer = new BlockPlacer();
    
    public static boolean isLMousePressed = false;
    public static boolean isRMousePressed = false;

    public static Scene currentScene;

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
        
        
        
        

        game.setTickFunction((d) -> {
            if (currentScene != null)
                currentScene.update(d);
        });

        game.setDrawFunction((d) -> {
            
            for(GameObject obj : currentScene.gameObjects) {
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

        Scene scene = new Scene();
        scene.addObject(testBody);
        scene.addObject(testStaticBody);
        changeScene(scene);

        // add stuff

        frame.setVisible(true);
        
        Thread loopThread = new Thread(() -> {
            game.startLoops();
        });
        
        loopThread.start();
    }

    public static void changeScene(Scene s) {
        currentScene = s;
    }
}