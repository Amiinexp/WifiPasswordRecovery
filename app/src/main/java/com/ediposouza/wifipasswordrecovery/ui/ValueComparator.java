package com.ediposouza.wifipasswordrecovery.ui;

import java.util.Comparator;

class ValueComparator implements Comparator<String> {

    public int compare(String a, String b) {
        return a.toLowerCase().compareTo(b.toLowerCase());
    }
}