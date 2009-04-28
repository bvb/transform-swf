/*
 * RegisterCopy.java
 * Transform
 * 
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its contributors 
 *    may be used to endorse or promote products derived from this software 
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform.action;

import com.flagstone.transform.Strings;
import com.flagstone.transform.coder.Action;
import com.flagstone.transform.coder.ActionTypes;
import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

//TODO(doc) Review
/**
 * RegisterCopy is used to copy the item at the top of the stack to one of the
 * Flash Player's internal registers.
 * 
 * <p>
 * The Flash Player uses a stack to store values when executing the actions
 * associated with a button being pushed, frame being played, etc. If a value is
 * used repeatedly in a calculation, it must be pushed onto the stack each time
 * using an Push action. To speed up the execution of the calculation and reduce
 * the amount of code required the value can be saved to one of the internal
 * registers of the Flash Player using the RegisterCopy action. This copies the
 * value currently at the top of the stack into the specified register. Pushing
 * an RegisterIndex object onto the stack creates a reference to the register so
 * the Flash Player uses the value directly rather than pushing the value onto
 * the stack then immediately popping to use the value in a calculation.
 * </p>
 * 
 * <p>
 * The value is not removed from the stack. The number of registers supported
 * was expanded in Flash 7 from 4 to 256.
 * </p>
 * 
 * @see Register
 * @see Push
 */
public final class RegisterCopy implements Action {
	private static final String FORMAT = "RegisterCopy: { registerNumber=%d }";

	private int registerNumber;

	// TODO(doc)
	public RegisterCopy(final SWFDecoder coder) throws CoderException {
		coder.readByte();
		coder.readWord(2, false);
		registerNumber = coder.readByte();
	}

	/**
	 * Creates a RegisterCopy object with the register number.
	 * 
	 * @param anIndex
	 *            the number of one of the Flash Player's internal registers.
	 *            Must be in the range 0..255.
	 */
	public RegisterCopy(final int anIndex) {
		setRegisterNumber(anIndex);
	}

	// TODO(doc)
	public RegisterCopy(final RegisterCopy object) {
		registerNumber = object.registerNumber;
	}

	/**
	 * Returns the number of the Player register that the value on the stack
	 * will be copied to.
	 */
	public int getRegisterNumber() {
		return registerNumber;
	}

	/**
	 * Returns the number of the Player register that the value on the stack
	 * will be copied to.
	 * 
	 * @param anIndex
	 *            the number of one of the Flash Player's internal registers.
	 *            Must be in the range 0..255.
	 */
	public void setRegisterNumber(final int anIndex) {
		if (anIndex < 0 || anIndex > 255) {
			throw new IllegalArgumentException(Strings.REGISTER_RANGE);
		}
		registerNumber = anIndex;
	}

	public RegisterCopy copy() {
		return new RegisterCopy(this);
	}

	@Override
	public String toString() {
		return String.format(FORMAT, registerNumber);
	}

	public int prepareToEncode(final SWFEncoder coder, final Context context) {
		return 4;
	}

	public void encode(final SWFEncoder coder, final Context context)
			throws CoderException {
		coder.writeByte(ActionTypes.REGISTER_COPY);
		coder.writeWord(2, 2);
		coder.writeByte(registerNumber);
	}

	public void decode(final SWFDecoder coder, final Context context)
			throws CoderException {
		coder.readByte();
		coder.readWord(2, false);
		registerNumber = coder.readByte();
	}
}