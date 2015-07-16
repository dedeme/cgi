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

import java.util.ArrayList;


/**
 *
 * @version 1.0
 * @since 13-jun-2015
 * @author deme
 */
public class B41 {
  static String chars =
    "RSTUVWXYZa" +
    "bcdefghijk" +
    "lmnopqrstu" +
    "vwxyzABCDE" +
    "F";

  /**
   * Returns the number of code B41 'b'
   * @param b
   * @return
   */
  public static int b2n (char b) {
    return chars.indexOf(b);
  }

  /**
   * Returns the B41 code whose number is 'n'
   * @param n
   * @return
   */
  public static char n2b (int n) {
    return chars.charAt(n);
  }

  /**
   * Encodes a string in B41
   * @param s
   * @return
   */
  public static String encode (String s) {
    StringBuilder r = new StringBuilder();
    int i;
    for (i = 0; i < s.length(); i++) {
      int n = s.codePointAt(i);
      int n1 = n / 41;
      r.append(chars.charAt(n1 / 41)).append(
        chars.charAt(n1 % 41)).append(
        chars.charAt(n % 41));
    }
    return r.toString();
  }

  /**
   * Decodes a string codified with encode
   * @param c
   * @return
   */
  public static String decode (String c) {
    StringBuilder r = new StringBuilder();
    int i = 0;
    while (i < c.length()) {
      int n1 = chars.indexOf(c.charAt(i++));
      int n2 = chars.indexOf(c.charAt(i++));
      int n3 = chars.indexOf(c.charAt(i++));
      r.append(new String(Character.toChars(1681 * n1 + 41 * n2 + n3)));
    }
    return r.toString();
  }

  /**
   * Encodes an int[] in B41
   * @param bs
   * @return
   */
  public static String encodeBytes (int[] bs) {
    int lg = bs.length;
    boolean odd = false;
    if (lg % 2 != 0) {
      odd = true;
      --lg;
    }
    StringBuilder r = new StringBuilder();
    int i = 0;
    while (i < lg) {
      int n = bs[i]  * 256 + bs[i + 1];
      int n1 = n / 41;
      r.append(chars.charAt(n1 / 41)).append(
        chars.charAt(n1 % 41)).append(
        chars.charAt(n % 41));
      i += 2;
    }
    if (odd) {
      int n = bs[i];
      r.append(chars.charAt(n / 41)).append(chars.charAt(n % 41));
    }

    return r.toString();
  }

  /**
   * Decodes an int[] codified with encodeBytes
   * @param c
   * @return
   */
  public static int[] decodeInts (String c) {
    int lg = c.length();
    boolean odd = false;
    if (lg % 3 != 0) {
      odd = true;
      lg -= 2;
    }
    ArrayList<Integer>r = new ArrayList<>();
    int i = 0;
    while (i < lg) {
      int n1 = chars.indexOf(c.charAt(i++));
      int n2 = chars.indexOf(c.charAt(i++));
      int n3 = chars.indexOf(c.charAt(i++));
      int n = 1681 * n1 + 41 * n2 + n3;
      r.add(n / 256);
      r.add(n % 256);
    }
    if (odd) {
      int n1 = chars.indexOf(c.charAt(i++));
      int n2 = chars.indexOf(c.charAt(i++));
      int n = 41 * n1 + n2;
      r.add(n);
    }
    return r.stream().mapToInt((Integer v) -> {
      return v;
    }).toArray();
  }

  /**
   * Decodes a bytes[] codified with encodeBytes
   * @param c
   * @return
   */
  public static byte[] decodeBytes (String c) {
    int lg = c.length();
    boolean odd = false;
    if (lg % 3 != 0) {
      odd = true;
      lg -= 2;
    }
    ArrayList<Byte>r = new ArrayList<>();
    int i = 0;
    while (i < lg) {
      int n1 = chars.indexOf(c.charAt(i++));
      int n2 = chars.indexOf(c.charAt(i++));
      int n3 = chars.indexOf(c.charAt(i++));
      int n = 1681 * n1 + 41 * n2 + n3;
      r.add((byte)(n / 256));
      r.add((byte)(n % 256));
    }
    if (odd) {
      int n1 = chars.indexOf(c.charAt(i++));
      int n2 = chars.indexOf(c.charAt(i++));
      int n = 41 * n1 + n2;
      r.add((byte)n);
    }

    byte[] rb = new byte[r.size()];
    for (i = 0; i < rb.length; i++){
      rb[i] = r.get(i);
    }
    return rb;
  }

  /**
   * Compressing a B41 code. It is useful to codify strings.
   * @param s
   * @return
   */
  public static String compress (String s) {
    String c = encode(s);
    int n = 0;
    int i = 0;
    String tmp = "";
    StringBuilder r = new StringBuilder();
    while (i < c.length()) {
      if (c.substring(i, i + 2).equals("RT")) {
        ++n;
        tmp = tmp + c.charAt(i + 2);
        if (n == 10) {
          r.append(String.valueOf(n - 1)).append(tmp);
          tmp = "";
          n = 0;
        }
      } else {
        if (n > 0) {
          r.append(String.valueOf(n - 1)).append(tmp);
          tmp = "";
          n = 0;
        }
        r.append(c.substring(i, i + 3));
      }
      i += 3;
    }
    if (n > 0) {
      r.append(String.valueOf(n - 1)).append(tmp);
    }
    return r.toString();
  }

  /**
   * Decompress a B41 code compressed with compress
   * @param c
   * @return
   */
  public static String decompress (String c) {
    StringBuilder r = new StringBuilder();
    int i = 0;
    while (i < c.length()) {
      char ch = c.charAt(i++);
      if (ch >= '0' && ch <= '9') {
        int n = Character.digit(ch, 10) + 1;
        for (int j = 0; j < n; j++) {
          ch = c.charAt(i++);
          r.append("RT").append(ch);
        }
      } else {
        r.append(ch);
        for (int j = 0; j < 2; j++) {
          ch = c.charAt(i++);
          r.append(ch);
        }
      }
    }
    return decode(r.toString());
  }

}
