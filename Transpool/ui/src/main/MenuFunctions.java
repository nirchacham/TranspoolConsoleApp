package main;

import checks.FileCheck;
import classes.*;
import exceptions.*;
import print.Prints;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static print.Prints.*;

public class MenuFunctions {
    public static final int PASSENGERS_LIMIT = 4;
    public boolean loadXmlFile(Scanner scanner, TranspoolSystem system) {
        System.out.println("Please enter the xml path:");
        scanner.nextLine();
        String path = scanner.nextLine();
        FileCheck fileCheck = new FileCheck(path);
        try {
            fileCheck.runAllChecks();
            system.loadDataFromFile(fileCheck.getTransPool());//////////
            System.out.println("Xml file loaded successfully!\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Please enter another xml file.");
        }
        return true;
    }

    public boolean submitTripRequest(Scanner scanner, TranspoolSystem system) {
            boolean ok=false,isDeparture=false;
            int prefer;
            String origin,destination,name,time;
            Time usersTime = new Time();
            system.getMap().getStations().printStations();
            scanner.nextLine();
        do {
                System.out.println("Please choose origin station by it's name:");
                origin = scanner.nextLine();
               ok= checkIfValid(system, origin);
            }while(!ok);
            ok=false;
            do {
                System.out.println("Please choose destination station by it's name:");
                destination = scanner.nextLine();
                ok=checkIfValid(system, destination);
            }while (!ok);
            ok=false;
            System.out.println("Do you prefer to enter the wanted departure time or the wanted arrival time?" +
                    "\nFor departure time press 1.\nFor arrival time press 2.");
            do {
                try {
                prefer = scanner.nextInt();
                    switch (prefer) {
                        case 1:
                            scanner.nextLine();
                            System.out.println("Please enter departure time: (by format hh:mm) \nWe will round the time by multiples of 5");
                            isDeparture=true;
                            ok=true;
                            break;
                        case 2:
                            scanner.nextLine();
                            System.out.println("Please enter the wanted arrival time: (by format hh:mm) \nWe will round the time by multiples of 5");
                            ok=true;
                            break;
                        default:
                            System.out.println("You must choose an option between (1-2)");
                            break;
                    }
                }catch(InputMismatchException e) {
                    System.out.println("The choice must be a number!");
                    scanner.next();
                }
            }while (!ok);
            ok=false;
            do {
                scanner.reset();
                time = scanner.nextLine();
                ok = system.checkTime(time,usersTime);
            }while(!ok);

            System.out.println("Please enter your name:");
            do {
                name = scanner.nextLine();
                Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
                Matcher matcher = pattern.matcher(name);
                ok=true;
               // ok = name.matches(".*\\d+.*");
                if (!matcher.find()){
                    System.out.println("Input is invalid. It can not contain a number/special char.\nPlease try again.\n");
                    ok=false;
                }
            }while(!ok);

            TripRequest newRequest = new TripRequest(name, origin, destination,usersTime,isDeparture);
            system.getTrips().addTripRequestToList(newRequest);///////
            System.out.println("Your request was entered successfully");
            return true;
    }

    public boolean showTripsStatus(TranspoolSystem system) {
        System.out.println("These are all the existing trips in the system:");
        printTrips(system.getTrips().getTripList());
        return true;
    }

    public boolean showTripRequestsStatus(TranspoolSystem system) {
        Stream<TripRequest> requestStream = system.getTrips().getRequestsLists().stream();
        List<TripRequest> trList = requestStream.filter(tr -> !tr.isAssigned()).collect(Collectors.toList());
        printTripRequests(trList);
        Stream<TripRequest> assignedStream = system.getTrips().getRequestsLists().stream();
        List<TripRequest> trAssignedList = assignedStream.filter(tr -> tr.isAssigned()).collect(Collectors.toList());
        printAssignedTripRequests(trAssignedList);
        return true;
    }

