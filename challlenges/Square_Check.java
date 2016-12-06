package JavaCodeFightBot.challlenges;

import org.junit.experimental.theories.PotentialAssignment;

public class Square_Check {

	public static void main(String[] args) {
//		System.out.println(Square_Check(new int[] { 5, 7, 3, 1, 7, 3, 1, 5 }));
		System.out.println(Square_Check(new int[] { -1, 1, -1, 3, 1, 3, 1, 0 }));
	}

	static boolean Square_Check(int[] points) {
		double side = -1;
		double rhombus = -1;
		int x = 0, y = 1;
		for (int i = 0; i < 3; ++i) {
			for (int j = i + 1; j < 4; ++j) {
//				double d = distance(points[i * 2], points[i * 2 + 1], points[j * 2], points[j * 2 + 1]);
				double d = Math.sqrt(Math.pow(points[i * 2] - points[j * 2], 2) + Math.pow(points[i * 2 + 1] - points[j * 2 + 1], 2));
				System.out.println("  d " + d);
				if (side == -1) {
					side = d;
				} else if (rhombus == -1) {
					if (side != d) {
						rhombus = Math.max(side, d);
						side = Math.min(side, d);
						if (2*Math.pow(side,2) - Math.pow(rhombus, 2) > 0.001) {
							System.out.println(2* Math.pow(side,2) + "    " + Math.pow(rhombus, 2));
					
							System.out.println(" failed because not  created square angle "+side);
							return false;
						}
					}
				} else if (d != side && d != rhombus) {
					System.out.println(" failed because 3 sized");
					return false;
				} 

			}
		}

		return true;
	}

	static double distance(int x1, int x2, int y1, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

}
