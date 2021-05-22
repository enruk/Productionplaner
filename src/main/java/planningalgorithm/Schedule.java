package planningalgorithm;

import java.awt.Font;
import java.util.List;

import java.awt.BorderLayout;


import javax.swing.JFrame;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;




public class Schedule extends JFrame {

    public Schedule(int AnzMa, List<Machine> Res) {

        final CategoryDataset dataset = createDataset(AnzMa, Res);
        final JFreeChart chart = createChart(dataset,Res);
        final ChartPanel chartPanel = new ChartPanel(chart);

        JFrame frame = new JFrame("Schedule");
        frame.setTitle("Schedule");
        frame.setLayout(new BorderLayout(0, 5));

        frame.add(chartPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public int[] addToArray (int[] Arr, int what, int n){
        int[] NewArr = new int[Arr.length+n];
        for (int i=0;i<Arr.length;i++){
            NewArr[i] = Arr[i];
        }
        for (int j=Arr.length+1;j<Arr.length+n;j++){
            NewArr[j] = what;
        }

        return NewArr;
    }


    private CategoryDataset createDataset(int AnzMaschinen, List<Machine> Ressourcen) {

        //Get the maximum amount of operations of all machines
        int MaxOp = 0;
        for (int i=0;i<AnzMaschinen;i++){
            if(Ressourcen.get(i).plannedOperations != null){
                int temp = Ressourcen.get(i).plannedOperations.length;
                if (temp>MaxOp){
                    MaxOp = temp;
                }
            }
        }



        // Fill sequences of all other machines with zeros
        for (int i=0;i<AnzMaschinen;i++){
            if (Ressourcen.get(i).plannedOperations != null){
                int AnzOps = Ressourcen.get(i).plannedOperations.length;
                if (AnzOps<MaxOp){
                    int DiffOps = MaxOp - AnzOps;
                    Ressourcen.get(i).startingTimesOps = addToArray(Ressourcen.get(i).startingTimesOps, 0, DiffOps);
                    Ressourcen.get(i).endingTimesOps = addToArray(Ressourcen.get(i).endingTimesOps, 0, DiffOps);
                    Ressourcen.get(i).plannedOperations = addToArray(Ressourcen.get(i).plannedOperations,0,DiffOps);
                }
            }
            else{
                Ressourcen.get(i).startingTimesOps = new int[MaxOp];
                Ressourcen.get(i).endingTimesOps = new int[MaxOp];
                Ressourcen.get(i).plannedOperations = new int[MaxOp];
            }
        }


        
        //Filling Array Ganttplan which is needed to create the XYChart.Series
        for (int i=0;i<AnzMaschinen;i++){
            Ressourcen.get(i).ganntPlan = new int[2*MaxOp];
            Ressourcen.get(i).ganntPlan[0] = Ressourcen.get(i).startingTimesOps[0]; //Startzeit der erste Op = Belegungszeit, Index 0, Comment: Nicht ungebingt
            for (int j=1;j<MaxOp;j++){
                Ressourcen.get(i).ganntPlan[2*j-1] = Ressourcen.get(i).endingTimesOps[j-1] - Ressourcen.get(i).startingTimesOps[j-1];  //Prozess, Index: 1,3,5,
                if (Ressourcen.get(i).endingTimesOps[j-1]<Ressourcen.get(i).startingTimesOps[j]){
                    Ressourcen.get(i).ganntPlan[2*j] = Ressourcen.get(i).startingTimesOps[j] - Ressourcen.get(i).endingTimesOps[j-1];// Pause, Index: 2,4,5,6
                }
            }
            Ressourcen.get(i).ganntPlan[2*MaxOp-1] = Ressourcen.get(i).endingTimesOps[MaxOp-1] - Ressourcen.get(i).startingTimesOps[MaxOp-1];
        }



        double[][] data = new double[Ressourcen.get(0).ganntPlan.length][AnzMaschinen];
        for (int i=0;i<Ressourcen.get(0).ganntPlan.length;i++){
            for (int j=0;j<AnzMaschinen;j++){
                data[i][j] = Ressourcen.get(j).ganntPlan[i];
            }
        }
        return DatasetUtilities.createCategoryDataset("Operations", "Machine ", data);
    }
      
    private JFreeChart createChart(final CategoryDataset dataset, List<Machine> Res) {
      
        final JFreeChart chart = ChartFactory.createStackedBarChart("Production Schedule", "", "Time",dataset, PlotOrientation.HORIZONTAL, false, true, false);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        int ColorChangeCounter = 0;
        for (int row=0; row<dataset.getRowCount(); row++) {
            
            if (row%2 == 0 || row == 0) {
                plot.getRenderer().setSeriesPaint(row, java.awt.Color.white);
            }
            else { 
                if (ColorChangeCounter%2 == 0 || ColorChangeCounter == 0) {
                    plot.getRenderer().setSeriesPaint(row, java.awt.Color.LIGHT_GRAY);
                }
                else{
                    plot.getRenderer().setSeriesPaint(row, java.awt.Color.GRAY);
                }
                ColorChangeCounter++;
            }
        }
    
        

        // BAR LABELS
        // calculate the column total for each column
        // column = Machine
        // row = Operations on Machines
        for (int col=0; col<dataset.getColumnCount(); col++) {
            
            double AddedValues = 0;
            int PlannedOpsCounter=0;
            for (int row=0; row<dataset.getRowCount(); row++) {
                if (dataset.getValue(row, col) != null) {
                    Number value = dataset.getValue(row, col);
                    double valuedouble = value.doubleValue();
                    int OperationsName;
                    if (row%2 == 0 || row == 0) {
                        //Do nothing
                    }
                    else { 
                        int valueint = value.intValue();
                        if ( valueint != 0){
                            OperationsName = Res.get(col).plannedOperations[PlannedOpsCounter]+1;
                            PlannedOpsCounter++;

                            //  display as decimal integer
                            String opName = String.valueOf(OperationsName);
                            // Create the annotation
                            CategoryTextAnnotation cta = new CategoryTextAnnotation(opName,dataset.getColumnKey(col), AddedValues + valuedouble*0.5);
                            Font font = new Font("Courier", Font.BOLD,12);
                            cta.setFont(font);
                            // Add to the plot
                            plot.addAnnotation(cta);
                        }
                    }

                    AddedValues += valuedouble;

                }           
            }
        }

        return chart;
    }

}