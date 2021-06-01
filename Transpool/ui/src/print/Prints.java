package print;

import classes.Trip;
import classes.TripRequest;

import java.util.List;

public class Prints {
    static public void printTrips(List<Trip> trips){
        if(trips.size()==0)
            System.out.println("There are currently no trips in the system.");
        for (Trip trip:trips){
            System.out.println("Trip number='" + trip.getTripNumber() + '\'' +
                    "\nDriver's name=" + trip.getDriverName() +
                    "\nCourse=" + trip.getCourse().getCourse() +
                    "\nTotal trip price=" + trip.getCourse().calculateCourseLength(trip.getCourse().getCourse().get(0),trip.getCourse().getCourse().get(trip.getCourse().getCourse().size()-1)) * trip.getPPK() +
                    "\nDeparture time=" + trip.getDepartureTime() +
                    "\nArrival time=" + trip.getArrivalTime() +
                    "\nPassenger's capacity=" + trip.getPassengersNumber() +
                    "\nAverage fuel consumption=" + trip.getAverageFuelConsumption()+
                    "\nPassengers details=");
            printPassengers(trip);
            printStationsInfo(trip);
            System.out.println("");
        }
    }

    static public void printPassengers(Trip trip ) {
        if(trip.getCurrPassengers().size() == 0)
            System.out.println("There are no passengers yet in this trip.");
        for(TripRequest tr:trip.getCurrPassengers()) {
            System.out.println( "#" + tr.getTripRequestNumber() + " " + tr.getPassengerName());
        }
    }

    static public void printStationsInfo(Trip trip ) {
        boolean isOk=false;
        if(trip.getCurrPassengers().size() == 0)
            return;
        System.out.println("Dropping off/Picking up stations:");
        for(String stations:trip.getCourse().getCourse()) {
            for (TripRequest tr : trip.getCurrPassengers()) {
                if (stations.equalsIgnoreCase(tr.getOrigin()) && !isOk) {
                    System.out.println("\nStation " + stations + ":\n" + tr.getPassengerName() + " is being picked up.");
                    isOk = true;
                } else if (stations.equalsIgnoreCase(tr.getDestination()) && !isOk) {
                    System.out.println("\nStation " + stations + ":\n" + tr.getPassengerName() + " is being dropped off.");
                    isOk = true;
                } else if (stations.equalsIgnoreCase(tr.getOrigin()) && isOk) {
                    System.out.println(tr.getPassengerName() + " is being picked up.");
                } else if (stations.equalsIgnoreCase(tr.getDestination()) && isOk) {
                    System.out.println(tr.getPassengerName() + " is being dropped off.");
                }
            }
            isOk = false;
        }
    }

    static public void printTripRequests(List<TripRequest> trList){
        if(trList.size()==0)
            System.out.println("There are currently no unassigned trip requests in the system.");
        for(TripRequest tr:trList){
                    System.out.print("TripRequest Number " + tr.getTripRequestNumber() + " {" +
                    "Passenger Name=" + tr.getPassengerName() +
                    ", Origin=" + tr.getOrigin() +
                    ", Destination=" + tr.getDestination());
                    if(tr.isDepartureTime()==true) {
                        System.out.print(", Departure time=" + tr.getTime());
                    }
                    else{
                        System.out.print(", Arrival time=" + tr.getTime());
                    }
                    System.out.println('}');
        }
    }

    static public void printAssignedTripRequests(List<TripRequest> trList){
        if(trList.size()==0)
            System.out.println("There are currently no assigned trip requests in the system.");
        for(TripRequest tr:trList){
            Trip trip = (Trip)tr.getTripHm().get(tr.getTripRequestNumber());
            System.out.print("TripRequest Number " + tr.getTripRequestNumber() + " is assigned to: ");
            printPossibleTrips(tr,trip);
        }
    }

    static public void printPossibleTrips(TripRequest tr,Trip trip){
        double length=trip.getCourse().calculateCourseLength(tr.getOrigin(),tr.getDestination());
        System.out.print("Trip Number " + trip.getTripNumber() + " {" +
                "Driver's Name='" + trip.getDriverName() +
                ", Trip's price: " + length*trip.getPPK());
        if(tr.isDepartureTime()==true)
               System.out.print( ", Estimated arrival time: "+trip.estimatedArrivalTimeToStation(tr.getDestination()));
        else
               System.out.print( ", Departure time: "+trip.estimatedArrivalTimeToStation(tr.getOrigin()));

        System.out.println(", Average fuel consumption: "+trip.getCourse().calculateFuel(tr.getOrigin(),tr.getDestination())/length+"}\n");
    }
}
