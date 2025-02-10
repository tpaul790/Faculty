package service;

import domain.GPSCoordinates;

public class ManhattanDistanceCalculator implements DistanceCalculator {
    @Override
    public Double computeDistance(GPSCoordinates point1, GPSCoordinates point2) {
        return Math.abs(point1.getLatitude() - point2.getLatitude()) + Math.abs(point1.getLongitude() - point2.getLongitude());
    }
}
