package Logic;

import Decoder.TBLU.TBLUDecoder;
import Decoder.TEMP.TEMPDecoder;
import Files.TBLU;
import Files.TEMP;
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
    private String TBLUfolderPATH;
    private boolean enableDarkmode;

    public static void main(String[] args) {

        launch(args);
    }

    public void initialize(){
        getSettings();

        //ask for file to extract
        this.selectedFile = getFile();


//        try {
//            TEMPDecoder tempDecoder = new TEMPDecoder();
//            TEMP TEMPfile = tempDecoder.decode(selectedFile);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            TBLUDecoder tbluDecoder = new TBLUDecoder();
            TBLU TBLUfile = tbluDecoder.decode(selectedFile);
            System.out.println(TBLUfile.toString());
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
        fileChooser.setTitle("Select a  file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TBLU files", "*.TBLU");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("TEMP files", "*.TEMP");
        fileChooser.getExtensionFilters().addAll(filter, filter);
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
                    if (line.contains("default TBLU path")) {
                        String[] splitLine = line.split(": ");
                        if(splitLine.length > 1) this.TBLUfolderPATH = line.split(": ")[1];
                        else this.TBLUfolderPATH = "";
                    }
                    if (line.contains("use dark-mode")) this.enableDarkmode = Boolean.parseBoolean(line.split(": ")[1]);
                }catch (NumberFormatException e){
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
