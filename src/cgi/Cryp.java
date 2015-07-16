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

package cgi;

import dmjava.It;
import java.util.Random;

/**
 *
 * @version 1.0
 * @since 13-jun-2015
 * @author deme
 */
public class Cryp {
  static Random random = new Random(new java.util.Date().getTime());

  /**
   * Encodes a string to B41
   * @param s
   * @return
   */
  public static String s2b (String s) {
    return B41.compress(s);
  }

  /**
   * Decodes a string codified with s2b()
   * @param c
   * @return
   */
  public static String b2s (String c) {
    return B41.decompress(c);
  }

  /**
   * Generates a B41 random key of a length 'lg'
   * @param lg
   * @return
   */
  public static String genK (int lg) {
    return It.join(It.range(lg).map(i -> {
      return String.valueOf(B41.n2b(random.nextInt(41)));
    }), "");
  }

  /**
   * Returns 'k' codified in irreversible way, using 'lg' B41 digits.
   * @param k String to codify
   * @param lg  Length of result
   * @return 'lg' B41 digits
   */
  public static String key (String k, int lg) {
    int lg2 = k.length() * 2;
    if (lg2 < lg * 2) {
      lg2 = lg * 2;
    }
    k = k + "codified in irreversibleDeme is good, very good!\n\r8@@";
    while (k.length() < lg2) {
      k += k;
    }
    k = k.substring(0, lg2);
    int dt[] = B41.decodeInts(B41.encode(k));
    lg2 = dt.length;

    int sum = 0;
    int i = 0;
    while (i < lg2) {
      sum = (sum + dt[i]) % 256;
      dt[i] = (sum + i + dt[i]) % 256;
      ++i;
    }
    while (i > 0) {
      --i;
      sum = (sum + dt[i]) % 256;
      dt[i] = (sum + i + dt[i]) % 256;
    }

    return B41.encodeBytes(dt).substring(0, lg);
  }

  /**
   * Encodes 'm' with key 'k'.
   * @param k Key for encoding
   * @param m Message to encode
   * @return 'm' codified in B41 digits.
   */
  public static String cryp (String k, String m) {
    final String m2 = B41.encode(m);
    final String k2 = key(k, m2.length());
    return It.join(It.range(m2.length()).map(i -> {
      return String.valueOf(
        B41.n2b((B41.b2n(m2.charAt(i)) + B41.b2n(k2.charAt(i))) % 41));
    }), "");
  }

  /**
   * Decodes 'c' using key 'k'. 'c' was codified with cryp().
   * @param k Key for decoding
   * @param c Text codified with cryp()
   * @return 'c' decoded
   */
  public static String decryp (String k, String c) {
    final String k2 = key(k, c.length());
    return B41.decode(It.join(It.range(c.length()).map(i -> {
      int n = B41.b2n(c.charAt(i)) - B41.b2n(k2.charAt(i));
      return String.valueOf(B41.n2b((n >= 0) ? n : n + 41));
    }), ""));
  }

  /**
   * Encodes automatically 'm' with a random key of 'nk' digits.
   * @param nK Number of digits for random key (1 to 40 both inclusive)
   * @param m Text for encoding
   * @return 'm' encoded in B41 digits
   */
  public static String autoCryp (int nK, String m) {
    int k1 = random.nextInt(41);
    char n = B41.n2b((nK + k1) % 41);
    String k = genK(nK);
    return "" + B41.n2b(k1) +
      n +
      k +
      cryp(k, m);
  }

  /**
   * Decodes a text codified with autoCryp()
   * @param c Codified text
   * @return Decoded text
   */
  public static String autoDecryp (String c) {
    int c1 = B41.b2n(c.charAt(1)) - B41.b2n(c.charAt(0));
    int nK = (c1 >= 0) ? c1 : c1 + 41;
    return decryp(c.substring(2, 2 + nK), c.substring(2 + nK));
  }

  /**
   * Encodes 'm' with key 'k' and an autoKey of length 'nK'
   * @param k Key for encoding
   * @param nK Digits to generate autoKey (1 to 40 both inclusive)
   * @param m Message to encode
   * @return 'm' codified in B41 digits.
   */
  public static String encode (String k, int nK, String m) {
    return cryp(k, autoCryp(nK, m));
  }

  /**
   * Decodes a string codified with encode()
   * @param k Key for encoding
   * @param c Message encoded with encode()
   * @return 'c' decoded.
   */
  public static String decode (String k, String c) {
    return autoDecryp(decryp(k, c));
  }

}
