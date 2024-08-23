package main;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.HashSet;
import data_structures.HashTableSC;
import data_structures.LinkedListStack;
import data_structures.SimpleHashFunction;
import data_structures.SinglyLinkedList;
import interfaces.List;
import interfaces.Map;
import interfaces.Set;
import interfaces.Stack;


public class TrainStationManager {
	
	private Set<String> cityListUnique = new HashSet<String>(50);
	private List<String> cityListUnique2 = new ArrayList<>();
	private List<String> sourceCityList = new ArrayList<>();
	private List<String> destinationCityList = new ArrayList<>();
	private List<Station> stationList = new ArrayList<>();
	private List<Station> stationList2 = new ArrayList<>();
	private SimpleHashFunction<String> hashFunction = new SimpleHashFunction<String>();
	private Map<String, List<Station>> stations = new HashTableSC<>(99, hashFunction);
	private Map<String, Station> shortestRoutes = new HashTableSC<>(99,hashFunction);
	private Map<String, Double> travelTimes = new HashTableSC<>(99,hashFunction);
	private Map<String, String> prevStation = new HashTableSC<>(99, hashFunction);
	public Map<String, List<String>> tracingRoute = new HashTableSC<>(99, hashFunction);
	private int infinity = Integer.MAX_VALUE;

	/**
	 * This class receives a file as input, processes the information with a buffer reader and
	 * find the shortest distances and paths from Westside to every other station in the map
	 * @param station_file
	 */
	public TrainStationManager(String station_file) {
		
		 String csvFile = "inputFiles/" + station_file; 
	        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	            String firstLine = br.readLine();

	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split("/"); 
	                
	                String fullLine = " ";
	                String srcCity = " ";
	                String destCity = " ";
	                int distance = 0;
	                int indexOfComa = 0;
	                int indexOfComa2 = 0;
	                
	                
	                for(int i = 0; i < values.length; i++) {
	                	fullLine = values[i];
	                	for(int j = 0; j < fullLine.length(); j++) {
	                    	if(fullLine.charAt(j) == ',') {
	                    		indexOfComa = j;
	                        	break;
	                    	}
	                    }
	                    for(int j = fullLine.length()-1; j >= 0; j--) {
	                    	if(fullLine.charAt(j) == ',') {
	                        	indexOfComa2 = j;
	                        	break;
	                    	}
	                    }
	                    srcCity = fullLine.substring(0, indexOfComa);
	                    destCity = fullLine.substring(indexOfComa+1, indexOfComa2);
	                    distance = Integer.parseInt(fullLine.substring(indexOfComa2+1));
	                    Station newStation = new Station(destCity, distance);
	                    Station newStation2 = new Station(srcCity, distance);
	                    cityListUnique.add(srcCity);
	                    cityListUnique.add(destCity);
	                    sourceCityList.add(srcCity);
	                    destinationCityList.add(destCity);
	                    stationList.add(newStation);
	                    stationList2.add(newStation2);
	                }
	                
	                
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading the file: " + e.getMessage());
	        }
	        
