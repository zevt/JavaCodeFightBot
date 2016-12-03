/*
 * Domain name forwarding lets GoDaddy domain owners automatically redirect their site visitors to a different site URL. Sometimes the visitors have to go through multiple redirects before ending up on the correct site.

Using the DNS Manager, GoDaddy customers can view redirects in a simple visual format. One handy feature is the ability to group the domains by the final website they redirect to. Your task is to implement this feature.

For the given redirects list, organize its domains into groups where for a specific group each domain eventually redirects visitors to the same website.

Example

For

redirects = [["godaddy.net", "godaddy.com"], 
             ["godaddy.org", "godaddycares.com"], 
             ["godady.com", "godaddy.com"],
             ["godaddy.ne", "godaddy.net"]]
the output should be

domainForwarding(redirects) = [["godaddy.com", "godaddy.ne", "godaddy.net", "godady.com"], 
                               ["godaddy.org", "godaddycares.com"]]
In the first group, "godaddy.ne" redirects to "godaddy.net", which in turn redirects to "godaddy.com". "godady.com" redirects visitors to "godaddy.com" as well.
In the second group, "godaddy.org" redirects visitors to "godaddycares.com".

Note, that domains in each group are sorted lexicographically. The first group goes before the second because "godaddy.com" is lexicographically smaller than "godaddycares.com".

Test:
***************************************************************
redirects: [["godaddy.net","godaddy.com"], 
 ["godaddy.org","godaddycares.com"], 
 ["godady.com","godaddy.com"], 
 ["godaddy.ne","godaddy.net"]]
Output:
Empty
Expected Output:
[["godaddy.com","godaddy.ne","godaddy.net","godady.com"], 
 ["godaddy.org","godaddycares.com"]]
 
 ******************************************
 Input:
redirects: [["a-b.c","a.c"], 
 ["aa-b.c","a-b.c"], 
 ["bb-b.c","a-b.c"], 
 ["cc-b.c","a-b.c"], 
 ["d-cc-b.c","bb-b.c"], 
 ["e-cc-b.c","bb-b.c"]]
Output:
Empty
Expected Output:
[["a-b.c","a.c","aa-b.c","bb-b.c","cc-b.c","d-cc-b.c","e-cc-b.c"]]


****************************************************************
redirects: [["a","b"]]
Output:
Empty
Expected Output:
[["a","b"]]

************************************** 4
redirects: [["c","d"], 
 ["f","b"]]
Output:
Empty
Expected Output:
[["b","f"], 
 ["c","d"]]
 ***************************************** 5
 Input:
redirects: [["a","z"], 
 ["c","b"]]
Output:
Empty
Expected Output:
[["b","c"], 
 ["a","z"]]
 
 *************************************
 * **/

package JavaCodeFightBot.godaddy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class DomainForwarding {
	private static String[][] Input01 = { { "godaddy.net", "godaddy.com" }, { "godaddy.org", "godaddycares.com" },
			{ "godady.com", "godaddy.com" }, { "godaddy.ne", "godaddy.net" } };

	private static String[][] ExpectOut01 = {{"godaddy.com","godaddy.ne","godaddy.net","godady.com"}, 
	                                         {"godaddy.org","godaddycares.com"}};

	private static String[][] Input02 = { { "a-b.c", "a.c" }, { "aa-b.c", "a-b.c" }, { "bb-b.c", "a-b.c" },
			{ "cc-b.c", "a-b.c" }, { "d-cc-b.c", "bb-b.c" }, { "e-cc-b.c", "bb-b.c" } };
	private static String[][] ExpectOut02 = {
			{ "a-b.c", "a.c", "aa-b.c", "bb-b.c", "cc-b.c", "d-cc-b.c", "e-cc-b.c" } }, Input03 = { { "a", "b" } };
	private static String[][] ExpectOut03 = { { "a", "b" } };
	private static String[][] Input04 = { { "c", "d" }, { "f", "b" } }, ExpectOut04 = { { "b", "f" }, { "c", "d" } };
	private static String[][] Input05 = { { "a", "z" }, { "c", "b" } };
	private static String[][] ExpectOut05 = { { "b", "c" }, { "a", "z" } };

	public static void main(String... args) {

			// Using JUnit test
		org.junit.runner.Result result = JUnitCore.runClasses(DomainForwarding.class);
		System.out.println(" Test Time Count in miliseconds = " + result.getRunTime());
		System.out.println(" Test Run Count =  " + result.getRunCount());
		System.out.println(" Test Failure Count =  " + result.getFailures());

	}

//	@Test
//	public void Test() {
//		Assert.assertArrayEquals(new String[]{"x","a","b"}, new String[] {"df","df","fd"});
//	}

	@Test
	public void Test1() {
		Assert.assertArrayEquals(ExpectOut01, domainForwarding(Input01));
	}


	@Test
	public void Test2() {
		Assert.assertArrayEquals(ExpectOut02, domainForwarding(Input02));
	}
	
	@Test
	public void Test3() {
		Assert.assertArrayEquals(ExpectOut03, domainForwarding(Input03));
	}

	@Test
	public void Test4() {
		Assert.assertArrayEquals(ExpectOut04, domainForwarding(Input04));
	}

	@Test
	public void Test5() {
		Assert.assertArrayEquals(ExpectOut05, domainForwarding(Input05));
	}


	static String[][] domainForwarding(String[][] redirects) {

		ArrayList<RedirectMap> listOfReMap = new ArrayList<>();
		listOfReMap.add(new RedirectMap().addNewDomainDesSet(redirects[0][0], redirects[0][1]));

		for (int i = 1; i < redirects.length; ++i) {
			boolean newDomainMap = true;
			for (int j = 0; j < listOfReMap.size(); ++j) {
				RedirectMap tempRM = listOfReMap.get(j);
				if (tempRM.accept(redirects[i][0], redirects[i][1])) {
					newDomainMap = false;
					break;
				}
			}
			if (newDomainMap) {
				listOfReMap.add(new RedirectMap().addNewDomainDesSet(redirects[i][0], redirects[i][1]));
			}
		}

		listOfReMap.sort(Comparator.comparing(RedirectMap::getDestination));

		listOfReMap.forEach(e -> {
			e.domainsTree.add(e.destination);
		});

		String[][] output;
		output = new String[listOfReMap.size()][];
		int index = 0;

		for (RedirectMap rediMap : listOfReMap) {
			output[index] = new String[rediMap.domainsTree.size()];
			int indexJ = 0;
			for (String s : rediMap.domainsTree) {
				output[index][indexJ++] = s;
			}
			++index;
		}

		return output;

	}

	static class RedirectMap {
		String destination;
		TreeSet<String> domainsTree = new TreeSet();

		public RedirectMap addNewDomainDesSet(String domain, String _destination) {
			domainsTree.add(domain);
			this.destination = _destination;
			return this;
		}

		String getDestination() {
			return destination;
		}

		boolean accept(String domain, String _destination) {
			if (this.destination == null || this.destination == "") {
				domainsTree.add(domain);
				this.destination = _destination;
				return true;
			} else if (this.destination.equals(_destination) || domainsTree.contains(_destination)) {
				domainsTree.add(domain);
				return true;
			} else if (domain.equals(this.destination)) {
				domainsTree.add(domain);
				destination = _destination;
				return true;
			}
			return false;
		}
	}

}
