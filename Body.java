import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Body extends GameObject{
    
    public Display display;
    public CollisionShape shape;
    public Vec2 velocity;
    public double bounce = 0;
    public final boolean isStatic;
    protected boolean isOnFloor = false;

    Body(Display display, CollisionShape shape, boolean isStatic) {
        super();
        this.display = display;
        this.shape = shape;
        this.isStatic = isStatic;
        this.shape.isStatic = isStatic;

        addChild(this.display);
        addChild(this.shape);

        this.velocity = new Vec2();
        this.globalPos = new Vec2();
    }

    public void setVelocity(Vec2 newV) {
        if (isStatic) {
            velocity = new Vec2();
            return;
        }
        velocity = new Vec2(newV);
    }


    @Override
    public void frameUpdate(double delta) {

        checkCollisions(delta);
        
        
        setLocalPos(localPos.add(velocity.mul(delta)));
    }


    public void checkCollisions(double delta) {

        setLocalPos(localPos.add(velocity.mul(delta)));
        isOnFloor = false;
        if (scene == null) return;
        for (GameObject obj : scene.gameObjects) {
            if (obj.equals(this) || !(obj instanceof Body)) 
                continue;
            

            Body body = (Body)obj;
            CollisionData colData = shape.collide(body.shape);
            
            if (colData.didCollide) {
                setLocalPos(localPos.add(colData.normal.mul(colData.penetrationDepth)));

                double relativeVelocity = Math.abs(velocity.x * colData.normal.x + velocity.y * colData.normal.y);

                setVelocity(velocity.add(colData.normal.mul(relativeVelocity)));
                if (colData.normal.y == -1) isOnFloor = true;
            }
        }
        
        if (this instanceof Player) {
            //System.out.println("final col index: " + colIdx);
            //System.out.println("dismissed: " + dismissedObjects);
        }
    }

}


class Player extends Body {

    public KeyListener keyListener;

    public double movementSpeed = 250;
    public double jumpHeight = 450;
    private double gravityRise = 10;
    private double gravityFall = 15; // hehe

    private int leftInput = KeyEvent.VK_A;
    private int rightInput = KeyEvent.VK_D;
    private int jumpInput = KeyEvent.VK_W;
    private int jumpInput2 = KeyEvent.VK_SPACE;
    
    private int xDir = 0;
    private boolean shouldJump = false;
    

    Player(Display display, CollisionShape shape) {
        super(display, shape, false);
        keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == leftInput) xDir -= 1;
                xDir = Math.max(xDir, -1);
                if (e.getKeyCode() == rightInput) xDir += 1;
                xDir = Math.min(xDir, 1);
                if (e.getKeyCode() == jumpInput || e.getKeyCode() == jumpInput2) shouldJump = true;
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == leftInput) xDir += 1;
                xDir = Math.min(xDir, 1);
                if (e.getKeyCode() == rightInput) xDir -= 1;
                xDir = Math.max(xDir, -1);
            }
        };
        Main.frame.addKeyListener(keyListener);

    }

    @Override
    public void frameUpdate(double delta) {
        super.frameUpdate(delta);
        
        
        move(xDir, shouldJump && isOnFloor);
    }

    private void move(int xDir, boolean shouldJump) {
        setVelocity(new Vec2(
            xDir * movementSpeed,
            shouldJump ? -jumpHeight : velocity.y + (velocity.y < 0 ? gravityRise : gravityFall)
        ));
        this.shouldJump = false;
    }
}


