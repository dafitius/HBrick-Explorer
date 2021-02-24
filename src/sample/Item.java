package sample;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Item implements Serializable {

    private String parent;
    private String Type;
    private String hash;
    private String name;
    private ArrayList<Item> children;
    private ArrayList<String> audioEmitters;
    private ArrayList<String> ANG_IEntity;
    private ArrayList<String> replicable;

    public  TreeItem<Item> treeItem = new TreeItem<Item>(this);

    public Item(String parent, String type, String hash, String name) {
        this.parent = parent;
        this.Type = type;
        this.hash = hash;
        this.name = name;
        children = new ArrayList<>();
        audioEmitters = new ArrayList<String>();
        ANG_IEntity = new ArrayList<String>();
        replicable = new ArrayList<String>();
    }

    public Item(String parent, String type, String hash, String name, ArrayList<String> audioEmitters, ArrayList<String> ANG_IEntity, ArrayList<String> replicable) {
        this.parent = parent;
        this.Type = type;
        this.hash = hash;
        this.name = name;
        children = new ArrayList<>();
        this.audioEmitters = audioEmitters;
        this.ANG_IEntity = ANG_IEntity;
        this.replicable = replicable;
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

    public ArrayList<String> getAudioEmitters() {
        return audioEmitters;
    }

    public ArrayList<String> getANG_IEntity() {
        return ANG_IEntity;
    }

    public ArrayList<String> getReplicable() {
        return replicable;
    }

    public void addAudioEmitters(String ae){
        this.audioEmitters.add(ae);
    }

    public void addANG_IEntity(String angie){
        this.audioEmitters.add(angie);
    }

    public void addreplicable(String rep){
        this.audioEmitters.add(rep);
    }

    public TreeItem<Item> getViewItem(){
        for(Item item : this.getChildren()){
            treeItem.getChildren().add(new TreeItem<>(item));
        }
        return treeItem;
    }

    public void sort(){
        Collections.sort(children, new ItemTypeComparator());
        Collections.sort(children, new ItemNameComparator());
    }

    @Override
    public String toString() {
        return name;
    }





}
