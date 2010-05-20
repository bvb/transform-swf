/*
 * DefineDataCodingTest.java
 * Transform
 *
 * Copyright (c) 2010 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.flagstone.transform;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

@RunWith(Parameterized.class)
public final class DefineDataCodingTest {

    private static final String CALCULATED_LENGTH =
        "Incorrect calculated length";
    private static final String NOT_FULLY_ENCODED =
        "Data was not fully encoded";
    private static final String NOT_FULLY_DECODED =
        "Data was not fully decoded";
    private static final String NOT_ENCODED =
        "Object was not encoded properly";
    private static final String NOT_DECODED =
        "Object was not decoded properly";

    @Test
    public void checkDefineDataIsEncoded() throws CoderException {
        final byte[] data = new byte[] {1, 2, 3, 4};
        final DefineData object = new DefineData(1, data);
        final byte[] binary = new byte[] {(byte) 0xCA, 0x15, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04};

        final SWFEncoder encoder = new SWFEncoder(binary.length);
        final Context context = new Context();

        final int length = object.prepareToEncode(context);
        object.encode(encoder, context);

        assertEquals(CALCULATED_LENGTH, binary.length, length);
        assertTrue(NOT_FULLY_ENCODED, encoder.eof());
        assertArrayEquals(NOT_ENCODED, binary, encoder.getData());
    }

    @Test
    public void checkDefineDataIsDecoded() throws CoderException {
        final byte[] data = new byte[] {1, 2, 3, 4};
        final byte[] binary = new byte[] {(byte) 0xCA, 0x15, 0x01, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04};

        final SWFDecoder decoder = new SWFDecoder(binary);
        final DefineData object = new DefineData(decoder);

        assertEquals(NOT_DECODED, 1, object.getIdentifier());
        assertEquals(NOT_DECODED, data, object.getData());
        assertTrue(NOT_FULLY_DECODED, decoder.eof());
   }

    @Test
    public void checkExtendedDefineDataIsDecoded() throws CoderException {
        final byte[] data = new byte[] {1, 2, 3, 4};
        final byte[] binary = new byte[] {(byte) 0xFF, 0x15,
                0x0A, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x02, 0x03, 0x04};

        final SWFDecoder decoder = new SWFDecoder(binary);
        final DefineData object = new DefineData(decoder);

        assertEquals(NOT_DECODED, 1, object.getIdentifier());
        assertEquals(NOT_DECODED, data, object.getData());
        assertTrue(NOT_FULLY_DECODED, decoder.eof());
   }
}