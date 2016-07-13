package com.tdsoft.awarenessapi.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 7/11/2016.
 */
public class AppUtil {
    public static String getAddress(Context context, double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 3);
        Address address = null;
        String strAddress = null;
        if (addresses != null && addresses.size() > 0) {
            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i);
                if (address != null) {
                    break;
                }
            }

            if(address!=null){
                StringBuilder strReturnedAddress = new StringBuilder("");
                for(int x = 0; x < address.getMaxAddressLineIndex(); x++) {
                    String comma = "";
                    if(x < (address.getMaxAddressLineIndex() - 1)){
                        comma = ", ";
                    }else{
                        comma = "";
                    }
                    strReturnedAddress.append(address.getAddressLine(x) + comma);
                }
                strAddress =strReturnedAddress.toString();
            }
        }



        return strAddress;
    }
}
