package Logic;

import Decoder.TBLU.TBLUDecoder;

import Decoder.TEMP.TEMPDecoder;

import Decoder.TEMP.BlockTypes.*;
import Decoder.TEMP.BlockTypes.properties.*;
import Decoder.TBLU.BlockTypes.*;

import Files.STemplateEntityFactory;
import Files.TBLU;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;


public class Main extends Application {


    private Map<String, Node> tabs;

    //settings
    private String hitmanEdition;
    private String TBLUfolderPATH;
    private boolean enableDarkmode;


    public static void main(String[] args) {

        launch(args);
    }

    public void initialize() {
        getSettings();
        this.tabs = new HashMap<>();
    }

    @Override
    public void start(Stage primaryStage) {
        initialize();

        HBox hbox = new HBox();
        BorderPane borderpane = new BorderPane();

        TabPane tabPane = new TabPane();
        Tab welcomeTab = new Tab("welcome");
        welcomeTab.setClosable(false);
        Tab addTab = new Tab("+");
        ListView<String> welcomeText = new ListView<>();
        welcomeText.getItems().addAll("welcome to brick explorer", "to use this application please add a file");
        this.tabs.put(welcomeTab.getText(), welcomeText);
        addTab.setClosable(false);
        tabPane.getTabs().addAll(welcomeTab, addTab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab.equals(addTab)) {
                tabPane.getTabs().remove(addTab);
                File selectedFile = getFile();
                if (selectedFile != null) tabPane.getTabs().add(new Tab(selectedFile.getName()));
                tabPane.getTabs().add(addTab);
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.select(tabPane.getTabs().get(tabPane.getTabs().size() - 2));
                tabPane.setSelectionModel(selectionModel);
                if (selectedFile != null) {
                    switch (selectedFile.getName().split("\\.")[1]) {
                        case "TEMP":
                            displayTEMPfile(selectedFile, borderpane);
                            break;
                        case "TBLU":
                            displayTBLUfile(selectedFile, borderpane);
                            break;
                    }
                }
            } else {
                borderpane.setCenter(tabs.get(selectedTab.getText()));
            }

        });


        //create a panel on the right for details

        borderpane.setCenter(hbox);
        borderpane.setTop(tabPane);
        primaryStage.setTitle("brick Viewer");
        primaryStage.setScene(new Scene(borderpane, 850, 675));
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/Logic/resources/icon.png")));
        if (this.enableDarkmode)
            primaryStage.getScene().getStylesheets().add(this.getClass().getResource("/Logic/resources/darkmode.css").toExternalForm());
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launched the app");
    }


    public TreeItem<String> populateTreeView(TreeView<String> treeView, subEntity subEntity, int depth, TreeItem<String> treeItem) {

        ArrayList<subEntity> subEntities = subEntity.getCC_children();
        for (subEntity entity : subEntities) {

            for (int i = 0; i < depth; i++) System.out.print("   ");
            //System.out.print("|---");
            //System.out.print(entity.getName() + "\n");
            //System.out.println("added " + entity.getName() + " to " + treeItem.getValue());
            TreeItem<String> item = new TreeItem<>(entity.getName());
            treeItem.getChildren().add(item);

            if (entity.isCC_hasChildren()) {
                populateTreeView(treeView, entity, depth + 1, item);
            }
        }
        if(depth == 0) return treeItem;
        return null;
    }


    private void displayTEMPfile(File selectedFile, BorderPane borderPane) {
        ListView<String> list = new ListView<>();
        if (!this.tabs.containsKey(selectedFile.getName())) {
            STemplateEntityFactory decodedTEMPfile = decodeTempFile(selectedFile);

            for (STemplateFactorySubEntity subEntity : decodedTEMPfile.getSubEntities()) {
                for (SEntityTemplateProperty sEntityTemplateProperty : subEntity.getPropertyValues()) {
                    if (sEntityTemplateProperty.getnPropertyID().getProp().equals("m_mTransform")) {
                        m_mTransform transform = (m_mTransform) sEntityTemplateProperty.getnPropertyID();
                        list.getItems().add(transform.getMatrix34().toString());
                    }
                }
            }
            this.tabs.put(selectedFile.getName(), list);
        } else list = (ListView<String>) this.tabs.get(selectedFile.getName());
        borderPane.setCenter(list);

    }

    private void displayTBLUfile(File selectedFile, BorderPane borderPane) {

        TreeView<String> treeView = new TreeView<>();
        subEntity rootEntity = null;
        if (!this.tabs.containsKey(selectedFile.getName())) {
            TBLU decodedTBLUfile = decodeTbluFile(selectedFile);

            //add children to items
            for (subEntity itemToFill : decodedTBLUfile.getBlock0()) {
                for (subEntity item : decodedTBLUfile.getBlock0()) {

                    if (item.getParentHash().equals(itemToFill.getHash())) {
                        itemToFill.addChild(item);
                    }

                }
                if (itemToFill.getHash().equals(decodedTBLUfile.getHeader().getRootHash())) {
                    rootEntity = itemToFill;
                }
            }
            if (rootEntity != null) {
                treeView.setRoot(populateTreeView(treeView, rootEntity, 0, new TreeItem<>(rootEntity.getName())));
            }
            this.tabs.put(selectedFile.getName(), treeView);
        } else treeView = (TreeView<String>) this.tabs.get(selectedFile.getName());
        borderPane.setCenter(treeView);
    }

    private STemplateEntityFactory decodeTempFile(File selectedFile) {

        TEMPDecoder tempDecoder = new TEMPDecoder();
        STemplateEntityFactory TEMPfile = null;
        try {
            TEMPfile = tempDecoder.decode(selectedFile, hitmanEdition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TEMPfile;
    }

    private TBLU decodeTbluFile(File selectedFile) {

        TBLUDecoder tbluDecoder = new TBLUDecoder();
        TBLU TBLUfile = null;
        try {
            TBLUfile = tbluDecoder.decode(selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TBLUfile;
    }


    public File getFile() {
        Stage selectFile = new Stage();
        FileChooser fileChooser = new FileChooser();
        if (!this.TBLUfolderPATH.isEmpty()) {
            File initialDir = new File(TBLUfolderPATH);
            if (initialDir.exists()) fileChooser.setInitialDirectory(initialDir);
        }
        fileChooser.setTitle("Select a  file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TBLU files", "*.TBLU");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("TEMP files", "*.TEMP");
        fileChooser.getExtensionFilters().addAll(filter, filter2);
        fileChooser.setSelectedExtensionFilter(filter2);
        File selectedFile = fileChooser.showOpenDialog(selectFile);
        System.out.println("Selected file: " + selectedFile);
        return selectedFile;
    }

    public void getSettings() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.txt"))) {
            String line = br.readLine();

            while (line != null) {
                try {
                    if (line.contains("default file path")) {
                        String[] splitLine = line.split(": ");
                        if (splitLine.length > 1) this.TBLUfolderPATH = line.split(": ")[1];
                        else this.TBLUfolderPATH = "";
                    }
                    if (line.contains("use dark-mode")) this.enableDarkmode = Boolean.parseBoolean(line.split(": ")[1]);
                    if (line.contains("hitman edition")) {
                        this.hitmanEdition = line.split(": ")[1];
                    }
                } catch (NumberFormatException e) {
                    System.out.println("detected typo inside the setting.txt: \n" + e.getMessage());
                }


                line = br.readLine();
            }

            System.out.println("[SETTINGS]");
            System.out.println("Hitman version: " + this.hitmanEdition);
            System.out.println("default TBLU path: " + this.TBLUfolderPATH);
            System.out.println("use dark-mode: " + this.enableDarkmode);

            System.out.println(" ");
        } catch (IOException e) {
            System.out.println("could not find settings.txt file");
        }
    }

}
