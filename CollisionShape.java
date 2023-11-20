
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

            double xRelative = Math.max(Math.abs(shape.globalPos.x - globalPos.x), Math.abs(shape.globalPos.x - (globalPos.x + size.x))) / shape.size.x;
            double yRelative = Math.max(Math.abs(shape.globalPos.y - globalPos.y), Math.abs(shape.globalPos.y - (globalPos.x + size.y))) / shape.size.y;
            
            if (xRelative < yRelative) {
                result.normal = result.normal.add(new Vec2(0, globalPos.y < shape.globalPos.y ? -1 : 1));
            } else {
                result.normal = result.normal.add(new Vec2(globalPos.x < shape.globalPos.x ? -1 : 1, 0));
            }
        }
        
        Vec2.print("normal: ", result.normal);

        result.targetShapeType = TYPE_RECT;

        return result;
    }

    

}