	        makeMap();
	        findShortestDistance();
	        getTravelTimes();
	        
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						StationGUI frame = new StationGUI();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	        
	}
	
	/**
	 * This method receives and creates a map with the information processed by the buffer reader.
	 */
	private void makeMap() {
		for(String sourceCity : cityListUnique) {
			cityListUnique2.add(sourceCity);
		}
		
		for(int i = 0; i < cityListUnique2.size(); i++) {
			stations.put(cityListUnique2.get(i), new SinglyLinkedList<Station>());
		}
		
		for(int i = 0; i < cityListUnique2.size(); i++) {
			for(int j = 0; j < sourceCityList.size(); j++) {
				if(cityListUnique2.get(i).equals(sourceCityList.get(j))) {
					stations.get(cityListUnique2.get(i)).add(stationList.get(j));
				}
			}
			for(int k = 0; k < destinationCityList.size(); k++) {
				if(cityListUnique2.get(i).equals(destinationCityList.get(k))) {
					stations.get(cityListUnique2.get(i)).add(stationList2.get(k));
				}
			}
		}
			
	}
	
	/**
	 * This method creates the map with every station and sets the shortest distance from each station to Westside,
	 * and stores which was the previous station before each station in the shortest path.
	 * It also calls the findShortestPath method.
	 */
	private void findShortestDistance() {
		for(int i = 0; i < cityListUnique2.size(); i++) {
			shortestRoutes.put(cityListUnique2.get(i), new Station("Westside", infinity));
			prevStation.put(cityListUnique2.get(i), "");
		}
		Stack<Station> toVisit = new LinkedListStack<>();
		Set<String> visited = new HashSet<>();
		String currentlyVisiting = "Westside"; 
		shortestRoutes.get(currentlyVisiting).setDistance(0);
		visited.add(currentlyVisiting);
			
			for(int i = 0; i < stations.get(currentlyVisiting).size(); i++) {
				String currentNeighbor = stations.get(currentlyVisiting).get(i).getCityName();
				if(stations.get(currentlyVisiting).get(i).getCityName() != "Westside") {
					sortStack(stations.get(currentlyVisiting).get(i), toVisit);
					shortestRoutes.get(currentNeighbor).setDistance(stations.get(currentlyVisiting).get(i).getDistance());
					prevStation.put(currentNeighbor, currentlyVisiting);
				}
			}
			
		while(visited.size() != cityListUnique.size()) {

			currentlyVisiting = toVisit.top().getCityName();
			visited.add(toVisit.pop().getCityName());
			
			for(int i = 0; i < stations.get(currentlyVisiting).size(); i++) {
				String currentNamei = stations.get(currentlyVisiting).get(i).getCityName();
				if(!stations.get(currentlyVisiting).get(i).getCityName().equals("Westside") && !visited.isMember(stations.get(currentlyVisiting).get(i).getCityName())) {
					sortStack(stations.get(currentlyVisiting).get(i), toVisit); //add to toVisit to check its neighbors later
					
					int currentValueToWestside = shortestRoutes.get(currentlyVisiting).getDistance();
					int currentValueFromWestsideToCurrenti = currentValueToWestside + stations.get(currentlyVisiting).get(i).getDistance();
					if(currentValueFromWestsideToCurrenti < shortestRoutes.get(currentNamei).getDistance()) {  //its smaller even when adding a number bc current value is infinity
						shortestRoutes.get(currentNamei).setDistance(currentValueFromWestsideToCurrenti);
						shortestRoutes.get(currentNamei).setCityName(currentlyVisiting);
						prevStation.put(currentNamei, currentlyVisiting);
					} 
				}
			}
		}
		
		findingShortesPath();
		
	}
	
	/**
	 * This method traces the shortest path from Westside to each station.
	 */
	public void findingShortesPath() {
		for(int i = 0; i < cityListUnique2.size(); i++) {
			
			List<String> resultPath = new ArrayList<>();
			List<String> reversePath = new ArrayList<>();
			String destinationCity = cityListUnique2.get(i);
			String destinationCity2 = cityListUnique2.get(i);
			reversePath.add(destinationCity2);
			
			while(!destinationCity2.equals("Westside")) {
				reversePath.add(destinationCity2);
				destinationCity2 = prevStation.get(destinationCity2);
				if(destinationCity2.equals("Westside")) {
					reversePath.add("Westside");
				}
			}
			
			for(int j = reversePath.size()-1; j >+ 0; j--) {
				resultPath.add(reversePath.get(j));
			}
			tracingRoute.put(destinationCity, resultPath);
		}
		
	}
		

	/**
	 * This method helps maintain a sorted stack, the stack is sorted by distance of each station.
	 * When you add a new station to the stack, it adds it to the correct position.
	 * @param station
	 * @param stackToSort
	 */
	public void sortStack(Station station, Stack<Station> stackToSort) {
		List<Station> temp = new ArrayList<>();
		Stack<Station> temp2 = new LinkedListStack<>();
		
		while(!stackToSort.isEmpty()) {
			temp.add(stackToSort.pop());
		}
		
		while(!temp.isEmpty()) {
			Station minStation = temp.get(0);
			
			for(int i = 1; i < temp.size(); i++) {
				if(temp.get(i).getDistance() < minStation.getDistance()) {
					minStation = temp.get(i);
				}
			}
			temp2.push(minStation);
			temp.remove(minStation);
		}
		
		while(!temp2.isEmpty() && station.getDistance() < temp2.top().getDistance()) {
			stackToSort.push(temp2.pop());
		}
		stackToSort.push(station);

		while(!temp2.isEmpty()) {
			stackToSort.push(temp2.pop());
		}

	}
	
	
	/**
	 * This method calculates and creates a map of the times it takes to go from Westside to every other station.
	 * @return travelTimes
	 */
	public Map<String, Double> getTravelTimes() {
		// 2.5 minutes per kilometer
		// 15 min per station
		for(int i = 0; i < cityListUnique2.size(); i++) {
			if(cityListUnique2.get(i).equals("Westside")) {
				travelTimes.put("Westside", (double) 0);
			} else {
				int stationCount = tracingRoute.get(cityListUnique2.get(i)).size()-2;
				double kiloMeter = shortestRoutes.get(cityListUnique2.get(i)).getDistance();
				double time = stationCount * 15 + kiloMeter * 2.5;
				travelTimes.put(cityListUnique2.get(i), time);
			}
		}
		return travelTimes;
	}

	/**
	 * This method returns the map of all the stations.
	 * @return stations
	 */
	public Map<String, List<Station>> getStations() {
		return stations;
	}


	/**
	 * This method sets all the cities in the station.
	 * @param cities
	 */
	public void setStations(Map<String, List<Station>> cities) {
		this.stations = cities;
	}


	/**
	 * This method returns the map of all the shortest route distances to Westside
	 * @return shortestRoutes
	 */
	public Map<String, Station> getShortestRoutes() {
		return shortestRoutes;
	}


	/**
	 * This method sets the shortest routes of all the stations
	 * @param shortestRoutes
	 */
	public void setShortestRoutes(Map<String, Station> shortestRoutes) {
		this.shortestRoutes = shortestRoutes;
	}
	
	/**
	 * BONUS EXERCISE THIS IS OPTIONAL
	 * Returns the path to the station given. 
	 * The format is as follows: Westside->stationA->.....stationZ->stationName
	 * Each station is connected by an arrow and the trace ends at the station given.
	 * 
	 * @param stationName - Name of the station whose route we want to trace
	 * @return (String) String representation of the path taken to reach stationName.
	 */
