import jdepend.framework.util.MetricsFormat;
import junit.framework.TestCase;

public class MetricsFormatTest extends TestCase {

	public void testFormatScore() {
		float m = 48.4F;

		Float s = MetricsFormat.toFormattedScore(m);

		System.out.println(s);

	}

}
