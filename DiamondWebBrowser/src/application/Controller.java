package application;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class Controller implements Initializable{
	
	@FXML
	private WebView webView;
	@FXML
	private TextField textField;
	@FXML
	private WebEngine engine;
	@FXML
	private WebHistory history;
	@FXML
	private ListView<String> listView;
	
	private String homePage;
	
	private double webZoom = 1;
	
	private List<String> historyList = new LinkedList<>(Arrays.asList("History"));
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		homePage = "www.google.com";
		textField.setText(homePage);
		engine = webView.getEngine();
		
		listView.getItems().addAll(historyList);
		
		loadPage();
	}
	
	public void loadPage() {
		engine.load("http://" + textField.getText());
	}
	
	public void refreshPage() {
		engine.reload();
	}
	
	public void zoomIn() {
		webZoom += 0.25;
		webView.setZoom(webZoom);
	}
	
	public void zoomOut() {
		webZoom -= 0.25;
		webView.setZoom(webZoom);
	}
	
	public void loadHomePage() {
		textField.setText(homePage);
		engine.load("http://" + textField.getText());
	}
	
	public void displayHistory() {
		history = engine.getHistory();
		ObservableList<WebHistory.Entry> entries = history.getEntries();
		
	    historyList.clear();
	    listView.getItems().clear();
		
		for(WebHistory.Entry entry: entries) {
			//System.out.println(entry.getUrl() + " " + entry.getLastVisitedDate());
			if(!historyList.contains(entry.getUrl() + " " + entry.getLastVisitedDate())) {
				historyList.add(entry.getUrl() + " " + entry.getLastVisitedDate());
			}
		}
		listView.getItems().addAll(historyList);
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				String url = listView.getSelectionModel().getSelectedItem();
				engine.load(url.substring(0, url.indexOf(" ")));
			}
		});
		
		if(listView.visibleProperty().get() == true) {
			listView.setVisible(false);
		} else {
			listView.setVisible(true);
		}
	}
	
	public void back() {
		history = engine.getHistory();
		ObservableList<WebHistory.Entry> entries = history.getEntries();
		
		history.go(-1);
		textField.setText(entries.get(history.getCurrentIndex()).getUrl());
	}
	
	public void forward() {
		history = engine.getHistory();
		ObservableList<WebHistory.Entry> entries = history.getEntries();
		
		history.go(1);
		textField.setText(entries.get(history.getCurrentIndex()).getUrl());
	}
}
