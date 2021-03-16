package Logic;

import Decoder.TBLU.TBLUDecoder;

import Decoder.TEMP.TEMPDecoder;
import Decoder.Enums.*;

import Decoder.TEMP.BlockTypes.*;
import Decoder.TBLU.BlockTypes.*;

import Files.STemplateEntityFactory;
import Files.TBLU;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main extends Application {

    private static double xOffset = 0;
    private static double yOffset = 0;
    //          name,           treeView, DetailsList
    private Map<String, Map.Entry<Node, Node>> tabs;

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

        ListView<String> welcomeText = new ListView<>();
        welcomeText.getItems().addAll("welcome to brick explorer", "to use this application please add a file");

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            selectionModel.select(selectedTab);
            if(this.tabs.containsKey(selectedTab.getText())) {
                borderpane.setCenter(tabs.get(selectedTab.getText()).getKey());
                if(tabs.get(selectedTab.getText()).getValue() != null) borderpane.setRight(tabs.get(selectedTab.getText()).getValue());
                else borderpane.setRight(null);
            }

        });


        //create a panel on the right for details

        borderpane.setCenter(hbox);;
        borderpane.setTop(tabPane);


        BorderPane topPane = new BorderPane();
        HBox topBar = buildBar(primaryStage, tabPane, topPane, borderpane);
        createDragFunction(primaryStage, topBar);
        VBox vbox = new VBox(topBar, borderpane);
        primaryStage.setTitle("brick Viewer");
        primaryStage.setScene(new Scene(vbox, 850, 675));
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/Logic/resources/icon.png")));
        if (this.enableDarkmode)
            primaryStage.getScene().getStylesheets().add(this.getClass().getResource("/Logic/resources/darkmode.css").toExternalForm());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launched the app");
    }


    public TreeItem<subEntity> populateTreeView(subEntity subEntity, int depth, TreeItem<subEntity> treeItem) {

        ArrayList<subEntity> subEntities = subEntity.getCC_children();
        for (subEntity entity : subEntities) {

            for (int i = 0; i < depth; i++) System.out.print("   ");
            TreeItem<subEntity> item = new TreeItem<subEntity>(entity);
            treeItem.getChildren().add(item);

            if (entity.isCC_hasChildren()) {
                populateTreeView(entity, depth + 1, item);
            }
        }
        if (depth == 0) return treeItem;
        return null;
    }

    private void displayTEMPfile(File selectedFile, BorderPane borderPane) {
        TreeView<String> treeView = new TreeView<>();
        if (!this.tabs.containsKey(selectedFile.getName())) {
            int i = 0;
            STemplateEntityFactory decodedTEMPfile = decodeTempFile(selectedFile);
            treeView.setRoot(new TreeItem<>("Template"));

            TreeItem<String> subType = new TreeItem<>("subType: " + EntityTemplateSubType.get(decodedTEMPfile.getSubType()));

            TreeItem<String> subEntities = new TreeItem<>("subEntities");

            TreeItem<String> propertyOverrides = new TreeItem<>("propertyOverrides");

            TreeItem<String> externalSceneTypeIndicesInResourceHeader = new TreeItem<>("externalSceneTypeIndicesInResourceHeader");
            if(decodedTEMPfile.getExternalSceneTypeIndicesInResourceHeader() != null) {
                for (int index : decodedTEMPfile.getExternalSceneTypeIndicesInResourceHeader()) {
                    externalSceneTypeIndicesInResourceHeader.getChildren().add(new TreeItem<String>(index + ""));
                }
            }
            TreeItem<String> blueprintIndexInResourceHeader = new TreeItem<>("blueprintIndexInResourceHeader: " + decodedTEMPfile.getBlueprintIndexInResourceHeader());

            treeView.getRoot().getChildren().addAll(subType, blueprintIndexInResourceHeader, externalSceneTypeIndicesInResourceHeader, propertyOverrides, subEntities);

            if(decodedTEMPfile.getSubEntities() != null) {
                for (STemplateFactorySubEntity subEntity : decodedTEMPfile.getSubEntities()) {
                    TreeItem<String> subEntityItem = new TreeItem<String>("sub Entity " + i);
                    TreeItem<String> entityTypeResourceIndex = new TreeItem<>("entityTypeResourceIndex");
                    TreeItem<String> logicalParent = new TreeItem<>("LogicalParent");
                    TreeItem<String> postInitPropertyValues = new TreeItem<>("postInitPropertyValues");
                    TreeItem<String> propertyValues = new TreeItem<>("propertyValues");
                    subEntityItem.getChildren().addAll(entityTypeResourceIndex, logicalParent, postInitPropertyValues, propertyValues);

                    entityTypeResourceIndex.getChildren().add(new TreeItem<>("entityTypeResourceIndex: " + subEntity.getEntityTypeResourceIndex()));
                    logicalParent.getChildren().add(new TreeItem<>("\"Entity ID\": " + subEntity.getLogicalParent().getEntityID()));
                    logicalParent.getChildren().add(new TreeItem<>("\"Entity Index\": " + subEntity.getLogicalParent().getEntityIndex()));
                    logicalParent.getChildren().add(new TreeItem<>("\"Exposed entity\": \"" + subEntity.getLogicalParent().getExposedEntity() + "\""));
                    logicalParent.getChildren().add(new TreeItem<>("\"External Scene Index\": " + subEntity.getLogicalParent().getExternalSceneIndex()));
                    subEntity.getPostInitPropertyValues().forEach(p -> {
                        if (!p.getnPropertyID().matches("[0-9]+"))
                            postInitPropertyValues.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getnProperty().toString()));
                        else
                            postInitPropertyValues.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getType() + ": { " + p.getnProperty().toString() + " }"));
                    });
                    subEntity.getPropertyValues().forEach(p -> {
                        if (!p.getnPropertyID().matches("[0-9]+"))
                            propertyValues.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getnProperty().toString()));
                        else
                            propertyValues.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getType() + ": { " + p.getnProperty().toString() + " }"));
                    });

                    subEntities.getChildren().add(subEntityItem);
                    i++;
                }
            }
            i = 0;
            if(decodedTEMPfile.getPropertyOverrides() != null) {
                for (SEntityTemplatePropertyOverride propertyOverride : decodedTEMPfile.getPropertyOverrides()) {

                    //Entity root
                    TreeItem<String> propertyOverrideItem = new TreeItem<String>("property override" + i);

                    TreeItem<String> PropertyOwner = new TreeItem<>("PropertyOwner");
                    TreeItem<String> propertyValue = new TreeItem<>("propertyValue");
                    propertyOverrideItem.getChildren().addAll(PropertyOwner, propertyValue);

                    PropertyOwner.getChildren().add(new TreeItem<>("\"Entity ID\": " + propertyOverride.getPropertOwner().getEntityID()));
                    PropertyOwner.getChildren().add(new TreeItem<>("\"Entity Index\": " + propertyOverride.getPropertOwner().getEntityIndex()));
                    PropertyOwner.getChildren().add(new TreeItem<>("\"Exposed entity\": \"" + propertyOverride.getPropertOwner().getExposedEntity() + "\""));
                    PropertyOwner.getChildren().add(new TreeItem<>("\"External Scene Index\": " + propertyOverride.getPropertOwner().getExternalSceneIndex()));

                    SEntityTemplateProperty p = propertyOverride.getPropertyValue();
                    if (!p.getnPropertyID().matches("[0-9]+"))
                        propertyValue.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getnProperty().toString()));
                    else
                        propertyValue.getChildren().add(new TreeItem<String>(p.getnPropertyID() + ": \n" + p.getType() + ": { " + p.getnProperty().toString() + " }"));

                    propertyOverrides.getChildren().add(propertyOverrideItem);
                    i++;
                }
            }
            this.tabs.put(selectedFile.getName(), new AbstractMap.SimpleEntry(treeView, null));
        } else treeView = (TreeView<String>) this.tabs.get(selectedFile.getName());

        borderPane.setRight(null);
        borderPane.setCenter(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("selected: " + newValue.getValue());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(newValue.getValue());
            clipboard.setContents(stringSelection, null);
        });
    }

    private void displayTBLUfile(File selectedFile, BorderPane borderPane) {

        TreeView<subEntity> treeView = new TreeView<>();
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
                treeView.setRoot(populateTreeView(rootEntity, 0, new TreeItem<subEntity>(rootEntity)));
            }
            this.tabs.put(selectedFile.getName(), new AbstractMap.SimpleEntry(treeView, null));
        } else treeView = (TreeView<subEntity>) this.tabs.get(selectedFile.getName());
        borderPane.setCenter(treeView);
        borderPane.setRight(null);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("selected: " + newValue.getValue());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(newValue.getValue().toString());
            clipboard.setContents(stringSelection, null);
        });
    }

    private void displayBrickfile(File selectedTEMPFile, File selectedTBLUFile, BorderPane borderPane) {

        TreeView<subEntity> treeView = new TreeView<>();
        ListView detailList = new ListView<String>();
        Map<String, Integer> nameAndIndex = new HashMap();
        subEntity rootEntity = null;
        if (!this.tabs.containsKey(selectedTEMPFile.getName() + "/" + selectedTBLUFile.getName())) {
            TBLU decodedTBLUfile = decodeTbluFile(selectedTBLUFile);
            STemplateEntityFactory decodedTEMPfile = decodeTempFile(selectedTEMPFile);

            //fill array with required names
            ArrayList<String> subEntityNames = new ArrayList<>(decodedTBLUfile.getBlock0().size());
            decodedTBLUfile.getBlock0().forEach(s -> {
                subEntityNames.add(s.getName());
            });

            if (decodedTBLUfile.getBlock0().size() == decodedTEMPfile.getSubEntities().size()) {

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
                    treeView.setRoot(populateTreeView(rootEntity, 0, new TreeItem<subEntity>(rootEntity)));
                }

            } else treeView = new TreeView<subEntity>(new TreeItem<subEntity>(new subEntity("these files do not form a brick together!")));

            detailList.setMinWidth(350);
            detailList.setMaxWidth(350);

            this.tabs.put(selectedTEMPFile.getName() + "/" + selectedTBLUFile.getName(), new AbstractMap.SimpleEntry(treeView, detailList));
        } else
            treeView = (TreeView<subEntity>) this.tabs.get(selectedTEMPFile.getName() + "/" + selectedTBLUFile.getName());
        borderPane.setCenter(treeView);
        borderPane.setRight(detailList);


        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("selected: " + newValue.getValue());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(newValue.getValue().getName());
            clipboard.setContents(stringSelection, null);

            detailList.getItems().add(newValue.getValue().getHash());
        });
    }

    private STemplateEntityFactory decodeTempFile(File selectedFile) {

        TEMPDecoder tempDecoder = new TEMPDecoder();
        STemplateEntityFactory TEMPfile = null;
        try {
            TEMPfile = tempDecoder.decode(selectedFile, hitmanEdition);
            System.out.println("TEMP: " + TEMPfile.getSubEntities().size());
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
            System.out.println("TBLU: " + TBLUfile.getBlock0().size());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return TBLUfile;
    }

    public void setDetailList(int Entityindex, STemplateEntityFactory decodedTEMPfile, ListView listView){

    }

    public File getFile(String fileType) {
        Stage selectFile = new Stage();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = null;
        if (!this.TBLUfolderPATH.isEmpty()) {
            File initialDir = new File(TBLUfolderPATH);
            if (initialDir.exists()) fileChooser.setInitialDirectory(initialDir);
        }
        switch (fileType) {
            case "TBLU":
                fileChooser.setTitle("Select a TBLU file");
                FileChooser.ExtensionFilter filterTBLU = new FileChooser.ExtensionFilter("TBLU files", "*.TBLU");
                fileChooser.getExtensionFilters().add(filterTBLU);
                fileChooser.setSelectedExtensionFilter(filterTBLU);
                selectedFile = fileChooser.showOpenDialog(selectFile);
                System.out.println("Selected file: " + selectedFile);
                break;
            case "TEMP":
                fileChooser.setTitle("Select a TEMP file");
                FileChooser.ExtensionFilter filterTEMP = new FileChooser.ExtensionFilter("TEMP files", "*.TEMP");
                fileChooser.getExtensionFilters().add(filterTEMP);
                fileChooser.setSelectedExtensionFilter(filterTEMP);
                selectedFile = fileChooser.showOpenDialog(selectFile);
                System.out.println("Selected file: " + selectedFile);
                break;
        }
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

    private HBox buildBar(Stage stage, TabPane tabPane, BorderPane borderPane , BorderPane mainpane) {
        String menuItemStyle = "-fx-font-size: 20; -fx-text-fill: #ec625f;";

        return new HBox(
                CreateMenuBar(menuItemStyle, tabPane, borderPane, mainpane),
                buildSpacer(),
                createOptionsBar(stage, menuItemStyle, true)
        );
    }

    public static Node buildSpacer() {
        Region spacer = new Region();
        //sets the spacer size to fit the entire bar
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        spacer.getStyleClass().add("menu-bar");
        return spacer;
    }

    private Node CreateMenuBar(String menuItemStyle, TabPane tabPane, BorderPane borderpane, BorderPane mainPane) {
        MenuBar menuBar = new MenuBar();

        //Menu items
        Menu addMenu = new Menu("import");
        addMenu.setStyle(menuItemStyle);

        MenuItem addTBLU = new MenuItem("add TBLU");
        MenuItem addTEMP = new MenuItem("add TEMP");
        MenuItem addBrick = new MenuItem("add Brick");

        addMenu.getItems().addAll(addTBLU, addTEMP, addBrick);
        Menu exportMenu = new Menu("export");
        exportMenu.setStyle(menuItemStyle);
        makeClickable(exportMenu);

        addTBLU.setOnAction(e -> {
            File selectedFile = getFile("TBLU");
            Tab tab = new Tab(selectedFile.getName());
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            displayTBLUfile(selectedFile, mainPane);
        });

        addTEMP.setOnAction(e -> {
            File selectedFile = getFile("TEMP");
            Tab tab = new Tab(selectedFile.getName());
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            displayTEMPfile(selectedFile, mainPane);
        });

        addBrick.setOnAction(e -> {
            File selectedTEMPFile = getFile("TEMP");
            File selectedTBLUFile = getFile("TBLU");
            Tab tab = new Tab(selectedTEMPFile.getName() + "/" + selectedTBLUFile.getName());
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            displayBrickfile(selectedTEMPFile, selectedTBLUFile, mainPane);
        });

        exportMenu.setOnAction(e -> {
            if (mainPane.getCenter() != null) {
                if (mainPane.getCenter() instanceof TreeView) {
                    exportTreeView((TreeView) mainPane.getCenter());
                }
            }
        });

        //add menu's to buildBar
        menuBar.getMenus().addAll(addMenu, exportMenu);

        return menuBar;
    }

    public static void createDragFunction(Stage stage, Node node) {
        node.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    public Node createOptionsBar(Stage stage, String menuItemStyle, boolean closeApplication) {
        MenuBar optionsBar = new MenuBar();

        //close button
        Menu closeMenu = buildGUIoption(new Menu(), "/Logic/resources/images/closeButton.png");
        closeMenu.setStyle(menuItemStyle);

        if (closeApplication) {
            //fullscreen button
            Menu fullScreenMenu = buildGUIoption(new Menu(), "/Logic/resources/images/fullscreenButton.png");
            fullScreenMenu.setStyle(menuItemStyle);

            //minimize button
            Menu minimizeMenu = buildGUIoption(new Menu(), "/Logic/resources/images/minimizeButton.png");
            minimizeMenu.setStyle(menuItemStyle);
            optionsBar.getMenus().addAll(minimizeMenu, fullScreenMenu, closeMenu);

            fullScreenMenu.setOnAction(e -> {
                if (stage.isFullScreen()) {
                    System.out.println("GUI set to non-fullscreen");
                    stage.setFullScreen(false);
                } else {
                    System.out.println("GUI set to fullscreen");
                    stage.setFullScreenExitHint("");
                    stage.setFullScreen(true);
                }

            });

            minimizeMenu.setOnAction(e -> {
                stage.setIconified(true);
            });
        } else
            optionsBar.getMenus().addAll(closeMenu);
        closeMenu.setOnAction(e -> {
            if (closeApplication) {
                Platform.exit();
            } else stage.close();
        });


        return optionsBar;
    }

    public Menu buildGUIoption(Menu menu, String imagePath) {
        ImageView minimizeView = new ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath)));
        minimizeView.setFitWidth(15);
        minimizeView.setFitHeight(15);
        menu.setGraphic(minimizeView);
        makeClickable(menu);
        return menu;
    }

    public static void makeClickable(Menu menu) {
        menu.getItems().add(new MenuItem(""));

        int numberOfMenuItems = menu.getItems().size();
        if (numberOfMenuItems == 1) {
            menu.showingProperty().addListener(
                    (observableValue, oldValue, newValue) -> {
                        if (newValue) {
                            menu.getItems().get(0).fire();
                        }
                    }
            );
        }
    }

    public void exportTreeView(TreeView tree) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("tree file", "*.txt"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null)
            return;
        if (file.exists()) clearFile(file);
        try {
            PrintWriter pWriter = new PrintWriter(file);
            TreeViewToTextFile(tree, 0, pWriter, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void TreeViewToTextFile(TreeView tree, int depth, PrintWriter fwriter, boolean printedRoot) {

        ObservableList<TreeItem<String>> files = tree.getRoot().getChildren();
        String line = "";

        if (!printedRoot) {
            System.out.println(tree.getRoot().getValue());
            fwriter.println(tree.getRoot().getValue());
        }
        for (TreeItem<String> f : files) {
            line = "";
            for (int i = 0; i < depth; i++) line += "    ";
            line += "|----";
            line += (f.getValue() + '\n');
            fwriter.print(line);
            System.out.print(line);
            if (f.getChildren().size() > 0) {
                TreeView<String> subTreeView = new TreeView<>(f);
                subTreeView.setRoot(f);
                TreeViewToTextFile(subTreeView, depth + 1, fwriter, true);
            }
        }
        if (depth == 0) fwriter.close();
    }

    public void clearFile(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

}
