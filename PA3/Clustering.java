/*
 * Name: Sylvia Vu
 * EID: sav987
 */

import java.util.HashMap;
import java.util.ArrayList;

public class Clustering {

    public int numStations;
    public int numHouses;

    public int[][] groups;      // (n x k) array that stores a 1 at the intersection of a house and a group
    public Float[] centroids;       // positions of the centroids
    public Float[][] responseTimes;     // DP implementation for response times

    public ArrayList<Float> housePositions;     // stores positions of the houses (given)
    public ArrayList<Group> clusters;   // will hold the final groups

    public HashMap<Integer, Integer> groupMap;  // quick way to look up which house is in which group


    public Clustering(int numHouses, int numStations, ArrayList<Float> positions){
        this.numHouses = numHouses;
        this.numStations = numStations;
        this.groups = new int[numHouses][numStations];
        this.housePositions = positions;
        this.centroids = new Float[numStations];
        this.groupMap = new HashMap<>();
        this.clusters = new ArrayList<Group>();
        this.responseTimes = new Float[numHouses][numStations];
    }


    public ArrayList<Group> makeClusters(TownPlan town) {
        ArrayList<Float> housePositions = town.getPositionHouses();

        for(int i = 0; i < numHouses; i++){
            groupMap.put(i, -1);
        }

        // one fire station
        if(numStations == 1){
            ArrayList<Integer> houseList = new ArrayList<>();
            for(int i = 0; i < numHouses; i++){
                houseList.add(i);
            }
            Group newGroup = new Group(0, houseList);
            clusters.add(newGroup);
            return this.clusters;
        }

        // initial centroids are at house positions
        int increments = numHouses / numStations;
        int index = 0;
//        System.out.println("Increments: " + increments + " houses");
//        for (int i = 0; i < numHouses; i += increments) {
//            centroids[index] = housePositions.get(i);
//            index++;
//            System.out.println("House number: " + i + ", Position: " + housePositions.get(i));
//        }

        for(int i = increments-1; i < numHouses; i += increments){
            centroids[index] = housePositions.get(i);
            index++;

            //System.out.println("House number: " + i + ", Position: " + housePositions.get(i));
        }

        //System.out.println("centroids length: " + centroids.length);

        // go through all houses and find which centroid they're closest to
        int updated = 1;
        while(updated > 0){
            updated = 0;
            for(int i = 0; i < numHouses; i++){     // i is the current house
               Float currHousePosition = housePositions.get(i);
               int nearestCentroid = -1;        // keep track of which centroid is closest to the current house
               Float closest = Float.MAX_VALUE;
               Float tempDistance;

               // finding the nearest centroid
               for(int j = 0; j < centroids.length; j++){
                   if((tempDistance = Math.abs(currHousePosition - centroids[j])) < closest){
                       closest = tempDistance;
                       nearestCentroid = j;
                   }
               }
               //System.out.println("Current house: " + i + ", nearest centroid: " + nearestCentroid);
               groups[i][nearestCentroid] = 1;      // update house/centroid in the table

               // update
               if(nearestCentroid != groupMap.get(i)){
                   updated++;
                   groupMap.replace(i, nearestCentroid);
               }
            }
            // TEST TO SEE WHAT GROUPS LOOKS LIKE
            for(int a = 0; a < numHouses; a++){
                for(int b = 0; b < numStations; b++){
                    System.out.print(groups[a][b]);
                    if(b == numStations-1){
                        System.out.println();
                    }
                }
            }

            recalculateCentroids();
        }

//        System.out.println("\nFINAL GROUPS");
//        for(int a = 0; a < numHouses; a++){
//            for(int b = 0; b < numStations; b++){
//                System.out.print(groups[a][b]);
//                if(b == numStations-1){
//                    System.out.println();
//                }
//            }
//        }

        // Create official groups
        for(int a = 0; a < numStations; a++){
            ArrayList<Integer> tempHouses = new ArrayList<>();

            for(int b = 0; b < numHouses; b++){
                if(groups[b][a] == 1){
                    tempHouses.add(b);
                }
            }
            Group newGroup = new Group(a, tempHouses);
            clusters.add(newGroup);
        }

        // TEST TO CHECK CLUSTERS
//        for(int x = 0; x < clusters.size(); x++){
//            System.out.println("Fire station: " + clusters.get(x).stationNumber + ", Houses: " + clusters.get(x).houses);
//        }

        return this.clusters;

    }


    public Float[][] fillResponseTimes(){ return this.responseTimes; }


    public void recalculateCentroids(){
        // go through Clustering.groups and look at the columns (columns are fire stations/clusters)
        // going down a column, a 1 indicates that a house belongs to that cluster
        // find the mean of the distances of all of those houses
        // the mean is the new centroid
        // update the values in Clustering.centroids
        // the while loop in makeClusters will run again

        Float tempSum;
        int housesInGroup;
        Float newPosition;

        // i = columns, j = rows
        for(int i = 0; i < numStations; i++) {
            tempSum = 0.0f;
            housesInGroup = 0;
            newPosition = -1.0f;

            for (int j = 0; j < numHouses; j++) {
                if (this.groups[j][i] == 1) {
                    tempSum += housePositions.get(j);
                    housesInGroup++;
                }

                newPosition = tempSum / housesInGroup;
                centroids[i] = newPosition;
            }

        }
        // TEST TO CHECK WHERE ALL NEW CENTROIDS ARE
//        System.out.println("\nNew centroids: ");
//        for (int a = 0; a < centroids.length; a++) {
//            System.out.print(centroids[a] + ", ");
//        }
    }


    public Float findOptimalResponse(ArrayList<Group> finalGroups){
        // go through Clustering.clusters
        // find optimal fire station location --> |firstHouse + lastHouse| / 2
        // store this in Group.stationPosition
        // calculate optimal response time --> |lastHouse - fire station location|
        // store this in Group.responseTime
        // store this value in responseTimes for each house in that group
        // keep track of overall optimal response (worst-case scenario)

        Float optimal = -1.0f;
        for(int i = 0; i < this.clusters.size(); i++){
            ArrayList<Integer> currHouses = this.clusters.get(i).houses;

            int firstHouse = currHouses.get(0);
            int lastHouse = currHouses.get(currHouses.size() - 1);
            Float currStationPosition = Math.abs(housePositions.get(firstHouse) + housePositions.get(lastHouse)) / 2;
            this.clusters.get(i).stationPosition = currStationPosition;

            Float currResponseTime = Math.abs(housePositions.get(lastHouse) - currStationPosition);
            this.clusters.get(i).responseTime = currResponseTime;
            if(currResponseTime > optimal){
                optimal = currResponseTime;
            }
        }

        return optimal;
    }


    public ArrayList<Float> getStationPositions(ArrayList<Group> finalGroups){
        ArrayList<Float> finalStationPositions = new ArrayList<>();

        for(int i = 0; i < this.clusters.size(); i++){
            Group currGroup = this.clusters.get(i);
            finalStationPositions.add(currGroup.stationPosition);
        }

        return finalStationPositions;
    }



}
