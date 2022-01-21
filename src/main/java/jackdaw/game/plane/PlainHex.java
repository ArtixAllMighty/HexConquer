package jackdaw.game.plane;

import framework.window.Window;
import jackdaw.game.Level;
import jackdaw.game.TexLoader;
import jackdaw.game.resources.Material;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlainHex extends Element {
    private final PlainType plainType;
    private Polygon hex;
    //store all six corner points to be used as intersections in the world map
    private ArrayList<Coord> corners = new ArrayList<>();
    private ArrayList<Road> roads = new ArrayList<>();
    private ArrayList<String> owners = new ArrayList<>();

    public PlainHex(Level level, PlainType plainType, double posX, double posY) {
        super(level, posX, posY);
        this.plainType = plainType;
        hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            Coord coord = new Coord((posX + Level.bone * Math.cos(i * 2 * Math.PI / 6)), (posY + Level.bone * Math.sin(i * 2 * Math.PI / 6)));
            hex.addPoint(coord.getPosX(), coord.getPosY());
            if (this.plainType != PlainType.WATER)
                corners.add(coord);
        }
        if (this.plainType != PlainType.WATER)
            for (int i = 0; i < corners.size(); i++) {
                Coord a = corners.get((i - 1) < 0 ? corners.size() - 1 : i - 1);
                Coord b = corners.get(i);
                roads.add(new Road(level, a, b, a.getPosY() == b.getPosY()));
            }
    }

//    public Color color() {
//        if (plainType != null)
//            return switch (plainType) {
//                case WOODS -> Color.green.darker().darker().darker();
//                case FIELDS -> Color.orange.brighter();
//                case PLAINS -> Color.green.darker();
//                case MOUNTAINS -> Color.darkGray;
//                case RIVERS -> Color.yellow.darker();
//                case EMPTY -> Color.white;
//                case PITS -> Color.red.darker().darker();
//            };
//        return Color.red;
//    }

    public void drawSprite(Graphics2D g) {
        if (plainType != null) {
            g.setClip(hex);
            BufferedImage img = switch (plainType) {
                case PLAINS -> TexLoader.PLAINS;
                case WOODS -> TexLoader.WOODS;
                case MOUNTAINS -> TexLoader.MOUNTAINS;
                case FIELDS -> TexLoader.FIELDS;
                case PITS -> TexLoader.PITS;
                case RIVERS -> TexLoader.RIVER;
                case EMPTY -> TexLoader.EMPTY;
                case WATER -> TexLoader.WATER;
            };
            if (img != null)
                g.drawImage(img, getPosition().getPosX() - (int) Level.bone, getPosition().getPosY() - (int) Level.bone, (int) Level.bone * 2, (int) Level.bone * 2, null);

            g.setClip(0, 0, Window.getWidth(), Window.getHeight());
        }
    }

    @Override
    public void draw(Graphics2D g) {
//        g.setColor(color());
//        g.fill(hex);
        drawSprite(g);

    }

    public ArrayList<Coord> getCorners() {
        return corners;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    @Override
    public void update() {

    }

    @Override
    public Shape getBoundingBox() {
        return hex;
    }

    public void addOwner(String owner) {
        owners.add(owner);
    }

    public ArrayList<String> getOwners() {
        return owners;
    }

    public Material getMaterial() {
        return switch (plainType) {
            case WOODS -> Material.WOOD;
            case PLAINS -> Material.WOOL;
            case FIELDS -> Material.WHEAT;
            case RIVERS -> Material.GOLD;
            case PITS -> Material.CLAY;
            case MOUNTAINS -> Material.METAL;
            default -> null;
        };
    }
}
