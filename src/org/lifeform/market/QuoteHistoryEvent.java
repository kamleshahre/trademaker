package org.lifeform.market;

public class QuoteHistoryEvent {
	public enum EventType {
		MARKET_CHANGE, NEW_BAR
	}

	private final EventType eventType;

	public QuoteHistoryEvent(final EventType eventType) {
		this.eventType = eventType;
	}

	public EventType getType() {
		return eventType;
	}

}
