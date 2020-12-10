/*
 * Name: Sylvia Vu
 * EID: sav987
 */

import java.util.ArrayList;


public class DP {

    public int numHouses;
    public int numStations;

    public ArrayList<Float> houses;
    public ArrayList<Float> times;

    public ArrayList<Float>[][] dp_pos;
    public Float[][] dp;
    public Float[][] range;
    public Float[][] medians;


    public DP(int numHouses, int numStations, ArrayList<Float> houses) {
        this.numHouses = numHouses;
        this.numStations = numStations;

        this.houses = houses;
        this.times = new ArrayList<>();

        this.dp_pos = new ArrayList[numHouses][numStations];
        this.dp = new Float[numHouses+1][numStations+1];
        this.range = new Float[numHouses+1][numHouses+1];
        this.medians = new Float[numHouses+1][numHouses+1];
    }


    public Float search() {
        // populate medians[][] and range[][]
        // medians[i][j] is the median POSITION between houses i and j
        // range[i][j] is the optimal response time between houses i and j
        for (int i = 0; i < numHouses; i++) {
            for (int j = i; j < numHouses; j++) {
                medians[i][j] = ((houses.get(i) + houses.get(j)) / 2);
                range[i][j] = Math.abs((houses.get(i) + houses.get(j)) / 2 - houses.get(j));
            }
        }


        // initialize dp tables
        for (int i = 0; i < numHouses; i++) {
            for (int j = 0; j < numStations; j++) {
                dp[i][j] = Float.MAX_VALUE;
                dp_pos[i][j] = new ArrayList<Float>();
            }
        }


        // initialize the first column of dp[][] and stationPositions[][]
        for (int i = 0; i < numHouses; i++) {
            dp[i][0] = range[0][i];
            int finalI = i;

            dp_pos[i][0] = new ArrayList<Float>(){{
                add(medians[0][finalI]);
            }};

            if (i < numStations && i > 0) {
                dp_pos[i][i] = new ArrayList<Float>(dp_pos[i-1][i-1]){{
                    add(medians[finalI][finalI]);
                }};
            }
        }


//        // TEST TO CHECK DP_POS INITIALIZATION
//        System.out.println();
//        for (int i = 0; i < numHouses; i++) {
//            for (int j = 0; j < numStations; j++) {
//                System.out.print(dp_pos[i][j] + " ");
//            }
//            System.out.println();
//        }


        Float maxCalc = 0.0f;
        Float tempRange = 0.0f;
        int row = 0;
        int col = 0;
        ArrayList<Float> tempList = new ArrayList<>();
        for (int i = 0; i < numHouses; i++) {
            for (int k = 1; k < numStations; k++) {

                if (i <= k) {
                    dp[i][k] = 0.0f;
                }

                for (int j = 0; j < i; j++) {
                    maxCalc =  Math.max(dp[j][k - 1], range[j + 1][i]);

                    if (maxCalc < dp[i][k]) {
                        int finalJ = j;
                        int finalI = i;
                        tempList = new ArrayList<Float>(dp_pos[finalJ][k-1]){{
                            add(medians[finalJ +1][finalI]);
                        }};
                    }
                    dp[i][k] = Math.min(dp[i][k], maxCalc);
                }

                dp_pos[i][k] = tempList;
            }
        }

//        System.out.println();
//        for (int i = 0; i < numHouses; i++) {
//            for (int j = 0; j < numStations; j++) {
//                System.out.print(dp[i][j] + " ");
//            }
//            System.out.println();
//        }

        return dp[numHouses-1][numStations-1];
    }


    public ArrayList<Float> getStationPositions() {
        return this.dp_pos[numHouses-1][numStations-1];
    }


}
