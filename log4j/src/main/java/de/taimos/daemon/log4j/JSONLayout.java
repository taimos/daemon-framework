package de.taimos.daemon.log4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import de.taimos.daemon.DaemonStarter;

public class JSONLayout extends Layout {
	
	@Override
	public void activateOptions() {
		//
	}
	
	@Override
	public String format(LoggingEvent event) {
		return this.createJSON(event);
	}
	
	@Override
	public boolean ignoresThrowable() {
		return false;
	}
	
	@Override
	public String getContentType() {
		return "application/json";
	}
	
	private String createJSON(LoggingEvent event) {
		Map<String, Object> log = new HashMap<>();
		log.put("daemon", DaemonStarter.getDaemonName());
		log.put("instance", DaemonStarter.getInstanceId());
		log.put("host", DaemonStarter.getHostname());
		log.put("phase", DaemonStarter.getCurrentPhase().name());
		log.put("timestamp", new Date(event.getTimeStamp()).toString());
		log.put("level", event.getLevel().toString());
		log.put("source", event.getLoggerName());
		log.put("message", event.getRenderedMessage());
		log.put("thread", event.getThreadName());
		
		if (event.getThrowableInformation() != null) {
			Throwable throwable = event.getThrowableInformation().getThrowable();
			List<String> stacktrace = new ArrayList<String>();
			log.put("throwable", throwable.toString());
			for (StackTraceElement ste : throwable.getStackTrace()) {
				stacktrace.add(ste.toString());
			}
			log.put("stacktrace", stacktrace);
		}
		if (event.getProperties() != null) {
			log.put("mdc", event.getProperties());
		}
		StringBuilder sb = new StringBuilder();
		this.addObject(sb, log);
		sb.append(Layout.LINE_SEP);
		return sb.toString();
	}
	
	private void addObject(StringBuilder sb, Map<String, Object> map) {
		sb.append("{");
		Set<Entry<String, Object>> entrySet = map.entrySet();
		boolean first = true;
		for (Entry<String, Object> entry : entrySet) {
			if (first) {
				first = false;
			} else {
				this.addSeparator(sb);
			}
			this.addField(sb, entry.getKey(), entry.getValue());
		}
		sb.append("}");
	}
	
	private void addField(StringBuilder sb, String field, Object value) {
		// Add field name
		sb.append("\"");
		sb.append(this.cleanString(field));
		sb.append("\":");
		
		this.addValue(sb, value);
	}
	
	private void addSeparator(StringBuilder sb) {
		sb.append(",");
	}
	
	private void addArray(StringBuilder sb, Object... values) {
		sb.append("[");
		for (int i = 0; i < values.length; i++) {
			this.addValue(sb, values[i]);
			if (i != (values.length - 1)) {
				this.addSeparator(sb);
			}
		}
		sb.append("]");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void addValue(StringBuilder sb, Object value) {
		if (value instanceof String) {
			sb.append("\"");
			sb.append(this.cleanString((String) value));
			sb.append("\"");
		} else if (value instanceof Map) {
			this.addObject(sb, (Map<String, Object>) value);
		} else if (value instanceof Object[]) {
			this.addArray(sb, (Object[]) value);
		} else if (value instanceof List) {
			this.addArray(sb, ((List) value).toArray());
		} else {
			throw new RuntimeException("Invalid value: " + value);
		}
	}
	
	private String cleanString(String value) {
		return value.replaceAll("\"", "");
	}
}
