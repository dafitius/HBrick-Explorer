package sample;

import java.util.Comparator;

public class ItemTypeComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        return o1.getType().compareTo(o2.getType());
    }
}