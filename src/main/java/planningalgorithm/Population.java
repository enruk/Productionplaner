package planningalgorithm;

import java.util.List;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;


public class Population {
    int p;                              // From GUI
    int nMa;                            // From GUI
    SettingsGA detailedSettingsGA;      // From GUI

    int maxGen;                         // From GUI
    int currentgen;

    int nOp;                            // From ProcessRead Excel
    List<Operationen> readProcess;     // From ProcessRead Excel
    int[][] precedenceMatrix;           // From ProcessRead Excel
    int[][] machineTimes;               // From ProcessRead Excel

    int[] PairingSorted;
    int[] PairingRandom;

    List<Individuum> Individuen;        //Current Population
    List<Individuum> Parents;           //Choosen Parents from Current Population
    List<Individuum> Children;          //New made Individuals
    List<Individuum> Temp;


    Population(int Populationsize, int numberOfMachines, int maxGenerations, SettingsGA SettingsGA){
        p = Populationsize;
        nMa = numberOfMachines;
        maxGen = maxGenerations;
        detailedSettingsGA = SettingsGA;
    }


    public static double randomNumber() {
        double RanNum;
        Random zufallszahl = new Random();
        RanNum = zufallszahl.nextDouble();
        return RanNum;
    }

    public static double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }

    public void copyArr (int[] arr, int[] dest){
        for (int i=0;i<arr.length;i++){
            dest[i] = arr[i];
        }
    }

    public int[] copyArraySection (int[] originArr, int startPointerArr, int[] destArr, int startPointerDest, int lenghtCopy){
        for (int i=0;i<lenghtCopy;i++){
            destArr[startPointerDest+i] = originArr[startPointerArr+i];
        }
        return destArr;
    } 

    public int[][] copyArray (int[][] Arr) {
        int[][] Copy = new int[Arr.length][Arr[0].length];
        for (int i = 0; i < Arr.length; i++) {
            for (int j = 0; j < Arr[0].length; j++) {
                Copy[i][j] = Arr[i][j];
            }
        }
        return Copy;
    }

    private int max(int[] FooArr) {
        int maximum = -100;
        for (int i=0;i<FooArr.length;i++) {
            if (FooArr[i] > maximum) {
                maximum = FooArr[i];
            }
        }
        return maximum;
    }

    private int min(int[] FooArr){
        int minimum = 10000;
        for (int i=0;i<FooArr.length;i++) {
            if (FooArr[i] < minimum) {
                minimum = FooArr[i];
            }
        }
        return minimum;
    }

    public void readData (String filepath){
        // EXCELDATEI AUSLESEN

        ProcessList ProzessRead = new ProcessList();
        try {
            ProzessRead.ReadoutExcel(nMa,filepath);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        readProcess = ProzessRead.processInformation;
        nOp = readProcess.size();

        precedenceMatrix = copyArray(ProzessRead.Präzedenzmatrix);
        machineTimes = copyArray(ProzessRead.machineMatrix);
    }

    public void initializeFirstGen(){

        // Zufällig Zuordnung in alle Individuen befüllen
        for (int i = 0; i < p; i++) {
            int[] RandomAllocation = new int[nOp];
            double RandomMachine;
            for (int j = 0; j < nOp; j++) {
                RandomMachine = round(randomNumber() * (nMa - 1), 0);
                int Machine = (int) RandomMachine;
                RandomAllocation[j] = Machine;
            }
            copyArr(RandomAllocation,Individuen.get(i).indiAllocation);
        }

        // Zufällig Sequenz in alle Individuen befüllen
        int[] PermuSortiert = new int[nOp];
        for (int j = 0; j < nOp; j++) {
            PermuSortiert[j] = j + 1;
        }

        for (int i = 0; i < p; i++) {
            int[] RandomSequenz = new int[nOp];
            copyArr(PermuSortiert,RandomSequenz);
            for (int j = 0; j < nOp; j++) {
                int randomposition = (int) round(randomNumber() * (nOp - 1), 0);
                int SaveNum = RandomSequenz[j];
                RandomSequenz[j] = RandomSequenz[randomposition];
                RandomSequenz[randomposition] = SaveNum;
            }
            copyArr(RandomSequenz,Individuen.get(i).indiSequence);
        }
    }


    public void calculateFitness(){
        
        // Erste Bewertung
    

        // Rangbasierte Fitness
        int nRank = detailedSettingsGA.nRanks;
        float HeighestRankFitness = detailedSettingsGA.RankedFitness;

        float[] RankedFitness = new float[nRank];

        for (int r=1;r<nRank+1;r++){
            RankedFitness[r-1] = (2-HeighestRankFitness) + (HeighestRankFitness - (2-HeighestRankFitness))*(r-1)/(nRank-1);
        }

        int[] FinishingTimes = new int[Temp.size()];
        for (int i=0;i<Temp.size();i++){
            int maxValue = 0;
            int tempValue = 0;
            for (int j=0;j<nOp;j++){
                tempValue = Temp.get(i).indiProcess.get(j).timeEnd;
                if(tempValue > maxValue){
                    maxValue = tempValue;
                }
            }
            FinishingTimes[i] = maxValue;
        }

        int MaxFinishingTimes = max(FinishingTimes);
        int MinFinishingTimes = min(FinishingTimes);
        
        int Range = MaxFinishingTimes - MinFinishingTimes;
        int RangeEachRank = Range / nRank;

        for (int r=1;r<nRank+1;r++){
            for (int i=0;i<Temp.size();i++){
                if (FinishingTimes[i] <= MaxFinishingTimes - (r-1)*RangeEachRank){
                    Temp.get(i).susRank = r; //eigentlich jetzt unnötig
                    Temp.get(i).timeFitness = RankedFitness[r-1];
                }
            }
        }

    }


    public void createScheduleGraph (){
        EventQueue.invokeLater(new Runnable(){

            @Override 
            public void run(){
                Schedule iniSchedule = new Schedule(nMa,Individuen.get(0).indiMachines); 
                iniSchedule.toFront();
                iniSchedule.repaint();
            }
        });
    }

    public void stochasticUniversalSampling(){
        //SUS Verfahren
        float SumFitness=0;
        for (int i=0;i<p;i++){
            SumFitness = SumFitness + Individuen.get(i).timeFitness;
        }

        float PointerRange = SumFitness / (2*p);
        float startPointer = PointerRange * (float)randomNumber();

        float[] PointerArray = new float[2*p];
        for (int i=0;i<2*p;i++){
            PointerArray[i] = startPointer + (i)*PointerRange;
        }

        float[] Wheel = new float[p+1];
        Wheel[0] = 0;

        for (int i=1;i<=p;i++){
            Wheel[i] = Individuen.get(i-1).timeFitness + Wheel[i-1];
        }

        float TopBorder;
        float BottomBorder;
        int DeletedPointers = 0;
        int foundPointer = 0;

        for (int w=1;w<=p;w++){
            // Count Wheelsections
            TopBorder = Wheel[w];
            BottomBorder = Wheel[w-1];
            DeletedPointers = DeletedPointers + foundPointer;
            foundPointer = 0;
            for (int i=DeletedPointers;i<p*2;i++){
                // Count Pointer
                // Check if Pointer in Wheelsection
                if(PointerArray[i] <= TopBorder && PointerArray[i] > BottomBorder){
                    foundPointer = foundPointer + 1;
                    Parents.add(Individuen.get(w-1));
                }
                if(PointerArray[i] > TopBorder){
                    break;
                }
            }
            continue;   // Kontrollieren!!!
        }
    }



    public void createPairs(){

        //Pairing
        PairingSorted = new int[p*2];
        for (int j = 0; j < p*2; j++) {
            PairingSorted[j] = j;
        }

        PairingRandom = new int[p*2];
        for (int i = 0; i < p*2; i++) {
            copyArr(PairingSorted,PairingRandom);
            for (int j = 0; j < p*2; j++) {
                int randomposition = (int) round(randomNumber() * (p*2 - 1), 0);
                int SaveNum = PairingRandom[j];
                PairingRandom[j] = PairingRandom[randomposition];
                PairingRandom[randomposition] = SaveNum;
            }
        }
    }



    public void nPointRecombination(){

        // N-Punkt-Rekombination
        if (Boolean.TRUE.equals(detailedSettingsGA.DoRecNPoint)){

            int RecN = 2; //Nicht aus GUI, besser berechnen aus nOp

            for (int i=0;i<p;i++){

                // Creating new Arrays
                int[] Allocation1 = new int[nOp];
                int[] Allocation2 = new int[nOp];
                copyArr(Parents.get(PairingRandom[i * 2]).indiAllocation,Allocation1 );
                copyArr(Parents.get(PairingRandom[i * 2+1]).indiAllocation,Allocation2);

                int[] Child = new int[nOp];

                
                // Making the Cuts
                int[] Cuts = new int[RecN+2];
                Cuts[0] = 0;

                for (int N=1;N<RecN+1;N++){
                    Cuts[N] = (int) round(randomNumber()*nOp,0);
                    if (N>1){
                        for (int j=0;j<N;j++){
                            while(Cuts[N] == Cuts[j] || Cuts[N]-Cuts[j]==1 || Cuts[N]-Cuts[j]==-1){
                                Cuts[N] = (int) round(randomNumber()*nOp,0);
                            }
                        }
                    }
                }
                Cuts[RecN+1] = nOp-1;
                Arrays.sort(Cuts);

                // Filling the Child
                for (int N=0;N<RecN+1;N++){
                    if((N%2)==0){
                        Child = copyArraySection(Allocation1, Cuts[N], Child, Cuts[N], Cuts[N+1]-Cuts[N]);
                        if(N==RecN){
                            Child[nOp-1] = Allocation1[nOp-1];
                        }
                    }
                    else{
                        Child = copyArraySection(Allocation1, Cuts[N], Child, Cuts[N], Cuts[N+1]-Cuts[N]);
                        if(N==RecN){
                            Child[nOp-1] = Allocation2[nOp-1];
                        }
                    }
                }
                Children.get(i).indiAllocation = copyArraySection(Child,0,Children.get(i).indiAllocation,0,nOp);
            }
        }
    }



    public void orderRecombination(){

        // Ordnungsrekombination
        if (Boolean.TRUE.equals(detailedSettingsGA.DoRecOrder)){

            for (int i=0;i<p;i++){
                int[] Sequence1 = new int[nOp];
                int[] Sequence2 = new int[nOp];
                Sequence1 = copyArraySection(Parents.get(PairingRandom[i * 2]).indiSequence, 0, Sequence1, 0, nOp);
                Sequence2 = copyArraySection(Parents.get(PairingRandom[i * 2+1]).indiSequence, 0, Sequence2, 0, nOp);


                // Get one section
                int sectionStart;
                int sectionEnd;

                sectionStart = (int) round(randomNumber()*nOp,0);
                sectionEnd = (int) round(randomNumber()*nOp,0);
                while (sectionEnd == sectionStart){
                    sectionEnd = (int) round(randomNumber()*nOp,0);
                }
                if (sectionEnd < sectionStart){
                    int temp = sectionStart;
                    sectionStart = sectionEnd;
                    sectionEnd = temp;
                }

                int[] Child = new int[nOp];

                Child = copyArraySection(Sequence1, sectionStart, Child, sectionStart, sectionEnd-sectionStart);

                for (int k=0;k<nOp;k++){
                    int value = Sequence2[k];
                    boolean result = IntStream.of(Child).anyMatch(x -> x == value);
                    if (!result){
                        Child[k] = value;
                    }
                }
                Children.get(i).indiSequence = copyArraySection(Child,0,Children.get(i).indiSequence,0,nOp);
            }

        }

    }



    public void tournamentSelection(){

        // Umweltselektion
        for (int q=0;q<detailedSettingsGA.QTournaments;q++){
            Collections.shuffle(Temp);
            for (int m=0;m<Temp.size()/2;m++){
                if (Temp.get(m*2).timeFitness>=Temp.get(m*2+1).timeFitness){
                    Temp.get(m*2).tournamentWins++;
                }
                else{
                    Temp.get(m*2+1).tournamentWins++;
                }
            }
        }

    }
}