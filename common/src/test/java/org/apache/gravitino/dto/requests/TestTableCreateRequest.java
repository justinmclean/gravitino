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
package org.apache.gravitino.dto.requests;

import org.apache.gravitino.dto.rel.ColumnDTO;
import org.apache.gravitino.dto.rel.indexes.IndexDTO;
import org.apache.gravitino.rel.indexes.Index;
import org.apache.gravitino.rel.types.Types;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTableCreateRequest {

  private static ColumnDTO[] createColumns() {
    return new ColumnDTO[] {
      ColumnDTO.builder().withName("id").withDataType(Types.IntegerType.get()).build()
    };
  }

  private static TableCreateRequest buildRequest(IndexDTO index) {
    return new TableCreateRequest(
        "tbl",
        null,
        createColumns(),
        null,
        null,
        null,
        null,
        new IndexDTO[] {index});
  }

  @Test
  public void testValidateWithNullIndexFieldNamesThrowsIllegalArgumentException() {
    IndexDTO invalidIndex = new IndexDTO(Index.IndexType.PRIMARY_KEY, "pk", null);

    TableCreateRequest request = buildRequest(invalidIndex);

    IllegalArgumentException exception =
        Assertions.assertThrows(IllegalArgumentException.class, request::validate);
    Assertions.assertTrue(
        exception.getMessage().contains("Index field names cannot be null or empty"));
  }

  @Test
  public void testValidateWithEmptyIndexFieldNamesThrowsIllegalArgumentException() {
    IndexDTO invalidIndex =
        new IndexDTO(Index.IndexType.PRIMARY_KEY, "pk", new String[][] {});

    TableCreateRequest request = buildRequest(invalidIndex);

    IllegalArgumentException exception =
        Assertions.assertThrows(IllegalArgumentException.class, request::validate);
    Assertions.assertTrue(
        exception.getMessage().contains("Index field names cannot be null or empty"));
  }
}
