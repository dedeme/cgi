/*
 * Copyright 11-jul-2014 ºDeme
 *
 * This file is part of 'dmDoc'.
 *
 * 'dmDoc' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * 'dmDoc' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 'dmDoc'.  If not, see <http://www.gnu.org/licenses/>.
 */

/*globals dmdoc:true */

dmdoc = {};
(function (ns) {
  'use strict';

  var
    csec,
    csubsec,
    csubsubsec,

    sec_with_number,
    subsec_with_number,
    subsubsec_with_number,

    index;

  csec = 0;
  csubsec = 0;
  csubsubsec = 0;

  sec_with_number = true;
  subsec_with_number = true;
  subsubsec_with_number = true;

  index = "";

  /**
   * Allows put and quit section number.
   * @param {!boolean} value Indicates whether section number is put ("true")
   *  or not ("false")
   */
  ns.sectionWithNumber = function (value) {
    sec_with_number = value;
  };

  /**
   * Allows put and quit subsection number.
   * @param {!boolean} value Indicates whether subsection number is put
   *  ("true") or not ("false")
   */
  ns.subsectionWithNumber = function (value) {
    subsec_with_number = value;
  };

  /**
   * Allows put and quit subsubsection number.
   * @param {!boolean} value Indicates whether subsubsection number is put
   *  ("true") or not ("false")
   */
  ns.subsubsectionWithNumber = function (value) {
    subsubsec_with_number = value;
  };

  /**
   * Initializes the document and set title and subtitle.
   * @param {!string} title Title of the document.
   * @param {string=} subtitle Subtitle of the document.
   */
  ns.begin = function (title, subtitle) {
    subtitle = subtitle
      ? "<br><span class='subtitle'>" + subtitle + "</span>"
        : "";
    document.write(
      "<p class='title'>" +
        "<span class='title'>" + title + "</span>" +
        subtitle +
        "</p>" +
        "<div id='dmdoc_index'></div>"
    );
  };

  /**
   * Writes a section entry and adds it to index.
   * @param {!string} name Text of entry.
   * @param {string=} id Identifier of anchor.
   */
  ns.s = function (name, id) {
    var
      ref,
      text;

    csec = csec + 1;
    csubsec = 0;
    csubsubsec = 0;

    ref = id || "dm_sec_" + csec;

    index = index +
      "<tr><td align='right'>" +
      (sec_with_number ? csec + "." : "") +
      "</td><td colspan='3'>" +
      "<a class='index' href='#" + ref + "'>" + name + "</a>" +
      "</td></tr>";

    text = sec_with_number ? csec + ". " + name : name;
    document.write(
      "<p align='right'><a class='top' href='#dmdoc_top'>[ Top ]</a><p>" +
        "<hr><h1 id='" + ref + "'>" + text + "</h1>"
    );
  };

  /**
   * Writes a subsection entry and adds it to index.
   * @param {!string} name Text of entry.
   * @param {string=} id Identifier of anchor.
   */
  ns.ss = function (name, id) {
    var
      ref,
      text;

    csubsec = csubsec + 1;
    csubsubsec = 0;

    ref = id || "dm_sec_" + csec + "_" + csubsec;

    index = index +
      "<tr><td></td><td align='right'>" +
      ((sec_with_number && subsec_with_number)
        ? csec + "." + csubsec + "."
          : "&nbsp;&nbsp;&nbsp;&nbsp;") +
      "</td><td colspan='2'>" +
      "<a class='index' href='#" + ref + "'>" + name + "</a>" +
      "</td></tr>";

    text = (sec_with_number && subsec_with_number)
      ? csec + "." + csubsec + ". " + name
        : name;
    document.write("<h2 id='" + ref + "'>" + text + "</h2>");
  };

  /**
   * Writes a subsubsection entry and adds it to index.
   * @param {!string} name Text of entry.
   * @param {string=} id Identifier of anchor.
   */
  ns.sss = function (name, id) {
    var
      ref,
      text;

    csubsubsec = csubsubsec + 1;

    ref = id || "dm_sec_" + csec + "_" + csubsec + "_" + csubsubsec;

    index = index +
      "<tr><td></td><td></td><td align='right'>" +
      ((sec_with_number && subsec_with_number && subsubsec_with_number)
        ? csec + "." + csubsec + "." + csubsubsec
          : "&nbsp;&nbsp;&nbsp;&nbsp;") +
      "</td><td>" +
      "<a class='index' href='#" + ref + "'>" + name + "</a>" +
      "</td></tr>";

    text = (sec_with_number && subsec_with_number && subsubsec_with_number)
      ? csec + "." + csubsec + "." + csubsubsec + ". " + name
        : name;
    document.write("<h3 id='" + ref + "'>" + text + "</h3>");
  };

  ns.end = function () {
    var
      i;

    document.getElementById("dmdoc_index").innerHTML =
      "<hr id='dmdoc_top'><table class='index'>" + index + "</table>";
    document.write(
      "<p align='right'><a class='top' href='#dmdoc_top'>[ Top ]</a><p>" +
        "<hr>"
    );
    for (i = 0; i < 12; ++i) {
      document.write("<p>&nbsp</p>");
    }
  };

}(dmdoc));
