/**
 * EnumStaticValues.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This java enumeration contains all static values of the KMIP 1.0 
 * specification. 
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright ï¿½ 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */

package ch.ntb.inf.kmip.process;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public enum EnumStaticValues {

	// first value is default
	ProtocolVersionMajor(1),
	ProtocolVersionMinor(2, 1, 0);

	private List<Integer> values;

	public Collection<Integer> getValues() {
		return values;
	}

	EnumStaticValues(Integer... values) {
		this.values = asList(values);
	}

	public boolean hasValue(int value) {
		return values.contains(value);
	}

	public int getDefault() {
		return values.get(0);
	}
}
