public class Body extends GameObject{
    
    public Display display;
    public CollisionShape shape;
    public Vec2 velocity;
    public double bounce = 0;
    public final boolean isStatic;

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
        
        setVelocity(velocity.add(new Vec2(0, 2)));

        checkCollisions(delta);
        
        
        setLocalPos(localPos.add(velocity.mul(delta)));
    }


    public void checkCollisions(double delta) {

        setLocalPos(localPos.add(velocity.mul(delta)));

        for (GameObject obj : Main.gameObjects) {
            if (obj.equals(this) || !(obj instanceof Body)) continue;

            Body body = (Body)obj;
            CollisionData colData = shape.collide(body.shape);
            
            if (colData.didCollide) {
                setLocalPos(localPos.add(colData.normal.mul(colData.penetrationDepth)));
                setVelocity(velocity.add(colData.normal.mul(velocity.getLength())));
            }


        }
    }

}


