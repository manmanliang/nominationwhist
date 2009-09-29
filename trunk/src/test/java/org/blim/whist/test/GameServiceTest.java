package org.blim.whist.test;

import java.util.List;

import org.blim.whist.GameService;
import org.blim.whist.Card;

import com.google.common.collect.Lists;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import junit.framework.TestCase;

public class GameServiceTest extends TestCase {

	private GameService svcGame = new GameService();

	public void testDealRoundOfThirteenCards() throws Exception {
		List<Card> one = Lists.newArrayList();
		List<Card> two = Lists.newArrayList();
		List<Card> three = Lists.newArrayList();
		List<Card> four = Lists.newArrayList();
		
		svcGame.dealRound(13, one, two, three, four);

		assertTrue("Players do not have 13 cards each [" + one.size() + ", " + two.size() + ", " + three.size() + ", " + four.size() + "].", one.size() == 13 && two.size() == 13 && three.size() == 13 && four.size() == 13);
	}

}
