

# productionplanning
 
Genetic algorithm with a giffler thompson algorithm for FJSSP

Hello, my name is Henrik. I am an mechanical engineer who enjoys to code in his freetime. This my approch for the FJSSP.

PLEASE NOTE: 
- this is my first programming project (please have mercy with me :/)
- i am not a trained programmer (like i said, i am a mechanical engineer)
- my native language is german, sadly there are still commits, comments and variablenames in german. Sorry, i will replace them ASAP
- i used this first project to get in touch with programming and learning java (which i really enjoyed)
- the goal of this project was to learn and not to great a perfect program
- i would love every kind of feedback
- complete history of the programm is i an my other repo called "planningalgorithm" (i am too dumb for github)

Thank you

How to use the tool:
- Define your process with the excel sheet, use the template in the src folder
- for reference there is also an example process with an assembly priority graph
- For more information, how to write your process in the excel templete, see down below
- start the executable jar file "productionplanning-1.0-jar-with-dependencies" in the target folder
- Press on the "Search" - Button and pick your created excel sheet
- adjust the parameters of the genetic algorithm as you like or leave it at default


Some notes to the structure of the programm:
- i used a Genetic Algorithm
  - Mutation: One-Bit-Mutation / Swap Mutation (for Allocation) and Mixed-Mutation / Swap-Mutation (for Sequence)
  - Rekombination: N-Point_Recombination (Allocation) and Order-Recombination (Sequence)
  - Selektion: Q-Tournament Selection and a Plus-Selection (Children and %Parents go to next Generation)
  - Fitness: Productiontime based on ranks
  
- i used a list planning algorithm (Giffler-Thompson-Algorithm) to get only permitted schedules
  - for reference: Bierwirth, Christian ; Mattfeld, Dirk C.: Production Scheduling and Rescheduling with Genetic Algorithms. In: Evolutionary Computation 7 (1999), Nr. 1, S. 1–17.
  
  
 Some notes to the excel sheet templete and the example (src/test/java/planningalgorithm)
- Because a picture says more than a thousand words, please see the ExplanationExcelSheet.png 
  - the programm is made for k jobs with n operations
  - operations are connected with their predecessor operation to the jobs
  - Every Operation needs a number, but they dont need to be sorted
  - Programm is made for j jobs (j>0) each with n operations (n>0), executed on m machines (m>1)
  - predecessors must be given for every operation (except of cource for the starting operations)
  - starting operations get a "0" in the predecessor column
  - only permissible jobs are allowed (Please see the "Example for Permissible Assambly Priority Graphes.png")
  - if a machine cant do an operation just put in a "0" in the ressource/machine column
  
  
   
  
  
  
  
  
  
  
  
  
  
