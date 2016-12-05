package JavaCodeFightBot.datto;


import javax.management.remote.rmi._RMIConnectionImpl_Tie;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by  Viet Quoc Tran on 12/4/2016.
 */
public class BackupTimeEstimator {
    private static final int[] StartTime01 = {461620201, 461620202, 461620203};
    private static final int[] BackupDuration01 = {2, 2, 2};
    private static final int MaxThreads01 = 2;

    private static final int[] StartTime02 = {461620201, 461620202, 461620203};
    private static final int[] BackupDuration02 = {2, 2, 2};
    private static final int MaxThreads02 = 3;

    public static void main(String... args) {
        BackupTimeEstimator be = new BackupTimeEstimator();
        double[] result;
        result = be.backupTimeEstimator(StartTime01, BackupDuration01, MaxThreads01);

        NumberFormat formatter = new DecimalFormat("#0.00");
        for (double x : result) {
            System.out.println("  " + formatter.format(x));
        }
    }

    public double[] backupTimeEstimator(int[] startTimes, int[] backupDuration, int maxThreads) {
        
    	
    	
    	if (startTimes == null || backupDuration == null || maxThreads < 1 || startTimes.length == 0 || backupDuration.length == 0) {
            return new double[0];
        }
        double[] backupsEstimator = new double[backupDuration.length];

        LinkedList<Task> initialTaskList = new LinkedList<>();
        for (int i = 0; i < startTimes.length; ++i) {
            initialTaskList.add(new Task(i, startTimes[i], backupDuration[i]));
        }
        LinkedList<Task> completedTaskList;
        Controller controller = new Controller(initialTaskList, maxThreads);
        completedTaskList = controller.getCompletedTaskList();

        completedTaskList.forEach(e -> {
            backupsEstimator[e.index] = e.estimatedEndingTime;
        });

        return backupsEstimator;
    }

    class Task implements Comparable {
        int index;
        int startTime;
        double startImplementing;
        double remainingWordLoad;
        double estimatedEndingTime;

        public Task(int index, int startTime, double remainingWordLoad) {
            this.index = index;
            this.startTime = startTime;
            this.remainingWordLoad = remainingWordLoad;
        }

        @Override
        public int compareTo(Object o) {
            Task task = (Task) o;
            double byDuration = remainingWordLoad - task.remainingWordLoad;
            int byStartTime = startTime - task.startTime;

            if (byDuration > 0) return 1;
            else if (byDuration < 0) return -1;
            else
                return byStartTime;
        }

        public Task reEvaluateCompletion(double moment, int numberOfTask) {

            remainingWordLoad = remainingWordLoad - (moment - startImplementing) / numberOfTask;
            startImplementing = moment;
            estimatedEndingTime = startImplementing + (remainingWordLoad * numberOfTask);

            return this;
        }

        public Task setStartImplementing(double moment) {
            startImplementing = moment;
            return this;
        }

        @Override
        public String toString() {
            return "" + index;
        }
    }

    class QueuedTasks {
        TreeSet<Task> tasks;

        public QueuedTasks() {
            tasks = new TreeSet<>();
        }

        public void addTask(Task task) {
            tasks.add(task);
        }

        public int size() {
            return tasks.size();
        }

        public Task takeFirst() {
            return tasks.pollFirst();
        }

    }

    class ImplementingTasks {
        //        int maxThreads;
        TreeSet<Task> tasks;
        double nextCompleteTaskMoment;

        public ImplementingTasks() {
            tasks = new TreeSet<>();
        }

        public void addTask(Task task, double moment) {
            task.setStartImplementing(moment);
            reEvaluate(moment);

            System.out.println(tasks.add(task));
            reEvaluate(moment);
            nextCompleteTaskMoment = tasks.first().reEvaluateCompletion(moment, tasks.size()).estimatedEndingTime;
        }

        private void reEvaluate(double moment) {
            tasks.forEach(e -> {
                e.reEvaluateCompletion(moment, tasks.size());
            });
        }

        public Task removeCompletedTask() {

            reEvaluate(tasks.first().estimatedEndingTime);
            Task task = tasks.pollFirst();

            if (tasks == null)
                tasks = new TreeSet<>();
            return task;
        }

        public int size() {
            return tasks.size();
        }

    }

    class Controller {
        LinkedList<Task> initialTaskList;
        LinkedList<Task> completedTaskList;
        ImplementingTasks runningTasks;
        QueuedTasks queuedTasks;
        int maxThread;

        public Controller(LinkedList<Task> initialTaskList, int maxThread) {
            this.initialTaskList = initialTaskList;
            this.maxThread = maxThread;
            completedTaskList = new LinkedList<>();
            runningTasks = new ImplementingTasks();
            queuedTasks = new QueuedTasks();
        }

        public LinkedList<Task> getCompletedTaskList() {
            if (initialTaskList.size() == 0)
                return null;

            while (initialTaskList.size() > 0 || runningTasks.size() > 0) {
                if (runningTasks.size() > 0) {
                    if (initialTaskList.size() == 0 || runningTasks.nextCompleteTaskMoment < initialTaskList.getFirst().startTime) {
                        double moment = runningTasks.nextCompleteTaskMoment;
                        completedTaskList.add(runningTasks.removeCompletedTask());
                        moveQueuedTasksToImplementingTasks(moment);
                    } else if (runningTasks.nextCompleteTaskMoment == initialTaskList.getFirst().startTime) {
                        queuedTasks.addTask(initialTaskList.removeFirst());
                        double moment = runningTasks.nextCompleteTaskMoment;
                        completedTaskList.add(runningTasks.removeCompletedTask());
                        moveQueuedTasksToImplementingTasks(moment);
                    } else {

                        if (runningTasks.size() < maxThread) {
                            Task t = initialTaskList.removeFirst();
                            runningTasks.addTask(t, t.startTime);
                        } else {
                            queuedTasks.addTask(initialTaskList.removeFirst());
                        }
                    }
                } else if (initialTaskList.size() > 0) {
                    Task firstTask = initialTaskList.removeFirst();
                    runningTasks.addTask(firstTask, firstTask.startTime);
                }
            }
            return completedTaskList;
        }

        private void moveQueuedTasksToImplementingTasks(double moment) {
            if (queuedTasks.size() > 0 && runningTasks.size() < maxThread) {
                runningTasks.addTask(queuedTasks.takeFirst(), moment);
            }
        }
    }
}

/*
Test 2
Input:
startTimes: [461620201, 461620202, 461620203]
backupDuration: [2, 2, 2]
maxThreads: 3
Output:
Empty
Expected Output:
[461620204.5, 461620206.5, 461620207]
Console Output:
Empty

* */