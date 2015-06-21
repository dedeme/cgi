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
import cgi.Cryp;

/**
 *
 * @author deme
 */
public class CrypTest {

  public CrypTest() {
  }

  @Test
  public void all() {
    assertEquals("0g", Cryp.s2b("a"));
    assertEquals("a", Cryp.b2s(Cryp.s2b("a")));
    assertEquals("1ghRRx0iRWBRWr", Cryp.s2b("ab cñç"));
    assertEquals("ab cñç", Cryp.b2s(Cryp.s2b("ab cñç")));
    assertEquals("RRbRRa0gVFR0hRRx0i", Cryp.s2b("\n\ta€b c"));
    assertEquals("\n\ta€b c", Cryp.b2s(Cryp.s2b("\n\ta€b c")));
    assertEquals(6, Cryp.genK(6).length());
    assertEquals("WpYzY", Cryp.key("Generaro", 5));
    assertEquals("VTlxr", Cryp.key("Generara", 5));

    assertEquals("01", Cryp.decryp("abc", Cryp.cryp("abc", "01")));
    assertEquals("11", Cryp.decryp("abcd", Cryp.cryp("abcd", "11")));
    assertEquals("", Cryp.decryp("abc", Cryp.cryp("abc", "")));
    assertEquals("a", Cryp.decryp("c", Cryp.cryp("c", "a")));
    assertEquals("ab c", Cryp.decryp("xxx", Cryp.cryp("xxx", "ab c")));
    assertEquals(
      "\n\ta€b c", Cryp.decryp("abc", Cryp.cryp("abc", "\n\ta€b c")));

    assertEquals("01", Cryp.autoDecryp(Cryp.autoCryp(8, "01")));
    assertEquals("11", Cryp.autoDecryp(Cryp.autoCryp(4, "11")));
    assertEquals("", Cryp.autoDecryp(Cryp.autoCryp(2, "")));
    assertEquals("a", Cryp.autoDecryp(Cryp.autoCryp(8, "a")));
    assertEquals("ab c", Cryp.autoDecryp(Cryp.autoCryp(4, "ab c")));
    assertEquals("\n\ta€b c", Cryp.autoDecryp(Cryp.autoCryp(2, "\n\ta€b c")));

    assertEquals("01", Cryp.decode("abc", Cryp.encode("abc", 2, "01")));
    assertEquals("11", Cryp.decode("abcd", Cryp.encode("abcd", 1, "11")));
    assertEquals("", Cryp.decode("abc", Cryp.encode("abc", 2, "")));
    assertEquals("a", Cryp.decode("c", Cryp.encode("c", 6, "a")));
    assertEquals("ab c", Cryp.decode("xxx", Cryp.encode("xxx", 40, "ab c")));
    assertEquals("\n\ta€b c",
      Cryp.decode("abc", Cryp.encode("abc", 2, "\n\ta€b c")));
  }

}
