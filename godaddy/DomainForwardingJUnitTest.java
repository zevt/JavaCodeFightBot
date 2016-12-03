package JavaCodeFightBot.godaddy;

import org.junit.runner.JUnitCore;

public class DomainForwardingJUnitTest {
	public static void main(String... args) {
		org.junit.runner.Result result = JUnitCore.runClasses(DomainForwarding.class);
		System.out.println("Tests run = " + result.getRunCount() + "\nTests failed = " + result.getFailures());
	}
	// sysout
}
