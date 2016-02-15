/*
 * Copyright 2016 Gregory Jensen
 *
 *
 * This file is part of concordion-json-compare-plugin.
 *
 * concordion-json-compare-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * concordion-json-compare-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with concordion-json-compare-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.thetasinner.concordion.extension.json.internal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ThetaSinner on 13/02/2016.
 *
 * Tooling for JSON comparison.
 *
 * The areJsonDocumentsSimilar method tries to check quickly if the documents are equal.
 * If the documents are not equal then a slower comparison is done which keeps track of any differences.
 * The results of the slower comparison are stored in jsonDiffLines to be displayed later.
 */
public class JsonComparer {
  private final List<String> jsonDiffLines = new ArrayList<String>();

  public boolean areJsonDocumentsSimilar(JSONObject jsonObjectExpected, JSONObject jsonObjectActual) {
    if (jsonObjectExpected.similar(jsonObjectActual)) {
      return true;
    }

    compareJsonObjects(jsonObjectExpected, jsonObjectActual, JsonKeyPath.DUMMY_ROOT_ELEMENT);
    return false;
  }

  public List<String> getJsonDiffLines()
  {
    return jsonDiffLines;
  }

  private void compareJsonObjects(JSONObject expected, JSONObject actual, String keyPath) {
    Set<String> expectedKeySet = expected.keySet();
    Set<String> actualKeySet = actual.keySet();

    checkNumberOfKeysMatches(expectedKeySet, actualKeySet, keyPath);

    for (final String key : mergeSets(expectedKeySet, actualKeySet)) {
      if (!expectedKeySet.contains(key)) {
        jsonDiffLines.add("Did not expect presence of key <" + key + ">" + JsonKeyPath.format(keyPath));
        continue;
      }

      if (!actualKeySet.contains(key)) {
        jsonDiffLines.add("Expected presence of key <" + key + ">" + JsonKeyPath.format(keyPath));
        continue;
      }

      routeComparison(expected, actual, keyPath, key);
    }
  }

  private void compareJsonArrays(JSONArray expected, JSONArray actual, String keyPath) {
    int expectedLength = expected.length();
    int actualLength = actual.length();

    checkArrayLengthsMatch(expectedLength, actualLength, keyPath);

    int maxArrayLength = Math.max(expectedLength, actualLength);
    for (int i = 0; i < maxArrayLength; i++) {
      if (i >= expectedLength) {
        jsonDiffLines.add("Extra array element <" + actual.get(i) + ">" + JsonKeyPath.format(keyPath));
        continue;
      }

      if (i >= actualLength) {
        jsonDiffLines.add("Missing array element <" + expected.get(i) + ">" + JsonKeyPath.format(keyPath));
        continue;
      }

      routeComparison(expected, actual, keyPath, i);
    }
  }

  private <E, A> void compareValues(E expected, A actual, String keyPath) {
    if (!expected.getClass().equals(actual.getClass())) {
      jsonDiffLines.add("Type mismatch, expected <" + expected.getClass().getName() + "> but was <" + actual.getClass().getName() + ">" + JsonKeyPath.format(keyPath));
    }

    if (!expected.equals(actual)) {
      jsonDiffLines.add("Value mismatch, expected <" + expected + "> but was <" + actual + ">" + JsonKeyPath.format(keyPath));
    }
  }

  private void routeComparison(JSONArray expected, JSONArray actual, String keyPath, int index) {
    Object expectedValue = expected.get(index);
    Object actualValue = actual.get(index);

    routeComparison(expectedValue, actualValue, JsonKeyPath.navigateTo(keyPath, index));
  }

  private void routeComparison(JSONObject expected, JSONObject actual, String keyPath, String key) {
    Object expectedValue = expected.get(key);
    Object actualValue = actual.get(key);

    routeComparison(expectedValue, actualValue, JsonKeyPath.navigateTo(keyPath, key));
  }

  private void routeComparison(Object expected, Object actual, String keyPath) {
    if ((expected instanceof JSONObject) && (actual instanceof JSONObject)) {
      compareJsonObjects((JSONObject) expected, (JSONObject) actual, keyPath);
    }
    else if ((expected instanceof JSONArray) && (actual instanceof JSONArray)) {
      compareJsonArrays((JSONArray) expected, (JSONArray) actual, keyPath);
    }
    else {
      compareValues(expected, actual, keyPath);
    }
  }

  private void checkNumberOfKeysMatches(Set<String> expectedKeySet, Set<String> actualKeySet, String keyPath) {
    if (expectedKeySet.size() != actualKeySet.size()) {
      jsonDiffLines.add("Expected number of keys <" + expectedKeySet.size() + "> but was <" + actualKeySet.size() + ">" + JsonKeyPath.format(keyPath));
    }
  }

  private void checkArrayLengthsMatch(int expectedLength, int actualLength, String keyPath) {
    if (expectedLength != actualLength) {
      jsonDiffLines.add("Expected array length <" + expectedLength + "> but was <" + actualLength + ">" + JsonKeyPath.format(keyPath));
    }
  }

  private Set<String> mergeSets(Set<String> expectedKeySet, Set<String> actualKeySet) {
    Set<String> mergedKeySet = new HashSet<String>(expectedKeySet.size());
    mergedKeySet.addAll(expectedKeySet);
    mergedKeySet.addAll(actualKeySet);
    return mergedKeySet;
  }
}
