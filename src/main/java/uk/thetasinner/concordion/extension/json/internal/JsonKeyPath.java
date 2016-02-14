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

/**
 * Created by ThetaSinner on 14/02/2016.
 *
 * Helper class for tracking and formatting the current location in the document.
 */
class JsonKeyPath {
  public static final String DUMMY_ROOT_ELEMENT = "documentRoot";
  public static final String JSON_PATH_SEPARATOR = ".";

  public static String navigateTo(String from, String to) {
    return from + JSON_PATH_SEPARATOR + to;
  }

  public static String navigateTo(String from, int to) {
    return navigateTo(from, String.valueOf(to));
  }

  public static String format(String keyPath)
  {
    return " at [" + keyPath + "]";
  }
}
