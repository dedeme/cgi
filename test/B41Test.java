/*
 * Copyright 13-jun-2015 ºDeme
 *
 * This file is part of 'cgi'.
 *
 * 'cgi' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * 'cgi' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 'cgi'.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.junit.Test;
import static org.junit.Assert.*;
import cgi.*;

/**
 *
 * @author deme
 */
public class B41Test {

  public B41Test() {
  }

  @Test
  public void all () {
    assertEquals('R', B41.n2b(cgi.B41.b2n('R')));
    assertEquals('f', B41.n2b(B41.b2n('f')));
    assertEquals('F', B41.n2b(B41.b2n('F')));
    assertEquals(0, B41.b2n('R'));
    assertEquals(14, B41.b2n('f'));
    assertEquals(40, B41.b2n('F'));
    assertEquals('R', B41.n2b(0));
    assertEquals('f', B41.n2b(14));
    assertEquals('F', B41.n2b(40));

    String s = "ARazrmona Gómez, Antonio (€)";
    assertEquals("RSpRTRRTgRTFRTxRTsRTuRTtRTgRRxRSvRWDRTsRTkRTFRSURRxRSpRTt" +
      "RTzRTuRTtRToRTuRRxRRFVFRRSR", B41.encode(s));
    assertEquals(s, B41.decode(B41.encode(s)));
    assertEquals("RSp7RgFxsutgRRxRSvRWD2skFRSURRxRSp5tzutouRRxRRFVFRRSR",
      B41.compress(s));
    assertEquals(s, B41.decompress(B41.compress(s)));

    int a[] = {0, 23, 116, 225};
    assertEquals("RRoixx", B41.encodeBytes(a));
    int ar[] = B41.decodeBytes(B41.encodeBytes(a));
    for (int i = 0; i < a.length; i++) {
      assertEquals(a[i], ar[i]);
    }

    int a2[] = {0, 23, 5, 116, 225};
    assertEquals("RRoRzTWl", B41.encodeBytes(a2));
    int a2r[] = B41.decodeBytes(B41.encodeBytes(a2));
    for (int i = 0; i < a2.length; i++) {
      assertEquals(a2[i], a2r[i]);
    }
  }
}
