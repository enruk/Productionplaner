package planningalgorithm;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class gui extends Application {

    List<Machine> Res;
    int AnzMa;
    int p;
    int maxGen;
    int currentgen;
    StringProperty outputtest = new SimpleStringProperty();

    String filepath;
    Scene menu;
    Scene settings;
    Scene error;

    public static int[] AddToArray(int[] Arr, int what, int n) {
        int[] NewArr = new int[Arr.length + n];
        for (int i = 0; i < Arr.length; i++) {
            NewArr[i] = Arr[i];
        }
        for (int j = Arr.length + 1; j < Arr.length + n; j++) {
            NewArr[j] = what;
        }
        return NewArr;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Genetic Algorithm for FJSSPs");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);

        Font titleFont = new Font("Arial", 32);
        Font captionFont = new Font("Arial", 20);

        // SETTINGS
        BorderPane SettingsLayout = new BorderPane();
        SettingsLayout.setPadding(new Insets(20, 20, 20, 20));

        // Top
        Label TitleSettings = new Label("Settings genetic algorithm");
        TitleSettings.setFont(titleFont);
        SettingsLayout.setTop(TitleSettings);

        // RIGHT
        VBox VBoxRightSet = new VBox(20);
        VBoxRightSet.setPadding(new Insets(50, 0, 10, 0));

        Label TitleSelection = new Label("Selection");
        TitleSelection.setFont(captionFont);

        HBox SetQTour = new HBox(20);
        Label TitleSetQTour = new Label("Q - Tournaments");
        TitleSetQTour.setMinWidth(200);
        TextField QTour = new TextField("3");
        QTour.setMaxWidth(100);
        SetQTour.getChildren().addAll(TitleSetQTour, QTour);

        HBox SetSelReplace = new HBox(20);
        Label TitleSetSelReplace = new Label("Parents for selection[%]");
        TitleSetSelReplace.setMinWidth(200);
        TextField SelReplace = new TextField("50");
        SelReplace.setMaxWidth(100);
        SetSelReplace.getChildren().addAll(TitleSetSelReplace, SelReplace);

        Label TitleFitness = new Label("Fitness");
        TitleFitness.setFont(captionFont);

        HBox SetRanks = new HBox(20);
        Label TitleSetRanks = new Label("Number of Ranks");
        TitleSetRanks.setMinWidth(200);
        TextField Ranks = new TextField("5");
        Ranks.setMaxWidth(100);
        SetRanks.getChildren().addAll(TitleSetRanks, Ranks);

        HBox SetRankedFit = new HBox(20);
        Label TitleSetRankedFit = new Label("Ranked Fitness for heighest Rank");
        TitleSetRankedFit.setMinWidth(200);
        TextField RankedFit = new TextField("1.8");
        RankedFit.setMaxWidth(100);
        SetRankedFit.getChildren().addAll(TitleSetRankedFit, RankedFit);

        HBox Set5 = new HBox(20);
        Label TitleSet5 = new Label("Eingabe 5");
        TitleSet5.setMinWidth(200);
        TextField Setting5 = new TextField();
        Setting5.setMaxWidth(100);
        Set5.getChildren().addAll(TitleSet5, Setting5);

        Label EmptyRow4 = new Label("");

        VBoxRightSet.getChildren().addAll(TitleSelection, SetQTour, SetSelReplace, EmptyRow4, TitleFitness, SetRanks,
                SetRankedFit);
        SettingsLayout.setRight(VBoxRightSet);

        // CENTER
        VBox VBoxCenterSet = new VBox(20);
        VBoxCenterSet.setPadding(new Insets(50, 0, 10, 0));

        Label TitleMutationAllo = new Label("Mutation Allocation");
        TitleMutationAllo.setFont(captionFont);

        HBox CheckMut1 = new HBox(20);
        Label TitleCheck1 = new Label("Mutation Type");
        TitleCheck1.setMinWidth(200);
        CheckBox mutBoxAlloBit = new CheckBox();
        mutBoxAlloBit.setText("One-Bit-Mutation");
        mutBoxAlloBit.setSelected(true);
        CheckBox mutBoxAlloSwap = new CheckBox();
        mutBoxAlloSwap.setText("Swap-Mutation");
        mutBoxAlloSwap.setSelected(false);
        CheckMut1.getChildren().addAll(TitleCheck1, mutBoxAlloBit, mutBoxAlloSwap);

        mutBoxAlloBit.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                mutBoxAlloSwap.setSelected(oldValue);
            }
        });

        mutBoxAlloSwap.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                mutBoxAlloBit.setSelected(oldValue);
            }
        });

        HBox SetMutAllo = new HBox(20);
        Label TitleSetMutAllo = new Label("Mutation probability allocation:");
        TitleSetMutAllo.setMinWidth(200);
        TextField MutAlloProb = new TextField("0.2");
        MutAlloProb.setMaxWidth(100);
        SetMutAllo.getChildren().addAll(TitleSetMutAllo, MutAlloProb);

        Label TitleMutationSeq = new Label("Mutation Sequence");
        TitleMutationSeq.setFont(captionFont);

        HBox CheckMut2 = new HBox(20);
        Label TitleCheckMut2 = new Label("Mutation Type");
        TitleCheckMut2.setMinWidth(200);
        CheckBox mutBoxSeqMix = new CheckBox();
        mutBoxSeqMix.setText("Mixed - Mutation");
        mutBoxSeqMix.setSelected(true);
        CheckBox mutBoxSeqSwap = new CheckBox();
        mutBoxSeqSwap.setText("Swap - Mutation");
        mutBoxSeqSwap.setSelected(false);
        CheckMut2.getChildren().addAll(TitleCheckMut2, mutBoxSeqMix, mutBoxSeqSwap);

        mutBoxSeqMix.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                mutBoxSeqSwap.setSelected(oldValue);
            }
        });

        mutBoxSeqSwap.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                mutBoxSeqMix.setSelected(oldValue);
            }
        });

        HBox SetMutSeq = new HBox(20);
        Label TitleSetMutSeq = new Label("Mutation probability sequence:");
        TitleSetMutSeq.setMinWidth(200);
        TextField MutSeqProb = new TextField("0.2");
        MutSeqProb.setMaxWidth(100);
        SetMutSeq.getChildren().addAll(TitleSetMutSeq, MutSeqProb);

        Label TitleRecombinationAllo = new Label("Recombination Allocation");
        TitleRecombinationAllo.setFont(captionFont);

        HBox CheckRec1 = new HBox(20);
        Label TitleCheckRec1 = new Label("Recombination Type");
        TitleCheckRec1.setMinWidth(200);
        CheckBox recBoxAlloNPoint = new CheckBox();
        recBoxAlloNPoint.setText("N - Point - Recombination");
        recBoxAlloNPoint.setSelected(true);
        CheckBox recBoxAlloUnif = new CheckBox();
        recBoxAlloUnif.setText("Uniform - Recombination (Not ready yet");
        recBoxAlloUnif.setSelected(false);
        CheckRec1.getChildren().addAll(TitleCheckRec1, recBoxAlloNPoint, recBoxAlloUnif);

        recBoxAlloNPoint.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                recBoxAlloUnif.setSelected(oldValue);
            }
        });

        recBoxAlloUnif.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                recBoxAlloNPoint.setSelected(oldValue);
            }
        });

        HBox SetRecAllo = new HBox(20);
        Label TitleSetRecAllo = new Label("Crossover probability allocation:");
        TitleSetRecAllo.setMinWidth(200);
        TextField RecAlloProb = new TextField("1.0");
        RecAlloProb.setMaxWidth(100);
        SetRecAllo.getChildren().addAll(TitleSetRecAllo, RecAlloProb);

        HBox RecN = new HBox(20);
        Label TitleSetRecN = new Label("N (N-Point-Crossover):");
        TitleSetRecN.setMinWidth(200);
        TextField SetRecN = new TextField("2");
        SetRecN.setMaxWidth(100);
        RecN.getChildren().addAll(TitleSetRecN, SetRecN);

        Label TitleRecombinationSeq = new Label("Recombination Sequence");
        TitleRecombinationSeq.setFont(captionFont);

        HBox CheckRec2 = new HBox(20);
        Label TitleCheckRec2 = new Label("Recombination Type");
        TitleCheckRec2.setMinWidth(200);
        CheckBox recBoxSeqOrder = new CheckBox();
        recBoxSeqOrder.setText("Order - Recombination");
        recBoxSeqOrder.setSelected(true);
        CheckBox recBoxSeqPMX = new CheckBox();
        recBoxSeqPMX.setText("PMX - Recombination (Not ready yet)");
        recBoxSeqPMX.setSelected(false);
        CheckRec2.getChildren().addAll(TitleCheckRec2, recBoxSeqOrder, recBoxSeqPMX);

        recBoxSeqOrder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                recBoxSeqPMX.setSelected(oldValue);
            }
        });

        recBoxSeqPMX.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                recBoxSeqOrder.setSelected(oldValue);
            }
        });

        HBox SetRecSeq = new HBox(20);
        Label TitleSetRecSeq = new Label("Crossover probability Sequence:");
        TitleSetRecSeq.setMinWidth(200);
        TextField RecSeqProb = new TextField("1.0");
        RecSeqProb.setMaxWidth(100);
        SetRecSeq.getChildren().addAll(TitleSetRecSeq, RecSeqProb);

        VBoxCenterSet.getChildren().addAll(TitleMutationAllo, CheckMut1, SetMutAllo, TitleMutationSeq, CheckMut2,
                SetMutSeq, TitleRecombinationAllo, CheckRec1, SetRecAllo, RecN, TitleRecombinationSeq, CheckRec2,
                SetRecSeq);
        SettingsLayout.setCenter(VBoxCenterSet);

        // BOTTOM
        HBox HBoxBottom2 = new HBox(20);
        HBoxBottom2.setAlignment(Pos.TOP_RIGHT);

        Button Backbutton = new Button("Back");
        Backbutton.setOnAction(e -> primaryStage.setScene(menu));
        HBoxBottom2.getChildren().add(Backbutton);

        SettingsLayout.setBottom(HBoxBottom2);

        // SET SCENE
        settings = new Scene(SettingsLayout, 1200, 700);

        // MENU
        BorderPane MainLayout = new BorderPane();
        MainLayout.setPadding(new Insets(20, 20, 20, 20));

        // TOP
        VBox VBoxTop = new VBox(20);

        Label TitleTop = new Label("File input");
        TitleTop.setFont(titleFont);
        HBox HBoxTop = new HBox(20);

        TextField ProcessInput = new TextField("C:/Users/Documents/ExampleProcess.xls");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select process (.xls only)");
        Button ButtonChooseProcess = new Button("Search");

        ButtonChooseProcess.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            filepath = file.getAbsolutePath();
            ProcessInput.clear();
            ProcessInput.setText(filepath);
        });

        HBoxTop.getChildren().addAll(ButtonChooseProcess, ProcessInput);
        HBox.setHgrow(ProcessInput, Priority.ALWAYS);

        VBoxTop.getChildren().addAll(TitleTop, HBoxTop);
        MainLayout.setTop(VBoxTop);

        // CENTER
        VBox VBoxCenter = new VBox(20);
        Label TitleCenter = new Label("Input of the process variables");
        TitleCenter.setFont(captionFont);
        VBoxCenter.setPadding(new Insets(50, 0, 10, 0));

        HBox NumRes = new HBox(20);
        Label NumResTitle = new Label("Number of resources");
        NumResTitle.setMinWidth(200);
        TextField NumResText = new TextField("2");
        NumResText.setMaxWidth(100);
        NumRes.getChildren().addAll(NumResTitle, NumResText);

        HBox PSize = new HBox(20);
        Label PSizeTitle = new Label("Populationsize");
        PSizeTitle.setMinWidth(200);
        TextField PSizeText = new TextField("100");
        PSizeText.setMaxWidth(100);
        PSize.getChildren().addAll(PSizeTitle, PSizeText);

        HBox maxG = new HBox(20);
        Label maxGTitle = new Label("Maximum generations");
        maxGTitle.setMinWidth(200);
        TextField maxGText = new TextField("150");
        maxGText.setMaxWidth(100);
        maxG.getChildren().addAll(maxGTitle, maxGText);

        HBox maxCalTime = new HBox(20);
        Label maxCalTimeTitle = new Label("Maximum calculation time [min]");
        maxCalTimeTitle.setMinWidth(200);
        TextField maxCalTimeText = new TextField("15");
        maxCalTimeText.setMaxWidth(100);
        maxCalTime.getChildren().addAll(maxCalTimeTitle, maxCalTimeText);

        VBoxCenter.getChildren().addAll(TitleCenter, NumRes, PSize, maxG, maxCalTime);
        MainLayout.setCenter(VBoxCenter);

        // BOTTOM
        HBox HBoxBottom = new HBox(20);
        HBoxBottom.setAlignment(Pos.TOP_RIGHT);

        Button ButtonSettings = new Button ("Detailed settings Genetic Algorithm");
        ButtonSettings.setMinSize(225, 25);
        ButtonSettings.setOnAction(e->primaryStage.setScene(settings));

        Button StartGA = new Button("Start");
        StartGA.setMinSize(60, 25);

        Label currentGen = new Label("Current Generation");
        currentGen.setMinWidth(200);

        final ProgressBar progressBar = new ProgressBar(0);
        progressBar.prefWidthProperty().bind(HBoxBottom.widthProperty().subtract(20+ButtonSettings.getWidth()+StartGA.getWidth()));
        progressBar.setMinHeight(25);
        progressBar.setProgress(0);
        final Label statusLabel = new Label();
        statusLabel.setMinWidth(250);
        statusLabel.setTextFill(Color.BLUE);

        
        

        StartGA.setOnAction(new EventHandler<ActionEvent>(){

            @Override 
            public void handle(ActionEvent event){

                StartGA.setDisable(true);
                ButtonChooseProcess.setDisable(true);
                

                // Read Main
                String pSizeStr = PSizeText.getText();
                p = Integer.parseInt(pSizeStr);
                String AnzMaStr = NumResText.getText();
                AnzMa = Integer.parseInt(AnzMaStr);
                String MaxGenStr = maxGText.getText();
                maxGen = Integer.parseInt(MaxGenStr);

                // Read Settings
                SettingsGA DetailedSettings = new SettingsGA();

                // Selection
                String QTourStr = QTour.getText();
                DetailedSettings.QTournaments = Integer.parseInt(QTourStr);
                String SelReplaceStr = SelReplace.getText();
                DetailedSettings.SelReplacement = Integer.parseInt(SelReplaceStr);

                // Fitness
                String RanksStr = Ranks.getText();
                DetailedSettings.nRanks = Integer.parseInt(RanksStr);
                String RankedFitStr = RankedFit.getText();
                DetailedSettings.RankedFitness = Float.valueOf(RankedFitStr);

                // Mutation Allocation
                DetailedSettings.DoAlloBit = mutBoxAlloBit.isSelected();
                DetailedSettings.DoAlloSwap = mutBoxAlloSwap.isSelected();
                String MutAlloProbStr = MutAlloProb.getText();
                DetailedSettings.MutAlloProbability = Float.valueOf(MutAlloProbStr);

                // Mutation Sequence
                DetailedSettings.DoSeqMix = mutBoxSeqMix.isSelected();
                DetailedSettings.DoSeqSwap = mutBoxSeqSwap.isSelected();
                String MutSeqProbStr = MutSeqProb.getText();
                DetailedSettings.MutSeqProbability = Float.valueOf(MutSeqProbStr);

                // Recombination Allocation
                DetailedSettings.DoRecNPoint = recBoxAlloNPoint.isSelected();
                DetailedSettings.DoRecUnif = recBoxAlloUnif.isSelected();
                String RecAlloProbStr = RecAlloProb.getText();
                DetailedSettings.RecAlloProbability = Float.valueOf(RecAlloProbStr);

                // Recombination Sequence
                DetailedSettings.DoRecOrder = recBoxSeqOrder.isSelected();
                DetailedSettings.DoRecPMX = recBoxSeqPMX.isSelected();
                String RecSeqProbStr = RecSeqProb.getText();
                DetailedSettings.RecSeqProbability = Float.valueOf(RecSeqProbStr);


                // Create Task
                taskGA taskGeneticAlgorithm = new taskGA(p,AnzMa,maxGen,DetailedSettings,filepath);

                //UnBind
                progressBar.progressProperty().unbind();
                statusLabel.textProperty().unbind();

                //Bind
                progressBar.progressProperty().bind(taskGeneticAlgorithm.progressProperty());
                statusLabel.textProperty().bind(taskGeneticAlgorithm.messageProperty());

                //Start
                new Thread(taskGeneticAlgorithm).start();

                taskGeneticAlgorithm.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle (WorkerStateEvent t){
                        Throwable throwable = taskGeneticAlgorithm.getException();
                        String strError = String.valueOf(throwable);

                        // Error
                        HBox errorLayout = new HBox();
                        errorLayout.setPadding(new Insets(20, 20, 20, 20));
                        Label labelError = new Label(strError);
                        currentGen.setMinWidth(200);

                        errorLayout.getChildren().addAll(labelError);
                        error = new Scene(errorLayout,100,200);

                        primaryStage.setScene(error);
                    }
                });


                //When completed Task
                taskGeneticAlgorithm.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle(WorkerStateEvent t){
                        StartGA.setDisable(false);
                        ButtonChooseProcess.setDisable(false);
                    }
                });

            }
        });

        HBox HBoxBottomMessage = new HBox(20);
        HBoxBottomMessage.setAlignment(Pos.TOP_LEFT);

        Label labelProgressMessage = new Label("Progress");
        maxCalTimeTitle.setMinWidth(200);

        HBoxBottomMessage.getChildren().addAll(labelProgressMessage,statusLabel);
        HBoxBottom.getChildren().addAll(progressBar,ButtonSettings,StartGA);

        VBox VBoxBottom = new VBox(20);
        VBoxBottom.getChildren().addAll(HBoxBottomMessage,HBoxBottom);

        MainLayout.setBottom(VBoxBottom);
        
        menu = new Scene(MainLayout,1000,500);

        primaryStage.setScene(menu);
        primaryStage.setResizable(true);
        primaryStage.show();

    }
}