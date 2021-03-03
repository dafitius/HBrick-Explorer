package sample;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Item implements Serializable {

    //pre-set
    private String parent;
    private String Type;
    private String hash;
    private String name;


    //set later
    private ArrayList<Item> children;
    private Map<String, ArrayList<String>> linkedData;
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
    }
    public Item(String parent, String type, String hash, String name, Map<String, ArrayList<String>> linkedData) {
        this.parent = parent;
        this.Type = type;
        this.hash = hash;
        this.name = name;
        children = new ArrayList<>();
        this.linkedData =linkedData;

    }
    public Item(String parent, String type, String hash, String name, ArrayList<String> ANG_IEntity, ArrayList<String> audioEmitters, ArrayList<String> audioVolumetric, ArrayList<String> gates, ArrayList<String> replicable, ArrayList<String> rooms) {
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

    public ArrayList<String> getAudioEmitters() {

        if(linkedData.containsKey("AudioEmitters")){
            return linkedData.get("AudioEmitters");
        }else return null;

    }

    public ArrayList<String> getANG_IEntity() {
        if(linkedData.containsKey("Activatable_NormalGameplay, IEntity")){
            return linkedData.get("Activatable_NormalGameplay, IEntity");
        }else return null;
    }

    public ArrayList<String> getReplicable() {
        if(linkedData.containsKey("Replicable")){
            return linkedData.get("Replicable");
        }else return null;
    }

    public ArrayList<String> getAudioVolumetric() {
        if(linkedData.containsKey("AudioVolumetricGeom ")){
            return linkedData.get("AudioVolumetricGeom ");
        }else return null;
    }

    public ArrayList<String> getGates() {
        if(linkedData.containsKey("Gates")){
            return linkedData.get("Gates");
        }else return null;
    }

    public ArrayList<String> getRooms() {
        if(linkedData.containsKey("Rooms")){
            return linkedData.get("Rooms");
        }else return null;
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

    public void sortLinkedArrays(){
        Collections.sort(getANG_IEntity());
        Collections.sort(getAudioEmitters());
        Collections.sort(getAudioVolumetric());
        Collections.sort(getGates());
        Collections.sort(getReplicable());
        Collections.sort(getRooms());
    }

    @Override
    public String toString() {
        return name;
    }





}
