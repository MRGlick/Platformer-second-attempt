import java.util.ArrayList;

public class Scene {

    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    public Vec2 cameraPos = new Vec2();
    private Func startFunc, updateFunc;

    Scene() {
        start();
    }

    public void update(double delta) {
        updateFunc.apply(delta);
        for (GameObject obj : gameObjects) {
            obj.frameUpdate(delta);
        }
    }   

    public void start() {
        startFunc.apply(0);
    }

    public void setStartFunc(Func func) {
        startFunc = func;
    }
    public void setUpdateFunc(Func func) {
        updateFunc = func;
    }


    public void addObject(GameObject obj) {
        gameObjects.add(obj);
        obj.scene = this;
    }
    public void removeObject(GameObject obj) {
        gameObjects.remove(obj);
    }
}
