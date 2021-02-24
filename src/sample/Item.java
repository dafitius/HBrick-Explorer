package sample;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Item implements Serializable {

    //pre-set
    private String parent;
    private String Type;
    private String hash;
    private String name;
    private ArrayList<String> ANG_IEntity;
    private ArrayList<String> audioEmitters;
    private ArrayList<String> audioVolumetric;
    private ArrayList<String> gates;
    private ArrayList<String> replicable;
    private ArrayList<String> rooms;

    //set later
    private ArrayList<Item> children;
    private boolean isANG_IEntity = false;
    private boolean isAudioEmitter = false;
    private boolean isAudioVolumetric = false;
    private boolean isGate = false;
    private boolean isReplicable = false;
    private boolean isRoom = false;


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
    public Item(String parent, String type, String hash, String name, ArrayList<String> ANG_IEntity, ArrayList<String> audioEmitters, ArrayList<String> audioVolumetric, ArrayList<String> gates, ArrayList<String> replicable, ArrayList<String> rooms) {
        this.parent = parent;
        this.Type = type;
        this.hash = hash;
        this.name = name;
        this.ANG_IEntity = ANG_IEntity;
        this.audioEmitters = audioEmitters;
        this.audioVolumetric = audioVolumetric;
        this.gates = gates;
        this.replicable = replicable;
        this.rooms = rooms;
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

    public ArrayList<String> getAudioEmitters() {
        return audioEmitters;
    }

    public ArrayList<String> getANG_IEntity() {
        return ANG_IEntity;
    }

    public ArrayList<String> getReplicable() {
        return replicable;
    }

    public void setANG_IEntity(boolean ANG_IEntity) {
        isANG_IEntity = ANG_IEntity;
    }
    public void setAudioEmitter(boolean audioEmitter) {
        isAudioEmitter = audioEmitter;
    }
    public void setAudioVolumetric(boolean audioVolumetric) {
        isAudioVolumetric = audioVolumetric;
    }
    public void setGate(boolean gate) {
        isGate = gate;
    }
    public void setReplicable(boolean replicable) {
        isReplicable = replicable;
    }
    public void setRoom(boolean room) {
        isRoom = room;
    }

    public boolean isANG_IEntity() {
        return isANG_IEntity;
    }
    public boolean isAudioEmitter() {
        return isAudioEmitter;
    }
    public boolean isAudioVolumetric() {
        return isAudioVolumetric;
    }
    public boolean isGate() {
        return isGate;
    }
    public boolean isReplicable() {
        return isReplicable;
    }
    public boolean isRoom() {
        return isRoom;
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
