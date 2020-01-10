package exercises06;

import java.util.Optional;

public class Subscriber {
	int id;
	String name, surname, dateOfBirth;
	Boolean subscriptionPaid;
	Optional<String> title, address, town, country, postcode, gender;
	
	public Subscriber(String id, String name, String surname, String title, String address, String town, String country, String postcode, String subscriptionPaid, String gender, String dateOfBirth) {
		this.id = Integer.parseInt(id);
		this.name = name;
		this.surname = surname;
		this.subscriptionPaid = Boolean.parseBoolean(subscriptionPaid);
		this.dateOfBirth = dateOfBirth;
		this.title = title.equals("-") ? Optional.empty() : Optional.of(title);
		this.address = address.equals("-") ? Optional.empty() : Optional.of(address);
		this.town = town.equals("-") ? Optional.empty() : Optional.of(town);
		this.country = country.equals("-") ? Optional.empty() : Optional.of(country);
		this.postcode = postcode.equals("-") ? Optional.empty() : Optional.of(postcode);
		this.gender = gender.equals("-") ? Optional.empty() : Optional.of(gender);
	}
	
	@Override
	public String toString() {
		return this.id + " " +
				this.name + " " +
				this.surname;
	}
	
}
