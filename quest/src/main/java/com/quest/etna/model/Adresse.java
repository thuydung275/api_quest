package com.quest.etna.model;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 
@Table(name = "adresse")
public class Adresse {
	
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name="id", unique=true, nullable=false)
    private Integer id;
	
	@Column(name="rue", nullable=false, length=100)
	private String rue;
	
	@Column(name="postal_code", nullable=false, length=30)
	private String postalCode;
	
	@Column(name="city", nullable=false, length=50)
	private String city;
	
	@Column(name="country", nullable=false, length=50)
	private String country;
	
	@Column(name="creation_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	  
	@Column(name="updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@JoinColumn(name = "user_id", nullable = true)
    @ManyToOne(optional = true)
    @JsonIgnore
    private User user;
	
	public Adresse() {
    }
    
    public Adresse(Integer id, String rue, String postalCode, String city, String country, Date creationDate, Date updatedDate, User user) {
        this.id = id;
        this.rue = rue;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }
    
    public Adresse(Integer id, String rue, String postalCode, String city, String country, Date creationDate, Date updatedDate) {
        this(id, rue, postalCode, city, country, new Date(),new Date(), null);
    }
    
    public Adresse(Integer id, String rue, String postalCode, String city, String country, Date creationDate) {
        this(id, rue, postalCode, city, country, new Date(), null, null);
    }

    public Adresse(Integer id, String rue, String postalCode, String city, String country) {
        this(id, rue, postalCode, city, country, null, null, null);
    }
    
    public Adresse(Integer id, String rue, String postalCode, String city, String country, User user) {
        this(id, rue, postalCode, city, country, null, null, user);
    }
    
    public Adresse(Integer id, String rue, String postalCode, String city) {
        this(id, rue, postalCode, city, null, null, null, null);
    }

    public Adresse(Integer id, String rue, String postalCode) {
        this(id, rue, postalCode, null, null, null, null, null);
    }
    
    public Adresse(String rue, String postalCode, String city, String country) {
        this(null, rue, postalCode, city, country, null, null, null);
    }

    public Adresse(Integer id, String rue) {
        this(id, rue, null, null, null, null, null, null);
    }
    
    public Adresse(Integer id) {
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@PrePersist
	protected void onCreate() {
		this.creationDate = this.updatedDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((rue == null) ? 0 : rue.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Adresse other = (Adresse) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (rue == null) {
			if (other.rue != null)
				return false;
		} else if (!rue.equals(other.rue))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Adress [id=" + id + ", rue=" + rue + ", postalCode=" + postalCode + ", city=" + city + ", country="
				+ country + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}

}
