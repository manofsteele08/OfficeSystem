package office;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BigBlue on 7/11/2014.
 */
public class MonthlyRes extends JPanel{

    String DATE;
    String task = "";

    public MonthlyRes(){
       
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        DATE = dateFormat.format(date);

        if(DATE.equals("01")){
            task = "PR: Wash and Sanitize: Kitchen, play set, big blocks, and shelf (inside and outside)";
        }
        if(DATE.equals("02")){
           task = "PR: Wash and Sanitize Cubbies and Piano";
        }
        if(DATE.equals("03")){
            task = "PR: Wash and sanitize small tables and chairs, countertops, handles and cupboard doors";
        }
        if(DATE.equals("04")){
            task = "PR: Wash and Sanitize all folding chairs";
        }
        if(DATE.equals("05")){
            task = "K: Clean out and sanitize inside of refrigerator";
        }
        if(DATE.equals("06")){
            task = "PR: Clean both sliding door windows and the bottom slide area, door handles, frames, mirror window both sides,. Wipe out window sills in kitchen area.";
        }
        if(DATE.equals("07")){
            task = "PR: Wash and sanitize all toys and the toy cupboard";
        }
        if(DATE.equals("08")){
            task = "K: Clean stove top (under burners), front and handles. Was wall behind stove";
        }
        if(DATE.equals("09")){
            task = "K: Clean out and organize pan cupboard";
        }
        if(DATE.equals("10")){
            task = "K: Clean inside Oven";
        }
        if(DATE.equals("11")){
            task = "K:Clean out and organize spice cupboard";
        }
        if(DATE.equals("12")){
            task = "K: Clean out and organize dish cupboard";
        }
        if(DATE.equals("13")){
            task = "K: Clean out and organize dish cupboard"; //ON HERE TWICE!!!
        }
        if(DATE.equals("14")){
            task = "K: Clean out and organize the cleaning closet";
        }
        if(DATE.equals("15")){
            task = "Clean out and organize the lower lazy susan cupboard";
        }
        if(DATE.equals("16")){
            task = "K: Pull refrigerator out and clean sides and underneath";
        }
        if(DATE.equals("17")){
            task = "K:Clean Out and organize the cupboards above the sink and the refrigerator";
        }
        if(DATE.equals("18")){
            task = "K: Clean and sanitize the small table and all small green and red chairs";
        }
        if(DATE.equals("19")){
            task = "K: Clean and sanitize microwave inside and out and underneath";
        }
        if(DATE.equals("20")){
            task = "K: Clean and sanitize all large cupboard doors with numbers and handles, vacuum or dust top of cupboard, portable demonstration table (doors, handles, & vacuum underneath)";
        }
        if(DATE.equals("21")){
            task = "K: Clean and sanitize all doors, frames and handles, folding tables (tops and edges, remove tape from bottom)";
        }
        if(DATE.equals("22")){
            task = "K: Clean out, sanitize and organize the cupboards above and under the sink";
        }
        if(DATE.equals("23")){
            task = "K: Clean out, sanitize and organize the drawers next to the sink";
        }
        if(DATE.equals("24")){
            task = "K: Clean out, sanitize and organize the cupboards next to the refrigerator";
        }
        if(DATE.equals("25")){
            task = "K: Clean out, sanitize and organize the drawers at the end of the bar";
        }
        if(DATE.equals("26")){
            task = "K: Vacuum all corners and edges where the carpet meets the wall";
        }
        if(DATE.equals("27")){
            task = "K: Clean the windows inside and outside, dust blinds and wipe out sills";
        }
        if(DATE.equals("28")){
            task = "K: Pull stove out and clean sides and floor underneath";
        }
        if(DATE.equals("29")){
            task = "PR, K: Wash walls where needed, clean white board, sanitize sink and wall behind the sink";
        }
        if(DATE.equals("30")){
            task = "K: Clean out and organize the coat closets (2)";
        }
        if(DATE.equals("31")){
            task = "K: Clean out and organize dish cupboard"; //NEED ONE HERE!!!
        }
    }
    public String getRes(){
        return task;
    }
    
}