    /*
    public boolean runCase5(Scanner scanner, TranspoolSystem system) {
        boolean isOk=false;
        int choice = 0,choice2=0,choice3;
        List<TripRequest> request;
        TripRequest userRequest = null;
        Stream<TripRequest> requestStream = system.getTrips().getRequestsLists().stream();
        requestStream.filter(tr -> !tr.isAssigned()).forEach(System.out::println);
        if(system.getTrips().getRequestsLists().size()==0) {
            System.out.println("There are no pre assigned requests, please enter a request first (choice number two).");
            return false;
        }
        do {
            System.out.println("Please choose the request number that you want to assign:");
            try {
                choice = scanner.nextInt();
                userRequest = system.bringTripRequest(choice);
                isOk=true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter only a number.");
            } catch (TripRequestDoesntExists e){
                System.out.println(e.getMessage());
            }
            // Stream<TripRequest> requestStream2 = system.getTrips().getRequestsLists().stream();
            //request=requestStream2.filter(r ->r.getTripRequestNumber()==choice).collect(Collectors.toList());
            //TripRequest userRequest = system.bringTripRequest(choice);
        }while(!isOk);

            System.out.println("Please choose the maximum options of assigns that you want to get:");
            do {
                try {
                    choice2 = scanner.nextInt();
                    isOk=true;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter only a number.");
                }
            }while(!isOk);
            List<Trip> opTrips = system.bringOptionalTrips(userRequest);

//        Stream <Trip> optionalTrips = system.getTrips().getTripList().stream();
//        List<Trip> opTrips = optionalTrips.
//                filter(trip -> trip.getPassengersNumber()>0).
//                filter(trip->trip.findPossibleTrip(request.get(0))).
//                sorted((t1,t2)->t1.compareByPrice(t1,t2,request.get(0))).
//                sorted((t1,t2)->t1.compareByTime(t1,t2,request.get(0))).collect(Collectors.toList())
//
                //limit(choice2).forEach(t->printPossibleTrips(request.get(0),t));

        if(opTrips.size()==0){
            System.out.println("There are no trips that match your request.");
            return false;
        }
        for(int i=0;i<choice2 && i<opTrips.size();i++){
            printPossibleTrips(request.get(0),opTrips.get(i));
        }
        System.out.println("Please choose the trip number that you want to be assigned to.");
        // take care if he doesnt want to choose any trip (exit)
        choice3=scanner.nextInt();
        request.get(0).setAssigned(true);
        List<Trip> trip;
        Stream<Trip> tripStream = system.getTrips().getTripList().stream();
        trip = tripStream.filter(t->t.getTripNumber()==choice3).collect(Collectors.toList());
        trip.get(0).setPassengersNumber(trip.get(0).getPassengersNumber()-1);
        trip.get(0).updateCurrPassengers(request.get(0));
        request.get(0).updateTripHm(trip.get(0));

        return true;
    }
    */

    public boolean findTripRequestMatch(Scanner scanner, TranspoolSystem system) {
        boolean isOk=false;
        int choice = 0,choice2=0,choice3=0;
        TripRequest userRequest = null;
        Stream<TripRequest> requestStream = system.getTrips().getRequestsLists().stream();
        List<TripRequest> trList = requestStream.filter(tr -> !tr.isAssigned()).collect(Collectors.toList());
        printTripRequests(trList);
        if(system.getTrips().getRequestsLists().size()==0) {
            System.out.println("There are no pre assigned requests, please enter a request first (choice number two).");
            return true;
        }
        do {
            System.out.println("Please choose the request number that you want to assign:");
            try {
                choice = scanner.nextInt();
                userRequest = system.bringTripRequest(choice);
                isOk=true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter only a number.");
                scanner.next();
            } catch (TripRequestDoesntExists e){
                System.out.println(e.getMessage());
            }
        }while(!isOk);
        isOk=false;
        System.out.println("Please choose the maximum options of assigns that you want to get:");
        do {
            try {
                choice2 = scanner.nextInt();
                isOk=true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter only a number.");
                scanner.next();
            }
        }while(!isOk);
        List<Trip> opTrips = system.bringOptionalTrips(userRequest);

        if(opTrips.size()==0){
            System.out.println("There are no trips that match your request.");
            return true;
        }
        for(int i=0;i<choice2 && i<opTrips.size();i++){
            printPossibleTrips(userRequest,opTrips.get(i));
        }
        isOk=false;
        System.out.println("Please choose the trip number that you want to be assigned to.\nIf you want to return to the menu press -1.");
        do {
            try {
                choice3 = scanner.nextInt();
                if(choice3==-1)
                    return true;
                Trip trip = system.bringWantedTrip(opTrips,choice3);
                system.addPassengerToTrip(trip,userRequest);
                System.out.println("The trip request was assigned successfully!\n");
                isOk=true;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Please enter only a number.");
            }catch (TripDoesntExists e) {
                System.out.println(e.getMessage());
            }
        }while(!isOk);

        return true;
    }

