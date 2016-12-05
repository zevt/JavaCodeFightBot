package JavaCodeFightBot.datto;

public class BackupTimeEstimator {

	public static void main(String[] args) {
		
	}
	
	double[] backupTimeEstimator(int[] startTimes, int[] backupDuration, int maxThreads) {
		
		if (startTimes == null ||  backupDuration == null || maxThreads < 1 || startTimes.length == 0 || backupDuration.length == 0) {
			return new double[0];
		}
		
		double[] backupsEstimator = new double[backupDuration.length];
		
		
		
		return backupsEstimator;
	}

}
