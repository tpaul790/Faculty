package service;

import domain.GPSCoordinates;

public class EuclideanDistanceCalculator implements DistanceCalculator{

	@Override
	public Double computeDistance(GPSCoordinates point1, GPSCoordinates point2) {
		// TODO implement euclidean distance computation between two points
		return Math.sqrt(Math.pow((point2.getLatitude()-point1.getLatitude()),2)+Math.pow((point2.getLongitude()-point1.getLongitude()),2));
	}

}
