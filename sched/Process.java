
public class Process {
    public int releaseTime;
  public int execTime;
  public int deadline;
  public int period;
  public int cpuTime;

  public Process(int execTime, int deadline, int period) {
   this.execTime = execTime;
   this.deadline = deadline;
   this.period = period;
  }

  @Override
  public String toString() {
    return "Release time: " + releaseTime + " Execution time: " + execTime + " Deadline: "
            + deadline + " Period: " + period + " CPU time: "
            + cpuTime;
  }
}
