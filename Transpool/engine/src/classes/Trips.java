package classes;

import java.util.ArrayList;
import java.util.List;

public class Trips {
    private List<Trip> tripList;
    private List<TripRequest> requestsLists;
    private java.util.Map hm;


    public Trips(List<Trip> tripList,java.util.Map hm) {
        this.tripList = tripList;
        this.requestsLists= new ArrayList<TripRequest>();
        this.hm=hm;
    }

    public void addTripRequestToList(TripRequest request) {
        requestsLists.add(request);
    }

    public void addTripToList(Trip trip) {
        tripList.add(trip);
    }

    public List<TripRequest> getRequestsLists() {
        return requestsLists;
    }

    public void setRequestsLists(List<TripRequest> requestsLists) {
        this.requestsLists = requestsLists;
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }
}