/*
 * Copyright 17-jun-2015 ºDeme
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
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @version 1.0
 * @since 17-jun-2015
 * @author deme
 */
public class Hub {

  static int readCounter = 0;
  static boolean writeProcess = false;

  ScriptEngine engine;
  Invocable invocable;
  PrintWriter out;
  String request;
  String appName;
  String script;
  String func;

  public Hub(
    PrintWriter out, String request
  ) {
    engine = new ScriptEngineManager().getEngineByName("nashorn");
    invocable = (Invocable) engine;
    this.out = out;
    this.request = B41.decompress(request);
  }

  void wwwsend() {
    try {
      String response = (String) invocable.invokeFunction(func, request);
      out.println("Content-Type: text/plain");
      out.println();
      out.print(B41.compress(response));
      out.close();
    } catch (Exception e) {
      wwwerror(e);
    }
  }

  void wwwerror(Exception ex) {
    String response = ex.getMessage() + "\n"
      + request + "\n"
      + It.join(It.from(ex.getStackTrace()).map(
          e -> {
            return e.toString();
          }));
    out.println("Content-Type: text/plain");
    out.println();
    out.print(B41.compress(response));
    out.close();
  }

  void eval() {
    try {
      engine.eval(new FileReader(
        "scripts" + File.separator + appName + File.separator + script
      ));
      wwwsend();
    } catch (Exception e) {
      wwwerror(e);
    }
  }

  public static void process(PrintWriter out, String request) {
    Hub hub = new Hub(out, request);
    try {
      hub.engine.eval(""
        + "var hub_send = function(data) {"
        + " var d = JSON.parse(data);"
        + "  return Java.to("
        + "    [d.sync, d.app_name, d.script, d.func], "
        + "    'java.lang.String[]');"
        + "}"
      );
      String[] data = (String[]) hub.invocable.invokeFunction(
        "hub_send", hub.request);
      String sync = data[0];
      hub.appName = data[1];
      hub.script = data[2];
      hub.func = data[3];

      switch (sync) {
        case "SEQ":
          processSeq(hub);
        case "READ":
          processRW(hub, true);
        case "WRITE":
          processRW(hub, false);
        default:
          hub.wwwerror(
            new Exception(sync + " is not a synchronization operation"));
      }
    } catch (Exception e) {
      hub.wwwerror(e);
    }
  }

  public static void processSeq(Hub hub) {
    hub.eval();
  }

  @SuppressWarnings("SleepWhileHoldingLock")
  synchronized public static void processRW(Hub hub, boolean isRead) {
    if (isRead) {
      while (writeProcess) {
        try {
          Thread.sleep(5);
        } catch (InterruptedException ex) {
          Logger.getLogger(Hub.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      readCounter++;
      processSeq(hub);
      readCounter--;
    } else {
      while (writeProcess) {
        try {
          Thread.sleep(5);
        } catch (InterruptedException ex) {
          Logger.getLogger(Hub.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      writeProcess = true;
      while (readCounter > 0) {
        try {
          Thread.sleep(5);
        } catch (InterruptedException ex) {
          Logger.getLogger(Hub.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      processSeq(hub);
      writeProcess = false;
    }
  }
}
