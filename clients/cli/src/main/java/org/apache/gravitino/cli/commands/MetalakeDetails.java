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

import org.apache.gravitino.cli.ErrorMessages;
import org.apache.gravitino.client.GravitinoClient;
import org.apache.gravitino.exceptions.NoSuchMetalakeException;

/** Displays the details of a metalake. */
public class MetalakeDetails extends Command {
  protected String metalake;

  /**
   * Displays metalake details.
   *
   * @param url The URL of the Gravitino server.
   * @param metalake The name of the metalake.
   */
  public MetalakeDetails(String url, String metalake) {
    super(url);
    this.metalake = metalake;
  }

  /** Displays the name and comment of a metalake. */
  public void handle() {
    String comment = "";
    try {
      GravitinoClient client = buildClient(metalake);
      comment = client.loadMetalake(metalake).comment();
    } catch (NoSuchMetalakeException err) {
      System.err.println(ErrorMessages.UNKNOWN_METALAKE);
      return;
    } catch (Exception exp) {
      System.err.println(exp.getMessage());
      return;
    }

    System.out.println(metalake + "," + comment);
  }
}