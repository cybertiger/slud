package org.cyberiantiger.slud.mapping;

import org.h2.mvstore.MVStore;
import org.h2.mvstore.rtree.MVRTreeMap;

import java.io.File;

public class MapDB {
    private final MVStore db;
    private final MVMap<String, AreaData> areaData;
    private final MVRTreeMap<RoomData> roomMap;

    public MapDB(File file) {
        db = MVStore.open(null);
        roomMap = db.openMap("test", new MVRTreeMap.Builder<Integer>().dimensions(4));
    }


}
