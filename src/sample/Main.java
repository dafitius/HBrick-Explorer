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

    ItemLibrary itemLibrary;
    private String selectedItemName;

    @Override
    public void start(Stage primaryStage) throws Exception {

        File selectedFile = getFile();

        File fl = convertFileToJson(selectedFile);

        int LoD = 4;
        String filename = fl.getName().substring(0, 15);
        Scanner lineCounter = new Scanner(fl);
        int linecount = 0;

        File fileToCheck = new File(System.getProperty("user.dir") + "\\" + filename + ".dat");

        if(Files.exists(fileToCheck.toPath())){
                this.itemLibrary = deserializeItemsArray(filename);
        }
        else {

            while (lineCounter.hasNextLine()) {
                lineCounter.nextLine();
                linecount++;
            }
            lineCounter.close();
            System.out.println(linecount + " lines counted");


            Scanner sc = new Scanner(fl);
            String file = "";
            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                file += line;
                if (i % (linecount / 30) == 0) {
                    System.out.println("read " + i + " of " + linecount + " lines");
                }
                i++;
            }
            sc.close();

            this.itemLibrary = new ItemLibrary(filename);
            //Extract the parts with directory structure
            ArrayList<String> lines = new ArrayList<>();
            for (String line : file.split("},")) {
                if (line.contains("parent")) {
                    lines.add(line);
                    System.out.println("added line");
                }
            }
            //create the objects from the JSON
            for (String line : lines) {
                line = line.substring(1);
                String[] parts = line.split(",");
                System.out.println("have split line");
                String parent = parts[0].split("\"")[3];
                String type = parts[1].split("\"")[3];
                String hash = parts[2].split("\"")[3];
                String name = parts[3].split("\"")[3];
                this.itemLibrary.add(new Item(parent, type, hash, name));
            }
        }

            //find the children
            for (Item itemToFill : this.itemLibrary.getItems()) {
                for (Item item : this.itemLibrary.getItems()) {
                    if (item.getParent().equals(itemToFill.getName())) {
                        itemToFill.addChild(item);
                        System.out.println("added child" + item.getChildren().toString());
                    }
                }

            }
            System.out.println("found and added all children");
            serializeItemsArray(this.itemLibrary);

            //Add info to GUI

            TreeView<Item> treeView = new TreeView<>();
            TreeItem<Item> root = new TreeItem<Item>();



            for (Item item : itemLibrary.getItems()) {
                if (item.getName().equals(itemLibrary.getRoot())) {
                    root = item.getViewItem();
                }
            }
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


        HBox hbox = new HBox();
        ListView itemDetails = new ListView();
        treeView.setMinWidth(300);


        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedItemName = newValue.getValue().toString();
                Item selectedItem = new Item();
                for(Item item : itemLibrary.getItems()){
                    if(item.getName().equals(selectedItemName)){
                        selectedItem = item;
                    }
                }
            System.out.println(selectedItem.getName());
            Label name = new Label(selectedItem.getName());
            Label type = new Label(selectedItem.getType());
            Label hash = new Label(selectedItem.getHash());
            itemDetails.getItems().clear();
            itemDetails.getItems().addAll(name, type, hash);
        });









        treeView.setRoot(root);

        BorderPane borderpane = new BorderPane();
        hbox.getChildren().addAll(treeView, itemDetails);
        borderpane.setCenter(hbox);
        primaryStage.setTitle("TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 660, 675));
        primaryStage.show();
    }

    public static void serializeItemsArray(ItemLibrary itemLibrary){
        try {
            FileWriter itemsWriter = new FileWriter(itemLibrary.getName() + ".dat");
            System.out.println("Successfully wrote to the file.");
            for(Item item : itemLibrary.getItems()){
                itemsWriter.write(item.getParent() + "#" + item.getType() + "#" + item.getHash() + "#" + item.getName() + "\n");
            }
            itemsWriter.close();

            System.out.println("printed all items ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static ItemLibrary deserializeItemsArray(String name){
        ItemLibrary itemLibrary = new ItemLibrary(name);
        try(BufferedReader br = new BufferedReader(new FileReader(name + ".dat"))) {

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
            System.out.println("succesfully deserialized Array");

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
        borderPane.setCenter(new TextArea());

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(excCommand("python \"C:\\Users\\david\\Documents\\Github projects\\Hitman_TBLU_viewer\\decoder\\TBLUdecode.py\" \"" + selectedFile.getPath() + "\" JSON")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                textArea.appendText(line + "\n");
            }
            Scene scene = new Scene(textArea);
            convertStage.setScene(scene);
            convertStage.show();

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
