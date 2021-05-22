package planningalgorithm;

public class Operationen {
    int number;
    String opName;
    int[] predecessor;
    //ArrayList<Integer> predecessor;
    int[] remainingPredecessors;            // Operation that has to be done before this operation
    int[] availableMachines;                // Machines i that can execute this operation
    int[] timesProductionOnMachines;        // Productiontime for this operation on every machine i
    int timeStart;                          // Starting time of this operation
    int timeWorking;                        // Execution time of this operation
    int timeEnd;                            // endtime of this operation
    int workingMachine;                     // actual machine that is doing this operation
    int statusTemp; // -1 = Done, 0 = Not ready, 1 = ready to start
    boolean operationDone;
    boolean operationNotReady;
    boolean operationReadyToStart; 
    boolean readyToStart;   //A

    Operationen(int nOp, int nMa){
        operationDone = false;
        operationNotReady = true;
        operationReadyToStart = false; 
        availableMachines = new int[nMa];
        predecessor = new int[nOp];
        remainingPredecessors = new int[nOp];
    }
}
