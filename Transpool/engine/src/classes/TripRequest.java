package classes;

import java.util.HashMap;
import java.util.Map;

public class TripRequest {
    private String passengerName;
    private String origin;
    private String destination;
    private Time time;
    private boolean isAssigned;
    private static int counter =0;
    private int TripRequestNumber;
    private java.util.Map tripHm;
    private boolean isDepartureTime;

    public TripRequest(String passengerName, String origin, String destination, Time time,boolean isDepartureTime) {
        this.passengerName = passengerName;
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.time.roundHour();
        this.isDepartureTime = isDepartureTime;
        TripRequestNumber = ++counter;
        tripHm = new HashMap<>();///
    }

    public void updateTripHm(Trip trip){
        tripHm.put(this.TripRequestNumber,trip);
    }

    public Map getTripHm() {
        return tripHm;
    }

    public int getTripRequestNumber() {
        return TripRequestNumber;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isDepartureTime() {
        return isDepartureTime;
    }

}
