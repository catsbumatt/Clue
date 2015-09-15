package cluedo;

import cards.*;

/**
 * The secret Envelope which contains the murderer, the murder location and the
 * murder weapon
 *
 * @author catsbumatt
 *
 */
public class SecretEnvelope {

	private PersonCard murderer;
	private WeaponCard murderWeapon;
	private RoomCard murderLoc;

	public SecretEnvelope(Card pc, Card wc, Card rc) {
		murderer = (PersonCard) pc;
		murderWeapon = (WeaponCard) wc;
		murderLoc = (RoomCard) rc;
	}

	public PersonCard getMurderer() {
		return murderer;
	}

	public WeaponCard getMurderWeapon() {
		return murderWeapon;
	}

	public RoomCard getMurderLoc() {
		return murderLoc;
	}

}
