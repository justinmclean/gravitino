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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.gravitino.cli.FullName;
import org.apache.gravitino.cli.GravitinoOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFulllName {

  private Options options;
  private static final Map<String, String> DEFAULTS = new HashMap<>(System.getenv());
  private static Map<String, String> envMap;

  /* Used to simulate setting environment variables. */
  @BeforeAll
  public static void accessFields() throws Exception {
    envMap = new HashMap<>();
    Class<?> clazz = Class.forName("java.lang.ProcessEnvironment");
    Field environmentField = clazz.getDeclaredField("theEnvironment");
    Field unmodifiableEnvironmentField = clazz.getDeclaredField("theUnmodifiableEnvironment");
    removeStaticFinalAndSetValue(environmentField, envMap);
    removeStaticFinalAndSetValue(unmodifiableEnvironmentField, envMap);
  }

  private static void removeStaticFinalAndSetValue(Field field, Object value) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, value);
  }

  @BeforeEach
  public void setUp() {
    options = new GravitinoOptions().options();
    envMap.clear();
    envMap.putAll(DEFAULTS);
  }

  @Test
  public void entityFromCommandLineOption() throws Exception {
    String[] args = {"--metalake", "metalake_demo"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String metalakeName = fullName.getMetalakeName();
    assertEquals("metalake_demo", metalakeName);
  }

  @Test
  public void entityFromEnvironmentVariable() throws Exception {
    String[] args = {}; // No command line metalake option
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    System.getenv().put("GRAVITINO_METALAKE", "metalake_env");

    String metalakeName = fullName.getMetalakeName();
    assertEquals("metalake_env", metalakeName);
  }

  @Test
  public void entityFromFullNameOption() throws Exception {
    String[] args = {"--name", "metalakeA.catalogB.schemaC.tableD"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String metalakeName = fullName.getMetalakeName();
    assertEquals("metalakeA", metalakeName);
    String catalogName = fullName.getCatalogName();
    assertEquals("catalogB", catalogName);
    String schemaName = fullName.getSchemaName();
    assertEquals("schemaC", schemaName);
    String tableName = fullName.getTableName();
    assertEquals("tableD", tableName);
  }

  @Test
  public void entityFromFullNameOptionWithoutMetalake() throws Exception {
    String[] args = {"--name", "catalogB.schemaC.tableD"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    System.getenv().put("GRAVITINO_METALAKE", "metalake_env");

    String metalakeName = fullName.getMetalakeName();
    assertEquals("metalake_env", metalakeName);
    String catalogName = fullName.getCatalogName();
    assertEquals("catalogB", catalogName);
    String schemaName = fullName.getSchemaName();
    assertEquals("schemaC", schemaName);
    String tableName = fullName.getTableName();
    assertEquals("tableD", tableName);
  }

  @Test
  public void entityNotFound() throws Exception {
    String[] args = {};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String metalakeName = fullName.getMetalakeName();
    assertNull(metalakeName);
  }

  @Test
  public void catalogFromCommandLineOption() throws Exception {
    String[] args = {"--catalog", "catalogA"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String catalogName = fullName.getCatalogName();
    assertEquals("catalogA", catalogName);
  }

  @Test
  public void schemaFromCommandLineOption() throws Exception {
    String[] args = {"--schema", "schemaA"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String schemaName = fullName.getSchemaName();
    assertEquals("schemaA", schemaName);
  }

  @Test
  public void tableFromCommandLineOption() throws Exception {
    String[] args = {"--table", "tableA"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String tableName = fullName.getTableName();
    assertEquals("tableA", tableName);
  }

  @Test
  public void malformedName() throws Exception {
    String[] args = {"--name", "metalake.catalog"};
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);
    String tableName = fullName.getTableName();
    assertNull(tableName);
  }

  @Test
  public void missingName() throws Exception {
    String[] args = {}; // No name provided
    CommandLine commandLine = new DefaultParser().parse(options, args);
    FullName fullName = new FullName(commandLine);

    String namePart = fullName.getNamePart(GravitinoOptions.TABLE, 3);
    assertNull(namePart);
  }
}