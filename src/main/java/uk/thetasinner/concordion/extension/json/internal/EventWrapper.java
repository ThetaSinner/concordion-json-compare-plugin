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
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertEqualsListener;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.internal.util.Announcer;

/**
 * Created by ThetaSinner on 13/02/2016.
 * <p>
 * Wrapper around Concordion events because it's a bit hideous...
 */
public class EventWrapper {
  private final Announcer<AssertEqualsListener> listeners = Announcer.to(AssertEqualsListener.class);

  public void registerEvent(AssertEqualsListener listener) {
    listeners.addListener(listener);
  }

  public void triggerSuccessEvent(ResultRecorder resultRecorder, Element element) {
    recordResult(resultRecorder, Result.SUCCESS);
    announceSuccess(element);
  }

  public void triggerFailureEvent(ResultRecorder resultRecorder, Element element, Object expected, Object actual) {
    recordResult(resultRecorder, Result.FAILURE);
    announceFailure(element, (String) expected, actual);
  }

  private static void recordResult(ResultRecorder resultRecorder, Result result) {
    resultRecorder.record(result);
  }

  private void announceSuccess(Element element) {
    listeners.announce().successReported(new AssertSuccessEvent(element));
  }

  private void announceFailure(Element element, String expected, Object actual) {
    listeners.announce().failureReported(new AssertFailureEvent(element, expected, actual));
  }
}
