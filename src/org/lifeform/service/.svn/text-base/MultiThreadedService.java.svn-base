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
package org.lifeform.service;

import java.util.concurrent.Executor;

public class MultiThreadedService {
	private class ProcessTask implements Runnable {
		@SuppressWarnings("unused")
		private final Object someObject;

		public ProcessTask(final Object anObject) {
			this.someObject = anObject;
		}

		public void run() {
		}
	}

	private final Executor taskExecutor;

	public MultiThreadedService(final Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void process(final Object anObject) {
		taskExecutor.execute(new ProcessTask(anObject));
	}
}
