package app.funfinder.roomservice.utils;

import java.util.Arrays;

public class CoordsUtils {

    public static final Integer DEFAULT_SEARCH_RANGE_IN_METERS = 20000;
    public static Float[] getGeoPointFormString(String point) {
        return Arrays.stream(point.split(",")).map(Float::valueOf).toArray(Float[]::new);

    }

    public static String getCoordString(Float lat, Float lon) {
        return lat + "," + lon;
    }
}
