/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.core;

public final class MT {
	private int index;
	private static final int SIZE = 624;

	private final int[] mt_buffer = new int[SIZE];

	public MT() {
		java.util.Random r = new java.util.Random();
		for (int i = 0; i < SIZE; i++) {
			mt_buffer[i] = r.nextInt();
		}
		index = 0;
	}

	// ESCA-JAVA0076:
	// ESCA-JAVA0177:
	// ESCA-JAVA0123:
	public int random() {
		if (index == SIZE) {
			index = 0;
			int i = 0;
			int s;
			for (; i < SIZE - 397; i++) {
				s = (mt_buffer[i] & 0x80000000)
						| (mt_buffer[i + 1] & 0x7FFFFFFF);
				mt_buffer[i] = mt_buffer[i + 397] ^ (s >> 1)
						^ ((s & 1) * 0x9908B0DF);
			}
			for (; i < 623; i++) {
				s = (mt_buffer[i] & 0x80000000)
						| (mt_buffer[i + 1] & 0x7FFFFFFF);
				mt_buffer[i] = mt_buffer[i - (SIZE - 397)] ^ (s >> 1)
						^ ((s & 1) * 0x9908B0DF);
			}

			s = (mt_buffer[623] & 0x80000000) | (mt_buffer[0] & 0x7FFFFFFF);
			mt_buffer[623] = mt_buffer[396] ^ (s >> 1) ^ ((s & 1) * 0x9908B0DF);
		}
		return mt_buffer[index++];
	}
}
