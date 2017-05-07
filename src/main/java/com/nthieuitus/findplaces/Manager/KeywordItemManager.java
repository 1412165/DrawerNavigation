package com.nthieuitus.findplaces.Manager;

import com.nthieuitus.findplaces.Model.KeywordItem;
import com.nthieuitus.findplaces.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class KeywordItemManager {
    static List<KeywordItem> keywordItemList = null;
    public static List<KeywordItem> populateDefaultKeywordItems() {
        if (keywordItemList == null) {
            keywordItemList = new ArrayList<>();
            keywordItemList.add(new KeywordItem("Airport", R.drawable.airport));
            keywordItemList.add(new KeywordItem("ATM", R.drawable.atm));
            keywordItemList.add(new KeywordItem("Book Store", R.drawable.book_store));
            keywordItemList.add(new KeywordItem("Bank", R.drawable.bank));
            keywordItemList.add(new KeywordItem("Bar", R.drawable.bar));
            keywordItemList.add(new KeywordItem("Beauty salon", R.drawable.beauty_salon));
            keywordItemList.add(new KeywordItem("Car Rental", R.drawable.car_rental));
            keywordItemList.add(new KeywordItem("Car Repair", R.drawable.car_repair));
            keywordItemList.add(new KeywordItem("Car Wash", R.drawable.car_wash));
            keywordItemList.add(new KeywordItem("Church", R.drawable.church));
            keywordItemList.add(new KeywordItem("Clothing Store", R.drawable.clothing_store));
            keywordItemList.add(new KeywordItem("Coffee", R.drawable.coffee));
            keywordItemList.add(new KeywordItem("Dentist", R.drawable.dentist));
            keywordItemList.add(new KeywordItem("Department Store", R.drawable.department_store));
            keywordItemList.add(new KeywordItem("Doctor", R.drawable.doctor));
            keywordItemList.add(new KeywordItem("Electronics Store", R.drawable.electronics_store));
            keywordItemList.add(new KeywordItem("Florist", R.drawable.florist));
            keywordItemList.add(new KeywordItem("Food", R.drawable.food));
            keywordItemList.add(new KeywordItem("Gas Station", R.drawable.gas_station));
            keywordItemList.add(new KeywordItem("Grocery", R.drawable.grocery));
            keywordItemList.add(new KeywordItem("Hindu Temple", R.drawable.hindu_temple));
            keywordItemList.add(new KeywordItem("Hospital", R.drawable.hospital));
            keywordItemList.add(new KeywordItem("Hotel", R.drawable.hotel));
            keywordItemList.add(new KeywordItem("Library", R.drawable.library));
            keywordItemList.add(new KeywordItem("Liquor Store", R.drawable.liquor_store));
            keywordItemList.add(new KeywordItem("Lodging", R.drawable.lodging));
            keywordItemList.add(new KeywordItem("Movie Theatre", R.drawable.movie_theatre));
            keywordItemList.add(new KeywordItem("Museum", R.drawable.museum));
            keywordItemList.add(new KeywordItem("Night Club", R.drawable.night_club));
            keywordItemList.add(new KeywordItem("Park", R.drawable.park));
            keywordItemList.add(new KeywordItem("Parking", R.drawable.parking));
            keywordItemList.add(new KeywordItem("Pharmacy", R.drawable.pharmacy));
            keywordItemList.add(new KeywordItem("Pizza", R.drawable.pizza));
            keywordItemList.add(new KeywordItem("Places", R.drawable.places));
            keywordItemList.add(new KeywordItem("Police", R.drawable.police));
            keywordItemList.add(new KeywordItem("Post Office", R.drawable.post_office));
            keywordItemList.add(new KeywordItem("Pub", R.drawable.pub));
            keywordItemList.add(new KeywordItem("Restaurant", R.drawable.restaurant));
            keywordItemList.add(new KeywordItem("Shopping Mall", R.drawable.shopping_mall));
            keywordItemList.add(new KeywordItem("Spa", R.drawable.spa));
            keywordItemList.add(new KeywordItem("Stadium", R.drawable.stadium));
            keywordItemList.add(new KeywordItem("Subway Station", R.drawable.subway_station));
            keywordItemList.add(new KeywordItem("Supermarket", R.drawable.supermarket));
            keywordItemList.add(new KeywordItem("Taxi Stand", R.drawable.taxi_stand));
            keywordItemList.add(new KeywordItem("Theatre", R.drawable.theater));
            keywordItemList.add(new KeywordItem("Train Station", R.drawable.train_station));
            keywordItemList.add(new KeywordItem("Travel Agency", R.drawable.travel_agency));
        }

        return keywordItemList;
    }

    public static int findIconByKeyword(String keyword) {
        List<KeywordItem> keywordItems = populateDefaultKeywordItems();
        for (KeywordItem keywordItem : keywordItems) {
            if (keyword.contains(keywordItem.text)) {
                return keywordItem.icon;
            }
        }
        return R.drawable.places;
    }
}
