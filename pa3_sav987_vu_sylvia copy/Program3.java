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
        DP solution = new DP(town.getHouseCount(), town.getStationCount(), town.getPositionHouses());

        Float optResponse = solution.search();

        town.setResponse(optResponse);

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
        DP solution = new DP(town.getHouseCount(), town.getStationCount(), town.getPositionHouses());

        solution.search();
        ArrayList<Float> positions = solution.getStationPositions();

        town.setPositionFireStations(positions);

        return town;
    }
}
