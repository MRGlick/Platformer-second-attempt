

import java.awt.Color;
import java.awt.Graphics;

abstract public class Display extends GameObject{

    protected boolean isVisible = false;
    public Color color;

    public void draw(Graphics g) {}

    public DrawShape getDisplayShape() {
        return new DrawShape() {
            
        };
    }

    public void setColor(Color c) {
        color = c;
    }

}


class RectDisplay extends Display {

    public DrawRect displayRect;
    public Vec2 size;
    
    

    RectDisplay(Vec2 size, Color color) {
        this.size = size;
        Vec2 cameraPos = scene != null ? scene.cameraPos : Vec2.ZERO;
        displayRect = new DrawRect(globalPos.sub(cameraPos), size);
        setColor(color);
    }

    
    @Override
    public void setColor(Color c) {
        color = c;
        displayRect.color = c;
    }

    public DrawShape getDisplayShape() {
        return displayRect;
    }

    
    @Override public void updatePos() {
        super.updatePos();
        Vec2 cameraPos = scene != null ? scene.cameraPos : Vec2.ZERO;
        displayRect.position = globalPos.sub(cameraPos);
    }
}
