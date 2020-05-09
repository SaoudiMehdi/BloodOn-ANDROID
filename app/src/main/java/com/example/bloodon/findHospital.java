package com.example.bloodon;
import java.util.ArrayList;

public class findHospital {


        String label;
        String name;
        double lat;
        double lng ;
        int imag ;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }


        public findHospital(String label, String name, int img ){

            this.label= label;
            this.name= name;
            this.imag=img;



        }

        public findHospital() {

        }

        public String getlabel() {
            return label;
        }

        public String getname() {
            return name;
        }

        public int getimage() {
            return imag;
        }

        public void setlabel(String label) {
            this.label = label;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setImag(int imag) {
            this.imag = imag;
        }

        public static ArrayList<findHospital> getHospitals() {
            ArrayList<findHospital> hosp =new ArrayList<>() ;

            int[] images = {R.drawable.chufes,R.drawable.churabat,R.drawable.chutanger,R.drawable.chucasablanca,R.drawable.chuoujda,R.drawable.chumarrakesh} ;
            String[] name ={"CHU Hassan II","CHU Ibn Sina","CHU Tanger","CHU Ibn Rochd","CHU Mohammed VI","CHU Mohammed VI"} ;
            String[] label ={"Fès","Rabat","Tanger","Casablanca","Oujda","Marrakesh"} ;
            double[] lat ={34.007658,33.984826,35.684031,33.577953,34.656432,31.664876};
            double[] lng ={-4.962671,-6.850256,-5.926492,-7.622414,-1.910519,-7.995570} ;
            for (int i = 0; i < images.length; i++) {

                findHospital hospital = new findHospital();
                hospital.setlabel(label[i]);
                hospital.setName(name[i]);
                hospital.setImag(images[i]);
                hospital.setLat(lat[i]);
                hospital.setLng(lng[i]);


                hosp.add(hospital);
            }

            return hosp ;
        }
    }


