/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.gravitino.cli.commands;

import org.apache.gravitino.Metalake;
import org.apache.gravitino.client.GravitinoAdminClient;

public class AllMetalakeDetails extends Command {

  /**
   * Parameters needed to list all metalakes.
   *
   * @param url The URL of the Gravitino server.
   */
  public AllMetalakeDetails(String url) {
    super(url);
  }

  /** Displays the name and comment of all metalakes. */
  public void handle() {
    Metalake[] metalakes = new Metalake[0];
    try {
      GravitinoAdminClient client = buildAdminClient();
      metalakes = client.listMetalakes();
    } catch (Exception exp) {
      System.err.println(exp.getMessage());
      return;
    }

    StringBuilder all = new StringBuilder();
    for (int i = 0; i < metalakes.length; i++) {
      if (i > 0) {
        all.append(",");
      }
      all.append(metalakes[i].name() + "," + metalakes[i].comment() + System.lineSeparator());
    }

    System.out.print(all.toString());
  }
}