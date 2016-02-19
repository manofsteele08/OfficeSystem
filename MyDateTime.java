package office;

import java.util.Calendar;

/**
 * Created by BigBlue on 7/11/2014.
 */
public class MyDateTime {
    
    public String DATE;
    Calendar cal;

    public MyDateTime(){
        cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

//        System.out.println("Current Date: " + year + "-" + (month+1) + "-" + day);
        DATE = year + "-" + (month+1) + "-" + day;
    }
    
    public String getDate(){
        return DATE;
    }
    
    public Calendar myCal(){
        return cal;
    }
}
