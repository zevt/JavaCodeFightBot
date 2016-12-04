/*
 * One of the easiest ways to back up files is with an incremental backup. This method only backs up files that have changed since the last backup.

You are given a list of changes that were made to the files in your database, where for each valid i, changes[i][0] is the timestamp of the time the change was made, and changes[i][1] is the file id.

Knowing the timestamp of the last backup lastBackupTime, your task is to find the files which should be included in the next backup. Return the ids of the files that should be backed up as an array sorted in ascending order.

Example

For lastBackupTime = 461620205 and

changes = [[461620203, 1], 
           [461620204, 2], 
           [461620205, 6],
           [461620206, 5], 
           [461620207, 3], 
           [461620207, 5], 
           [461620208, 1]]
 * 
 * */

package JavaCodeFightBot.datto;

import java.util.TreeSet;

public class FirstChallenge {

	public static void main(String[] args) {

		
	}

	public static int[] incrementalBackups(int lastBackupTime, int[][] changes) {
		int[] outArray = {};
		if (changes == null || changes.length == 0) return outArray;
		
		TreeSet<Integer> intTree = new TreeSet();
		for (int i = 0; i< changes.length; ++i) {
			if (lastBackupTime < changes[i][0]) {
				intTree.add(changes[i][1]);
			}
		}
		
		outArray = new int[intTree.size()];
		int index = 0;
		for (Integer n:intTree) {
			outArray[index++] = n;
		}
		return outArray;
	}
}



/*
 * Test 1
 * Input:
lastBackupTime: 461620205
changes: [[461620203,1], 
 [461620204,2], 
 [461620205,6], 
 [461620206,5], 
 [461620207,3], 
 [461620207,5], 
 [461620208,1]]
Output:
Empty
Expected Output:
[1, 3, 5]
Console Output:
Empty
 * */
 