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

import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ThetaSinner on 13/02/2016.
 *
 * This does the actual comparison of JSON and reports success/failure.
 */
public class JsonAssertCommand extends AbstractCommand {
  private final EventWrapper eventWrapper = new EventWrapper();
  private final JsonComparer jsonComparer = new JsonComparer();

  @Override
  public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
    JSONObject expected = getExpected(commandCall);
    JSONObject actual = getActual(commandCall, evaluator, resultRecorder);

    if (jsonComparer.areJsonDocumentsSimilar(expected, actual)) {
      eventWrapper.triggerSuccessEvent(resultRecorder, commandCall.getElement());
    }
    else {
      eventWrapper.triggerFailureEvent(resultRecorder, commandCall.getElement(), expected.toString(2), actual.toString(2));
    }
  }

  private JSONObject getActual(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
    String actual;
    try {
      actual = (String) evaluator.evaluate(commandCall.getExpression());
    } catch (final Exception e) {
      throw new RuntimeException("Exception retrieving actual.", e);
    }

    if (actual == null) {
      throw new RuntimeException("Unable to retrieve actual.");
    }

    try {
      return new JSONObject(actual);
    } catch (final JSONException je) {
      throw new RuntimeException("Actual is invalid JSON [" + actual + "]. ", je);
    }

  }

  private JSONObject getExpected(CommandCall commandCall) {
    String expected = commandCall.getElement().getText();

    if (expected == null) {
      throw new RuntimeException("Unable to retrieve expected.");
    }

    try {
      return new JSONObject(expected);
    } catch (final JSONException je) {
      throw new RuntimeException("Expected is invalid JSON [" + expected + "]. ", je);
    }
  }

  public EventWrapper getEventWrapper() {
    return eventWrapper;
  }

  public JsonComparer getJsonComparer() {
    return jsonComparer;
  }

}
