/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sirix.axis.concurrent;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sirix.Holder;
import org.sirix.XdmTestHelper;
import org.sirix.axis.AbsAxisTest;
import org.sirix.exception.SirixException;
import org.sirix.exception.SirixXPathException;
import org.sirix.service.xml.xpath.XPathAxis;
import org.sirix.service.xml.xpath.XPathStringChecker;

/**
 * JUnit-test class to test the functionality of the XPathAxis.
 *
 * @author Tina Scherer
 * @author Patrick Lang
 */
public class ConXPathAxisTest {

  private Holder holder;

  @Before
  public void setUp() throws SirixException {
    XdmTestHelper.deleteEverything();
    XdmTestHelper.createTestDocument();
    holder = Holder.generateRtx();
  }

  @After
  public void tearDown() throws SirixException {
    holder.close();
    XdmTestHelper.closeEverything();
  }

  @Test
  public void testSteps() {

    try {

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/text:p/b"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b/c"), new long[] {7L, 11L});

      AbsAxisTest.testIAxisConventions(new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::p:a/child::b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::p:" + "a/child::b/child::c"),
          new long[] {7L, 11L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testAttributes() {

    try {

      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveToDocumentRoot();

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a[@i]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/@i"), new long[] {3L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/@i/@*"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/b[@p:x]"), new long[] {9L});

      XPathStringChecker.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "descendant-or-self::node()/@p:x = 'y'"),
          new String[] {"true"});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[text()]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[element()]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[node()/text()]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[./node()]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[./node()/node()/node()]"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[//element()]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[/text()]"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[16<65]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[13>=4]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[13.0>=4]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[4 = 4]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[3=4]"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[3.2 = 3.22]"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[(3.2 + 0.02) = 3.22]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[@i = \"j\"]"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "descendant-or-self::node()[@p:x = \"y\"]"),
          new long[] {9L});

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "p:a[@i eq \"j\"]"),
      // new long[] { 1L });

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a[@i=\"k\"]"), new long[] {});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/b[@p:x=\"y\"]"), new long[] {9L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testNodeTests() {

    try {
      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveToDocumentRoot();

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/node()"), new long[] {4L, 5L, 8L, 9L, 13L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/text()"), new long[] {4L, 8L, 13L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b/text()"), new long[] {6L, 12L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/b/node()"), new long[] {6L, 7L, 11L, 12L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testDescendant() {
    try {

      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveToDocumentRoot();

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a//b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(new XPathAxis(holder.getXdmNodeReadTrx(), "//p:a"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "descendant-or-self::p:a"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/descendant-or-self::b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/descendant::b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "p:a/descendant::p:a"), new long[] {});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }
  }

  @Test
  public void testAncestor() {
    try {

      // Find ancestor starting from nodeKey 8L.
      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "ancestor::p:a"), new long[] {1L});

      holder.getXdmNodeReadTrx().moveTo(13L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "ancestor::p:a"), new long[] {1L});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "ancestor::node()"), new long[] {9L, 1L});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "ancestor-or-self::node()"), new long[] {11L, 9L, 1L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testParent() {
    try {
      // Find ancestor starting from nodeKey 8L.
      holder.getXdmNodeReadTrx().moveTo(9L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "parent::p:a"), new long[] {1L});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "parent::b"), new long[] {9L});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "parent::node()"), new long[] {9L});

      holder.getXdmNodeReadTrx().moveTo(13L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "parent::node()"), new long[] {1L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }
  }

  @Test
  public void testSelf() {
    try {
      // Find ancestor starting from nodeKey 8L.
      holder.getXdmNodeReadTrx().moveTo(1L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "self::p:a"), new long[] {1L});

      holder.getXdmNodeReadTrx().moveTo(9L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "self::b"), new long[] {9L});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "./node()"), new long[] {});

      holder.getXdmNodeReadTrx().moveTo(11L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "self::node()"), new long[] {11L});

      holder.getXdmNodeReadTrx().moveTo(1L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "./b/node()"), new long[] {6L, 7L, 11L, 12L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testPosition() {
    try {
      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveTo(1L);

      AbsAxisTest.testIAxisConventions(new XPathAxis(holder.getXdmNodeReadTrx(), "b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/c"), new long[] {7L, 11L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/text()"), new long[] {6L, 12L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b/c"), new long[] {7L, 11L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  //
  @Test
  public void testDupElemination() {
    try {
      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveTo(1L);

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::node()/parent::node()"), new long[] {1L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/c"), new long[] {7L, 11L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/text()"), new long[] {6L, 12L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b/c"), new long[] {7L, 11L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testUnabbreviate() {
    try {
      // Find descendants starting from nodeKey 0L (root).
      holder.getXdmNodeReadTrx().moveTo(1L);

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::b"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::*"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::text()"), new long[] {4L, 8L, 13L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "attribute::i"), new long[] {3L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "attribute::*"), new long[] {3L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "parent::node()"), new long[] {0L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "self::blau"), new long[] {});

      AbsAxisTest.testIAxisConventions(new XPathAxis(holder.getXdmNodeReadTrx(), "/"), new long[] {0L});

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[fn:position() = 1]"), new long[] { 4L });
      //
      // // IAxisTest.testIAxisConventions(new XPathAxis(
      // holder.getRtx(), "child::b[fn:position() = fn:last()]"), new
      // long[] {8L});
      //
      // IAxisTest.testIAxisConventions(new XPathAxis(
      // holder.getRtx(), "child::b[fn:position() = fn:last()-1]"), new
      // long[] {4L});
      //
      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[fn:position() > 1]"), new long[] { 8L });

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::b[attribute::p:x = \"y\"]"), new long[] {9L});

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[attribute::p:x = \"y\"][fn:position() = 1]"),
      // new long[] { 8L });

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[attribute::p:x = \"y\"][1]"), new long[] { 8L });

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[attribute::p:x = \"y\"][fn:position() = 3]"), new
      // long[]
      // {});

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[attribute::p:x = \"y\"][3]"), new long[] {});

      // IAxisTest.testIAxisConventions(new XPathAxis(holder.getRtx(),
      // "child::b[fn:position() = 2][attribute::p:x = \"y\"]"),
      // new long[] { 8L });

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::b[child::c]"), new long[] {5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::*[text() or c]"), new long[] {5L, 9L});

      // IAxisTest.testIAxisConventions(new XPathAxis(
      // holder.getRtx(),
      // "child::*[text() or c][fn:position() = fn:last()]"), new long[]
      // {8L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "child::*[text() or c], /node(), //c"),
          new long[] {5l, 9L, 1L, 7L, 11L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testMultiExpr() {
    try {
      holder.getXdmNodeReadTrx().moveTo(1L);

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b, b, b"), new long[] {5L, 9L, 5L, 9L, 5L, 9L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/c, ., //c"), new long[] {7L, 11L, 1L, 7L, 11L});

      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "b/text(), //text(), descendant-or-self::element()"),
          new long[] {6L, 12L, 4L, 8L, 13L, 6L, 12L, 1L, 5L, 7L, 9L, 11L});

      holder.getXdmNodeReadTrx().moveTo(5L);
      AbsAxisTest.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "/p:a/b/c, ., .., .//text()"),
          new long[] {7L, 11L, 5L, 1L, 6L});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

  @Test
  public void testCount() throws IOException {
    try {
      holder.getXdmNodeReadTrx().moveTo(1L);

      XPathStringChecker.testIAxisConventions(
          new XPathAxis(holder.getXdmNodeReadTrx(), "fn:count(//node())"), new String[] {"10"});

    } catch (final SirixXPathException mExp) {
      mExp.getStackTrace();
    }

  }

}
