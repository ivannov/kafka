package com.nosoftskills.kafka.user.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Payment {

	public static enum CARD_TYPES {
		VISA, MASTERCARD, AMERICANEXPRESS
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Payment payment = (Payment) o;
		return Objects.equals(name, payment.name) &&
				Objects.equals(cardNumber, payment.cardNumber) &&
				cardType == payment.cardType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, cardNumber, cardType);
	}

	@Override
	public String toString() {
		return "Payment{" +
				"name='" + name + '\'' +
				", cardNumber='" + cardNumber + '\'' +
				", cardType=" + cardType +
				'}';
	}
}