    public boolean addNewTrip(Scanner scanner, TranspoolSystem system) {
        String name,course,currFrom,currTo,time;
        Time usersTime = new Time();
        Course newCourse;
        ArrayList<String> route = new ArrayList<>();
        int ppk=0,capacity=0;
        boolean ok=false;
        System.out.println("Please enter your name:");
        scanner.nextLine();
        do {
            //scanner.nextLine();
            name = scanner.nextLine();
            Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
            Matcher matcher = pattern.matcher(name);
            ok=true;
            //ok = name.matches(".*\\d+.*");
            if (!matcher.find()){
                System.out.println("Input is invalid. It can not contain a number.\nPlease try again.\n");
                //scanner.nextLine();
                ok=false;
            }
        }while(!ok);
        ok=false;
        system.getMap().getStations().printStations();
        System.out.println("Please enter the stations name that you are going through separated with ','" +
                "\n(For example: Arlozorov,Habima,Florentin)");
        do {
            try {
                course = scanner.nextLine();
                Pattern pattern = Pattern.compile("^[a-zA-Z, ]+$");
                Matcher matcher = pattern.matcher(course);
                ok=true;
                //ok = course.matches(".*\\d+.*");
                if (!matcher.find()) {
                    System.out.println("Input is invalid. It can not contain a number.\nPlease try again.\n");
                    ok=false;
                }
                else {
                    route = system.createRoute(course);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                ok=false;
            }
        }while(!ok);
        ok=false;
        System.out.println("Please enter departure time of the trip: (by format hh:mm) \nWe will round the time by multiples of 5");
        do {
           // scanner.reset();
            time = scanner.nextLine();
            ok = system.checkTime(time,usersTime);
        }while(!ok);
        ok=false;
        System.out.println("Please enter the price per kilometer:");
        do {
            try {
                ppk = scanner.nextInt();
                ok=true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter only a number.");
                scanner.next();
            }
        }while(!ok);
        ok=false;
        System.out.println("Please enter the capacity of passengers that you can drive:");
        do {
            try {
                capacity = scanner.nextInt();
                if(capacity<0 || capacity > PASSENGERS_LIMIT) {
                    ok = false;
                    System.out.println("The number of passenger must be between 0 and " + PASSENGERS_LIMIT);
                }
                else
                    ok=true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter only a number.");
                scanner.next();
            }
        }while(!ok);

        Trip newTrip = new Trip(name,capacity,ppk,new Course(route,system.getHm()),usersTime,system.getHm());
        system.getTrips().addTripToList(newTrip);
        return true;
    }


    public boolean checkIfValid(TranspoolSystem system, String origin)  {
        try {
            boolean isOk;
            isOk = origin.matches(".*\\d+.*");
            if (isOk) {
                throw new HasNumber();
            }
            isOk = system.getMap().getStations().checkStationExistence(origin.trim());
            if (!isOk) {
                throw new StationDoesntExists(origin);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

//    public List<Trip> sortTrips(List<Trip> tripList, TripRequest tripRequest) {
//        Comparator<Trip> comp = (t1, t2) -> t1.compareByPrice(t1, t2, tripRequest);
//        Comparator<Trip> comp2 = (t1, t2) -> t1.compareByTime(t1, t2, tripRequest);
//        List<Trip> temp1 = new ArrayList<>();
//        List<Trip> temp2 = new ArrayList<>();
//        for (Trip trip : tripList) {
//            if (trip.getPassengersNumber() > 0) {
//                if (trip.findPossibleTrip(tripRequest)) {
//                    temp1.add(trip);
//                    temp2.add(trip);
//                }
//            }
//        }
//        temp1.sort(comp);
//        temp2.sort(comp2);
//        return temp1;
//    }
}









//Trip::printPossibleTrips(request.getOrigin(),request.getDestination())
