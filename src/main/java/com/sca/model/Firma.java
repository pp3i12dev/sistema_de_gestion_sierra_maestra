package com.sca.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name="firma")
public class Firma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Column(name="firma")
    private String firma;
    
	@Lob
	private byte[] dedo1;

	@Lob
	private byte[] dedo2;
	
	@Lob
	private byte[] dedo3;

	@Lob
	private byte[] dedo4;

	public Firma(long id, String firma, byte[] dedo1, byte[] dedo2, byte[] dedo3, byte[] dedo4) {
		super();
		this.id = id;
		this.firma = firma;
		this.dedo1 = dedo1;
		this.dedo2 = dedo2;
		this.dedo3 = dedo3;
		this.dedo4 = dedo4;
	}

	public Firma() {
		super();
	}	
}
