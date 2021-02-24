package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    String filename;
    ItemLibrary itemLibrary;
    private String selectedItemName;

    //settings
    int LoD = 3;
    boolean useCache = false;
    boolean useOldJsons = false;

    @Override
    public void start(Stage primaryStage) throws Exception {


        //ask for file to extract
        File selectedFile = getFile();
        File jsonFile = new File(selectedFile.getPath() + ".BIN1decoded.JSON");

        //see if file has been decoded before
        File fl;
        if(jsonFile.exists() && useOldJsons){
             fl = jsonFile;
        }
        else fl = convertFileToJson(selectedFile);

        //set level of detail in the treeview

        //get a filename from the file
        this.filename = fl.getName().substring(0, 16);

        //Build the ItemLibrary with the information from the file
        buildItemLibrary(fl);


        //Add info to GUI
        TreeView<Item> treeView = new TreeView<>();
        TreeItem<Item> root = itemLibrary.getRoot().getViewItem();
        treeView.setRoot(root);

        if(LoD >= 0){
            for (TreeItem<Item> treeview : root.getChildren()) {
                treeview.getChildren().addAll(treeview.getValue().getViewItem().getChildren());
                if(LoD >= 1) {
                    for (TreeItem<Item> treeview2 : treeview.getChildren()) {
                        treeview2.getChildren().addAll(treeview2.getValue().getViewItem().getChildren());
                        if (LoD >= 2) {
                            for (TreeItem<Item> treeview3 : treeview2.getChildren()) {
                                treeview3.getChildren().addAll(treeview3.getValue().getViewItem().getChildren());
                                if (LoD >= 3) {
                                    for (TreeItem<Item> treeview4 : treeview3.getChildren()) {
                                        treeview4.getChildren().addAll(treeview4.getValue().getViewItem().getChildren());
                                        if (LoD >= 4) {
                                            for (TreeItem<Item> treeview5 : treeview4.getChildren()) {
                                                treeview5.getChildren().addAll(treeview5.getValue().getViewItem().getChildren());
                                                if (LoD >= 5) {
                                                    for (TreeItem<Item> treeview6 : treeview5.getChildren()) {
                                                        treeview6.getChildren().addAll(treeview6.getValue().getViewItem().getChildren());
                                                        if (LoD >= 6) {
                                                            for (TreeItem<Item> treeview7 : treeview6.getChildren()) {
                                                                treeview7.getChildren().addAll(treeview7.getValue().getViewItem().getChildren());
                                                                if (LoD >= 7) {
                                                                    for (TreeItem<Item> treeview8 : treeview7.getChildren()) {
                                                                        treeview8.getChildren().addAll(treeview8.getValue().getViewItem().getChildren());
                                                                        if (LoD >= 8) {
                                                                            for (TreeItem<Item> treeview9 : treeview8.getChildren()) {
                                                                                treeview9.getChildren().addAll(treeview9.getValue().getViewItem().getChildren());
                                                                                if (LoD >= 9) {
                                                                                    for (TreeItem<Item> treeview10 : treeview9.getChildren()) {
                                                                                        treeview10.getChildren().addAll(treeview10.getValue().getViewItem().getChildren());
                                                                                        if (LoD >= 10) {
                                                                                            for (TreeItem<Item> treeview11 : treeview10.getChildren()) {
                                                                                                treeview11.getChildren().addAll(treeview11.getValue().getViewItem().getChildren());
                                                                                                if (LoD >= 11) {
                                                                                                    for (TreeItem<Item> treeview12 : treeview11.getChildren()) {
                                                                                                        treeview12.getChildren().addAll(treeview12.getValue().getViewItem().getChildren());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("added all items inside the tree");
        //create a panel on the left for details
        HBox hbox = new HBox();
        ListView itemDetails = new ListView();
        treeView.setMinWidth(500);

        //get selected item from the tree
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedItemName = newValue.getValue().toString();
                Item selectedItem = new Item();
                for(Item item : itemLibrary.getItems()){
                    if(item.getName().equals(selectedItemName)){
                        selectedItem = item;
                    }
                }
            System.out.println(selectedItem.getName());

            Label name = new Label("Name:   " + selectedItem.getName());
            Label type = new Label("Type:   " + selectedItem.getType());
            Label hash = new Label("Hash:   " + selectedItem.getHash());
            Label parent = new Label("Parent: " + selectedItem.getParent());
            itemDetails.getItems().clear();
            itemDetails.getItems().addAll(name, type, hash, parent);
        });

        BorderPane borderpane = new BorderPane();
        hbox.getChildren().addAll(treeView, itemDetails);
        borderpane.setCenter(hbox);
        primaryStage.setTitle("TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 660, 675));
        primaryStage.show();
        System.out.println("launch the app");
    }

    public void buildItemLibrary(File fl){

        File cacheFile = new File(System.getProperty("user.dir") + "\\cache\\" + filename + ".dat");
        //check if file is already serialized
        if(cacheFile.exists() && useCache){
            this.itemLibrary = deserializeItemsArray(filename);
        }
        else {

            int linecount = 0;

            try {
                Scanner lineCounter = new Scanner(fl);
                while (lineCounter.hasNextLine()) {
                    lineCounter.nextLine();
                    linecount++;
                }
                lineCounter.close();
            }catch (FileNotFoundException e){
                System.out.println("file was not found");
            }

            String file = "";
            try {
            Scanner sc = new Scanner(fl);
            int i = 0;
            int percentRead = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                file += line;

                if (i % (linecount / 10) == 0) {
                    System.out.println("read " + percentRead + "% of the file");
                    percentRead += 10;
                }
                i++;
            }
            sc.close();
            }catch (FileNotFoundException e){
                System.out.println("file was not found");
            }


            this.itemLibrary = new ItemLibrary(filename);


            //Extract the parts with directory structure
            ArrayList<String> lines = new ArrayList<>();
            String lastItemName = "";
            ArrayList<ArrayList> linkedDataArrays = new ArrayList<>();
            for (String line : file.split("},")) {
                //System.out.println(line);
                if (line.contains("parent")) {
                    lines.add(line);
                    line = line.substring(1);
                    String[] parts = line.split(",");
                    String name = parts[3].split("\"")[3];
                    lastItemName = name;
                }
                if (line.contains("\"Activatable_NormalGameplay, IEntity\":")) {
                    //line.split("\"Activatable_NormalGameplay, IEntity\":");
                    ArrayList<String> ANG_IEntity = new ArrayList<>();
                    ANG_IEntity.add("ANG");
                    ANG_IEntity.add(lastItemName);
                    for(String string : line.split("\"Activatable_NormalGameplay, IEntity\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            ANG_IEntity.add(string);
                        }
                    }
                    System.out.println("ANG " + ANG_IEntity.toString());
                    linkedDataArrays.add(ANG_IEntity);
                    //System.out.println("Activatable_NormalGameplay, IEntity = " + line.split("\"Activatable_NormalGameplay, IEntity\":")[1]);
                }
                if (line.contains("\"AudioEmitters\":")) {
                    ArrayList<String> audioEmitters = new ArrayList<>();
                    audioEmitters.add("AE");
                    audioEmitters.add(lastItemName);
                    for(String string : line.split("\"AudioEmitters\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            audioEmitters.add(string);
                        }
                    }
                    System.out.println("AE " + audioEmitters.toString());
                    linkedDataArrays.add(audioEmitters);
                    //System.out.println("AudioEmitters = " + line.split("\"AudioEmitters\":")[1]);
                }
                if (line.contains("\"AudioVolumetricGeom\":")) {
                    ArrayList<String> audioVolumetricGeom = new ArrayList<>();
                    audioVolumetricGeom.add("AVG");
                    audioVolumetricGeom.add(lastItemName);
                    for(String string : line.split("\"AudioVolumetricGeom\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            audioVolumetricGeom.add(string);
                        }
                    }
                    System.out.println("AVG " + audioVolumetricGeom.toString());
                    linkedDataArrays.add(audioVolumetricGeom);
                    //System.out.println("AudioEmitters = " + line.split("\"AudioEmitters\":")[1]);
                }
                if (line.contains("\"Gates\":")) {
                    ArrayList<String> gates = new ArrayList<>();
                    gates.add("GATE");
                    gates.add(lastItemName);
                    for(String string : line.split("\"Gates\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            gates.add(string);
                        }
                    }
                    System.out.println("GATE " + gates.toString());
                    linkedDataArrays.add(gates);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }

                if (line.contains("\"Replicable\":")) {
                    ArrayList<String> replicable = new ArrayList<>();
                    replicable.add("REP");
                    replicable.add(lastItemName);
                    for(String string : line.split("\"Replicable\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            replicable.add(string);
                        }
                    }
                    System.out.println("REP " + replicable.toString());
                    linkedDataArrays.add(replicable);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }
                if (line.contains("\"Rooms\":")) {
                    ArrayList<String> rooms = new ArrayList<>();
                    rooms.add("ROOM");
                    rooms.add(lastItemName);
                    for(String string : line.split("\"Rooms\":")[1].split("\"")){
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if(string.length() >= 3){
                            rooms.add(string);
                        }
                    }
                    System.out.println("ROOM " + rooms.toString());
                    linkedDataArrays.add(rooms);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }
            }
            System.out.println("extracted items from JSON");
            //create the objects from the JSON
            for (String line : lines) {
                line = line.substring(1);
                String[] parts = line.split(",");
                String parent = parts[0].split("\"")[3];
                String type = parts[1].split("\"")[3];
                String hash = parts[2].split("\"")[3];
                String name = parts[3].split("\"")[3];

                //add linked data
                ArrayList<String> ANG_IEntity = new ArrayList<>();
                ArrayList<String> audioEmitters = new ArrayList<>();
                ArrayList<String> audioVolumetricGeom = new ArrayList<>();
                ArrayList<String> gates = new ArrayList<>();
                ArrayList<String> replicable = new ArrayList<>();
                ArrayList<String> rooms = new ArrayList<>();

                for(ArrayList arrayList : linkedDataArrays){
                    if(arrayList.size() > 2) {
                        if (arrayList.get(1).equals(name)) {
                            arrayList.remove(name);
                            if (arrayList.get(0).equals("ANG")) {
                                arrayList.remove("ANG");
                                ANG_IEntity.addAll(arrayList);
                            }
                            if (arrayList.get(0).equals("AE")) {
                                arrayList.remove("AE");
                                audioEmitters.addAll(arrayList);
                            }
                            if (arrayList.get(0).equals("AVG")) {
                                arrayList.remove("AVG");
                                audioVolumetricGeom.addAll(arrayList);
                            }
                            if (arrayList.get(0).equals("GATE")) {
                                arrayList.remove("GATE");
                                gates.addAll(arrayList);
                            }
                            if (arrayList.get(0).equals("REP")) {
                                arrayList.remove("REP");
                                replicable.addAll(arrayList);
                            }
                            if (arrayList.get(0).equals("ROOM")) {
                                arrayList.remove("ROOM");
                                rooms.addAll(arrayList);
                            }
                        }
                    }
                }

                this.itemLibrary.add(new Item(parent, type, hash, name, audioEmitters, ANG_IEntity, replicable));
            }
        }

        //Add a root node
        this.itemLibrary.add(new Item("root", "", "", filename));
        System.out.println("Put all items inside ItemLibrary");

        //find the children
        for (Item itemToFill : this.itemLibrary.getItems()) {
            for (Item item : this.itemLibrary.getItems()) {
                if (item.getParent().equals(itemToFill.getName())) {
                    itemToFill.addChild(item);
                }
                if (itemToFill.getParent().equals("root") && item.getParent().contains("0xff")){
                    itemToFill.addChild(item);
                }
            }

        }
        System.out.println("found and added all children");
        //sort the children
        this.itemLibrary.sortAllItems();
        System.out.println("sorted all items");
        //serialize the itemsArray
        serializeItemsArray(this.itemLibrary);

    }

    public void serializeItemsArray(ItemLibrary itemLibrary){
        try {
            FileWriter itemsWriter = new FileWriter("cache\\" + itemLibrary.getName() + ".dat");
            for(Item item : itemLibrary.getItems()){
                itemsWriter.write(item.getParent() + "#" + item.getType() + "#" + item.getHash() + "#" + item.getName() + "\n");
            }
            itemsWriter.close();

            System.out.println(this.filename + " was succesfully serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ItemLibrary deserializeItemsArray(String name){
        ItemLibrary itemLibrary = new ItemLibrary(name);
        try(BufferedReader br = new BufferedReader(new FileReader("cache\\" + name + ".dat"))) {

            String line = br.readLine();

            while (line != null) {
                String[] itemInfo = line.split("#");
                String parent = itemInfo[0];
                String type = itemInfo[1];
                String hash = itemInfo[2];
                String string = itemInfo[3];
                itemLibrary.add(new Item(parent, type, hash, string));

                line = br.readLine();

            }
            System.out.println(this.filename + " was succesfully deserialized");

        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return itemLibrary;
    }

    public File getFile(){
        Stage selectFile = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a TBLU file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TBLU files", "*.TBLU");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File selectedFile = fileChooser.showOpenDialog(selectFile);
        System.out.println(selectedFile);
        return selectedFile;
    }


    public InputStream excCommand(String command){
        Runtime rt = Runtime.getRuntime();

        try {

            return rt.exec(command).getInputStream();
        }
        catch (Exception e){
            System.out.println("failed to execute command");
        }
        return  null;
    }

    public File convertFileToJson(File selectedFile){
        Stage convertStage = new Stage();
        BorderPane borderPane = new BorderPane();
        TextArea textArea = new TextArea();
        borderPane.setCenter(textArea);
        Scene scene = new Scene(borderPane);
        convertStage.setScene(scene);
        convertStage.show();
        try {
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(excCommand("python \".\\decoder\\TBLUdecode.py\" \"" + selectedFile.getPath() + "\" JSON")));
            System.out.println("line" + line);
            while (line != null) {
                line = reader.readLine();
                System.out.println("line" + line);
                textArea.appendText(line + "\n");


            }



            if(reader.readLine() == null){
                convertStage.close();
                try{
                    return new File(selectedFile.getPath() + ".BIN1decoded.JSON");
                }
                catch (Exception e){
                    System.out.println("an error occured while converting the TBLU to a JSON file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {




        launch(args);
    }




}
