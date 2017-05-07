package com.nthieuitus.findplaces.Model;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class Route {
    public String summary;
    public List<Leg> legs; // A route with no waypoints will contain exactly one leg within the legs array
    public String overview_polyline;
    public String copyrights;
}
