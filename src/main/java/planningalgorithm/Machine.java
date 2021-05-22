package planningalgorithm;

public class Machine {
    int number;
    String name;
    int timeOccupation;
    int timeOccupationLastRun;
    int[] plannedOperations;
    int[] startingTimesOps;
    int[] endingTimesOps;
    int[] ganntPlan;


    Machine(int Num, int BelegteZeit){
        number = Num;
        timeOccupation = BelegteZeit;
    }


}
