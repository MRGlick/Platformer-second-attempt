import java.util.ArrayList;

abstract public class GameObject {
    
    public Vec2 globalPos, localPos;
    public ArrayList<GameObject> children = new ArrayList<>();
    public GameObject parent = null;
    public boolean inScene = false;
    public Scene scene;

    GameObject() {

        setLocalPos(new Vec2());
    }

    public void addChild(GameObject ch) {
        children.add(ch);
        ch.parent = this;
    }

    public void setLocalPos(Vec2 p) {
        localPos = p;
        if (parent != null) {
            globalPos = parent.globalPos.add(localPos);
        } else {
            globalPos = localPos;
        }
        for (GameObject c : children) {
            c.updatePos();
        }
    }

    public void updatePos() {
        setLocalPos(localPos);
    }

    public void setGlobalPos(Vec2 p) {
        globalPos = p;
        if (parent != null) {
            localPos = parent.globalPos.sub(localPos);
        } else {
            localPos = globalPos;
        }
        for (GameObject c : children) {
            c.updatePos();
        }
    }

    public void frameUpdate(double delta){}

    
    public void destroy() {
        scene.removeObject(this);
        inScene = false;
        for (GameObject child : children) {
            child.destroy();
        }
        scene = null;
    }
}
