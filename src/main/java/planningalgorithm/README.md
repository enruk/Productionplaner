# planningalgorithm
Genetic algorithm with a giffler thompson algorithm for FJSSP

Hello, my name is Henrik. I am an mechanical engineer who enjoys to code in his freetime. This my approch for the FJSSP.

PLEASE NOTE: 
- this is my first programming project (please have mercy with me :/)
- i am not a trained programmer (like i said, i am an mechanical engineer)
- my native language is german, sadly there are still commits, comments and variablenames in german. Sorry, i will replace them ASAP
- i used this first project to get in touch with programming and learning java (which i really enjoyed)
- i would love every kind of feedback

Thank you


Some notes to the structure of the programm:
- i used a Genetic Algorithm
  - Mutation: One-Bit-Mutation / Swap Mutation (for Allocation) and Mixed-Mutation / Swap-Mutation (for Sequence)
  - Rekombination: N-Point_Recombination (Allocation) and Order-Recombination (Sequence)
  - Selektion: Q-Tournament Selection and a Plus-Selection (Children and %Parents go to next Generation)
  - Fitness: Productiontime based on ranks
  
- i used a list planning algorithm (Giffler-Thompson-Algorithm) to get only permitted schedules
  - for reference: Bierwirth, Christian ; Mattfeld, Dirk C.: Production Scheduling and Rescheduling with Genetic Algorithms. In: Evolutionary Computation 7 (1999), Nr. 1, S. 1–17.
  
  
 Some notes to the excel sheet:
  - the programm is made for k jobs with n operations
  - operations are connected with their predecessor operaion to the jobs
  - Every Operation needs a number
  - Programm is made for n operations and m machines (only practical limit)
  - predecessors must be given for every operation (except of cource for the starting operations)
  - starting operations get a "0"
  - multiple jobs can be given, every operation only need its predecessors
  - if machine cant do an operation just put in a "0"
  
  
   
  
  
  
  
  
  
  
  
  
  
