package environment;

import java.util.ArrayList;
import java.util.List;

public class PointParser {

    private List<Point> points;

    public PointParser(String[] pointsToParse){
        this.points = new ArrayList<>();
        for(String point : pointsToParse){
            String[] coordinate = point.split(",");
            this.points.add(new Point(Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1])));
        }
    }

    public List<Point> getPoints(){
        return this.points;
    }
}
