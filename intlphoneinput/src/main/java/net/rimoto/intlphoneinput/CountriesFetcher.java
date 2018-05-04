package net.rimoto.intlphoneinput;


import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class CountriesFetcher {
    private static CountryList mCountries;

    /**
     * Fetch JSON from RAW resource
     *
     * @param context  Context
     * @param resource Resource int of the RAW file
     * @return JSON
     */
    private static String getJsonFromRaw(Context context, int resource) {
        String json;
        try {
            InputStream inputStream = context.getResources().openRawResource(resource);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Import CountryList from RAW resource
     *
     * @param context Context
     * @return CountryList
     */
    public static CountryList getCountries(Context context) {
        if (mCountries != null) {
            return mCountries;
        }
        mCountries = new CountryList();


        Locale[] locales = Locale.getAvailableLocales();

        try {
            JSONArray countries = new JSONArray(getJsonFromRaw(context, R.raw.countries));
            for (int i = 0; i < countries.length(); i++) {
                try {
                    JSONObject country = (JSONObject) countries.get(i);

                    mCountries.add(new Country(country.getString("name"), country.getString("iso2"), country.getInt("dialCode")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (Country country : mCountries) {

            for (Locale locale : locales) {

                if (StringUtils.equalsIgnoreCase(locale.getCountry(), country.getIso())) {

                    country.setName(locale.getDisplayCountry());

                    break;
                }

            }

        }

        Collections.sort(mCountries, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return StringUtils.compareIgnoreCase(StringUtils.stripAccents(o1.getName()), StringUtils.stripAccents(o2.getName()));
            }
        });

        return mCountries;
    }


    public static class CountryList extends ArrayList<Country> {
        /**
         * Fetch item index on the list by iso
         *
         * @param iso Country's iso2
         * @return index of the item in the list
         */
        public int indexOfIso(String iso) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getIso().toUpperCase().equals(iso.toUpperCase())) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Fetch item index on the list by dial coder
         *
         * @param dialCode Country's dial code prefix
         * @return index of the item in the list
         */
        @SuppressWarnings("unused")
        public int indexOfDialCode(int dialCode) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getDialCode() == dialCode) {
                    return i;
                }
            }
            return -1;
        }
    }
}
