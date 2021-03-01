package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.JOptionPane.showMessageDialog;


public class Main extends Application {

    File selectedFile;
    String filename;
    ItemLibrary itemLibrary;
    private String selectedItemName;
    ArrayList<Stage> openPopUps;

    //settings
    int LoD;
    boolean useCache;
    boolean useOldJsons;
    boolean enablePopups;
    boolean printDecoder;

    public static void main(String[] args) {

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        getSettings();

        openPopUps = new ArrayList<>();


        //ask for file to extract
        this.selectedFile = getFile();
        File jsonFile = new File(selectedFile.getPath() + ".BIN1decoded.JSON");

        //see if file has been decoded before
        File fl;
        if (jsonFile.exists() && useOldJsons) {
            fl = jsonFile;
        } else fl = convertFileToJson(selectedFile);


        //get a filename from the file
        this.filename = fl.getName().substring(0, 16);

        //Build the ItemLibrary with the information from the file
        buildItemLibrary(fl);


        //Add info to GUI
        TreeView<Item> treeView = new TreeView<>();
        TreeItem<Item> root = itemLibrary.getRoot().getViewItem();
        treeView.setRoot(root);

        try {
            if (LoD >= 0) {
                for (TreeItem<Item> treeview : root.getChildren()) {
                    treeview.getChildren().addAll(treeview.getValue().getViewItem().getChildren());
                    if (LoD >= 1) {
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
        } catch (StackOverflowError e) {
            showMessageDialog(null, "The stack has overflown \n Level of detail could be lower then expected!\"");
        }
        System.out.println("added all items inside the tree");
        //create a panel on the left for details
        HBox hbox = new HBox();
        ListView itemDetails = new ListView();
        treeView.setMinWidth(500);

        //get selected item from the tree

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedItemName = newValue.getValue().toString();
                Item selectedItem = new Item();
                for (Item item : itemLibrary.getItems()) {
                    if (item.getName().equals(selectedItemName)) {
                        selectedItem = item;
                    }
                }

                System.out.println("selected: " + selectedItem.getName());

                Label name = new Label("Name: " + selectedItem.getName());
                Label type = new Label("Type: " + selectedItem.getType());
                Label hash = new Label("Hash: " + selectedItem.getHash());
                Label parent = new Label("Parent: " + selectedItem.getParent());
                Label isANG = new Label("is ActivatableIEntity: " + selectedItem.isANG_IEntity());
                Label isAE = new Label("is AudioEmitter: " + selectedItem.isAudioEmitter());
                Label isAVG = new Label("is AudioVolumetricGeom: " + selectedItem.isAudioVolumetric());
                Label isGATE = new Label("is Gate: " + selectedItem.isGate());
                Label isREP = new Label("is Replicable: " + selectedItem.isReplicable());
                Label isROOM = new Label("is Room: " + selectedItem.isRoom());

                if (selectedItem.isANG_IEntity()) {
                    isANG.setStyle("-fx-font-weight: bold");
                }
                if (selectedItem.isAudioEmitter()) {
                    isAE.setStyle("-fx-font-weight: bold");
                }
                if (selectedItem.isAudioVolumetric()) {
                    isAVG.setStyle("-fx-font-weight: bold");
                }
                if (selectedItem.isGate()) {
                    isGATE.setStyle("-fx-font-weight: bold");
                }
                if (selectedItem.isReplicable()) {
                    isREP.setStyle("-fx-font-weight: bold");
                }
                if (selectedItem.isRoom()) {
                    isROOM.setStyle("-fx-font-weight: bold");
                }


                for (Stage popup : openPopUps) {
                    popup.close();
                }
                openPopUps.clear();

                itemDetails.getItems().clear();
                if (!selectedItem.isANG_IEntity() &&
                        !selectedItem.isAudioEmitter() &&
                        !selectedItem.isAudioVolumetric() &&
                        !selectedItem.isGate() &&
                        !selectedItem.isReplicable() &&
                        !selectedItem.isRoom()) {
                    itemDetails.getItems().addAll(name, type, hash, parent);
                } else {
                    itemDetails.getItems().addAll(name, type, hash, parent, isANG, isAE, isAVG, isGATE, isREP, isROOM);
                }

                if (enablePopups) {
                    if (selectedItem.getANG_IEntity().size() > 0)
                        arraylistPopUp("Activatable IEntitys:", selectedItem.getANG_IEntity());
                    if (selectedItem.getAudioEmitters().size() > 0)
                        arraylistPopUp("Audio Emitters:", selectedItem.getAudioEmitters());
                    if (selectedItem.getAudioVolumetric().size() > 0)
                        arraylistPopUp("Audio Volumetric Geomerties:", selectedItem.getAudioVolumetric());
                    if (selectedItem.getGates().size() > 0) arraylistPopUp("Gates:", selectedItem.getGates());
                    if (selectedItem.getReplicable().size() > 0)
                        arraylistPopUp("Replicables:", selectedItem.getReplicable());
                    if (selectedItem.getRooms().size() > 0) arraylistPopUp("Rooms:", selectedItem.getRooms());
                }
            } catch (NullPointerException e) {
                System.out.println("no information found on selected item");
            } catch (Exception e) {
                System.out.println("an error seems to have occured :(");
            }
        });


        BorderPane borderpane = new BorderPane();
        hbox.getChildren().addAll(treeView, itemDetails);
        borderpane.setCenter(hbox);
        primaryStage.setTitle("TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 850, 675));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launch the app");
    }

