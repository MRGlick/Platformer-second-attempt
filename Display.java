

import java.awt.Color;
import java.awt.Graphics;

abstract public class Display extends GameObject{

    protected boolean isVisible = false;

    public void draw(Graphics g) {}

    public DrawShape getDisplayShape() {
        return new DrawShape() {
            
        };
    }

}


class RectDisplay extends Display {

    public DrawRect displayRect;
    public Vec2 size;
    public Color color;
    

    RectDisplay(Vec2 size, Color color) {
        this.size = size;
        displayRect = new DrawRect(globalPos, size);
    }

    

    public void setColor(Color c) {
        color = c;
        displayRect.color = c;
    }

    public DrawShape getDisplayShape() {
        return displayRect;
    }

    
    @Override public void updatePos() {
        super.updatePos();
        displayRect.position = globalPos;
    }
}
