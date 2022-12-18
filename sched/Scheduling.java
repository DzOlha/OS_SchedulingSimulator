// This file contains the main() function for the sched.Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class Scheduling {
  private static int runtime = 1000;
  private static Vector<Process> processVector = new Vector<>();
  private static Results result = new Results("null","null",0);
  private static String resultsFile = "C:/moss/sched/Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;

    try {
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processVector.addElement(
                  new Process(
                          Integer.parseInt(st.nextToken()),
                          Integer.parseInt(st.nextToken()),
                          Integer.parseInt(st.nextToken())
                  )
          );
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
  }

  private static void debug() {
    int i = 0;
    int size = processVector.size();
    for (i = 0; i < size; i++) {
      System.out.println(i + " " + processVector.elementAt(i));
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i = 0;
    String configurationFile = "C:/moss/sched/scheduling.conf";
    File f = new File(configurationFile);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(configurationFile);
    Results result = SchedulingAlgorithm.Run(runtime, processVector);
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Process #\treleaseTime\texecutionTime\tdeadline\tperiod\tcpuTime");
      for (i = 0; i < processVector.size(); i++) {
        Process process = processVector.elementAt(i);
        out.print(i);
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        if (process.releaseTime < 100) { out.print("\t\t\t"); } else { out.print("\t\t"); }
        out.print(process.releaseTime);
        if (process.execTime < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(process.execTime);
        if (process.deadline < 100) { out.print("\t\t\t\t"); } else { out.print("\t\t\t"); }
        out.print(process.deadline);
        if (process.period < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(process.period);
        out.print("\t\t");
        out.print(process.cpuTime);
        out.println("\t\t");
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
  System.out.println("Completed.");
  }
}

