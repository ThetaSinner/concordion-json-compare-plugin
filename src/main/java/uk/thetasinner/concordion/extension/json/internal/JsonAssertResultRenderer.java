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

import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.internal.listener.AssertResultRenderer;

import java.util.List;

/**
 * Created by ThetaSinner on 14/02/2016.
 * <p>
 * Custom renderer too append the JSON diff to the result.
 */
public class JsonAssertResultRenderer extends AssertResultRenderer {
  private JsonComparer jsonComparer;

  @Override
  public void failureReported(AssertFailureEvent event) {
    super.failureReported(event);

    if (jsonComparer != null) {
      appendJsonDiffLinesToResult(event);
    }
  }

  public void setJsonComparer(JsonComparer jsonComparer) {
    this.jsonComparer = jsonComparer;
  }

  private void appendJsonDiffLinesToResult(AssertFailureEvent event) {
    Element jsonDiffWrapper = createWrappedDiff();

    event.getElement().appendChild(jsonDiffWrapper);
  }

  private Element createWrappedDiff() {
    Element jsonDiffWrapper = new Element("div");
    jsonDiffWrapper.addStyleClass("special");

    addDiffLinesToWrapper(jsonDiffWrapper);

    return jsonDiffWrapper;
  }

  private void addDiffLinesToWrapper(Element jsonDiffWrapper) {
    List<String> jsonDiffLines = jsonComparer.getJsonDiffLines();
    for (final String jsonDiffLine : jsonDiffLines) {
      addDiffLine(jsonDiffWrapper, jsonDiffLine);
    }

    // Clear out the diff lines for this comparison.
    jsonDiffLines.clear();
  }

  private void addDiffLine(Element jsonDiffWrapper, String jsonDiffLine) {
    Element jsonDiffElement = new Element("p");
    jsonDiffElement.appendText(jsonDiffLine);

    jsonDiffWrapper.appendChild(jsonDiffElement);
  }
}
