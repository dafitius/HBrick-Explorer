package Decoder.TBLU.BlockTypes;

import java.util.ArrayList;

public class subEntity implements Block {

    private String parentName;
    private String parentHash;
    private String type;
    private String hash;
    private String name;
    private propertyAliases subBlock0List;
    private Block0_1 subBlock1;
    private Block0_2 subBlock2;
    private entitySubsets subBlock3;
    private ArrayList<subEntity> CC_children = new ArrayList<>();
    private boolean CC_hasChildren = false;

    public subEntity(String parentName, String parentHash, String type, String hash, String name, propertyAliases subBlock0List, Block0_1 subBlock1, Block0_2 subBlock2, entitySubsets subBlock3) {
        this.parentName = parentName;
        this.parentHash = parentHash;
        this.type = type;
        this.hash = hash;
        this.name = name;
        this.subBlock0List = subBlock0List;
        this.subBlock1 = subBlock1;
        this.subBlock2 = subBlock2;
        this.subBlock3 = subBlock3;
    }

    public subEntity(String name){
        this.name = name;
    }


    public String getParentName() {
        return parentName;
    }

    public String getParentHash() {
        return parentHash;
    }

    public String getType() {
        return type;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

    public propertyAliases getSubBlock0List() {
        return subBlock0List;
    }

    public Block0_1 getSubBlock1() {
        return subBlock1;
    }

    public Block0_2 getSubBlock2() {
        return subBlock2;
    }

    public entitySubsets getSubBlock3() {
        return subBlock3;
    }

    public void addChild(subEntity subEntity){
        this.CC_children.add(subEntity);
        this.CC_hasChildren = true;
    }

    public ArrayList<subEntity> getCC_children() {
        return CC_children;
    }

    public boolean isCC_hasChildren() {
        return CC_hasChildren;
    }

    @Override
    public String toString() {
        return name;
    }
}
