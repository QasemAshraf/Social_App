package com.example.socialapp.Activities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static String getCurrentDate(){

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("dd-MM-yyy / HH:mm");
        return simpleDateFormat.format(date);

    }

}
