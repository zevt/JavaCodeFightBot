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
//    private static final int[] StartTime01 = {461620201, 461620202, 461620203};
    private static final int[] StartTime01 = {1, 2, 3, 4, 5, 6, 7};
    private static final int[] BackupDuration01 = {16, 16, 13,14,15,16,17 };
    private static final int MaxThreads01 = 5;

    private static final int[] StartTime02 = {461620201, 461620202, 461620203};
    private static final int[] BackupDuration02 = {2, 2, 2};
    private static final int MaxThreads02 = 3;



    private static final int[] StartTime06 = {461620209, 461620965, 461621051, 461621056, 461621075, 461622316, 461622336, 461623323, 461623784, 461625533, 461625785, 461625852, 461626762, 461627149, 461627246, 461628443, 461628598, 461628908, 461629081, 461629122};
    private static final int[] BackupDuration06  = {6608, 9870, 7191, 8114, 6069, 4844, 848, 1865, 6274, 6776, 6114, 6142, 6588, 7426, 7921, 6123, 5964, 4144, 8753, 3998};
    private static final int MaxThreads06  = 7;

//    Expected Output:
//            [461661193.35, 461689319.35, 461670867.35, 461677340.01666665, 461663058.26666665, 461656220.6666667, 461628272, 461641327, 461685245, 461703652.6666667, 461703991.35, 461706052.26666665, 461716983.35, 461729322.01666665, 461736752.81666666, 461731772.0166667, 461738709.20000005, 461732357.0166667, 461741841.00000006, 461738585.9666667]


    private static final int[] StartTime07 =  {461620415, 461620416, 461620421, 461620424, 461620425, 461620427, 461620434, 461620435, 461620449};
    private static final int[] BackupDuration07 =  {3, 7, 6, 3, 10, 7, 3, 6, 10};
    private static final int MaxThreads07 =  5;

//    Expected Output:
//            [461620420, 461620435.3333333, 461620445.3333333, 461620437.8333333, 461620464.9166667, 461620458.75, 461620449.4166666, 461620462.25, 461620470]

    public static void main(String... args) {
        BackupTimeEstimator be = new BackupTimeEstimator();
        double[] result;
        result = be.backupTimeEstimator(StartTime06, BackupDuration06, MaxThreads06);

        NumberFormat formatter = new DecimalFormat("#0.00000000");
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
            initialTaskList.add(new Task(i, startTimes[i]- startTimes[0], backupDuration[i]));
        }
        initialTaskList.forEach( e-> {
            System.out.println(e.startTime+ "   ");
        });
        LinkedList<Task> completedTaskList;
        Controller controller = new Controller(initialTaskList, maxThreads);
        completedTaskList = controller.getCompletedTaskList();

        completedTaskList.forEach(e -> {
            backupsEstimator[e.index] = e.estimatedEndingTime + startTimes[0];
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

        public Task reEvaluateCompletion(double moment, double numberOfTask) {

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
        LinkedList<Task> tasks;

        public QueuedTasks() {
            tasks = new LinkedList<>();
        }

        public void addTask(Task task) {
            System.out.println(tasks.add(task));
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
//            this.maxThreads = maxThreads;
        }

        public void addTask(Task task, double moment) {
            task.setStartImplementing(moment);
            // Re-Evaluate make sure that the current task will go to correct order
            reEvaluate(moment);

//            System.out.println("Add Element " + task.index + " to " + tasks);
            System.out.println(tasks.add(task));
            reEvaluate(moment);
//            nextCompleteTaskMoment = tasks.first().reEvaluateCompletion(moment, tasks.size()).estimatedEndingTime;
//            System.out.println("nextCompleteTaskMoment  "+ nextCompleteTaskMoment);
        }
//        public double nextCompleteTaskMoment(d) {
//            return tasks.first().reEvaluateCompletion(moment, tasks.size()).estimatedEndingTime;
//        }
        private void reEvaluate(double moment) {
            System.out.println("------------------------------------------------------------");
            tasks.forEach(e -> {
                e.reEvaluateCompletion(moment, tasks.size());
            });
            System.out.println("After evaluation  At Moment: "+ moment);
            tasks.forEach(e->{
                System.out.println("ID: " + e.index+ " WordLoadRemain =  "+ e.remainingWordLoad + " Estimated Complete  " + e.estimatedEndingTime);
            });
            if (tasks.size() > 0)
                nextCompleteTaskMoment = tasks.first().reEvaluateCompletion(moment, tasks.size()).estimatedEndingTime;
        }

        public Task removeCompletedTask() {

            reEvaluate(tasks.first().estimatedEndingTime);
            Task task = tasks.pollFirst();

            if (tasks == null)
                tasks = new TreeSet<>();
            else {
                reEvaluate(task.estimatedEndingTime);
            }
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
            System.out.println("Current number of THreads -------------> "+ runningTasks.size());
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


Test 6
Input:

startTimes: [461620209, 461620965, 461621051, 461621056, 461621075, 461622316, 461622336, 461623323, 461623784, 461625533, 461625785, 461625852, 461626762, 461627149, 461627246, 461628443, 461628598, 461628908, 461629081, 461629122]
backupDuration: [6608, 9870, 7191, 8114, 6069, 4844, 848, 1865, 6274, 6776, 6114, 6142, 6588, 7426, 7921, 6123, 5964, 4144, 8753, 3998]
maxThreads: 7
Expected Output:
[461661193.35, 461689319.35, 461670867.35, 461677340.01666665, 461663058.26666665, 461656220.6666667, 461628272, 461641327, 461685245, 461703652.6666667, 461703991.35, 461706052.26666665, 461716983.35, 461729322.01666665, 461736752.81666666, 461731772.0166667, 461738709.20000005, 461732357.0166667, 461741841.00000006, 461738585.9666667]


Test 7

startTimes: [461620209, 461620965, 461621051, 461621056, 461621075, 461622316, 461622336, 461623323, 461623784, 461625533, 461625785, 461625852, 461626762, 461627149, 461627246, 461628443, 461628598, 461628908, 461629081, 461629122]
backupDuration: [6608, 9870, 7191, 8114, 6069, 4844, 848, 1865, 6274, 6776, 6114, 6142, 6588, 7426, 7921, 6123, 5964, 4144, 8753, 3998]
maxThreads: 7
Expected Output:
[461661193.35, 461689319.35, 461670867.35, 461677340.01666665, 461663058.26666665, 461656220.6666667, 461628272, 461641327, 461685245, 461703652.6666667, 461703991.35, 461706052.26666665, 461716983.35, 461729322.01666665, 461736752.81666666, 461731772.0166667, 461738709.20000005, 461732357.0166667, 461741841.00000006, 461738585.9666667]
* */

