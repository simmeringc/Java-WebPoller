/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.model;

public class Node {

    private String key;
    private String data;

    public Node(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public String getData() { return data; }
    public String getKey() {
        return key;
    }
}
