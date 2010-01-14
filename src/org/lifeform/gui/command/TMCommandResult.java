package org.lifeform.gui.command;

public class TMCommandResult {
	public boolean clearSelection;
	public boolean clearBlock;
	public boolean quitFlag;

	public TMCommandResult(final boolean clearSelection, final boolean clearBlock, final boolean quitFlag) {
		this.clearSelection = clearSelection;
		this.clearBlock = clearBlock;
		this.quitFlag = quitFlag;
	}
}
