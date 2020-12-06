/*
 * Name: Sylvia Vu
 * EID: sav987
 */

import java.util.ArrayList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program3 extends AbstractProgram3 {

    /**
     * Determines the solution of the optimal response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "response" field set to the optimal response time
     */
    @Override
    public TownPlan OptimalResponse(TownPlan town) {

        int numHouses = town.getHouseCount();
        int numStations = town.getStationCount();
        ArrayList<Float> housePositions = town.getPositionHouses();
        Clustering clusterData = new Clustering(numHouses, numStations, housePositions);

        ArrayList<Group> finalGroups = clusterData.makeClusters(town);

        // TEST TO CHECK CLUSTERS
//        for(int x = 0; x < clusterData.clusters.size(); x++){
//            System.out.println("Fire station: " + clusterData.clusters.get(x).stationNumber + ", Houses: " + clusterData.clusters.get(x).houses);
//        }

        Float optimal = clusterData.findOptimalResponse(finalGroups);

        // TEST TO CHECK OPTIMAL TIME
//        System.out.println("Optimal response time is: " + optimal);

        town.setResponse(optimal);

        return town;
    }

    /**
     * Determines the solution of the set of fire station positions that optimize response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "position_fire_stations" field set to the optimal fire station positions
     */
    @Override
    public TownPlan OptimalPosFireStations(TownPlan town) {
        int numHouses = town.getHouseCount();
        int numStations = town.getStationCount();
        ArrayList<Float> housePositions = town.getPositionHouses();
        Clustering clusterData = new Clustering(numHouses, numStations, housePositions);

        ArrayList<Group> finalGroups = clusterData.makeClusters(town);
        clusterData.findOptimalResponse(finalGroups);

        ArrayList<Float> finalStationPositions = clusterData.getStationPositions(finalGroups);

        town.setPositionFireStations(finalStationPositions);
        String output = town.getSolutionString();
        System.out.print(output);

        return town;
    }





}
