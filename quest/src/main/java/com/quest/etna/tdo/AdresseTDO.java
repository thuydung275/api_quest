package com.quest.etna.tdo;

public class AdresseTDO {
	
	private Integer id;
	private String rue;
	private String postalCode;
	private String city;
	private String country;
    private Integer userId;
	
	public AdresseTDO() {
	}
	
	public AdresseTDO(Integer id, String rue, String postalCode, String city, String country, Integer userId) {
		this.id = id;
		this.rue = rue;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.userId = userId;
	}
	
	public AdresseTDO(Integer id, String rue, String postalCode, String city, String country) {
		this(id, rue, postalCode, city, country, null);
	}
	
	public AdresseTDO(Integer id, String rue, String postalCode, String city) {
		this(id, rue, postalCode, city, null, null);
	}

	public AdresseTDO(Integer id, String rue, String postalCode) {
		this(id, rue, postalCode, null, null, null);
	}
	
	public AdresseTDO(Integer id, String rue) {
		this(id, rue, null, null, null, null);
	}
	
	public AdresseTDO(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
