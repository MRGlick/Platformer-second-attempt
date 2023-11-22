
// How to use: Create an instance of the 'game' class inside of the project
// start the loops only at the END of the main method

@FunctionalInterface
interface Func {
    void apply(double delta);
}

public class Game {
    
    public final double TPS;
    public final double FPS;
    public double gameSpeed = 1;
    public Func tickFunc = (d) -> {};
    public Func drawFunc = (d) -> {};
    public Func startFunc = (d) -> {};

    Game(double fps, double tps) {
        FPS = fps;
        TPS = tps;
    }

    public void setTickFunction(Func func) {
        tickFunc = func;
    }
    public void setDrawFunction(Func func) {
        drawFunc = func;
    }
    public void setStartFunction(Func func) {
        startFunc = func;
    }

    private void physicsUpdate(double delta) {
        tickFunc.apply(delta);
    }

    private void drawUpdate(double delta) {
        drawFunc.apply(delta);
    }

    private void start() {
        startFunc.apply(0);
    }

    public void startLoops() {
        start();

        long billion = 1000000000;

        long tickCounter = 0;
        long tickLength = (long)((1 / TPS) * billion * (1/gameSpeed));
        long frameCounter = 0;
        long frameLength = (long)((1 / FPS) * billion * (1/gameSpeed));
        long deltaNano = 0;
        double delta = 0;
        double drawDelta = 1 / FPS;
        double tickDelta = 1 / TPS;
        long last = 0;

        while (true) {
            deltaNano = System.nanoTime() - last;
            delta = ((double)deltaNano) / billion;
            drawDelta += delta;
            tickDelta += delta;
            last = System.nanoTime();
            frameCounter += deltaNano;
            if (frameCounter >= frameLength) {
                frameCounter = 0;
                drawUpdate(drawDelta);
                drawDelta = 0;
            }
            tickCounter += deltaNano;
            if (tickCounter >= tickLength) {
                tickCounter = 0;
                physicsUpdate(tickDelta > 10 ? 1/TPS : tickDelta);
                tickDelta = 0;
            }
            
        }
    }

}
