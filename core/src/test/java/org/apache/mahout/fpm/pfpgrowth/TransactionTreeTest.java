/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.fpm.pfpgrowth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionTreeTest extends MahoutTestCase {
  private static final Logger log = LoggerFactory.getLogger(TransactionTreeTest.class);

  private static final int MAX_DUPLICATION = 50;

  private static final int MAX_FEATURES = 30;

  private static final int MAX_TRANSACTIONS = 500000;

  private static final int MEGABYTE = 1000000;

  private static final int NUM_OF_FPTREE_FIELDS = 4;

  private static final int SIZE_INT = 4;

  private static final int SIZE_LONG = 8;

  private static final int SKIP_RATE = 10;

  private Random gen;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    gen = RandomUtils.getRandom();
  }

  public List<Integer> generateRandomArray() {
    List<Integer> list = new ArrayList<Integer>();
    for (int i = 0; i < MAX_FEATURES; i++) {
      if (gen.nextInt() % SKIP_RATE == 0) {
        list.add(i);
      }
    }
    return list;
  }

  public void testTransactionTree() {
    TransactionTree tree = new TransactionTree();
    int nodes = 0;
    int total = 0;
    for (int i = 0; i < MAX_TRANSACTIONS; i++) {
      List<Integer> array = generateRandomArray();
      total += array.size();
      nodes += tree.addPattern(array, 1 + gen.nextInt(MAX_DUPLICATION));
    }

    log.info("Input integers: " + total);
    log.info("Input data Size: " + total * SIZE_INT / (double) MEGABYTE);
    log.info("Nodes in Tree: " + nodes);
    log.info("Size of Tree: " + (nodes * SIZE_INT * NUM_OF_FPTREE_FIELDS + tree.childCount() * SIZE_INT)
        / (double) MEGABYTE);
    TransactionTree vtree = new TransactionTree();
    Iterator<Pair<List<Integer>, Long>> it = tree.getIterator();
    StringBuilder sb = new StringBuilder();
    int count = 0;
    int items = 0;
    while (it.hasNext()) {
      Pair<List<Integer>, Long> p = it.next();
      vtree.addPattern(p.getFirst(), p.getSecond());
      items += p.getFirst().size();
      count++;
      String s = p.toString();
      sb.append(s);

    }
    log.info("Number of transaction integers: " + items);
    log.info("Size of Transactions: " + (items * SIZE_INT + count * SIZE_LONG) / (double) MEGABYTE);
    log.info("Number of Transactions: " + count);
    tree.getCompressedTree();
    it = vtree.getIterator();
    StringBuilder sb1 = new StringBuilder();
    while (it.hasNext()) {
      Pair<List<Integer>, Long> p = it.next();
      String s = p.toString();
      sb1.append(s);

    }
    assertEquals(sb.toString(), sb1.toString());

    TransactionTree mtree = new TransactionTree();
    MultiTransactionTreeIterator mt = new MultiTransactionTreeIterator(vtree.getIterator());
    while (mt.hasNext()) {
      mtree.addPattern(mt.next(), 1);
    }

    it = mtree.getIterator();
    StringBuilder sb2 = new StringBuilder();
    while (it.hasNext()) {
      Pair<List<Integer>, Long> p = it.next();
      String s = p.toString();
      sb2.append(s);

    }
    assertEquals(sb.toString(), sb2.toString());
  }

}
