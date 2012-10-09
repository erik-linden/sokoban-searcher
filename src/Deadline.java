import java.util.Calendar;

public class Deadline {

	private long t;

	/**
	 * Constructs a {@link Deadline} instance set <code>l</code> milliseconds
	 * into the future.
	 *
	 * @param l
	 *            the number of milliseconds until the deadline.
	 */
	public Deadline(long l) {
		t = Calendar.getInstance().getTimeInMillis() + l;
	}

	long timeUntil() {
		return t - Calendar.getInstance().getTimeInMillis();
	}

}
