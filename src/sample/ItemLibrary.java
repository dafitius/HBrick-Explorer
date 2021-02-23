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

    public Item getRoot(){
        boolean foundRoot = false;
        Item root = new Item("","","","No root found");
        String rootString = "";
        for(Item item : items){
            if(item.getParent().contains("root")){

                rootString = item.getName();
                root = item;
                if(foundRoot){
                    System.out.println(item.getName() + " and " + rootString + "might both be root files");
                    System.out.println("Multiple contenders for a root file have been found, this file might not work as e");
                }
                foundRoot = true;

            }


        }
        if(!foundRoot) {
            System.out.println("error, no root could be located");
        }

        return root;

    }

    public void sortAllItems(){
        for (Item item : items){
            item.sort();
        }
    }

}