//	public String traceRoute(String stationName) {
//		// Remove if you implement the method, otherwise LEAVE ALONE
//		throw new UnsupportedOperationException();
//	}
	
	public String traceRoute(String stationName) {
		String result = "";
		if(stationName.equals("Westside")) {
			return "Westside";
		}
		result =  tracingRoute.get(stationName).get(0);
		for(int i = 1; i < tracingRoute.get(stationName).size(); i++) {
			result += "->" + tracingRoute.get(stationName).get(i);
		}
		return result;

	}
	
	public static void main(String[] args) {
		
		TrainStationManager trainStationManager1 = new TrainStationManager("stations.csv");
		
//		for(String x : trainStationManager1.cityListUnique2) {
//			System.out.println(x);
//		}
//		System.out.println();
//		for(String x : trainStationManager1.sourceCityList) {
//			System.out.println(x);
//		}
//		System.out.println();
//		for(Station x : trainStationManager1.stationList) {
//			System.out.println(x.getCityName() + " " + Integer.toString(x.getDistance()));
//		}
		
		System.out.println();
		System.out.println("Stations (Map)");
        for (String sourceCityIt : trainStationManager1.stations.getKeys()) {
            System.out.print(sourceCityIt + " {");
            for(Station stationIt : trainStationManager1.stations.get(sourceCityIt)) {
            	System.out.print(" (" + stationIt.getCityName() + "," + Integer.toString(stationIt.getDistance()) + ") ");
            }
            System.out.print("}");
            System.out.println();
        }
        
		System.out.println();
		System.out.println("Shortest Routes");
		for(String city : trainStationManager1.shortestRoutes.getKeys()) {
			System.out.println(city + "{" + trainStationManager1.shortestRoutes.get(city) + "}");
		}
		
		System.out.println();
		System.out.println("Travel Times");
		for(String city : trainStationManager1.travelTimes.getKeys()) {
			System.out.println(city + "{" + trainStationManager1.travelTimes.get(city) + "}");
		}
		
		System.out.println();
		System.out.println("prev Station");
		for(String city : trainStationManager1.prevStation.getKeys()) {
			System.out.println(city + ": " + trainStationManager1.prevStation.get(city));
		}
		
		System.out.println();
		System.out.println("Trace Route");
		for(String city : trainStationManager1.cityListUnique2) {
			System.out.println(city + ": " + trainStationManager1.traceRoute(city));
		}
		
		
		///////////////////////////////////////////////////////////////////////////////
		
//		TrainStationManager trainStationManager2 = new TrainStationManager("stationsTwo.csv");
//		
//		System.out.println();
//		System.out.println("Stations (Map)");
//        for (String sourceCityIt : trainStationManager2.stations.getKeys()) {
//            System.out.print(sourceCityIt + " {");
//            for(Station stationIt : trainStationManager2.stations.get(sourceCityIt)) {
//            	System.out.print(" (" + stationIt.getCityName() + "," + Integer.toString(stationIt.getDistance()) + ") ");
//            }
//            System.out.print("}");
//            System.out.println();
//        }
//        
//		System.out.println();
//		System.out.println("Shortest Routes");
//		for(String city : trainStationManager2.shortestRoutes.getKeys()) {
//			System.out.println(city + "{" + trainStationManager2.shortestRoutes.get(city) + "}");
//		}
//		
//		System.out.println();
//		System.out.println("Travel Times");
//		for(String city : trainStationManager2.travelTimes.getKeys()) {
//			System.out.println(city + "{" + trainStationManager2.travelTimes.get(city) + "}");
//		}
//
//		System.out.println();
//		System.out.println("Trace Route");
//		for(String city : trainStationManager2.cityListUnique2) {
//			System.out.println(city + ": " + trainStationManager2.traceRoute(city));
//		}
	}

}