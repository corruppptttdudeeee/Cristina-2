import java.util.*;

class Process {
    int pid; // Process ID
    int arrivalTime; // Arrival time
    int burstTime; // Burst time
    int remainingTime; // Remaining burst time
    int completionTime; // Completion time
    int waitingTime; // Waiting time
    int turnaroundTime; // Turnaround time

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class SRTFScheduler {
    private List<Process> processes;
    private List<String> ganttChart;
    private int currentTime;
    private int totalIdleTime;

    public SRTFScheduler() {
        processes = new ArrayList<>();
        ganttChart = new ArrayList<>();
        currentTime = 0;
        totalIdleTime = 0;
    }

    public void addProcess(int pid, int arrivalTime, int burstTime) {
        processes.add(new Process(pid, arrivalTime, burstTime));
    }

    public void execute() {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        int completed = 0;
        Process currentProcess = null;
        
        while (completed < processes.size()) {
            // Add new processes to the ready queue
            for (Process process : processes) {
                if (process.arrivalTime == currentTime) {
                    readyQueue.add(process);
                }
            }

            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            if (!readyQueue.isEmpty()) {
                Process nextProcess = readyQueue.poll();
                if (nextProcess != currentProcess) {
                    ganttChart.add("P" + (nextProcess.pid + 1));
                }
                nextProcess.remainingTime--;
                currentProcess = nextProcess;
                if (nextProcess.remainingTime == 0) {
                    nextProcess.completionTime = currentTime + 1;
                    completed++;
                }
            } else {
                ganttChart.add("Idle");
                totalIdleTime++;
            }
            currentTime++;
        }

        calculateTimes();
    }

    private void calculateTimes() {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        for (Process process : processes) {
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            totalTurnaroundTime += process.turnaroundTime;
            totalWaitingTime += process.waitingTime;
        }

        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double cpuUtilization = 100.0 * (currentTime - totalIdleTime) / currentTime;

        System.out.println("Gantt Chart: " + String.join(" -> ", ganttChart));
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("CPU Utilization: " + cpuUtilization + "%");
    }

    public void displayReadyQueue() {
        System.out.println("Ready Queue:");
        for (Process process : processes) {
            if (process.arrivalTime <= currentTime && process.remainingTime > 0) {
                System.out.println("P" + (process.pid + 1) + " (Remaining Time: " + process.remainingTime + ")");
            }
        }
    }
}

public class SRTFInteractive {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SRTFScheduler scheduler = new SRTFScheduler();

        System.out.println("Enter the number of processes:");
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter arrival time and burst time for process " + (i + 1) + ":");
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            scheduler.addProcess(i, arrivalTime, burstTime);
        }

        scheduler.execute();

        System.out.println("Do you want to see the ready queue at a specific time? (yes/no)");
        if (scanner.next().equalsIgnoreCase("yes")) {
            System.out.println("Enter the time:");
            int time = scanner.nextInt();
            scheduler.displayReadyQueue();
        }

        scanner.close();
    }
}
