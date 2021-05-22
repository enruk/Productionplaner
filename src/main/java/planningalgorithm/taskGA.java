package planningalgorithm;

import javafx.concurrent.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class taskGA extends Task<List<Integer>> {

    
    int p;
    int nMa;
    int maxGen;
    int currentGen;
    SettingsGA detailedSettings;
    String path;

    taskGA(int Populationsize, int nMachines, int maxGenerations, SettingsGA Settings, String filepath){  
        p = Populationsize;             //Source: GUI
        nMa = nMachines;                //Source: GUI
        maxGen = maxGenerations;        //Source: GUI
        detailedSettings = Settings;    //Source: GUI
        path = filepath;                //Source: GUI
    }

    
    @Override 
    protected List<Integer> call() throws Exception{
        
        List<Integer> Generations = new ArrayList<>();
        
        // Genetic Algorithm
        Population P = new Population(p,nMa,maxGen,detailedSettings);
        updateMessage("Creating Population");

        // Read Data
        P.readData(path);
        updateMessage("Reading Data");


        // Create Population
        currentGen = 1;
        Generations.add(Integer.valueOf(currentGen));
        this.updateProgress(currentGen, maxGen);
        P.Individuen = new ArrayList<>(100);
        for (int i = 0; i < p; i++) {
            Individuum indi = new Individuum(i, currentGen, P.nOp, nMa);
            P.Individuen.add(indi);
        }


        // Initizise First Generation
        P.initializeFirstGen();
        updateMessage("Initialising Population");

        // Decoding First Generation
        for (int i=0;i<p;i++){
            P.Individuen.get(i).correctingAllocation(P.readProcess);
            P.Individuen.get(i).decodierung(P.precedenceMatrix,P.machineTimes);
        }
        updateMessage("Decoding");

        // Put Individuals in Temp
        P.Temp = new ArrayList<>(P.Individuen.size());
        for (int i=0;i<P.Individuen.size();i++){
            P.Temp.add(P.Individuen.get(i));
        }

        // Fitness
        P.calculateFitness();
        updateMessage("Calculating Fitness");


        // Output fist Generation
        P.createScheduleGraph();
        updateMessage("Creating Graph");

        while (currentGen < maxGen){

            // Elternselektion
            P.Parents = new ArrayList<Individuum>(p*2);
            updateMessage("Gather the Paretns");
            
            // Selecting Parents with SUS
            P.stochasticUniversalSampling();
            updateMessage("Stochastic Universal Sampling");

            // Make Pairs from Parents
            P.createPairs();
            updateMessage("Making Couples");


            //Recombination
            currentGen++; // New Generation, so count up
            String gen = String.valueOf(currentGen);
            Generations.add(Integer.valueOf(currentGen));
            updateProgress(currentGen, maxGen); //tell GUI
            updateMessage(gen);

            P.Children = new ArrayList<Individuum>(p);
            for (int i = 0; i < p; i++) {
                Individuum indi = new Individuum(i, currentGen, P.nOp, nMa);
                P.Children.add(indi);
            }
            updateMessage("Making some love <3");

            //Unifrom Recombination
            //missing so far


            //N-Point-Recombination
            P.nPointRecombination();
            updateMessage("N-Point-Recombination");

            // PMX / Kantenrekombination
            // Missing so far


            //Order Recombination
            P.orderRecombination();
            updateMessage("Order-Recombination");

            //Childmutation
            for (int i=0;i<p;i++){

                //Allocation
                //One-Bit-Mutation
                if (Boolean.TRUE.equals(detailedSettings.DoAlloBit)){
                    P.Children.get(i).einbitmutation(P.nOp,detailedSettings.MutAlloProbability);
                }

                //Swap-Mutation - currently missing



                //Sequence
                //Mixed-Muation
                if (Boolean.TRUE.equals(detailedSettings.DoSeqMix)){
                    P.Children.get(i).mixedmutation(P.nOp, detailedSettings.MutAlloProbability, 1);
                }

                //Swap-Mutation - currently missing
            }
            updateMessage("Mutating Childs");


            // Decoding Children
            for (int i=0;i<p;i++){
                P.Children.get(i).correctingAllocation(P.readProcess);
                P.Children.get(i).decodierung(P.precedenceMatrix,P.machineTimes);
            }
            updateMessage("Decoding Childs");

            // Ersetzungsstrategie
            // First Attempt: Take only the 50 best old Individuals
            Collections.sort(P.Individuen, new FitnessComparator());


            //Bring Parents and Children together
            P.Temp.clear();
            P.Temp = new ArrayList<>(p/2+p);
            for (int i=0;i<p/2;i++){
                P.Temp.add(P.Individuen.get(i));
            }

            for (int i=0;i<p;i++){
                P.Temp.add(P.Children.get(i));
            }

            // Fitness, but from Temp, need to be fixed
            P.calculateFitness();
            updateMessage("Calculating Fitness");

            // Tournament Selection
            P.tournamentSelection();
            updateMessage("Let them Fight!");

            // Sorting Temp by Wins in Tournamentselection
            Collections.sort(P.Temp, new TournamentWinsComparator());
            P.Individuen.clear();
            updateMessage("Sorting, Soting, Sorting");

            // Add the best p Individuals to Individuen
            for (int i=0;i<p;i++){
                P.Individuen.add(P.Temp.get(i));
            }
            updateMessage("Only the Best");


            // Clean up
            P.Parents.clear();
            P.Children.clear();
            P.Temp.clear();
            updateMessage("Clean up!");
        }

        Collections.sort(P.Individuen, new FitnessComparator());
        P.createScheduleGraph();
        updateMessage("Show me the Best");

        return Generations;
    }

    @Override 
    protected void succeeded(){
        super.succeeded();
        updateMessage("Done!");
    }

    @Override 
    protected void cancelled(){
        super.cancelled();
        updateMessage("Cancelled!");
    }

    @Override 
    protected void failed(){
        super.failed();
        updateMessage("Failed!");
    }

}






class FitnessComparator implements Comparator<Individuum> {
    @Override
    public int compare(Individuum a, Individuum b) {
        return a.timeFitness < b.timeFitness ? 1 : a.timeFitness == b.timeFitness ? 0 : -1;
    }
}

class TournamentWinsComparator implements Comparator<Individuum> {
    @Override
    public int compare(Individuum a, Individuum b) {
        return a.tournamentWins < b.tournamentWins ? 1 : a.tournamentWins == b.tournamentWins ? 0 : -1;
    }
}
