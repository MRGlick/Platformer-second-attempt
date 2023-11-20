import javax.swing.JFrame;

public class MyFrame extends JFrame{
    MyFrame(String windowName, int sizeX, int sizeY) {
        this.setSize(sizeX, sizeY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLocationRelativeTo(null);
        this.setName(windowName);
        this.setLayout(null);
    }

    MyFrame(String windowFrame) {
        this(windowFrame, 1200, 800);
    }

    MyFrame(int sizeX, int sizeY) {
        this("Java Applet", sizeX, sizeY);
    }
}
