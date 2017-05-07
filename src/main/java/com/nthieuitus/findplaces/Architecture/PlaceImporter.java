package com.nthieuitus.findplaces.Architecture;

import java.util.List;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public abstract class PlaceImporter {
    public abstract PlaceInfo Convert(Object input);
    public abstract boolean IsValidInput(Object input);
    public abstract List<PlaceInfo> ConvertList(Object places);
}
