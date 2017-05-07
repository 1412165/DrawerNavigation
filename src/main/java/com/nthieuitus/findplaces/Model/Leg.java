package com.nthieuitus.findplaces.Model;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class Leg {
    public List<Step> steps;
    public Distance distance;
    public Duration duration;
    public MyLocation startLocation;
    public String startAddress;
    public MyLocation endLocation;
    public String endAddress;
}
