/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	Map<String, Album> albums;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	txtResult.clear();
    	Album a1 = this.cmbA1.getValue();
    	
    	if(a1 == null) {
    		txtResult.appendText("Selezionare un album valido!\n");
    		return;
    	}
    	
    	String res = this.model.getAdiacenze(a1);
    	txtResult.setText(res);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	int x;
    	try {
			x = Integer.parseInt(this.txtX.getText());
		} catch (NumberFormatException e) {
			txtResult.setText("Inserire un numero valido!\n");
			e.printStackTrace();
			return;
		}
    	
    	Album a1 = this.cmbA1.getValue();
    	
    	if(a1 == null) {
    		txtResult.appendText("Selezionare un album valido!\n");
    		return;
    	}
    	
    	Album a2 = this.cmbA1.getValue();
    	
    	if(a2 == null) {
    		txtResult.appendText("Selezionare un album valido!\n");
    		return;
    	}		
    	
    	String res = this.model.calcolaPercorso(a1, a2, x);
    	this.txtResult.setText(res);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.cmbA1.getItems().clear();
    	this.cmbA2.getItems().clear();
    	
    	String num = this.txtN.getText();
    	Integer n = null;
    	
    	try {
			n = Integer.parseInt(num);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.txtResult.appendText("Inserisci un numero valido!\n");
			return;
		}
    	
    	this.model.creaGrafo(n);
    	
    	this.txtResult.appendText("GRAFO CREATO!\n");
    	this.txtResult.appendText(this.model.getNVertex() + "\n");
    	this.txtResult.appendText(this.model.getNEdge() + "\n");
    	
    	this.albums = new TreeMap<>();
    	
    	for(Album a : this.model.getGraph().vertexSet()) {
    		if(a != null)
    			this.albums.put(a.getTitle(), a);
    	}
    	
    	this.cmbA1.getItems().addAll(albums.values());
    	this.cmbA2.getItems().addAll(albums.values());
    	
    	this.cmbA1.setDisable(false);
        this.cmbA2.setDisable(false);
        this.btnPercorso.setDisable(false);
        this.btnAdiacenze.setDisable(false);
        this.txtX.setDisable(false);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

        this.cmbA1.setDisable(true);
        this.cmbA2.setDisable(true);
        this.btnPercorso.setDisable(true);
        this.btnAdiacenze.setDisable(true);
        this.txtX.setDisable(true);
    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
