import java.util.HashMap;

public class BlockPlacer extends GameObject {
    

    public static HashMap<Vec2, Body> placedTiles = new HashMap<>();

    public void placeBlock(Vec2 position) {
        Vec2 tilePos = (position.divide(Main.TILE_SIZE).floor().mul(Main.TILE_SIZE));
        for (Vec2 key : placedTiles.keySet()) {
            if (key.equals(tilePos)) return;
        }
        Body block = new Body(
            new RectDisplay(Main.TILE_SIZE, Main.TILE_COLOR),
            new RectShape(Main.TILE_SIZE),
            true
        );
        
        block.setGlobalPos(tilePos);
        scene.addObject(block);
        
        placedTiles.put(tilePos, block);
    }

    public void removeBlock(Vec2 position) {
        Vec2 tilePos = (position.divide(Main.TILE_SIZE).floor().mul(Main.TILE_SIZE));
        
        for (Vec2 key : placedTiles.keySet()) {
            if (key.equals(tilePos)) {
                Body block = placedTiles.get(key);
                block.destroy();
                placedTiles.remove(key);
                break;
            }
        }
    }
}