    public void buildItemLibrary(File fl) {


        File cacheFile = new File(System.getProperty("user.dir") + "\\cache\\" + filename + ".dat");
        //check if file is already serialized
        if (cacheFile.exists() && useCache) {
            this.itemLibrary = deserializeItemsArray(filename);
        } else {
            //count the amount of lines
            String fileAsString = "";
            try {
                fileAsString = readTextFile(fl.getPath(), StandardCharsets.US_ASCII);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.itemLibrary = new ItemLibrary(filename);


            //Extract the parts with directory structure
            ArrayList<String> lines = new ArrayList<>();
            String lastItemName = "";
            ArrayList<ArrayList> linkedDataArrays = new ArrayList<>();
            for (String line : fileAsString.split("},")) {
                if (line.contains("parent")) {
                    lines.add(line);
                    line = line.substring(1);
                    String[] parts = line.split(",");
                    String name = parts[3].split("\"")[3];
                    lastItemName = name;
                }
                if (line.contains("\"Activatable_NormalGameplay, IEntity\":")) {
                    ArrayList<String> ANG_IEntity = new ArrayList<>();
                    ANG_IEntity.add("ANG");
                    ANG_IEntity.add(lastItemName);
                    for (String string : line.split("\"Activatable_NormalGameplay, IEntity\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            ANG_IEntity.add(string);
                        }
                    }
                    System.out.println(ANG_IEntity.size() + " Activatable_NormalGameplay, IEntity's found in " + ANG_IEntity.get(1));
                    linkedDataArrays.add(ANG_IEntity);
                    //System.out.println("Activatable_NormalGameplay, IEntity = " + line.split("\"Activatable_NormalGameplay, IEntity\":")[1]);
                }
                if (line.contains("\"AudioEmitters\":")) {
                    ArrayList<String> audioEmitters = new ArrayList<>();
                    audioEmitters.add("AE");
                    audioEmitters.add(lastItemName);
                    for (String string : line.split("\"AudioEmitters\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            audioEmitters.add(string);
                        }
                    }
                    System.out.println(audioEmitters.size() + " audioEmitters found in " + audioEmitters.get(1));
                    linkedDataArrays.add(audioEmitters);
                    //System.out.println("AudioEmitters = " + line.split("\"AudioEmitters\":")[1]);
                }
                if (line.contains("\"AudioVolumetricGeom\":")) {
                    ArrayList<String> audioVolumetricGeom = new ArrayList<>();
                    audioVolumetricGeom.add("AVG");
                    audioVolumetricGeom.add(lastItemName);
                    for (String string : line.split("\"AudioVolumetricGeom\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            audioVolumetricGeom.add(string);
                        }
                    }
                    System.out.println(audioVolumetricGeom.size() + " audioVolumetricGeometries found in " + audioVolumetricGeom.get(1));
                    linkedDataArrays.add(audioVolumetricGeom);
                    //System.out.println("AudioEmitters = " + line.split("\"AudioEmitters\":")[1]);
                }
                if (line.contains("\"Gates\":")) {
                    ArrayList<String> gates = new ArrayList<>();
                    gates.add("GATE");
                    gates.add(lastItemName);
                    for (String string : line.split("\"Gates\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            gates.add(string);
                        }
                    }
                    System.out.println(gates.size() + " gates found in " + gates.get(1));
                    linkedDataArrays.add(gates);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }
                if (line.contains("\"Replicable\":")) {
                    ArrayList<String> replicable = new ArrayList<>();
                    replicable.add("REP");
                    replicable.add(lastItemName);
                    for (String string : line.split("\"Replicable\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            replicable.add(string);
                        }
                    }
                    System.out.println(replicable.size() + " replicables found in " + replicable.get(1));
                    linkedDataArrays.add(replicable);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }
                if (line.contains("\"Rooms\":")) {
                    ArrayList<String> rooms = new ArrayList<>();
                    rooms.add("ROOM");
                    rooms.add(lastItemName);
                    for (String string : line.split("\"Rooms\":")[1].split("\"")) {
                        string = string.replaceAll(",", "");
                        string = string.replaceAll("]", "");
                        string = string.replaceAll("}", "");
                        string = string.replaceAll("\\{", "");
                        string = string.replaceAll("\\[", "");
                        string = string.replaceAll("\\\\s+", "");
                        string = string.replaceAll(" ", "");
                        if (string.length() >= 3) {
                            rooms.add(string);
                        }
                    }
                    System.out.println(rooms.size() + " rooms found in " + rooms.get(1));
                    linkedDataArrays.add(rooms);
                    //System.out.println("Replicable = " + line.split("\"Replicable\":")[1]);
                }
            }

            //create the objects from the JSON
            ArrayList<String> ANG_IEntity = new ArrayList<>();
            ArrayList<String> audioEmitters = new ArrayList<>();
            ArrayList<String> audioVolumetricGeom = new ArrayList<>();
            ArrayList<String> gates = new ArrayList<>();
            ArrayList<String> replicable = new ArrayList<>();
            ArrayList<String> rooms = new ArrayList<>();

            for (String line : lines) {
                line = line.substring(1);
                String[] parts = line.split(",");
                String parent = parts[0].split("\"")[3];
                String type = parts[1].split("\"")[3];
                String hash = parts[2].split("\"")[3];
                String name = parts[3].split("\"")[3];

                //add linked data
                for (ArrayList arrayList : linkedDataArrays) {
                    if (arrayList.size() > 2) {
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

                this.itemLibrary.add(new Item(parent, type, hash, name, ANG_IEntity, audioEmitters, audioVolumetricGeom, gates, replicable, rooms));

                for (Item item : itemLibrary.getItems()) {
                    if (ANG_IEntity.contains(item.getName())) item.setANG_IEntity(true);
                    if (audioEmitters.contains(item.getName())) item.setAudioEmitter(true);
                    if (audioVolumetricGeom.contains(item.getName())) item.setAudioVolumetric(true);
                    if (gates.contains(item.getName())) item.setGate(true);
                    if (replicable.contains(item.getName())) item.setReplicable(true);
                    if (rooms.contains(item.getName())) item.setRoom(true);
                }

                ANG_IEntity = new ArrayList<>();
                audioEmitters = new ArrayList<>();
                audioVolumetricGeom = new ArrayList<>();
                gates = new ArrayList<>();
                replicable = new ArrayList<>();
                rooms = new ArrayList<>();


            }
            for (Item item : itemLibrary.getItems()) {
                item.sortLinkedArrays();
            }

        }

        //Add a root node
        this.itemLibrary.add(new Item("root", "", "", filename));


        //find the children
        for (Item itemToFill : this.itemLibrary.getItems()) {
            for (Item item : this.itemLibrary.getItems()) {
                if (item.getParent().equals(itemToFill.getName())) {
                    itemToFill.addChild(item);
                }
                if (itemToFill.getParent().equals("root") && item.getParent().contains("0xff")) {
                    itemToFill.addChild(item);
                }
            }

        }
        System.out.println("succesfully constructed ItemLibrary");
        //sort the children
        this.itemLibrary.sortAllItems();
        System.out.println("sorted all items");
        //serialize the itemsArray
        //serializeItemsArray(this.itemLibrary);

    }

    public File getFile() {
        Stage selectFile = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a TBLU file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TBLU files", "*.TBLU");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File selectedFile = fileChooser.showOpenDialog(selectFile);
        System.out.println("Selected file: " + selectedFile);
        return selectedFile;
    }

    public InputStream excCommand(String command) {
        Runtime rt = Runtime.getRuntime();

        try {

            return rt.exec(command).getInputStream();
        } catch (Exception e) {
            System.out.println("failed to execute command");
        }
        return null;
    }

    public File convertFileToJson(File selectedFile) {
        Stage convertStage = new Stage();
        BorderPane borderPane = new BorderPane();

        Label loadingLabel = new Label("Please hold on while the file is being decoded");
        loadingLabel.setStyle("-fx-font-weight: bold; -fx-font-fill: Black");
        if ((selectedFile.length() / 1024) > 1000) {
            loadingLabel.setText(loadingLabel.getText() + "\n Big file detected conversion might take longer then expected");
        }

        borderPane.setCenter(loadingLabel);
        Scene scene = new Scene(borderPane);
        convertStage.setScene(scene);
        convertStage.show();


        try {

            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(excCommand("python \".\\decoder\\TBLUdecode.py\" \"" + selectedFile.getPath() + "\" JSON")));

            System.out.println("Running decoder");
            if(printDecoder) System.out.println("-----------------------------------------------------------");
            while (line != null) {
                line = reader.readLine();
                if(printDecoder) System.out.println(line);

            }
            if(printDecoder) System.out.println("-----------------------------------------------------------");

            if (reader.readLine() == null) {
                convertStage.close();
                try {
                    return new File(selectedFile.getPath() + ".BIN1decoded.JSON");
                } catch (Exception e) {
                    System.out.println("an error occured while converting the TBLU to a JSON file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void arraylistPopUp(String name, ArrayList<String> arrayList) {

        arrayList.remove(0);
        ListView listView = new ListView();
        listView.getItems().addAll(arrayList);

        Scene scene = new Scene(listView, 250, 750);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(name);
        stage.setX(250 * openPopUps.size());
        stage.setY(150);
        stage.show();
        openPopUps.add(stage);
    }

    public void serializeItemsArray(ItemLibrary itemLibrary) {
        try {
            FileWriter itemsWriter = new FileWriter("cache\\" + itemLibrary.getName() + ".dat");
            for (Item item : itemLibrary.getItems()) {
                itemsWriter.write(item.getParent() + "#" + item.getType() + "#" + item.getHash() + "#" + item.getName() + "\n");
            }
            itemsWriter.close();

            System.out.println(this.filename + " was succesfully serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ItemLibrary deserializeItemsArray(String name) {
        ItemLibrary itemLibrary = new ItemLibrary(name);
        try (BufferedReader br = new BufferedReader(new FileReader("cache\\" + name + ".dat"))) {

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

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return itemLibrary;
    }

    static String readTextFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public void getSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.txt"))) {
            String line = br.readLine();

            while (line != null) {
                if (line.contains("Level of detail")) this.LoD = Integer.parseInt(line.split(": ")[1]);
                if (line.contains("use cache")) this.useCache = Boolean.parseBoolean(line.split(": ")[1]);
                if (line.contains("use old jsons")) this.useOldJsons = Boolean.parseBoolean(line.split(": ")[1]);
                if (line.contains("enable popups")) this.enablePopups = Boolean.parseBoolean(line.split(": ")[1]);
                if (line.contains("enable decoder prints")) this.printDecoder = Boolean.parseBoolean(line.split(": ")[1]);


                line = br.readLine();
            }

            System.out.println("[SETTINGS]");
            System.out.println("Level of detail: " + this.LoD);
            System.out.println("use cache: " + this.useCache);
            System.out.println("use old jsons: " + this.useOldJsons);
            System.out.println("enable popups: " + this.enablePopups);
            System.out.println("enable decoder prints: " + this.printDecoder);
            System.out.println(" ");
        } catch (IOException e) {
            System.out.println("could not find settings.txt file");
        }
    }

}
