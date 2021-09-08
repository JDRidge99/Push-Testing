package canopy.app.util;

/**
 * Possible RFC-2446 VEVENT calendar component methods.
 */
public enum CalendarMethod {
	PUBLISH,
	REQUEST,
	REPLY,
	ADD,
	CANCEL,
	REFRESH,
	COUNTER,
	DECLINECOUNTER
}