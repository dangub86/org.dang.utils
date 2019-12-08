package farmtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDate;

@SpringBootApplication
public class SpringBootVuejsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootVuejsApplication.class, args);
	}

	public interface Computus {

		/**
		 * When does Easter happen in a given year?
		 * @param year Which year do you want to know about Easter? <em>e.g.</em> 2016
		 * @return the day of March. 1 = March 1st, 2 = March 2nd, 31 = March 31st, 32 = April 1st, 33 = April 2nd, et cetera.
		 */
		int getEaster(int year) throws IllegalArgumentException;
	}

	public static class IanTaylorComputus implements Computus {

		public static final IanTaylorComputus INSTANCE = new IanTaylorComputus();

		private IanTaylorComputus(){}

		public int getEaster(final int year) {
			if(year < 0){
				throw new IllegalArgumentException("getEaster: year must be non-negative, but was called with year = " + year);
			}

			final int a = year % 19;
			final int b = year >> 2;
			final int c = b / 25 + 1;
			final int d = (c * 3) >> 2;
			int e = ((a * 19) - ((c * 8 + 5) / 25) + d + 15) % 30;
			e += (29578 - a - e * 32) >> 10;
			e -= ((year % 7) + b - d + e + 2) % 7;
			return e;
		}
	}

	public static abstract class Easter4JBase {

		protected static int getEasterAsDayOfMarch(int year){
			return IanTaylorComputus.INSTANCE.getEaster(year);
		}

	}

	/**
	 * Figure out, for a given year, when Easter occurs.
	 * Returns JDK8 {@link java.time.LocalDate} instances.
	 *
	 * <h3>Usage</h3>
	 * <pre>
	 * Easter4J.{@link #getEaster(int) getEaster}(2016);</pre>
	 *
	 * @see IanTaylorComputus IanTaylorComputus for the underlying implementation
	 */
	public static class Easter4J extends Easter4JBase {

		private Easter4J(){}

		/**
		 * Figure out, for a given year, when Easter occurs.
		 *
		 * <h3>Usage</h3>
		 * <pre>
		 * LocalDate easter = Easter4J.getEaster(2016);
		 * assert easter.getMonth() == 3;
		 * assert easter.getDayOfMonth() == 27;</pre>
		 *
		 * @param year Non-negative integer representing the Gregorian year <em>e.g.</em> <code>2016</code>.
		 * @return JDK8 {@link java.time.LocalDate} for the day that Easter occurs on the given year
		 * @throws IllegalArgumentException if {@code year} is negative
		 */
		public static LocalDate getEaster(final int year) {
			final int dayOfMarch = getEasterAsDayOfMarch(year);
			return LocalDate.of(year, 3, 1).plusDays(dayOfMarch - 1);
		}
	}

	@Bean
	public void testEaster() {
		int year = 2050;
		LocalDate easter = Easter4J.getEaster(year);
		System.out.println("EASTER FOR " + year + " -> " + easter);
	}
	
	
	
	
		@Bean
	public void testDuration() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(3).plusHours(8).plusMinutes(43).plusSeconds(27);
		Duration diff = Duration.between(start, end);
		System.out.println("DURATION -> " + printDuration(diff));
	}

	private String printDuration(Duration diff) {
		return "PT" + diff.toDays() + "D" + toHours(diff) + "H" + toMinutes(diff) + "M" + toSeconds(diff) + "S";
	}

	private int toHours(Duration diff) {
		return (int) (diff.toHours() % 24);
	}

	private int toMinutes(Duration diff) {
		return (int) (diff.toMinutes() % 60);
	}

	private int toSeconds(Duration diff) {
		return (int) (diff.getSeconds() % 60);
	}
}
