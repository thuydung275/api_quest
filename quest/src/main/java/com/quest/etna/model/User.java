package com.quest.etna.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;

@Entity 
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
	
	public enum UserRole {
		ROLE_USER,
		ROLE_ADMIN
	}
	
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
	  
	@Column(name="username", nullable=false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	  
	@Enumerated(EnumType.STRING)
	@Column(name="role", columnDefinition = "varchar(32) default 'ROLE_USER'")
	private UserRole userRole;
	  
	@Column(name="creation_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	  
	@Column(name="updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE, mappedBy="user", orphanRemoval=true)
    private List<Adresse> adresseList = new ArrayList<>();

	
	public User() {
    }
    
    public User(Integer id, String username, String password, UserRole userRole, Date creationDate, Date updatedDate, List<Adresse> adresseList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
        this.adresseList = adresseList;
    }
    
    public User(Integer id, String username, String password, UserRole userRole, Date creationDate) {
    	this(id, username, password, userRole, creationDate, new Date(), null);
    }
    
    public User(Integer id, String username, String password, UserRole userRole) {
    	this(id, username, password, userRole, new Date(), new Date(), null);
    }
    
    public User(Integer id, String username, String password) {
    	this(id, username, password, UserRole.ROLE_USER, new Date(), new Date(), null);
    }
    
    public User(Integer id, String username) {
    	this(id, username, null, UserRole.ROLE_USER, new Date(), new Date(), null);
    }
    
    public User(Integer id) {
    	this.id = id;
    }

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public Date getcreationDate() {
		return creationDate;
	}
	
	public void setcreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getupdatedDate() {
		return updatedDate;
	}
	
	public void setupdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@XmlTransient
    public List<Adresse> getAdresseList() {
        return adresseList;
    }

    public void setAdresseList(List<Adresse> adresseList) {
        this.adresseList = adresseList;
    }
    
    public void addAdresse(Adresse adresse) {
        this.adresseList.add(adresse);
    }
	
	@PrePersist
	protected void onCreate() {
		this.creationDate = new Date();
		this.updatedDate  = new Date();
		if (this.userRole == null) {
			this.userRole = UserRole.ROLE_USER;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userRole != other.userRole)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", userRole=" + userRole
				+ ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}
}
