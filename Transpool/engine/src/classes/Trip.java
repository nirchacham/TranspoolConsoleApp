package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Trip {
    private String driverName;
    private int passengersNumber;
    private double PPK;
    private double averageFuelConsumption;
    private Course course;
    private int tripNumber;
    private static int counter = 100;
    private Time departureTime;
    private Time arrivalTime;
    private java.util.Map hm;/////
    private Set<TripRequest> currPassengers;


    public Trip(String driverName, int passengersNumber, double PPK, Course course,int departure,java.util.Map hm) {
        this.driverName = driverName;
        this.passengersNumber = passengersNumber;
        this.PPK = PPK;
        this.course = course;
        this.hm=hm;////
        double length=course.calculateCourseLength(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1));
        averageFuelConsumption=course.calculateFuel(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1))/length;
        departureTime=new Time(departure,0);
        arrivalTime=estimatedArrivalTimeToStation(course.getCourse().get(course.getCourse().size()-1));
        tripNumber=++counter;
        currPassengers = new HashSet<>();
    }

    public Trip(String driverName, int passengersNumber, double PPK, Course course,Time departure,java.util.Map hm) {
        this.driverName = driverName;
        this.passengersNumber = passengersNumber;
        this.PPK = PPK;
        this.course = course;
        this.hm=hm;////
        double length=course.calculateCourseLength(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1));
        averageFuelConsumption=course.calculateFuel(course.getCourse().get(0),course.getCourse().get(course.getCourse().size()-1))/length;
        departureTime= departure;
        arrivalTime=estimatedArrivalTimeToStation(course.getCourse().get(course.getCourse().size()-1));
        tripNumber=++counter;
        currPassengers = new HashSet<>();
    }

    public int compareByPrice(Trip first,Trip second,TripRequest request){
        double firstPrice=(first.course.calculateCourseLength(request.getOrigin(),request.getDestination()))*first.PPK;
        double secondPrice=(second.course.calculateCourseLength(request.getOrigin(),request.getDestination()))*second.PPK;
        if(firstPrice>secondPrice)
            return 1;
        else if (firstPrice==secondPrice)
            return 0;
        else
            return -1;
    }

    public int compareByTime(Trip first,Trip second,TripRequest request){
        Time firstTime=first.estimatedArrivalTimeToStation(request.getDestination());
        Time secondTime=first.estimatedArrivalTimeToStation(request.getDestination());
        return firstTime.compareTime(secondTime);
    }


    public void calculateArrivalTime(){
        int sum=0;
        Road road = new Road();
        for(int i=0;i<course.getCourse().size()-1;i++){
            road= (Road)hm.get(course.getCourse().get(i)+","+course.getCourse().get(i+1));
            if(road==null)
                road= (Road)hm.get(course.getCourse().get(i+1)+","+course.getCourse().get(i));
            sum+=(road.getRoadLength()/road.getMaxSpeed())*60;
        }
        this.arrivalTime = new Time(departureTime);
        this.arrivalTime.updateTime(sum/60,sum%60);
        this.arrivalTime.roundHour();
    }

    public boolean findPossibleTrip(TripRequest request){
        int from=-1;
        int to=-1;
        boolean isOk=false;
        ArrayList<String> courseArr = course.getCourse();
        for(int i=0;i<courseArr.size();i++) {
            if (courseArr.get(i).equalsIgnoreCase(request.getOrigin()))
                from = i;
            if (courseArr.get(i).equalsIgnoreCase(request.getDestination())) {
                to = i;
                break;
            }
        }
        if (from<to&&from!=-1&&to!=-1)
            isOk= true;
        if(isOk) {
            if (request.isDepartureTime() == true) {
                return request.getTime().equals(estimatedArrivalTimeToStation(request.getOrigin()));
            }
            else {
                return request.getTime().equals(estimatedArrivalTimeToStation(request.getDestination()));
            }
        }
        return false;
    }

    public Time estimatedArrivalTimeToStation(String station){
        int sum=0;
        Road road = new Road();
        for(int i=0;i<course.getCourse().size()-1;i++){
            road= (Road)hm.get(course.getCourse().get(i)+","+course.getCourse().get(i+1));
            if(road==null)
                road= (Road)hm.get(course.getCourse().get(i+1)+","+course.getCourse().get(i));
            if(course.getCourse().get(i).equalsIgnoreCase(station))
                break;
            sum+=(road.getRoadLength()/road.getMaxSpeed())*60;
        }
        Time tempTime= new Time(departureTime);
        tempTime.updateTime(sum/60,sum%60);
        tempTime.roundHour();
        return tempTime;
    }


    public int getTripNumber() {
        return tripNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getPassengersNumber() {
        return passengersNumber;
    }

    public double getAverageFuelConsumption() {
        return averageFuelConsumption;
    }


    public void setPassengersNumber(int passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public double getPPK() {
        return PPK;
    }

    public void setPPK(double PPK) {
        this.PPK = PPK;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }


    public Set<TripRequest> getCurrPassengers() {
        return currPassengers;
    }

    public void updateCurrPassengers(TripRequest request) {
        currPassengers.add(request);
    }

}
