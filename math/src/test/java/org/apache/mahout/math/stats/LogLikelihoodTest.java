package org.apache.mahout.math.stats;
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

import org.junit.Assert;
import org.junit.Test;

public class LogLikelihoodTest extends Assert {

  @Test
  public void testEntropy() throws Exception {

    assertEquals(1.386294, LogLikelihood.entropy(1, 1), 0.0001);
    //TODO: more tests here
    try {
      LogLikelihood.entropy(-1, -1);//exception
      fail();
    } catch (IllegalArgumentException e) {
      
    }
  }

  @Test
  public void testLogLikelihood() throws Exception {
    //TODO: check the epsilons
    assertEquals(2.772589, LogLikelihood.logLikelihoodRatio(1, 0, 0, 1), 0.0001);
    assertEquals(27.72589, LogLikelihood.logLikelihoodRatio(10, 0, 0, 10), 0.0001);
    assertEquals(39.33052, LogLikelihood.logLikelihoodRatio(5, 1995, 0, 100000), 0.0001);
    assertEquals(4730.737, LogLikelihood.logLikelihoodRatio(1000, 1995, 1000, 100000), 0.001);
    assertEquals(5734.343, LogLikelihood.logLikelihoodRatio(1000, 1000, 1000, 100000), 0.001);
    assertEquals(5714.932, LogLikelihood.logLikelihoodRatio(1000, 1000, 1000, 99000), 0.001);
  }

  @Test
  public void testRootLogLikelihood() throws Exception {
    // positive where k11 is bigger than expected.
    assertTrue(LogLikelihood.rootLogLikelihoodRatio(904, 21060, 1144, 283012) > 0.0);
    
    // negative because k11 is lower than expected
    assertTrue(LogLikelihood.rootLogLikelihoodRatio(36, 21928, 60280, 623876) < 0.0);
  }
}
