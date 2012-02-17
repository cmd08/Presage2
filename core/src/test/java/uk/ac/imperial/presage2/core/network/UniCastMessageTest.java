/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 *
 * 	This file is part of Presage2.
 *
 *     Presage2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Presage2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser Public License
 *     along with Presage2.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 */
package uk.ac.imperial.presage2.core.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author Sam Macbeth
 *
 */
public class UniCastMessageTest extends MessageTest {

	protected NetworkAddress lastTo;
	
	/**
	 * Test method for {@link uk.ac.imperial.presage2.core.network.UnicastMessage#UnicastMessage(org.imperial.isn.presage2.core.messaging.Performative, uk.ac.imperial.presage2.core.network.NetworkAddress, uk.ac.imperial.presage2.core.network.NetworkAddress, uk.ac.imperial.presage2.core.Time)}.
	 */
	@Test
	@Override
	public void testMessage() {
		Message<?> m = this.getRandomMessage();
		
		assertNotNull(m);
		
	}

	@Override
	protected UnicastMessage<?> getRandomMessage() {
		this.lastTime = this.randomTime();
		this.lastFrom = this.randomAddress();
		this.lastTo = this.randomAddress();
		this.lastPerf = this.randomPerformative();
		return new UnicastMessage<Object>(lastPerf, lastFrom, lastTo, lastTime);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetTo() {
		UnicastMessage<?> m = this.getRandomMessage();
		assertEquals(this.lastTo, m.getTo());
	}

}