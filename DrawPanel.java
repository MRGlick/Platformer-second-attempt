import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
    
    private Image image;
    private Graphics graphics;
    public ArrayList<DrawShape> shapesToDraw = new ArrayList<>();
    public Color bgColor = Color.BLACK;
    private boolean drawing = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        drawing = true;
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (DrawShape shape : shapesToDraw) {
            shape.draw(g);
        }
        shapesToDraw = new ArrayList<>();
        drawing = false;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void addShape(DrawShape shape) {
        shapesToDraw.add(shape);
    }
}

abstract class DrawShape {

    public Color color;
    public Vec2 position;

    DrawShape() {
        color = Color.BLACK;
    }

    public void draw(Graphics g) {}

}

class DrawRect extends DrawShape {
    
    public Vec2 size;

    DrawRect(Vec2 position, Vec2 size) {
        super();
        this.position = position;
        this.size = size;
    } 

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int)position.x, (int)position.y, (int)size.x, (int)size.y);
    }
}

class DrawCircle extends DrawShape {
    public double radius;

    DrawCircle(Vec2 position, double radius) {
        super();
        this.position = position;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)radius * 2, (int)radius * 2);
    }
}

class DrawImage extends DrawShape {
    
    Image image;
    public Vec2 size;

    DrawImage(Vec2 position, Vec2 size, Image image) {
        super();
        this.position = position;
        this.size = size;
        this.image = image;
    }   

    @Override
    public void draw(Graphics g) {
        Image scaledImage = scaleImage(image, (int)size.x, (int)size.y);
        g.drawImage(scaledImage, (int)position.x, (int)position.y, null);
    }

    // function provided by chatGPT because getScaledInstance is stupid
    private Image scaleImage(Image originalImage, int newWidth, int newHeight) {
        if (originalImage == null || newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // Create a new BufferedImage with the desired dimensions and type
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        
        // Get the graphics context of the scaled image
        Graphics2D g2d = scaledImage.createGraphics();
        
        // Use the graphics context to draw the original image, scaling it to fit the new dimensions
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);

        // Dispose of the graphics context to release resources
        g2d.dispose();

        return scaledImage;
    }
}
