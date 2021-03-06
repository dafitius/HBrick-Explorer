package Logic;

import Decoder.TBLU.TBLUDecoder;
import Files.TBLU;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

import static javax.swing.JOptionPane.showMessageDialog;


public class Main extends Application {

    File selectedFile;


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

        //ask for file to extract
        this.selectedFile = getFile();


        try {
            TBLUDecoder tbluDecoder = new TBLUDecoder();
            TBLU TBLUfile = tbluDecoder.decode(selectedFile);
            System.out.println(TBLUfile.printHeader());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage){
        initialize();

        //create a panel on the right for details
        HBox hbox = new HBox();
        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(hbox);
        primaryStage.setTitle("TBLU tree viewer");
        primaryStage.setScene(new Scene(borderpane, 850, 675));
        primaryStage.getIcons().add(new javafx.scene.image.Image(this.getClass().getResource("icon.png").toExternalForm()));
        if(this.enableDarkmode) primaryStage.getScene().getStylesheets().add(this.getClass().getResource("darkmode.css").toExternalForm());
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        System.out.println("launched the app");
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
