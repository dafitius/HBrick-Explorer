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

    public String getRoot(){
        boolean foundRoot = false;
        String rootString = "";
        for(Item item : items){
            if(item.getParent().contains("0xffffffff")){

                rootString = item.getName();
                if(foundRoot){
                    System.out.println(item.getName() + " and " + rootString + "might both be root files");
                    System.out.println("Multiple contenders for a root file have been found, this file might not work as e");
                }
                foundRoot = true;

            }


        }
        if(foundRoot) {
            System.out.println("Root " + rootString + " has been located");
        }
        else{
            System.out.println("error, no root could be located");
        }

        return rootString;

    }
}
