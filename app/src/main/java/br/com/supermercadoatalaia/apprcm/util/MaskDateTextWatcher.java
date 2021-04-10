package br.com.supermercadoatalaia.apprcm.util;

import java.util.Calendar;

public class MaskDateTextWatcher {

    public static String aplicaMascara(String data) {
        Calendar cal = Calendar.getInstance();
        String clean = data.replaceAll("[^\\d.]|\\.", "");

        if (clean.length() >= 8){
            //This part makes sure that when we finish entering numbers
            //the date is correct, fixing it otherwise
            int day  = Integer.parseInt(clean.substring(0,2));
            int mon  = Integer.parseInt(clean.substring(2,4));
            int year = Integer.parseInt(clean.substring(4,8));

            mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
            cal.set(Calendar.MONTH, mon-1);
            year = (year<1900)?1900:(year>2100)?2100:year; //Para ano com 4 digitos
            cal.set(Calendar.YEAR, year);
            //Setar ano e mes primeiro para não ter problema em fevereiro com ano bisexto
            day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE) : day;
            clean = String.format("%02d%02d%02d",day, mon, year);
        }

        if(clean.length() >= 8) {
            return String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));
        }else if(clean.length() == 7) {
            return String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 7));
        }else if(clean.length() == 6) {
            return String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 6));
        }else if(clean.length() == 5) {
            return String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 5));
        }else if(clean.length() == 4) {
            return String.format("%s/%s/", clean.substring(0, 2),
                    clean.substring(2, 4));
        }else if(clean.length() == 3) {
            return String.format("%s/%s", clean.substring(0, 2), clean.substring(2, 3));
        }else if(clean.length() == 2) {
            return String.format("%s/", clean.substring(0, 2));
        }

        return data;
    }
}
