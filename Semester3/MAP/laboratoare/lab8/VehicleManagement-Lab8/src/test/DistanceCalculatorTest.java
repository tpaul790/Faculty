package test;

import org.junit.Before;
import org.junit.Test;

import domain.GPSCoordinates;
import service.DistanceCalculator;
import service.EuclideanDistanceCalculator;
import service.ManhattanDistanceCalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
public class DistanceCalculatorTest {
	
	private DistanceCalculator dist;
	
	@Before
	public void setUp(){
		dist=new ManhattanDistanceCalculator();
	}
	
	@Test
	public void testDistanceComputation(){
		GPSCoordinates point1=new GPSCoordinates(2.0, 3.0);
		GPSCoordinates point2=new GPSCoordinates(3.5, 2.5);
		
		//TODO add assertions for distance computation
		Double distance = dist.computeDistance(point1, point2);
//		assertEquals(Math.sqrt(2.5), distance, 0.001);
	}

}
