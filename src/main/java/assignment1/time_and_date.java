package assignment1;

import java.util.Calendar;
import java.util.GregorianCalendar;

import assignment1.main_frame;

public class time_and_date extends Thread{
    public void run() {  
        while (true) {  
            GregorianCalendar time = new GregorianCalendar();  
            int hour = time.get(Calendar.HOUR_OF_DAY);  
            int minute = time.get(Calendar.MINUTE);  
            int second = time.get(Calendar.SECOND);  
            main_frame.timelabel.setText("    time£º" + hour + ":" + minute + ":" + second);    
        }  
    }  
}  
