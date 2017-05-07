package com.nthieuitus.findplaces.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class PlaceList {
    public List<Place> places;
    public String next_page_token;

    public PlaceList() {
        places = new ArrayList<Place>();
    }

    public boolean canLoadMore() {
        return next_page_token != null;
    }}
