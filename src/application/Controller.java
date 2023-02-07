package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Controller implements Initializable{

	@FXML
	private Pane pane;
	
	@FXML
	private Button PLB,PSB,RB,PB,NB;
	@FXML
	private ProgressBar SPB;
	@FXML
	private ComboBox<String> SCB;
	@FXML
	private Slider SL;
	@FXML
	private Label ML;
	
	
	
	
	private Media media;

	private MediaPlayer MP;
	
	private File Directory;
	private File[] files;
	private ArrayList<File> songs;
	private int songNumber = 1;
	private int[] speeds  = {25,50,75,100,125,150,175,200};
	private Timer timer;
	private TimerTask task;
	private boolean running; 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		songs = new ArrayList<File>();
		Directory = new File("Music");
		files = Directory.listFiles();
		if(files != null) {
			for(File f : files) {
				songs.add(f);
				System.out.println(f);
			}
		}
		
		media = new Media(songs.get(songNumber).toURI().toString());
		MP = new MediaPlayer(media);
		
		
		ML.setText(songs.get(songNumber).getName());
		
		
		for(int i = 0;i<speeds.length;i++) {
			SCB.getItems().addAll(Integer.toString(speeds[i]) + "%");
		}
		
		SCB.setOnAction(this::ChangeSpeed);
		
		SL.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				MP.setVolume(SL.getValue()*0.01);
				
			}
		});
		
	}
	public void PlayMedia() {
		BeginTimer();
		MP.setVolume(SL.getValue()*0.01);
		MP.play();
	}
	public void PauseMedia() {
		CancelTimer();
		MP.setVolume(SL.getValue()*0.01);
		MP.pause();
	}
	public void ResetMedia() {
		SPB.setProgress(0);
		MP.setVolume(SL.getValue()*0.01);
		MP.seek(Duration.seconds(0.0));
	}
	public void PreviousMedia() {
		SPB.setProgress(0);
		SCB.getSelectionModel().select(3);
		ChangeSpeed(null);
		if(songNumber > 0 ) {
			songNumber--;
			MP.stop();
			if(running) {
				CancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			MP = new MediaPlayer(media);
			ML.setText(songs.get(songNumber).getName());
			MP.play();
			BeginTimer();
			MP.setVolume(SL.getValue()*0.01);
			
		}
		else {
			songNumber = songs.size()-1;
			MP.stop();
			if(running) {
				CancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			MP = new MediaPlayer(media);
			ML.setText(songs.get(songNumber).getName());
			MP.play();
			BeginTimer();
			MP.setVolume(SL.getValue()*0.01);
		}
		
	}
	public void NextMedia() {
		SPB.setProgress(0);
		SCB.getSelectionModel().select(3);
		ChangeSpeed(null);
		if(songNumber <songs.size() - 1) {
			songNumber++;
			MP.stop();
			if(running) {
				CancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			MP = new MediaPlayer(media);
			ML.setText(songs.get(songNumber).getName());
			MP.play();
			BeginTimer();
			MP.setVolume(SL.getValue()*0.01);
		}
		else {
			songNumber = 0;
			MP.stop();
			if(running) {
				CancelTimer();
			}
			media = new Media(songs.get(songNumber).toURI().toString());
			MP = new MediaPlayer(media);
			ML.setText(songs.get(songNumber).getName());
			MP.play();
			BeginTimer();
			MP.setVolume(SL.getValue()*0.01);
		}
	}
	public void ChangeSpeed(ActionEvent e) {
	
		int index = SCB.getSelectionModel().getSelectedIndex();
		
		MP.setRate((speeds[index])*0.01);
		
		
		
	}
	public void BeginTimer() {
		timer= new Timer();
		task = new TimerTask() {
			
			
			public void run() {
				running = true;
				double current = MP.getCurrentTime().toSeconds();
				
				double end = media.getDuration().toSeconds();
				
				System.out.println(current);
				System.out.println(end);
				System.out.println(current/end);
				
				SPB.setProgress(current/end);
				if(current/end ==1) {
					CancelTimer();
				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
		
	}
	public void CancelTimer() {
		running =false;
		timer.cancel();
		
		
	}
}
