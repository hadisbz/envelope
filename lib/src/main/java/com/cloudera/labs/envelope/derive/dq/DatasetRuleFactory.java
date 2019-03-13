/*
 * Copyright (c) 2015-2019, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.labs.envelope.derive.dq;

import com.cloudera.labs.envelope.load.LoadableFactory;
import com.typesafe.config.Config;

public class DatasetRuleFactory extends LoadableFactory {

  private static final String TYPE_CONFIG_NAME = "type";

  public static DatasetRule create(String name, Config config, boolean configure) {
    if (!config.hasPath(TYPE_CONFIG_NAME)) {
      throw new RuntimeException("Rule type not specified");
    }

    String ruleType = config.getString(TYPE_CONFIG_NAME);

    DatasetRule rule;
    // First, see if this is a Dataset rule
    try {
      rule = loadImplementation(DatasetRule.class, ruleType);
    } catch (Exception e) {
      // It's probably a row rule
      rule = new DatasetRowRuleWrapper();
    }

    if (configure) {
      rule.configure(name, config);
    }

    return rule;
  }

}
