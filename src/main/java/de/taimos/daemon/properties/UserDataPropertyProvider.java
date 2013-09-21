package de.taimos.daemon.properties;

public class UserDataPropertyProvider extends SimpleHTTPPropertyProvider {
	
	public UserDataPropertyProvider() {
		super("http://169.254.169.254/latest/user-data");
	}
	
}
