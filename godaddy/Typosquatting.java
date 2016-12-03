
/*
 * Typosquatting is a hack that relies on mistakes made by Internet users when
 * inputting a website address into a web browser. So if a user is trying to go
 * to godaddy.com but they accidentally type in goddady.com and someone else
 * owns that domain, they could pretend to be GoDaddy and steal valuable user
 * information.
 * 
 * Assume that GoDaddy is introducing a new feature that helps users protect
 * their domains from typosquatting. It is known that a typosquatter's URL is
 * usually similar to the victim's domain, but has some typos in it, where a
 * typo means that letters in two adjacent positions have been swapped.
 * 
 * Given n, the number of additional domains the owner is willing to buy to
 * protect their domain against typosquatting, GoDaddy calculates the maximum
 * number k such that all of the domains with k or fewer typos can be bought
 * (excluding the original domain itself).
 * 
 * Your task is to implement an algorithm that finds k given n and a domain
 * name.
 * 
 * Example
 * 
 * For n = 7 and domain = "godaddy.com", the output should be typosquatting(n,
 * domain) = 1.
 * 
 * For k = 1 the following typos can be made:
 * 
 * "ogdaddy.com" "gdoaddy.com" "goadddy.com" "goddady.com" "godadyd.com"
 * "godaddy.ocm" "godaddy.cmo" 7 domains to buy altogether. That's exactly the
 * number of domains the user can afford, so the answer is 1.
 * 
 * For n = 9 and domain = "omg.tv", the output should be typosquatting(n,
 * domain) = 2.
 * 
 * For k = 1, the following typos can be made:
 * 
 * "mog.tv" "ogm.tv" "omg.vt" For k = 2, 4 more typos can be obtained:
 * 
 * "mgo.tv" (from "mog.tv") "mog.vt" (from "mog.tv" or "omg.vt") "gom.tv" (from
 * "ogm.tv") "ogm.vt" (from "ogm.tv" or "omg.vt") For k = 3, there're 3 more
 * typos to consider:
 * 
 * "gmo.tv" (from "mgo.tv" of "gom.tv") "mgo.vt" (from "mgo.tv" or "mog.vt")
 * "gom.vt" (from "gom.tv" or "ogm.vt") Since n = 9, it's not enough to buy all
 * domains with 3 or fewer typos, but it's enough to buy with 2 or fewer, so the
 * answer is 2.
 * 
 * Note that equal domain strings that may be obtained differently are
 * considered the same.
 * 
 */
package JavaCodeFightBot.godaddy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.junit.Assert;

public class Typosquatting {

	public static void main(String[] args) {

		org.junit.runner.Result result = JUnitCore.runClasses(Typosquatting.class);
		System.out.println(" Total Test time in milisecond = " + result.getRunTime());
		System.out.println(" Total Test Count = " + result.getRunCount());
		System.out.println(" Total Failure Count = " + result.getFailureCount());
		System.out.println(" Failure:  " + result.getFailures());

	}

	static int typosquatting(int n, String domain) {

		final char dot = '.';
		TreeSet<String> domainTree = new TreeSet();
		LinkedList<String> lastLevelDomainList = new LinkedList();

		int level = 0;
		domainTree.add(domain);
		lastLevelDomainList.add(domain);
		int currentSize = 0;
		while (domainTree.size() <= n + 1) {
			currentSize = domainTree.size();
			Iterator<String> iterDomain = lastLevelDomainList.iterator();
			LinkedList<String> tempDomainList = new LinkedList();
			while (domainTree.size() <= n + 1 && iterDomain.hasNext()) {
				String e = iterDomain.next();
				for (int index = 0; index < e.length() - 1; ++index) {
					char a = e.charAt(index);
					char b = e.charAt(index + 1);
					if (a != dot && b != dot && a != b) {
						String newDomain = e.substring(0, index) + b + a + e.substring(index + 2, e.length());
						if (domainTree.add(newDomain)) {
							tempDomainList.add(newDomain);
						}
					}
					if (domainTree.size() > n + 1) {
						break;
					}
				}
			}

			lastLevelDomainList = tempDomainList;
			++level;
			if (currentSize == domainTree.size()) {
				break;
			}
		}

		if (currentSize == domainTree.size()) {
			return -1;
		} else
			return level - 1;
	}

	@Test
	public void Test1() {
		Assert.assertEquals(1, typosquatting(7, "godaddy.com"));
	}

	@Test
	public void Test2() {
		Assert.assertEquals(2, typosquatting(9, "omg.tv"));
	}

	@Test
	public void Test3() {
		Assert.assertEquals(1, typosquatting(8, "godaddy.com"));
	}

	@Test
	public void Test4() {
		Assert.assertEquals(0, typosquatting(1, "godaddy.com"));
	}

	@Test
	public void Test5() {
		Assert.assertEquals(0, typosquatting(0, "godaddy.com"));
	}

	@Test
	public void Test6() {
		Assert.assertEquals(-1, typosquatting(0, "aaa.aaa"));
	}

	@Test
	public void Test7() {
		Assert.assertEquals(-1, typosquatting(10, "aaa.aaa"));
	}

	@Test
	public void Test8() {
		Assert.assertEquals(2, typosquatting(86, "godaddy.godaddy.com"));
	}

	@Test
	public void Test9() {
		Assert.assertEquals(-1, typosquatting(0, "a.a"));
	}

	@Test
	public void Test10() {
		Assert.assertEquals(0, typosquatting(0, "a.ab"));
	}

	@Test
	public void Test11() {
		Assert.assertEquals(-1, typosquatting(1, "a.ab"));
	}

	@Test
	public void Test12() {
		Assert.assertEquals(0, typosquatting(0, "ab.a"));
	}

}

/*
 * Test 1 Input: n: 7 domain: "godaddy.com" Output: Empty Expected Output: 1
 * 
 * Test 2 n: 9 domain: "omg.tv" Output: Empty Expected Output: 2 Console Output:
 * Empty
 * 
 * 
 * Tes 3 n: 8 domain: "godaddy.com" Output: Empty Expected Output: 1
 * 
 * Test 4 Input: n: 1 domain: "godaddy.com" Output: Empty Expected Output: 0
 * Console Output: Empty
 * 
 * 
 * Test 5 Input: n: 0 domain: "godaddy.com" Output: Empty Expected Output: 0
 * Console Output: Empty
 * 
 * Test 6 n: 0 domain: "aaa.aaa" Output: Empty Expected Output: -1 Console
 * Output: Empty
 * 
 * Test 7 Input: n: 10 domain: "aaa.aaa" Output: Empty Expected Output: -1
 * Console Output: Empty
 * 
 * 
 * Test 8 Input: n: 85 domain: "godaddy.godaddy.com" Output: Empty Expected
 * Output: 2 Console Output: Empty
 * 
 * Test 9 Input: n: 0 domain: "a.a" Output: Empty Expected Output: -1 Console
 * Output: Empty
 * 
 * Input: n: 0 domain: "a.ab" Output: Empty Expected Output: 0 Console Output:
 * Empty
 * 
 * Input: n: 1 domain: "a.ab" Output: Empty Expected Output: -1 Console Output:
 * Empty
 * 
 * Input: n: 0 domain: "ab.a" Output: Empty Expected Output: 0 Console Output:
 * Empty
 * 
 */
