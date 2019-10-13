package main_frame;

import java.util.Calendar;
import java.util.GregorianCalendar;

import main_frame.demo;

public class time_and_date extends Thread{
    public void run() {  
        while (true) {  
            GregorianCalendar time = new GregorianCalendar();  
            int hour = time.get(Calendar.HOUR_OF_DAY);  
            int minute = time.get(Calendar.MINUTE);  
            int second = time.get(Calendar.SECOND);  
            demo.timelabel.setText("    time��" + hour + ":" + minute + ":" + second);    
        }  
    }  
}  
