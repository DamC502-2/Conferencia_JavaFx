package com.example.demo;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;



public class HelloController implements Initializable {
    public TabPane tabPanelMain;
    public ProgressBar progress1;
    public ToolBar button2;
    public MenuItem menuItemTXT;
    public MenuItem itemGuardar;

    /**
     * Funcion para incrementar el valor al ProgressBarz
     * @param mouseEvent
     */
    public void increaseProgressBar(MouseEvent mouseEvent) {
        if (progress1.getProgress() == 1.0){
            progress1.setProgress(0);
        }else {
            progress1.setProgress( progress1.getProgress() + 0.25);
        }

    }

    /***
     * Función para abrir el archivo txt y mostrarlo en una pestaña nueva
     * @param actionEvent
     */

    public void openTXT(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Text", "*.txt"));
        File fOpen  = fileChooser.showOpenDialog(tabPanelMain.getScene().getWindow());

        if (fOpen == null)
            return;
        Tab newTab = new Tab();
        TextArea newTextArea = new TextArea();
        newTab.setContent(newTextArea); //introduzco el textarea dentro la pestaña



        newTab.setText(fOpen.getName());
        String rLine;
        try {
            FileReader frOpen = new FileReader(fOpen);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fOpen), StandardCharsets.UTF_8));
            while ((rLine = bReader.readLine())!= null){
                newTextArea.appendText(rLine + "\n");
            }
            tabPanelMain.getTabs().add(newTab); // se añade la nueva tespaña
            newTab.setUserData(fOpen.getAbsolutePath()); // se añade la ruta absoluta del archivo abierto a la pestaña
            System.out.println(fOpen.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /***
     * Función para guardar el txt que esta en la pestaña activa
      * @param actionEvent
     */

    public void saveTXT(ActionEvent actionEvent) {
        Tab actualTab = tabPanelMain.getSelectionModel().getSelectedItem();
        try {
            TextArea actualText = (TextArea) actualTab.getContent();
            Path ubicacion = Paths.get((String) actualTab.getUserData()); //se obtiene la ruta del archivo por medio de la pestaña actual
            Files.writeString(ubicacion, actualText.getText());
            System.out.println("Guardado correctamente");


        }catch (Exception ex){
            ex.printStackTrace();
        }






    }

    public  class Duple {
        public String fieldS;
        public int value;

        public Duple(String field1, int value) {
            this.fieldS = field1;
            this.value = value;
        }

        public String getFieldS() {
            return fieldS;
        }

        public int getValue() {
            return value;
        }


    }


    public TreeView TreeViewFiles;
    public ImageView ImgView;
    public Button button1;
    public TextField textArea1;
    public ComboBox<Duple> comboBox1;
    public CheckBox ckBox1;
    public ToggleButton tgButton1;
    @FXML
    private Label welcomeText;
    @FXML
    private MenuItem openIMenu;



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Hola Estudiants de IPC1!");
    }

    /**
     * Funcion para leer una carpeta y desplegar todos los archivos de tipo
     * .png .jpg en un TreeViewer
     * @param actionEvent
     */
    @FXML
    protected void openFolder( ActionEvent actionEvent){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(Paths.get(System.getProperty("user.dir")).toFile());
        var selectedDir = chooser.showDialog(null);
        if (selectedDir != null){
            var rootDir = selectedDir.toPath();
            try{
                var rootItem = new TreeItem(rootDir.toString());
                TreeViewFiles.setRoot(rootItem);

                Files.walk(rootDir)
                        .filter(p  -> p.toString().endsWith(".jpg")||p.toString().endsWith(".png"))

                        .forEach(dir -> {
                                rootItem.getChildren().add(new TreeItem(dir.toString()));
                        });
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

    }


    public void closeApp(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    /***
     * Funcion para  mostrar la imagen seleccionada en el TreeViewer
     * @param mouseEvent
     */
    public void loadImg(MouseEvent mouseEvent) {

        if(this.TreeViewFiles.getRoot() != this.TreeViewFiles.getSelectionModel().getSelectedItem() ){
            TreeItem<String> item = (TreeItem<String>) this.TreeViewFiles.getSelectionModel().getSelectedItem();
            if (item != null){
                File imgF = new File(item.getValue());
                Image img = new Image(imgF.toURI().toString());
                this.ImgView.setFitHeight(img.heightProperty().getValue());
                this.ImgView.setFitWidth(img.widthProperty().getValue());
                this.ImgView.setImage(img);
            }
        }
    }

    /**
     * Funcion analoga al la funcion Formload de Swing
     * Encargada de cargar de datos al DropList
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboBox1.setConverter(new StringConverter<Duple>() {
            @Override
            public String toString(Duple duple) {
                if( duple != null){
                    return "item: " + duple.fieldS + " = " + duple.value;
                }else {
                    return "";
                }
            }
            @Override
            public Duple fromString(String s) {
                return null;
            }
        });
        comboBox1.setItems(FXCollections.observableArrayList(
                new Duple("valor", 1),
                new Duple("valor2", 2),
                new Duple("valor3", 3)
        ));
        comboBox1.getSelectionModel().selectFirst();
    }

    /**
     * Función encargada de leer la información del formulario dentro del Acordeón
     * @param mouseEvent
     */
    public void readFormData(MouseEvent mouseEvent) {
        Locale.setDefault( new Locale("en", "Guatemala"));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("conferencia IPC1");
        alert.setTitle("Info");
        alert.setContentText(textArea1.getText() + "\n combo " +
                comboBox1.getSelectionModel().getSelectedItem().fieldS
         + "\n checkBox" + ckBox1.isSelected() + "\n toggle: " +
                tgButton1.isSelected());
        alert.showAndWait();


    }
}