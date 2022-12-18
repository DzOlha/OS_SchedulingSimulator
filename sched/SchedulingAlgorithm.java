// Run() is called from sched.Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SchedulingAlgorithm {

  private static int gcd(int a, int b) {
    if (b == 0)
      return a;
    else
      return gcd(b, a % b);
  }

  private static int lcm(int a, int b)
  {
    return Math.abs(a * b) / gcd(a, b);
  }

  private static boolean canBeScheduledWithEDF(Vector<Process> processVector) {
    int lcm = 1;
    for (Process process: processVector) {
      lcm = lcm(lcm, process.deadline);
    }
    int counter = 0;
    for (Process process: processVector) {
      counter += process.execTime * (lcm / process.deadline);
    }
    return counter <= lcm;
  }
  private static void checkAllConditions(Vector<Process> pVector){
    if(pVector.isEmpty())
      throw new IllegalArgumentException("No processes in the vector!");
    if(!canBeScheduledWithEDF(pVector))
      throw new IllegalArgumentException("System can not be scheduled with EDF");
  }
  private static boolean isProcessReadyToStart(Vector<Process> pVector, int comptime) {
    for(Process p : pVector){
      if(comptime >= p.releaseTime * p.period){
        return true;
      }
    }
    return false;
  }
  private static boolean isProcessForRelease(Vector<Process> pVector, Map<Integer, Integer> executionAmountMap){
    for(Process p : pVector){
      if(p.releaseTime < executionAmountMap.get(pVector.indexOf(p))){
        return true;
      }
    }
    return false;
  }
  private static Process getNextProcess(
          Vector<Process> pVector,
          Map<Integer, Integer> executionAmountMap,
          int currCompTime
  ) {
    Vector<Process> tmp = new Vector<>();
    for(Process p: pVector){
      if(currCompTime >= p.releaseTime * p.period) {
        if(p.releaseTime < executionAmountMap.get(pVector.indexOf(p))){
          tmp.add(p);
        }
      }
    }
    if(!tmp.isEmpty()) {
      return tmp.stream().min(Comparator.comparingInt(p -> p.deadline)).get();
    }
    else
      return null;
  }
  public static Results Run(int runtime, Vector<Process> processVector) {
    int comptime = 0;

    //Check if the vector of processes contains at least one process and
    //if the processes can be scheduled by EDF algorithm.
    try {
      checkAllConditions(processVector);
    }catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    String resultsFile = "C:/moss/sched/Summary-Processes";

    //find the least common multiple of all the periods to know how many units we need to execute
    //the given number of processes
    int lcm = 1;
    for(Process process: processVector) {
      lcm = lcm(lcm, process.period);
    }
    Map<Integer, Integer> executionAmountMap = new HashMap<>();
    for (Process process: processVector) {
      executionAmountMap.put(processVector.indexOf(process), lcm / process.period);
    }
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      //take the process with the earliest deadline
      Process process = processVector.stream().min(Comparator.comparingInt(p -> p.deadline)).get();
      out.println("Process: " + processVector.indexOf(process) + " registered... (" + process + ")");
      int processExecutionTime = 0;
      while (comptime < runtime)
      {
        if(processExecutionTime == process.execTime)
        {
          processExecutionTime = 0;
          ++process.releaseTime;
          out.println("Process: " + processVector.indexOf(process) + " completed... (" + process + ")");

          while (isProcessForRelease(processVector, executionAmountMap)
                  && !isProcessReadyToStart(processVector, comptime)) {
            ++comptime;
          }
          int currentComputationTime = comptime;
          process = getNextProcess(processVector, executionAmountMap, currentComputationTime);
          if(process == null) {
            break;
          }
          out.println("Process: " + processVector.indexOf(process) + " registered... (" + process + ")");
        } else {
          ++processExecutionTime;
          ++comptime;
          ++process.cpuTime;
        }
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return new Results("Dynamic priority", "EDF", comptime);
  }
}