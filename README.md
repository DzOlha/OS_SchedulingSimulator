# Lab 2 Operating Systems
### Scheduling Algorithm: Earliest Deadline First
 Earliest Deadline First (EDF) or least time to go 
 is a dynamic priority scheduling algorithm used in real-time operating systems 
 to place process in a priority queue. Whenever a scheduling event occurs 
 (task finishes, new task released, etc.) the queue will be searched for 
 <b>the process closest to its deadline</b>. 
 This process is the next to be scheduled for execution

 To ensure that a set of processes is schedulable using EDF, 
 the following formula is used: 

### U = sum(Ci/Ti) <= 1, 
#### where i takes from [1; n]
#### U - is the CPU utilization
#### Ci - is the execution time for each process
#### Ti - is the period for each process
 
 EDF can guarantee that all deadlines are met given that 
 their utilization is less than oe equal to 100%. 
 
 EDF does not need the tasks or processes to be periodic 
 and also the tasks or processes require a fixed CPU burst time.

If processes are periodic we should find the least common multiple 
of all of their periods to know how many units we need to execute 
the given number of processes.

 Preemptive is allowed in the EDF scheduling algorithm.
 Limitations of EDF:
1. Transient Overload Problem
2. Resource Sharing Problem
3. Efficient Implementation Problem

### Algorithm:
#### ___For each time unit  {
#### _______For each ready process {
#### __________Process with the closest deadline will execute first
#### _______}
#### ___}
