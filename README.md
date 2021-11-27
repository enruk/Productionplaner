

# productionplanning
<br>

Genetic algorithm with a giffler thompson algorithm to solve the well known JSSP / FJSSP
<br>

Hello, my name is Henrik. I am a mechanical engineer who enjoys to code in his freetime. This my approch for the (F)JSSP.
<br>

PLEASE NOTE: 
- this is my first programming project
- i am not a trained programmer (like i said, i am a mechanical engineer)
- my native language is german, sadly there are still commits, comments and variablenames in german. Sorry, i will replace them ASAP
- i used this first project to get in touch with programming and learning java (which i really enjoy / enjoyed)
- the goal of this project was to learn and not to create a perfect program
- i would love every kind of feedback
- complete history of the programm is i an my other repo called "planningalgorithm"
<br>

Thank you!

<br>
<br>
<br>


1. How to use the tool:
- Define your process with the excel sheet, use the template in the src folder
- for reference there is also an example process with an assembly priority graph
- For more information, how to write your process in the excel templete, see down below the point 3.

- start the executable jar file "productionplanning-1.0-jar-with-dependencies" in the target folder
- Press on the "Search" - Button and pick your created excel sheet
- adjust the parameters of the genetic algorithm as you like or leave it at default
- Press "Start"




2. Some notes to the structure of the programm:
- i used a Genetic Algorithm
  - Mutation: One-Bit-Mutation / Swap Mutation (for Allocation) and Mixed-Mutation / Swap-Mutation (for Sequence)
  - Rekombination: N-Point_Recombination (Allocation) and Order-Recombination (Sequence)
  - Selektion: Q-Tournament Selection and a Plus-Selection (Children and %Parents go to next Generation)
  - Fitness: Productiontime based on ranks
<br>

- i used a list planning algorithm (Giffler-Thompson-Algorithm) to get only permitted schedules
  - for reference: Bierwirth, Christian ; Mattfeld, Dirk C.: Production Scheduling and Rescheduling with Genetic Algorithms. In: Evolutionary Computation 7 (1999), Nr. 1, S. 1–17.


<br>  
<br>
<br>


 3. Some notes to the excel sheet templete and the example (src/test/java/planningalgorithm)
- Because a picture says more than a thousand words, please see the ExplanationExcelSheet.png
- Jobs / Operations
  - programm is made for j jobs (j>0) each with n operations (n>0), executed on m machines (m>1)
  - only permissible jobs are allowed (Please see the "Example for Permissible Assambly Priority Graphes.png")
  - every Operation needs a number, but they dont need to be sorted
  - you can define multiple jobs, each with multiple operations, while the jobs dont have to be connected

<br>

- Predecessors:
  - predecessors must be given for every operation (except of cource for the starting operations)
  - assign a predecessor to an operation by entering the number of the predecessor in the predecessor column
  - if an operaion has multiple predecessors, separate them with a ";"
  - starting operations, so an operations that has no predecessor, get a "0" in the predecessor column

<br>

- Machines / Ressource
  - every machine you want to use must be defined in the excel sheet in a machine column
  - to define a machine, a processing time must be specified for each operation
  - if a machine can't do an operation just put in a "0" in the ressource/machine column
  - you can select how many machines you want to use in the GUI
  - each operation must be able to be executed on at least one machine/resource

  
  
   
  
  
  
  
  
  
  
  
  
  
