

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


Some notes to the structure of the programm:
- You can find the executable jar file "productionplanning-1.0-jar-with-dependencies" in the target folder

- i used a Genetic Algorithm
  - Mutation: One-Bit-Mutation / Swap Mutation (for Allocation) and Mixed-Mutation / Swap-Mutation (for Sequence)
  - Rekombination: N-Point_Recombination (Allocation) and Order-Recombination (Sequence)
  - Selektion: Q-Tournament Selection and a Plus-Selection (Children and %Parents go to next Generation)
  - Fitness: Productiontime based on ranks
  
- i used a list planning algorithm (Giffler-Thompson-Algorithm) to get only permitted schedules
  - for reference: Bierwirth, Christian ; Mattfeld, Dirk C.: Production Scheduling and Rescheduling with Genetic Algorithms. In: Evolutionary Computation 7 (1999), Nr. 1, S. 1–17.
  
  
 Some notes to the excel sheet you can find in the src foulder (src/test/java/planningalgorithm)
- Because a picture says more than a thousand words, please see the ExplanationExcelSheet.png 
  - the programm is made for k jobs with n operations
  - operations are connected with their predecessor operation to the jobs
  - Every Operation needs a number, sorted from top to bottom
  - Programm is made for j jobs (j>0) each with n operations (n>0), executed on m machines (m>1)
  - predecessors must be given for every operation (except of cource for the starting operations)
  - starting operations get a "0"
  - only permissible jobs are allowed (Please see the "Example for Permissible Assambly Priority Graphes.png")
  - if machine cant do an operation just put in a "0"
  
  
   
  
  
  
  
  
  
  
  
  
  
