package texasTest;

import org.junit.Assert;
import org.junit.Test;
import texasHoldem.*;

public class TexasTest {

	@Test
	public void FullHouse() {
		Texas texas = new Texas();

		Assert.assertEquals("White wins - high card: Ace", texas.compare("2H 3D 5S 9C KD", "2C 3H 4S 8C AH"));
		Assert.assertEquals("Black wins - Full House", texas.compare("2H 4S 4C 2D 4H", "2S 8S AS QS 3S"));
		Assert.assertEquals("Black wins - high card: 9", texas.compare("2H 3D 5S 9C KD", "2C 3H 4S 8C KH"));
		Assert.assertEquals("Tie", texas.compare("2H 3D 5S 9C KD", "2D 3H 5C 9S KH"));
	}
}
