package JavaCodeFightBot.datto;

public class TroubleFiles {
	private static final int[][] file01 = {{461618501,3}, 
	                               {461618502,1}, 
	                               {461618504,2}, 
	                               {461618506,5}, 
	                               {461618507,6}};
	private static final int[] backup01 = {461618501, 461618502, 461618504, 461618505, 461618506};
	public static void main(String[] args) {
		int[] out = troubleFiles(file01, backup01);
		System.out.println(" Result ");
		for (int i: out) {
			System.out.print(i + "  ");
		}
		
	}

	static int[] troubleFiles(int[][] files, int[] backups) {
		if (backups == null || backups.length == 0)
			return null;
		int[] troubleFileByBackup = new int[backups.length];
		for (int i = 0; i < backups.length; ++i)
			troubleFileByBackup[i] = 0;
		if (files == null || files.length == 0)
			return troubleFileByBackup;

		boolean[] availableMarker = new boolean[files.length];
		for (int i = 0; i < files.length; ++i) {
			availableMarker[i] = true;
		}
		
		int nthFile = 0;
		for (int i = 0; i < backups.length; ++i) {
			
			// Count Available File
			int totalFileSize = 0;
			
			while (nthFile < files.length && files[nthFile][0] <= backups[i]) {
				if (availableMarker[nthFile]) {
					totalFileSize += files[nthFile][1];
					availableMarker[nthFile] = false;
				}
				++nthFile;
			}
			// Count Totol Time neeeded
			int completepMoment = backups[i] + totalFileSize;
			// Check if files will be added during the the process and mark them as troublemaker
			while (nthFile < files.length && files[nthFile][0] <= completepMoment) {
				availableMarker[nthFile] = false;
				++troubleFileByBackup[i];
				++nthFile;
			}
			// return trouble number of trouble file
		}

		return troubleFileByBackup;
	}
}
