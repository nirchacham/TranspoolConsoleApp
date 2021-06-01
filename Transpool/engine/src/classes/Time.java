package classes;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import exceptions.OutOfRange;
import exceptions.WrongExtension;
import exceptions.WrongTimeFormat;
import org.w3c.dom.ls.LSOutput;

import java.util.Objects;

public class Time {
    private int hour;
    private int minutes;

    public Time(){hour=minutes=0;}

    public Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
        roundHour();
    }

    public Time(Time time) {
        this.hour=time.getHour();
        this.minutes=time.getMinutes();
        roundHour();
    }

    @Override
    public String toString() {
        String format=String.format("%02d",hour);
        String format2=String.format("%02d",minutes);
        return
                format + ":" + format2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return hour == time.hour &&
                minutes == time.minutes;
    }

    public int compareTime(Time time) {
        if (this.hour > time.getHour()) {
            return 1;
        }
        else if (this.hour == time.getHour()) {
            if (this.minutes > time.getMinutes()) {
                return 1;
            } else if (this.minutes == time.getMinutes()) {
                return 0;
            } else
                return -1;
        }
        else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(hour, minutes);
    }

    public void updateTime(int hour, int minutes) {
        if (this.minutes + minutes > 59) {
            this.hour++;
        }
        this.hour = (this.hour + hour) % 24;
        this.minutes = (this.minutes + minutes) % 60;
        this.roundHour();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void roundHour() {
        if(minutes>57) {
           // hour=(hour++)%24;
            hour++;
            hour=hour%24;
            minutes=0;
        }
        if(minutes%5<3)
            minutes=minutes-minutes%5;
        else
            minutes=minutes+(5-minutes%5);
    }


//    public void checkRangeOfInt(int hour, int minutes) throws OutOfRange {
//        if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59) {
//            throw new OutOfRange();
//        }
//    }
//
//    public boolean checkTime(TranspoolSystem system, String time) {
//        try {
//            if (time.length() != 5 || time.charAt(2) != ':') {
//                throw new WrongTimeFormat();
//            }
//            String[] parts = time.split(":");
//            int hour = Integer.parseInt(parts[0]);
//            int minutes = Integer.parseInt(parts[1]);
//            updateTime(hour, minutes);
//            checkRangeOfInt(hour, minutes);
//            return true;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return false;
//        }
//    }
}
