package Decoder.TBLU.BlockTypes;

public class Block4 implements Block{
    private String parent1Name;
    private String parent2Name;
    private String parent1Hash;
    private String parent2Hash;
    private String string1;
    private String string2;

    public Block4(String parent1Name, String parent2Name, String parent1Hash, String parent2Hash, String string1, String string2) {
        this.parent1Name = parent1Name;
        this.parent2Name = parent2Name;
        this.parent1Hash = parent1Hash;
        this.parent2Hash = parent2Hash;
        this.string1 = string1;
        this.string2 = string2;
    }


    @Override
    public String toString() {
        return "Block2{" +
                "parent1Name='" + parent1Name + '\'' +
                ", parent2Name='" + parent2Name + '\'' +
                ", parent1Hash='" + parent1Hash + '\'' +
                ", parent2Hash='" + parent2Hash + '\'' +
                ", string1='" + string1 + '\'' +
                ", string2='" + string2 + '\'' +
                '}';
    }

    public String printBlock(){
        return "if " + parent1Name + "." + string1 + "()" + " then " + parent2Name + "." + string2 + "()";
    }
}
