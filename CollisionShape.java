
class CollisionData {

    public Vec2 normal;
    public boolean didCollide = false;
    public double penetrationDepth = -1;
    public int targetShapeType = -1;

    CollisionData(boolean didCollide, Vec2 normal) {
        this.didCollide = didCollide;
        this.normal = normal;
    }

    CollisionData() {
        this(false, null);
    }
}

public class CollisionShape extends GameObject{
    
    protected int TYPE = -1;
    public static int TYPE_RECT = 0, TYPE_CIRCLE = 1;
    public boolean isStatic;

    public CollisionData collide(CollisionShape shape) {
        return new CollisionData();
    }

    public int getType() {
        return TYPE;
    }


    protected boolean isPointInRect(Vec2 point, Vec2 rectPos, Vec2 rectSize) {
        return numInRange(point.x, rectPos.x, rectPos.x + rectSize.x)
        && numInRange(point.y, rectPos.y, rectPos.y + rectSize.y);
    }

    protected boolean numInRange(double num, double min, double max) {
        return num <= max && num >= min;
    }
}

class RectShape extends CollisionShape {

    public Vec2 size;
    

    RectShape(Vec2 size) {
        setLocalPos(localPos);
        this.size = size;
        this.TYPE = CollisionShape.TYPE_RECT;
    }

    @Override
    public CollisionData collide(CollisionShape shape) {

        if (shape.TYPE == CollisionShape.TYPE_RECT) {
            return collideRect((RectShape)shape);
        }

        return new CollisionData();
    }

    private double dot(Vec2 v1, Vec2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    private boolean checkDir(Vec2 n1, Vec2 n2, Vec2 n3, Vec2 n4, Vec2 dir) {

        return dot(n1, dir) > 0 
        && dot(n2, dir) > 0 
        && dot(n3, dir) > 0 
        && dot(n4, dir) > 0;
    }

    private CollisionData collideRect(RectShape shape) {

        if (isStatic) return new CollisionData();

        CollisionData result = new CollisionData();

        // check if collides
        if ((numInRange(globalPos.x, shape.globalPos.x, shape.globalPos.x + shape.size.x) 
        || numInRange(shape.globalPos.x, globalPos.x, globalPos.x + size.x))
        && (numInRange(globalPos.y, shape.globalPos.y, shape.globalPos.y + shape.size.y)
        || numInRange(shape.globalPos.y, globalPos.y, globalPos.y + size.y))
        ) result.didCollide = true;

        // find normal
        if (result.didCollide) {
            
            result.normal = new Vec2();
            
            Vec2 center = globalPos.add(size.divide(2));
            Vec2 n1, n2, n3, n4;
            n1 = shape.globalPos.directionTo(center);
            n2 = shape.globalPos.add(new Vec2(shape.size.x, 0)).directionTo(center);
            n3 = shape.globalPos.add(new Vec2(0, shape.size.y)).directionTo(center);
            n4 = shape.globalPos.add(new Vec2(shape.size.x, shape.size.y)).directionTo(center);
            
            double currentMaxDist = -1; // used to make the normal non diagonal

            if (checkDir(n1, n2, n3, n4, Vec2.UP)) {
                result.normal = Vec2.UP;
                currentMaxDist = Math.abs(center.y - shape.globalPos.y);
            }
                
            if (checkDir(n1, n2, n3, n4, Vec2.DOWN)) {
                if (currentMaxDist < Math.abs(center.y - shape.globalPos.y - shape.size.y)) {
                    result.normal = Vec2.DOWN;
                    currentMaxDist = Math.abs(center.y - shape.globalPos.y - shape.size.y);
                }
                
            }
                
            if (checkDir(n1, n2, n3, n4, Vec2.LEFT)) {
                if (currentMaxDist < Math.abs(center.x - shape.globalPos.x)) {
                    result.normal = Vec2.LEFT;
                    currentMaxDist = Math.abs(center.x - shape.globalPos.x);
                }
            }
                
            if (checkDir(n1, n2, n3, n4, Vec2.RIGHT)) {
                if (currentMaxDist < Math.abs(center.x - shape.globalPos.x - shape.size.x)) {
                    result.normal = Vec2.RIGHT;
                    currentMaxDist = Math.abs(center.x - shape.globalPos.x - shape.size.x);
                }
            }

            if (result.normal.equals(new Vec2(1, 0))) result.penetrationDepth = (shape.globalPos.x + shape.size.x) - globalPos.x;
            else if (result.normal.equals(new Vec2(-1, 0))) result.penetrationDepth = (globalPos.x + size.x) - shape.globalPos.x;
            else if (result.normal.equals(new Vec2(0, 1))) result.penetrationDepth = (shape.globalPos.y + shape.size.y) - globalPos.y;
            else result.penetrationDepth = (globalPos.y + size.y) - shape.globalPos.y;
            if (!isStatic) {
                if (result.normal.y == 0) System.out.println("Collided with wall.");
            }
            
        }

        result.targetShapeType = TYPE_RECT;

        return result;
    }

    

}
