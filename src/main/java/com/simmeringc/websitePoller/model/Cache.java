/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.model;

import java.util.HashMap;

public class Cache {

    private HashMap<String,Node> pageMap = new HashMap<String, Node>();

    public HashMap<String, Node> getPageMap() { return pageMap; }

    public void put(String key, Node page) {
        pageMap.put(key, page);
    }

    public Node get(String key) {
        return pageMap.get(key);
    }

    public void clearCache() { pageMap.clear(); }
}
