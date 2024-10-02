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

package org.apache.gravitino.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/* Gravitino Command line options */
public class GravitinoOptions {
  public static final String HELP = "help";
  public static final String VERSION = "version";
  public static final String URL = "url";
  public static final String METALAKE = "metalake";
  public static final String CATALOG = "catalog";
  public static final String SCHEMA = "schema";
  public static final String TABLE = "table";
  public static final String NAME = "name";
  public static final String ENTITY = "entity";
  public static final String COMMAND = "command";
  public static final String LIST = "list";
  public static final String DETAILS = "details";
  public static final String CREATE = "create";
  public static final String UPDATE = "update";
  public static final String DELETE = "delete";

  /**
   * Builds and returns the CLI options for Gravitino.
   *
   * @return Valid CLI command options.
   */
  public Options options() {
    Options options = new Options();

    // Add options using helper method to avoid repetition
    options.addOption(createSimpleOption("h", HELP, "command help information"));
    options.addOption(createSimpleOption("v", VERSION, "Gravitino version"));
    options.addOption(createArgOption("u", URL, "Gravitino URL (default: http://localhost:8090)"));
    options.addOption(createArgOption("m", METALAKE, "metalake name"));
    options.addOption(createArgOption("c", CATALOG, "catalog name"));
    options.addOption(createArgOption("s", SCHEMA, "schema name"));
    options.addOption(createArgOption("t", TABLE, "table name"));
    options.addOption(createArgOption("f", NAME, "full entity name (dot separated)"));
    options.addOption(createArgOption("e", ENTITY, "entity type"));

    // One way of specifying the command
    options.addOption(
        createArgOption("x", COMMAND, "one of: list, details, create, delete, or update"));

    // Another way of specifying the command
    options.addOption(createSimpleOption("L", LIST, "list entity children"));
    options.addOption(createSimpleOption("D", DETAILS, "list details about an entity"));
    options.addOption(createSimpleOption("C", CREATE, "create an entity"));
    options.addOption(createSimpleOption("U", UPDATE, "update an entity"));
    options.addOption(createSimpleOption("R", DELETE, "delete an entity"));

    return options;
  }

  /**
   * Helper method to create an Option that does not require arguments.
   *
   * @param shortName The option name as a single letter
   * @param longName The long option name.
   * @param description The option description.
   * @return The Option object.
   */
  public Option createSimpleOption(String shortName, String longName, String description) {
    return new Option(shortName, longName, false, description);
  }

  /**
   * Helper method to create an Option that requires an argument.
   *
   * @param shortName The option name as a single letter
   * @param longName The long option name.
   * @param description The option description.
   * @return The Option object.
   */
  public Option createArgOption(String shortName, String longName, String description) {
    return new Option(shortName, longName, true, description);
  }
}