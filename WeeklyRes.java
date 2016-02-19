package office;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BigBlue on 7/11/2014.
 */
public class WeeklyRes {

    String DATE;
    String task = "";

    public WeeklyRes(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("E");
        Date date = new Date();
        DATE = dateFormat.format(date);

        if(DATE.equals("Mon")){
            task = "•Take Out Garbage (Office) & Recycling\n•Empty Shredder\n•Vacuum, Sweep";
        }
        if(DATE.equals("Tue")){
             task = "•Sanitize Phone, Keyboard, and Mouse\n•Clean Computer(s) and Desk(s)";
        }
        if(DATE.equals("Wed")){
             task = "•Stock up on Copies of forms, flyers, etc. (No NRO's)\n•Check Clean Buckets Supplies.\n•Wipe down all items in Cleaning Buckets and sanitize Cleaning Buckets.";
        }
        if(DATE.equals("Thu")){
            task = "•Sweep and Mop floor in front of front desk.\n•Make sure the Books and Movies are in Order\n•Dust Shelves (Books & Movies)";
        }
        if(DATE.equals("Fri")){
             task = "•Clean up desktop on computer\n•Organize Office Supplies\n•Organize Locked Cupboard\n•Check Food's Expiration Dates in Cupboards";
        }
        if(DATE.equals("Sat")){
             task = "•Clean Picture Shelves\n•Pull out Desk Chair and Vacuum\n•Check Office Closet for Organization";
        }
        if(DATE.equals("Sun")){
             task = "•Wipe Down Vacuums. Rewind Cords\n•Empty Vacuums if not already Empty\n•Dust Craft Shelves\n•Check Stock of Paper in Printer\n•Replace AVCC mop cloths";
        }
    }
    
    public String getRes(){
        return task;
    }
}
