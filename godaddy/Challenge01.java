
/*
 * GoDaddy makes a lot of different top-level domains available to its customers. A top-level domain is one that goes directly after the last dot ('.') in the domain name, for example .com in example.com. To help the users choose from available domains, GoDaddy is introducing a new feature that shows the type of the chosen top-level domain. You have to implement this feature.
To begin with, you want to write a function that labels the domains as "commercial", "organization", "network" or "information" for .com, .org, .net or .info respectively.
For the given list of domains return the list of their labels.
 * */
package JavaCodeFightBot.godaddy;


//import java.util.List

public class Challenge01 {
	public static void main(String... args) {
		String[] output = domainType(new String[] { "en.wiki.org", "codefights.com", "happy.net", "code.info" });
		for (String s : output) {
			System.out.println(s);
		}

	}

	static String[] domainType(String[] domains) {

		String[][] Domains = { { ".org", "organization" }, { ".com", "commercial" }, { ".net", "network" },
				{ ".info", "information" } };
		String[] output = new String[domains.length];
		for (int i = 0; i < domains.length; ++i) {
			for (int j = 0; j < Domains.length; ++j) {
				if (domains[i].endsWith(Domains[j][0])) {
					output[i] = Domains[j][1];
					break;
				}

			}
		}
		return output;
	}
}
