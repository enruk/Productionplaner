package planningalgorithm;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Individuum {
    int number;
    int birthGeneration;
    int nOp;
    int nMa;

    int[] indiAllocation;                   // Allocation of this Individual
    int[] indiSequence;                     // Sequenz of this Individual
    int[][] predecessorWorkingTimes;        // Do i need this?
    int[][] startingTimeMatrix;             // Do i need this?
    float timeFitness;
    int susRank;
    int tournamentWins;

    List<Machine> indiMachines;                 // Machines of this Individual
    List<Operationen> indiProcess;              // Process of this Individual


    //Konstruktor
    Individuum(int num, int startgen, int nOp, int nMa){
        number=num;
        this.nOp = nOp;
        this.nMa = nMa;
        birthGeneration=startgen;

        indiAllocation = new int[nOp];
        indiSequence = new int[nOp];
        predecessorWorkingTimes = new int[nOp][nOp];
        startingTimeMatrix = new int[nOp][nOp+1];

        // Liste der Maschinen erstellen
        indiMachines = new ArrayList<>(nMa);
        for (int i=0;i<nMa;i++) {
            Machine newMachine = new Machine(i,0);
            indiMachines .add(newMachine);
        }

        // Liste der Prozesse erstellen
        indiProcess = new ArrayList<>(nOp);
        for (int i=0;i<nOp;i++) {
            Operationen newOperation = new Operationen(nOp,nMa);
            indiProcess.add(newOperation);
        }
    }


    // Zufallszahl 
    private double randomNumber(){
        double ranNum;
        Random zufallszahl = new Random();
        ranNum = zufallszahl.nextDouble();
        return ranNum;
    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
     }

    private int max(int[] fooArr) {
        int maximum = -100;
        for (int i=0;i<fooArr.length;i++) {
            if (fooArr[i] > maximum) {
                maximum = fooArr[i];
            }
        }
        return maximum;
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


    public int[] addOneToArray (int[] arr, int what){
        // Case 1: Array is empty
        if (arr == null){
            int[] newArr = new int[1];
            newArr[0] = what;
            return newArr;
        }
        // Case 2: Array isnt empty
        else {
            int[] newArr = new int[arr.length + 1];
            for (int i=0;i<arr.length;i++){
                newArr[i] = arr[i];
            }
            newArr[arr.length] = what;
            return newArr;
        }
    }

    public int[] oneValue(int[] barArr, int value){
        int[] oneValueArr = new int[barArr.length];
        for (int i=0;i<barArr.length;i++){
            oneValueArr[i] = value;
        }
        return oneValueArr;
    }
 
    public int[] changeValue (int[] arrArr, int oldValue, int newValue){
        int[] newArrArr = new int[arrArr.length];
        for (int i=0;i<arrArr.length;i++){
            if (arrArr[i] == oldValue){
                arrArr[i] = newValue;
            }
        }
        return newArrArr;
    }

    public int count (int[] countArr, int value){
        int countDoku = 0;
        for (int i=0;i<countArr.length;i++){
            if (countArr[i]==value){
                countDoku++;
            }
        }
        return countDoku;
    }

    public int[][] copyMatrix (int[][] arr) {
        int[][] copy = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

    void calculateStartingTimeMatrix(){
        
    }


    
    void correctingAllocation (List<Operationen> operationsList){
        // Liste der Maschinen erstellen
        for (int i=0;i<nOp;i++){
            indiProcess.get(i).availableMachines = new int[nMa];
            copyArr(operationsList.get(i).availableMachines,indiProcess.get(i).availableMachines);
        }


        for (int i=0;i<nOp;i++){
            int requestedMachine = indiAllocation[i];
            if (indiProcess.get(i).availableMachines[requestedMachine] == 0){
                // Requested Machine cant execute the operation
                // Find available Machines
                List<Integer> numbersAvailableMachines = new ArrayList<>(); 
                for (int j=0;j<nMa;j++){
                    if (indiProcess.get(j).availableMachines[j] == 1){
                        numbersAvailableMachines.add(j);
                    }
                }
                // Pick random Machine of the available machines
                double randomMachine =  round((numbersAvailableMachines.size()-1) * randomNumber(),0);
                int newMachine = (int) randomMachine;

                //Put new Machine in Allocation
                indiAllocation[i] = numbersAvailableMachines.get(newMachine);
            }
        }


    }



    // Decodierung
    void decodierung(int[][] Vorrangmatrix, int[][] Maschinenzeiten){

        // VORBEREITUNG
        //Prozesszeitenmatrix bestimmen aus Zuordnung und Maschinenzeiten und Maschinenzeit in Matrix der Vorgänger-Prozess-Zeiten eintragen
        for (int z=0;z<nOp;z++){
            indiProcess.get(z).workingMachine = indiAllocation[z]; 
            indiProcess.get(z).timeWorking = Maschinenzeiten[z][indiProcess.get(z).workingMachine];
        }
    

        // z=zeile/row
        // s=spalte/column
        for (int z=0;z<nOp;z++){
            for (int s=0;s<nOp;s++){
                if  (Vorrangmatrix[z][s]==1){
                    predecessorWorkingTimes[z][s] = indiProcess.get(s).timeWorking;
                }
                else{
                    predecessorWorkingTimes[z][s] = 0;
                }
            }
        }

        
        // Get the predecessors of every Operation and save them under Process.get(x).Predecessors
        for (int i=0;i<nOp;i++){
            copyArr(Vorrangmatrix[i],indiProcess.get(i).predecessor);
            copyArr(Vorrangmatrix[i],indiProcess.get(i).remainingPredecessors);
        }

        // Get the starting Operations
        for (int z=0;z<nOp;z++){
            if (max(predecessorWorkingTimes[z])==0){
                indiProcess.get(z).operationReadyToStart = true;
                indiProcess.get(z).operationNotReady = false;
            }
        }

        
        int operationsDone = 0;
        while (operationsDone < nOp){

            
            for (int z=0;z<nOp;z++){
                int foundOp = 0;

                // Search for starting Operation
                if (indiProcess.get(z).operationReadyToStart == true){
                    foundOp = z;
                    for (int z2=0;z2<nOp;z2++){
                        if (predecessorWorkingTimes[z2][foundOp] !=0){
                            startingTimeMatrix[z2][foundOp] = max(startingTimeMatrix[foundOp]) + predecessorWorkingTimes[z2][foundOp]; //t_Op = t_PredecessorStart(from startingTimeMatrix) + t_PredecessorProcess(from predecessorTimes)
                        }
                    }

                // Mark the found operation as done
                for (int z3=0;z3<nOp;z3++){
                    if (indiProcess.get(z3).remainingPredecessors[foundOp] == 1){
                        indiProcess.get(z3).remainingPredecessors[foundOp] = 0; // FoundOp is done and is now no longer a predecessor on which other operations have to wait for
                    }
                }

                indiProcess.get(foundOp).operationDone = true;
                }
            }

            //Calculate new  Starting Operations
            for (int z=0;z<nOp;z++){

                // Operation has no remaining Predecessor and wasnt done yet: Ready to Start
                if (max(indiProcess.get(z).remainingPredecessors)==0 && indiProcess.get(z).operationDone == false){
                    indiProcess.get(z).operationReadyToStart = true;
                    indiProcess.get(z).operationNotReady = false;
                }

                // Operation has no remaining Predecessor, but i already done
                if (max(indiProcess.get(z).remainingPredecessors)==0 && indiProcess.get(z).operationDone == true){
                    indiProcess.get(z).operationReadyToStart = false;
                    indiProcess.get(z).operationNotReady = false;
                }

                // Operation still has remaining Predecessors
                if (max(indiProcess.get(z).remainingPredecessors)!=0){
                    indiProcess.get(z).operationNotReady = true;
                }
            }


            //Abbruchbedingungen ermitteln
            operationsDone = 0;
            for (int z=0;z<nOp;z++){
                if(indiProcess.get(z).operationDone == true){
                    operationsDone++;
                }
            }
        }


        //Fertigungszeiten berechnen
        int[] timesProduction = new int[nOp];
        for (int z=0;z<nOp;z++){
            timesProduction[z] = max(startingTimeMatrix[z]) + indiProcess.get(z).timeWorking;
        }


        int[] arrayA = new int[nOp];
        int[][] arrayV = copyMatrix(Vorrangmatrix);
        int[] arrayB = new int[nOp];
        int operationDash;
        int operationDashDash;
        int operationStar;


        for (int z=0;z<nOp;z++){
            if (max(Vorrangmatrix[z]) < 0.1)
                arrayA[z] = 1;
            else{
                arrayA[z] = 0; 
            }
        }
        

        // Beginn Giffler-Thompson Algorithmus
        int gtaBreakingCondition = 0;
        while (gtaBreakingCondition < nOp){
            operationDash = 0;
            operationDashDash = 0; 
            operationStar = 0;

            // GT - Step 2.H1: Get O' (Operation with earliest Productiontime, finsihed first)
            int earliestProductiontime = 1000;
            for (int z=0;z<nOp;z++){
                if (arrayA[z] == 1 && timesProduction[z]< earliestProductiontime){
                     operationDash = z;
                     earliestProductiontime = timesProduction[z];
                }
            }

            // GT - Step 2.H2: Get B, all Operations of A on the same Machine as O'
            int currentMachine = indiAllocation[operationDash];

            for (int z=0;z<nOp;z++){
                if (arrayA[z]==1 && indiAllocation[z]==currentMachine){
                    arrayB[z]=1;
                }
                else{
                    arrayB[z] = 0;
                }
            }
            
            // GT - Step 2.H3: Get O'' (Operation of B with ealiest starting Time)
            int earliestStartingtime = 1000;
            for (int z=0;z<nOp;z++){
                if (arrayB[z]==1 && max(startingTimeMatrix[z])<earliestStartingtime){
                    operationDashDash = z;
                    earliestStartingtime = max(startingTimeMatrix[z]);
                }
            }


            // GT - Step 2.H4: Delete operations outside of the sigma window
            
            //Check if earliest starttime is smaller then occupation
            int starttime;
            if (max(startingTimeMatrix[operationDashDash]) < indiMachines.get(currentMachine).timeOccupation){
                starttime = indiMachines.get(currentMachine).timeOccupation;
            }
            else{
                starttime = max(startingTimeMatrix[operationDashDash]);
            } 
            
            double sigma = 0.5;

            // Delete Operations outside
            for (int z=0;z<nOp;z++){
                if (arrayB[z]==1 && max(startingTimeMatrix[z]) > (starttime + sigma * (timesProduction[operationDash] - starttime))){
                    arrayB[z] = 0;
                }
            }


            //GT - Step 2.H5: Get O*
            int permutation = 1000;
            for (int z=0;z<nOp;z++){
                if (arrayB[z]==1 && indiSequence[z]<permutation){
                    permutation = indiSequence[z];
                    operationStar = z;
                }
            }


            // Mark OperationStern as done
            arrayA[operationStar] = -1; // OperationStar is done, mark with -1 in A
            for (int z=0;z<nOp;z++){
                if (arrayA[z] != -1){
                    arrayV[z][operationStar] = 0; // OperationStar is no longer predecessor of other operations
                }
            }
            arrayV[operationStar] = oneValue(arrayV[operationStar], -1); // OperationStar is done, mark with -1 in V

            // Set the starting time of operationStar
            if (max(startingTimeMatrix[operationStar]) < indiMachines.get(currentMachine).timeOccupation){
                indiProcess.get(operationStar).timeStart = indiMachines.get(currentMachine).timeOccupation;
            }
            if (max(startingTimeMatrix[operationStar]) >= indiMachines.get(currentMachine).timeOccupation){
                indiProcess.get(operationStar).timeStart = max(startingTimeMatrix[operationStar]);
            }

            // Set the ending time of operationStar
            indiProcess.get(operationStar).timeEnd = indiProcess.get(operationStar).timeStart + indiProcess.get(operationStar).timeWorking;

            // Set new occupation time of the current machine
            indiMachines.get(currentMachine).timeOccupation = indiProcess.get(operationStar).timeEnd;


            // GT - Schritt 4: Add successors of O* to A
            for (int z=0;z<nOp;z++){

                if (max(arrayV[z]) == 0){
                    arrayA[z] = 1;
                }
                if (max(arrayV[z]) == -1){
                    arrayA[z] = -1;
                }
                if (max(arrayV[z]) == 1){
                    arrayA[z] = 0;
                }
            }

            //GT - Step 5: Update occupation time of the current machine to every operation which is also done on that machine
            for (int z=0;z<nOp;z++){
                if (arrayA[z] != -1 && indiAllocation[z] == currentMachine){
                    startingTimeMatrix[z][nOp] = indiProcess.get(operationStar).timeEnd;
                }
            }



            //Update startingTimeMatrix, bc of new occupation time

            //Reset startingTimeMatrix except the last column nOp which includes the occupation times
            for (int r=0;r<nOp;r++){
                for (int c=0;c<nOp;c++){
                    startingTimeMatrix[r][c] = 0;
                }
            }


            //Update startingTimeMatrix, bc of new occupation time
            // Reset the status in every operation
            for (int i=0;i<nOp;i++){
                indiProcess.get(i).operationDone = false;
                indiProcess.get(i).operationNotReady = true;
                indiProcess.get(i).operationReadyToStart = false;
            }

            // Get the predecessors of every Operation  and save them under Process.get(x).Predecessors
            for (int i=0;i<nOp;i++){
                 copyArr(indiProcess.get(i).predecessor,indiProcess.get(i).remainingPredecessors);
            }

            // Get the new starting Operations
            for (int z=0;z<nOp;z++){
                if (max(predecessorWorkingTimes[z])==0){
                    indiProcess.get(z).operationReadyToStart = true;
                    indiProcess.get(z).operationNotReady = false;
                }
            }

            
            operationsDone = 0;
            while (operationsDone < nOp){

                
                for (int z=0;z<nOp;z++){
                int foundOp = 0;

                // Search for starting Operation
                if (indiProcess.get(z).operationReadyToStart == true){
                    foundOp = z;
                    for (int z2=0;z2<nOp;z2++){
                        if (predecessorWorkingTimes[z2][foundOp] !=0){
                            startingTimeMatrix[z2][foundOp] = max(startingTimeMatrix[foundOp]) + predecessorWorkingTimes[z2][foundOp]; //t_Op = t_PredecessorStart(from startingTimeMatrix) + t_PredecessorProcess(from predecessorTimes)
                        }
                    }

                // Mark the found operation as done
                for (int z3=0;z3<nOp;z3++){
                    if (indiProcess.get(z3).remainingPredecessors[foundOp] == 1){
                        indiProcess.get(z3).remainingPredecessors[foundOp] = 0; // FoundOp is done and is now no longer a predecessor on which other operations have to wait for
                    }
                }

                indiProcess.get(foundOp).operationDone = true;
                }
            }

            //Calculate new  Starting Operations
            for (int z=0;z<nOp;z++){

                // Operation has no remaining Predecessor and wasnt done yet: Ready to Start
                if (max(indiProcess.get(z).remainingPredecessors)==0 && indiProcess.get(z).operationDone == false){
                    indiProcess.get(z).operationReadyToStart = true;
                    indiProcess.get(z).operationNotReady = false;
                }

                // Operation has no remaining Predecessor, but i already done
                if (max(indiProcess.get(z).remainingPredecessors)==0 && indiProcess.get(z).operationDone == true){
                    indiProcess.get(z).operationReadyToStart = false;
                    indiProcess.get(z).operationNotReady = false;
                }
 
                // Operation still has remaining Predecessors
                if (max(indiProcess.get(z).remainingPredecessors)!=0){
                    indiProcess.get(z).operationNotReady = true;
                }
            }


                // Get termination condition
                operationsDone = 0;
                for (int z=0;z<nOp;z++){
                    if(indiProcess.get(z).operationDone == true){
                        operationsDone++;
                    }
                }
            }


            //Calculate production times
            for (int z=0;z<nOp;z++){
                timesProduction[z] = max(startingTimeMatrix[z]) + indiProcess.get(z).timeWorking;
            }

            //Give the working Machine some Information about the Operation: Needed for the BarChart
            indiMachines.get(currentMachine).plannedOperations = addOneToArray(indiMachines.get(currentMachine).plannedOperations, operationStar);
            indiMachines.get(currentMachine).startingTimesOps = addOneToArray(indiMachines.get(currentMachine).startingTimesOps, indiProcess.get(operationStar).timeStart);
            indiMachines.get(currentMachine).endingTimesOps = addOneToArray(indiMachines.get(currentMachine).endingTimesOps, indiProcess.get(operationStar).timeEnd);

            // Abbruchbedingung bestimmen
            gtaBreakingCondition = count(arrayA, -1);
        }
    }


    // 1-Bit-Mutation
    void einbitmutation(int nOp, double mutProbability){
        
        for (int i = 0; i<nOp; i++) {
            double random = randomNumber();
            if (random<mutProbability){
                if (indiAllocation[i]==1){
                    indiAllocation[i]=0;
                } 
                else {
                    indiAllocation[i]=1;
                }
            }
        }
    }

    //Mixed Mutation
    void mixedmutation(int nOp, float mixMutProbability, int typeCoding){

        double random = randomNumber();
        if(random > mixMutProbability){
            int sectionStart = (int) round(randomNumber()*nOp,0);
            int sectionEnd = (int) round(randomNumber()*nOp,0);
            while (sectionEnd == sectionStart){
                sectionEnd = (int) round(randomNumber()*nOp,0);
            }
            if (sectionEnd < sectionStart){
                int temp = sectionStart;
                sectionStart = sectionEnd;
                sectionEnd = temp;
            }

            int[] tempArr = new int[sectionEnd-sectionStart];
            if (typeCoding == 0){
                tempArr = copyArraySection(indiAllocation, sectionStart, tempArr, 0, sectionEnd-sectionStart);
                List<Integer> tempList = new ArrayList<>();
                for (int k=0;k<tempArr.length;k++){
                    tempList.add(tempArr[k]);
                }
                Collections.shuffle(tempList);
                int[] mixedArr = tempList.stream().mapToInt(i->i).toArray();
                indiAllocation = copyArraySection(mixedArr, 0, indiAllocation, sectionStart, sectionEnd-sectionStart);
            }
            else if (typeCoding == 1){
                tempArr = copyArraySection(indiSequence, sectionStart, tempArr, 0, sectionEnd-sectionStart);
                List<Integer> tempList = new ArrayList<>();
                for (int k=0;k<tempArr.length;k++){
                    tempList.add(tempArr[k]);
                }
                Collections.shuffle(tempList);
                int[] mixedArr = tempList.stream().mapToInt(i->i).toArray();
                indiSequence = copyArraySection(mixedArr, 0, indiSequence, sectionStart, sectionEnd-sectionStart);
            }
            else{
                System.out.println("Wrong Input for Coding Type of Mixed Mutation");
            }
        }
    }


    // Swap-Mutation
    void swapmutation(int nOp, float mutProbability){

        for (int i = 0; i<nOp; i++) {
            double random = randomNumber();
            if (random<mutProbability) {
                double random2 = round(randomNumber()*(nOp-1),0);
                int randomposition = (int)random2;
                int saveNumber = indiSequence[randomposition];
                indiSequence[randomposition] = indiSequence[i];
                indiSequence[i] = saveNumber;
            }
        }
    }

}