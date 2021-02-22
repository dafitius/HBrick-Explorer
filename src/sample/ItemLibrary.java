package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemLibrary implements Serializable {
    private ArrayList<Item> items;
    private String name;

    public ItemLibrary(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public void add(Item item){
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
