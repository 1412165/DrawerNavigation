package com.nthieuitus.findplaces.Model;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 07-May-17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<RouteFinder> route);
}
