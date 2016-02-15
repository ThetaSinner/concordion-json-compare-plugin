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

import org.concordion.api.Command;
import org.concordion.api.extension.ConcordionExtender;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by ThetaSinner on 15/02/2016.
 */
public class JsonComparisonExtensionTest {
  private Fixture fixture;


  @Before
  public void setUp() throws Exception {
    fixture = new Fixture();
  }

  @Test
  public void success() {
    fixture.givenConcordionExtenderIsMocked();
    fixture.whenAddToIsCalled();
    fixture.thenTheCommandIsBound();
  }

  private class Fixture {
    private final JsonComparisonExtension testClass = new JsonComparisonExtension();

    private ConcordionExtender concordionExtender;

    public void givenConcordionExtenderIsMocked() {
      concordionExtender = mock(ConcordionExtender.class);
    }

    public void whenAddToIsCalled() {
      testClass.addTo(concordionExtender);
    }

    public void thenTheCommandIsBound() {
      verify(concordionExtender).withCommand(eq(JsonComparisonExtension.JSON_COMPARISON_XML_NAMESPACE_URI), eq(JsonComparisonExtension.CONCORDION_JSON_EXTENSION_COMMAND), any(Command.class));
    }
  }
}