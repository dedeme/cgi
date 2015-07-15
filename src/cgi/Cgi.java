/*
 * Copyright 15-jun-2015 ºDeme
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

import dmjava.Io;
import dmjava.It;
import dmjava.net.Json;
import dmjava.net.JsonMaker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @version 1.0
 * @since 13-Jun-2015
 * @author deme
 */
public class Cgi {

  public static File cgiDir = Io.file("dmcgi");
  static int portNumber = 12565;

  static Json wwwread(String request) {
    return Json.fromJs(B41.decompress(request));
  }

  static void wwwsend(PrintWriter out, Json response) {
    out.println("Content-Type: text/plain");
    out.println();
    out.print(B41.compress(response.toString()));
    out.close();
  }

  static void wwwerror(PrintWriter out, Json request, Exception ex) {
    wwwsend(out, new JsonMaker()
      .add("error",
        ex.getMessage() + "\n"
        + request.toString() + "\n"
        + It.join(It.from(ex.getStackTrace()).map(
            e -> {
              return e.toString();
            })))
      .toJson());
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try (
      ServerSocket serverSocket = new ServerSocket(portNumber);) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));

        Runnable rn= () -> {
          try {
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
              if (inputLine.equals("-end-")) {
                out.println("java-cgi server end");
                System.exit(0);
              }
              Hub.process(out, inputLine);

              clientSocket.close();
              out.close();
              in.close();
            }
          } catch (IOException ex) {
            Logger.getLogger(Cgi.class.getName()).log(Level.SEVERE, null, ex);
          }
        };

        Thread th = new Thread(rn);
        th.start();
      }
    } catch (IOException ex) {
      System.out.println("Exception caught when trying to listen on port "
        + portNumber + " or listening for a connection");
      System.out.println(ex.getMessage());
    }
  }
}
