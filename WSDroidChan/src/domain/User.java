package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement  // Necesario en todos los domains, para la transferencias en json
public class User{
	
	private String login;
	private String password;
	private String registryDate;
	private String email;
	
	//Tienen que llevar por cojones un constructor vacio
	public User(){
		super();
	}
	
	public User(String login, String password, String email, String registryDate) {
		super();
		this.login = login;
		this.password = password;
		this.registryDate = registryDate;
		this.email = email;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegistryDate() {
		return registryDate;
	}
	public void setRegistryDate(String registryDate) {
		this.registryDate = registryDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((registryDate == null) ? 0 : registryDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (registryDate == null) {
			if (other.registryDate != null)
				return false;
		} else if (!registryDate.equals(other.registryDate))
			return false;
		return true;
	}
	
}


