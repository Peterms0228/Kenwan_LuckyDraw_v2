package com.example.kenwan_luckydraw_v2;

import java.util.Comparator;

public class LuckyDrawItemComparator implements Comparator<LuckyDrawItem> {
    public int compare(LuckyDrawItem obj1, LuckyDrawItem obj2) {
        return Integer.compare(obj1.getId(), obj2.getId());
    }
}
