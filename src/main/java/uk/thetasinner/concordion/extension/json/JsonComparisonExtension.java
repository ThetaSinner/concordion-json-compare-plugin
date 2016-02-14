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

package uk.thetasinner.concordion.extension.json;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import uk.thetasinner.concordion.extension.json.internal.JsonAssertCommand;
import uk.thetasinner.concordion.extension.json.internal.JsonAssertResultRenderer;

/**
 * Created by ThetaSinner on 13/02/2016.
 *
 * Concordion extension for JSON comparison.
 */
public class JsonComparisonExtension implements ConcordionExtension {
  public static final String JSON_COMPARISON_XML_NAMESPACE_URI = "http://uk.thetasinner/JsonComparisonExtension";
  public static final String CONCORDION_JSON_EXTENSION_COMMAND = "assertSimilar";

  @Override
  public void addTo(ConcordionExtender concordionExtender) {
    JsonAssertCommand assertCommand = new JsonAssertCommand();

    // The renderer needs the diff lines from the comparer.
    JsonAssertResultRenderer resultRenderer = new JsonAssertResultRenderer();
    resultRenderer.setJsonComparer(assertCommand.getJsonComparer());

    assertCommand.getEventWrapper().registerEvent(resultRenderer);
    concordionExtender.withCommand(JSON_COMPARISON_XML_NAMESPACE_URI, CONCORDION_JSON_EXTENSION_COMMAND, assertCommand);
  }
}
