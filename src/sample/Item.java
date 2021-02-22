package sample;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private String parent;
    private String Type;
    private String hash;
    private String name;
    private ArrayList<Item> children;
    public  TreeItem<Item> treeItem = new TreeItem<Item>(this);

    public Item(String parent, String type, String hash, String name) {
        this.parent = parent;
        this.Type = type;
        this.hash = hash;
        this.name = name;
        children = new ArrayList<>();
    }
    public Item(){
        this.parent = "null";
        this.Type = "null";
        this.hash = "null";
        this.name = "null";
        children = new ArrayList<>();
    }

    public String getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getChildren() {
        return children;
    }

    public void addChild(Item item){
        if(!children.contains(item)) {
            children.add(item);
        }
    }

    public String getType() {
        return Type;
    }

    public String getHash(){
        return hash;
    }


    public TreeItem<Item> getViewItem(){
        for(Item item : this.getChildren()){
            treeItem.getChildren().add(new TreeItem<>(item));
        }
        return treeItem;
    }

    @Override
    public String toString() {
        return name;
    }
}
