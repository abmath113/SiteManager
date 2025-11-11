package com.eps.sitemanager.model.userauth;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role implements Serializable {
	
	/**
	 * @author abhishek.thorat
	 */
	private static final long serialVersionUID = 4907783301864451905L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int roleId;
	
	@Column(name = "user_role", nullable = false)
	private String userRole;
	
	@Column(name = "role_desc")
	private String roleDesc;

}
