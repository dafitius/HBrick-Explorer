package sample;

import Decoder.TBLU.BlockTypes.Block0;
import Decoder.TBLU.BlockTypes.Block0_0List;
import Decoder.TBLU.BlockTypes.Block0_3;
import Decoder.TBLU.TBLUDecoder;
import Files.TBLU;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.showMessageDialog;


public class Main extends Application {

    File selectedFile;
    String filename;
    ItemLibrary itemLibrary;
    private HashMap<String, String> nameAndHash;
    private String selectedItemName;
    ArrayList<Stage> openPopUps;


    //settings
    private int LoD;
    private String pythonPATHVar;
    private boolean useCache;
    private boolean useOldJsons;
    private String TBLUfolderPATH;
    private boolean enableDarkmode;
    private boolean enablePopups;
    private boolean printDecoder;

    public static void main(String[] args) {

        launch(args);
    }

    public void initialize(){
        getSettings();

        openPopUps = new ArrayList<>();
        nameAndHash = new HashMap<>();

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
    }

    @Override
    public void start(Stage primaryStage){
        initialize();

        //Build treeview
        TreeView<Item> treeView = new TreeView<>();
        populateTreeView(treeView, this.itemLibrary);

        //create a panel on the right for details
        HBox hbox = new HBox();
        ListView itemDetails = new ListView();


        //get selected item from the tree and fill in details
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getDetails(itemDetails, newValue);
        });


        BorderPane borderpane = new BorderPane();
        hbox.getChildren().addAll(treeView, itemDetails);
        borderpane.setCenter(hbox);
        primaryStage.setTitle("Files.TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 850, 675));
        primaryStage.getIcons().add(new javafx.scene.image.Image(this.getClass().getResource("icon.png").toExternalForm()));
        if(this.enableDarkmode) primaryStage.getScene().getStylesheets().add(this.getClass().getResource("darkmode.css").toExternalForm());
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launched the app");
    }

    public void buildItemLibrary(File fl) {



        //check if file is already serialized


            //count the amount of lines

            this.itemLibrary = new ItemLibrary(filename);
        try {
            TBLUDecoder tbluDecoder = new TBLUDecoder();
            TBLU TBLUfile = tbluDecoder.decode(selectedFile);
            System.out.println("----------------------------------------------");
            System.out.println("size: " + TBLUfile.getHeader().getFileSize() + " bytes");
            System.out.println("type: " + TBLUfile.getHeader().getFileType());
            System.out.println("root: " + TBLUfile.getHeader().getRootName());
            System.out.println("unkown: " + TBLUfile.getHeader().getUnknown());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }


//            for (Item item : itemLibrary.getItems()) {
//                item.sortLinkedArrays();
//            }



        //Add a root node
        this.itemLibrary.add(new Item("root", "", "", filename));


        //find the children
        for (Item itemToFill : this.itemLibrary.getItems()) {
            for (Item item : this.itemLibrary.getItems()) {
                if (item.getParent().toLowerCase().equals(itemToFill.getHash().toLowerCase())) {
                    itemToFill.addChild(item);
                }
                if (itemToFill.getParent().equals("root") && item.getParent().toLowerCase().contains("ffffffffff")) {
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

    public void getDetails(ListView itemDetails, TreeItem newValue){
        try {
            selectedItemName = newValue.getValue().toString();
            Item selectedItem = new Item();
            for (Item item : itemLibrary.getItems()) {
                if (item.getHash().equals(nameAndHash.get(selectedItemName))) {
                    selectedItem = item;
                }
            }

            System.out.println("selected: " + selectedItem.getName());

            Label name = new Label("Name: " + selectedItem.getName());
            Label type = new Label("Type: " + selectedItem.getType());
            Label hash = new Label("Hash: " + selectedItem.getHash());
            //Label parent = new Label("Parent: " + selectedItem.getParent());
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
                itemDetails.getItems().addAll(name, type, hash);
            } else {
                itemDetails.getItems().addAll(name, type, hash, isANG, isAE, isAVG, isGATE, isREP, isROOM);
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
            System.out.println("an error seems to have occured: \n" + e.getMessage());
        }
    }

    public TreeView populateTreeView(TreeView treeView, ItemLibrary itemLibrary){

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
        treeView.setMinWidth(500);
        return treeView;
    }

    public File getFile() {
        Stage selectFile = new Stage();
        FileChooser fileChooser = new FileChooser();
        if(!this.TBLUfolderPATH.isEmpty()){
            File initialDir = new File(TBLUfolderPATH);
            if(initialDir.exists()) fileChooser.setInitialDirectory(initialDir);
        }
        fileChooser.setTitle("Select a TBLU file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Files.TBLU files", "*.TBLU");
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
            System.out.println("failed to execute command: \n"  + e.getMessage());
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(excCommand(this.pythonPATHVar + " \".\\decoder\\TBLUdecode.py\" \"" + selectedFile.getPath() + "\" JSON")));

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
                    System.out.println("an error occured while converting the TBLU to a JSON file: \n" + e.getMessage());
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
        if(this.enableDarkmode) stage.getScene().getStylesheets().add(this.getClass().getResource("darkmode.css").toExternalForm());
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

    static String readTextFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public void getSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.txt"))) {
            String line = br.readLine();

            while (line != null) {
                try {
                    if (line.contains("Level of detail")) this.LoD = Integer.parseInt(line.split(": ")[1]);
                    if (line.contains("python PATH var")) this.pythonPATHVar = line.split(": ")[1];
                    //if (line.contains("use cache")) this.useCache = Boolean.parseBoolean(line.split(": ")[1]);
                    this.useCache = false;
                    if (line.contains("use old jsons")) this.useOldJsons = Boolean.parseBoolean(line.split(": ")[1]);
                    if (line.contains("default TBLU path")) {
                        String[] splitLine = line.split(": ");
                        if(splitLine.length > 1) this.TBLUfolderPATH = line.split(": ")[1];
                        else this.TBLUfolderPATH = "";
                    }
                    if (line.contains("use dark-mode")) this.enableDarkmode = Boolean.parseBoolean(line.split(": ")[1]);
                    if (line.contains("enable popups")) this.enablePopups = Boolean.parseBoolean(line.split(": ")[1]);
                    if (line.contains("enable decoder prints"))
                        this.printDecoder = Boolean.parseBoolean(line.split(": ")[1]);
                }catch (NumberFormatException e){
                    System.out.println("detected typo inside the setting.txt: \n" + e.getMessage());
                }


                line = br.readLine();
            }

            System.out.println("[SETTINGS]");
            System.out.println("Level of detail: " + this.LoD);
            System.out.println("python PATH var: " + this.pythonPATHVar);
            System.out.println("default TBLU path" + this.TBLUfolderPATH);
            //System.out.println("use cache: " + this.useCache);
            System.out.println("use old jsons: " + this.useOldJsons);
            System.out.println("use dark-mode: " + this.enableDarkmode);
            System.out.println("enable popups: " + this.enablePopups);
            System.out.println("enable decoder prints: " + this.printDecoder);

            System.out.println(" ");
        } catch (IOException e) {
            System.out.println("could not find settings.txt file");
        }
    }

}
