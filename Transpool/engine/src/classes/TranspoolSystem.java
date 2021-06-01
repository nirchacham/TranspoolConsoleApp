package classes;

import exceptions.*;
import schema.Path;
import schema.Stop;
import schema.TransPool;
import schema.TransPoolTrip;
import schema.Route;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranspoolSystem {
    private Map map;
    private Trips trips;
    private java.util.Map hm;////

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Trips getTrips() {
        return trips;
    }

    public void setTrips(Trips trips) {
        this.trips = trips;
    }

    public void loadDataFromFile(TransPool transPool) {
        loadMap(transPool);
        loadTrips(transPool);
    }

    public void loadMap(TransPool transPool) {
        List<Road> roads = new ArrayList<>();
        List<Station> stations = new ArrayList<>();
        java.util.Map<String, Road> hm = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);///
        map = new Map(transPool.getMapDescriptor().getMapBoundries().getLength(), transPool.getMapDescriptor().getMapBoundries().getWidth());
        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {
            Road road = new Road(path.getFrom().trim(), path.getTo().trim(), path.isOneWay(), path.getLength(), path.getFuelConsumption(), path.getSpeedLimit());
            hm.put(path.getFrom().trim() + "," + path.getTo().trim(), road);///
            roads.add(road);
        }
        map.setRoads(new Roads(roads));
       this.hm = hm;////
        for (Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            Station station = new Station(stop.getName().trim(), stop.getX(), stop.getY());
            stations.add(station);
        }
        map.setStations(new Stations(stations));
    }

    public void loadTrips(TransPool transPool) {
        List<Trip> tripList = new ArrayList<>();
        ArrayList<String> course = new ArrayList<>();
        String route, curr;
        for (TransPoolTrip TPTrip : transPool.getPlannedTrips().getTransPoolTrip()) {
            route = TPTrip.getRoute().getPath();
            course = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(route, ",");
            while (st.hasMoreTokens()) {
                course.add(st.nextToken().trim());
            }
            //calculateTripLength(course);
            Trip trip = new Trip(TPTrip.getOwner(), TPTrip.getCapacity(), TPTrip.getPPK(), new Course(course,hm), TPTrip.getScheduling().getHourStart(),hm);////
            tripList.add(trip);
        }
        trips = new Trips(tripList,hm);///
    }

    public java.util.Map getHm() {
        return hm;
    }

    public void setHm(java.util.Map hm) {
        this.hm = hm;
    }

    public List<Trip> bringOptionalTrips(TripRequest request){
        Stream<Trip> optionalTrips = getTrips().getTripList().stream();
        List<Trip> opTrips = optionalTrips.
                filter(trip -> trip.getPassengersNumber()>0).
                filter(trip->trip.findPossibleTrip(request)).collect(Collectors.toList());
//                sorted((t1,t2)->t1.compareByPrice(t1,t2,request)).
//                sorted((t1,t2)->t1.compareByTime(t1,t2,request))
        return opTrips;
    }

    public Trip bringWantedTrip(List<Trip> tripList,int number) throws TripDoesntExists {
        //Stream<Trip> optionalTrips = getTrips().getTripList().stream();
        List<Trip> opTrips = tripList.stream().
                filter(trip -> trip.getTripNumber()==number).collect(Collectors.toList());
        if (opTrips.size()==0)
            throw new TripDoesntExists(number);
        return opTrips.get(0);
    }

    public TripRequest bringTripRequest(int tripNumber) throws TripRequestDoesntExists{
        List<TripRequest> request;
        Stream<TripRequest> requestStream2 = trips.getRequestsLists().stream();
        request=requestStream2.filter(r ->r.getTripRequestNumber()==tripNumber).collect(Collectors.toList());
        if(request.size()==0)
        {
            throw new TripRequestDoesntExists(tripNumber);
        }
        return request.get(0);
    }

    public void addPassengerToTrip(Trip trip,TripRequest userRequest) {
        userRequest.setAssigned(true);
        trip.setPassengersNumber(trip.getPassengersNumber()-1);
        trip.updateCurrPassengers(userRequest);
        userRequest.updateTripHm(trip);
    }


    public ArrayList<String> createRoute(String route) throws InvalidRoute, SingleStationCourse {
        boolean found=false;
        ArrayList<String> course = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(route,",");
        String currFrom = null;
        String currTo=st.nextToken().trim();
        if(!st.hasMoreTokens())
            throw new SingleStationCourse();
        while(st.hasMoreTokens()) {
            currFrom=currTo;
            currTo=st.nextToken().trim();
            for(Road road:map.getRoads().getRoadList()) {
                if(currFrom.equalsIgnoreCase(road.getOrigin().trim()) && currTo.equalsIgnoreCase(road.getDestination().trim()) ) {
                    found=true;
                    course.add(currFrom);
                    //course.add(currTo);
                    break;
                }
                if(currFrom.equalsIgnoreCase(road.getDestination().trim()) && currTo.equalsIgnoreCase(road.getOrigin().trim()) && road.isOneWay()==false) {
                    found=true;
                    course.add(currFrom);
                   // course.add(currTo);
                    break;
                }
            }
            if(!found) {
                course.clear();
                throw new InvalidRoute(currFrom, currTo);
            }
            found=false;
        }
        course.add(currTo);
        return course;
    }

    public void checkRangeOfInt(int hour, int minutes) throws OutOfRange {
        if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59) {
            throw new OutOfRange();
        }
    }

    public boolean checkTime(String time,Time updatedTime) {
        try {
            if (time.length() != 5 || time.charAt(2) != ':') {
                throw new WrongTimeFormat();
            }
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            updatedTime.updateTime(hour, minutes);
            checkRangeOfInt(hour, minutes);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}