/*
 * Name: Sylvia Vu
 * EID: sav987
 */

import java.util.ArrayList;

public class Group {

    public int stationNumber;
    public ArrayList<Integer> houses;
    public Float responseTime;
    public Float stationPosition;

    public Group(int station){
        this.stationNumber = station;
        this.houses = new ArrayList<>();
        this.responseTime = -1.0f;
        this.stationPosition = -1.0f;
    }

    public Group(int station, ArrayList<Integer> houses){
        this.stationNumber = station;
        this.houses = houses;
        this.responseTime = -1.0f;
        this.stationPosition = -1.0f;
    }
}
