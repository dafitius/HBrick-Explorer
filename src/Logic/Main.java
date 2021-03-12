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
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;


public class Main extends Application {


    private Map<String, ListView> tabs;

    //settings
    private String TBLUfolderPATH;
    private boolean enableDarkmode;

    public static void main(String[] args) {

        launch(args);
    }

    public void initialize() {
        getSettings();
        this.tabs = new HashMap<>();
    }

    public void addFile() {
        //ask for file to extract
        File selectedFile = getFile();


        Thread decodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                TEMPDecoder tempDecoder = new TEMPDecoder();
                STemplateEntityFactory TEMPfile = null;
                try {
                    TEMPfile = tempDecoder.decode(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (STemplateFactorySubEntity subEntity : TEMPfile.getSubEntities()) {
                    for (SEntityTemplateProperty sEntityTemplateProperty : subEntity.getPropertyValues()) {
                        if (sEntityTemplateProperty.getnPropertyID().getProp().equals("m_mTransform")) {
                            m_mTransform transform = (m_mTransform) sEntityTemplateProperty.getnPropertyID();
                            //System.out.println(transform.getMatrix34().getTrans());
                        }
                    }
                }
            }
        });
        decodeThread.setName("decodeThread");
        decodeThread.start();


//        try {
//            TBLUDecoder tbluDecoder = new TBLUDecoder();
//            TBLU TBLUfile = tbluDecoder.decode(selectedFile);
//            System.out.println(TBLUfile.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void start(Stage primaryStage) {
        initialize();

        HBox hbox = new HBox();
        BorderPane borderpane = new BorderPane();

        TabPane tabPane = new TabPane();
        Tab welcomeTab = new Tab("welcome");
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
                tabPane.getTabs().add(new Tab(selectedFile.getName()));
                tabPane.getTabs().add(addTab);
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.select(tabPane.getTabs().get(tabPane.getTabs().size() - 2));
                tabPane.setSelectionModel(selectionModel);
                switch (selectedFile.getName().split("\\.")[1]) {
                    case "TEMP":
                        displayTEMPfile(selectedFile, borderpane);
                        break;
                    case "TBLU":
                        displayTBLUfile(selectedFile, borderpane);
                        break;
                }
            } else{
                borderpane.setCenter(tabs.get(selectedTab.getText()));
            }

        });


        //create a panel on the right for details

        borderpane.setCenter(hbox);
        borderpane.setTop(tabPane);
        primaryStage.setTitle("TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 850, 675));
        primaryStage.getIcons().add(new javafx.scene.image.Image(this.getClass().getResource("icon.png").toExternalForm()));
        if (this.enableDarkmode)
            primaryStage.getScene().getStylesheets().add(this.getClass().getResource("darkmode.css").toExternalForm());
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launched the app");
    }

    private void displayTEMPfile(File selectedFile, BorderPane borderPane) {
        ListView<String> list = new ListView<>();
        if(!this.tabs.containsKey(selectedFile.getName())) {
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
        }else list = this.tabs.get(selectedFile.getName());
        borderPane.setCenter(list);

    }

    private void displayTBLUfile(File selectedFile, BorderPane borderPane) {

        ListView<String> list = new ListView<>();
        if(!this.tabs.containsKey(selectedFile.getName())) {
            TBLU decodedTBLUfile = decodeTbluFile(selectedFile);

        for(subEntity subEntity : decodedTBLUfile.getBlock0()){
            list.getItems().add(subEntity.getName());
        }
            this.tabs.put(selectedFile.getName(), list);
        }else list = this.tabs.get(selectedFile.getName());
        borderPane.setCenter(list);
    }


    private STemplateEntityFactory decodeTempFile(File selectedFile) {

        TEMPDecoder tempDecoder = new TEMPDecoder();
        STemplateEntityFactory TEMPfile = null;
        try {
            TEMPfile = tempDecoder.decode(selectedFile);
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
                    if (line.contains("default TBLU path")) {
                        String[] splitLine = line.split(": ");
                        if (splitLine.length > 1) this.TBLUfolderPATH = line.split(": ")[1];
                        else this.TBLUfolderPATH = "";
                    }
                    if (line.contains("use dark-mode")) this.enableDarkmode = Boolean.parseBoolean(line.split(": ")[1]);
                } catch (NumberFormatException e) {
                    System.out.println("detected typo inside the setting.txt: \n" + e.getMessage());
                }


                line = br.readLine();
            }

            System.out.println("[SETTINGS]");
            System.out.println("default TBLU path" + this.TBLUfolderPATH);
            System.out.println("use dark-mode: " + this.enableDarkmode);

            System.out.println(" ");
        } catch (IOException e) {
            System.out.println("could not find settings.txt file");
        }
    }

}
