package models;

/*
 * @Author(name="Lukas Pecak")
 */

public class Setting {
	public String apiUrl;
	
	public Setting(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public Setting() {}
	
	@Override
	public String toString() {
		return "Settings : |Api server url| ->" + apiUrl;
	}
}
