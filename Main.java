import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;




public class Main {

    public static Game game;
    public static JFrame frame;
    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static DrawPanel drawPanel = new DrawPanel();

    public static void addObject(GameObject obj) {
        gameObjects.add(obj);
    }

    public static void main(String[] args) {

        // replace with variables
        game = new Game(60, 60);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(((int)1200), ((int)800));
        frame.getContentPane().setBackground(new Color(20, 20, 20));
        frame.setTitle("My Platformer!");

        frame.add(drawPanel);
        
        drawPanel.bgColor = new Color(20, 20, 20);
        
        
        
        

        game.setTickFunction((d) -> {
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

        Body testBody = new Body(testDisplay, testShape, false);

        Body testStaticBody = new Body(new RectDisplay(new Vec2(500,50), Color.BLUE)
        , new RectShape(new Vec2(500, 50)), true);
        testStaticBody.setGlobalPos(new Vec2(120, 300));

        testBody.velocity = new Vec2(50, 50);

        // add stuff

        frame.setVisible(true);
        
        Thread loopThread = new Thread(() -> {
            game.startLoops();
        });
        
        loopThread.start();
    }
}