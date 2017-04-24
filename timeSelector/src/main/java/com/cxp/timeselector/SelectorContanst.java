package com.cxp.timeselector;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by santa on 16/7/19.
 */
public class SelectorContanst {
    private static SelectorContanst instance = null;
    public static ArrayList<String> MONTHS = null;
    public static ArrayList<String> YEARS = null;
    public static ArrayList<String> DAYS = null;
    public static ArrayList<String> HOURS = null;
    public static ArrayList<String> MINS = null;


    public static ArrayList<String> getMonths() {
        if (null == MONTHS) {
            synchronized (SelectorContanst.class){
                MONTHS = new ArrayList<>();
                MONTHS.add("");
                for (int i = 1 ; i<=12; i++) {
                    MONTHS.add((i<10?"0"+i:i)+"月");
                }
                MONTHS.add("");
            }
        }
        return MONTHS;
    }


    public static ArrayList<String> getYears() {
        if (null == YEARS) {
            synchronized (SelectorContanst.class){
                YEARS = new ArrayList<>();
                YEARS.add("");
                for (int i = Calendar.getInstance().get(Calendar.YEAR); i<=2500; i++) {
                    YEARS.add(i+"年");
                }
                YEARS.add("");
            }
        }
        return YEARS;
    }


    public static ArrayList<String> getDaysFromMonth(int month) {
        int max=31;
        if(month==0){
            max=31;
        }
        switch (month) {
            case 1:
                max = 31;
                break;
            case 2:
                max = 30;
                break;
            case 3:
                max = 31;
                break;
            case 4:
                max = 30;
                break;
            case 5:
                max = 31;
                break;
            case 6:
                max = 30;
                break;
            case 7:
            case 8:
                max = 31;
                break;
            case 9:
                max = 30;
                break;
            case 10:
                max = 31;
                break;
            case 11:
                max = 30;
                break;
            case 12:
                max = 31;
                break;
        }
                DAYS = new ArrayList<>();
        DAYS.add("");
                for (int i = 1 ; i<=max; i++) {
                    DAYS.add((i<10?"0"+i:i)+"日");
                }
        DAYS.add("");
        return DAYS;
    }
    public static ArrayList<String> getDays(int days) {
                DAYS = new ArrayList<>();
        DAYS.add("");
                for (int i = 1 ; i<=days; i++) {
                    DAYS.add((i<10?"0"+i:i)+"日");
                }
        DAYS.add("");
        return DAYS;
    }


    public static ArrayList<String> getHours() {
        if (null == HOURS) {
            synchronized (SelectorContanst.class){
                HOURS = new ArrayList<>();
                HOURS.add("");
                for (int i = 0 ; i<=23; i++) {
                    HOURS.add((i<10?"0"+i:i)+"时");
                }
                HOURS.add("");
            }
        }
        return HOURS;
    }


    public static ArrayList<String> getMins() {
        if (null == MINS) {
            synchronized (SelectorContanst.class){
                MINS = new ArrayList<>();
                MINS.add("");
                for (int i = 0 ; i<=59; i++) {
                    MINS.add((i<10?"0"+i:i)+"分");
                }
                MINS.add("");
            }
        }
        return MINS;
    }

}
