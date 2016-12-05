package com.nosoftskills.kafka.user.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PAYMENT")
public class Payment implements Serializable {

	public static enum CARD_TYPES {
		VISA, MASTERCARD, AMERICANEXPRESS
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Version
	private int version;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String cardNumber;

    @Column
    @Enumerated
    private CARD_TYPES cardType;

    public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Payment)) {
			return false;
		}
		Payment other = (Payment) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public CARD_TYPES getCardType() {
		return cardType;
	}

	public void setCardType(CARD_TYPES cardType) {
		this.cardType = cardType;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (name != null && !name.trim().isEmpty())
			result += "name: " + name;
		if (cardNumber != null && !cardNumber.trim().isEmpty())
			result += ", cardNumber: " + cardNumber;
		return result;
	}
}