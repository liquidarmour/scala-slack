/*
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ponkotuy.slack.methods

import com.ponkotuy.slack.HttpClient
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}


class IMSpec extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {

   private val testApiKey = "TEST_API_KEY"

   private var mockHttpClient: HttpClient = _
   var im : IM = _

  def success = JObject(JField("ok", true))

  override def beforeEach() {
    mockHttpClient = mock[HttpClient]
    when(mockHttpClient.get("im.close", Map("channel" -> "D12345", "token" -> testApiKey)))
        .thenReturn(success)
    when(mockHttpClient.get("im.close", Map("channel" -> "D54321", "token" -> testApiKey)))
        .thenReturn(("ok" -> true) ~ ("no_op" -> true) ~ ("already_closed" -> true))
    when(mockHttpClient.get("im.history", Map("channel" -> "C12345", "token" -> testApiKey)))
        .thenReturn(parse(
          """
            |{
            |    "ok": true,
            |    "latest": "1358547726.000003",
            |    "messages": [
            |        {
            |            "type": "message",
            |            "ts": "1358546515.000008",
            |            "user": "U2147483896",
            |            "text": "Hello"
            |        },
            |        {
            |            "type": "message",
            |            "ts": "1358546515.000007",
            |            "user": "U2147483896",
            |            "text": "World",
            |            "is_starred": true
            |        },
            |        {
            |            "type": "something_else",
            |            "ts": "1358546515.000007",
            |            "wibblr": true
            |        }
            |    ],
            |    "has_more": true
            |}
          """.stripMargin))
    when(mockHttpClient.get("im.history", Map("channel" -> "C12345", "latest" -> "1358546515.000007", "token" -> testApiKey)))
        .thenReturn(parse(
          """
            |{
            |    "ok": true,
            |    "latest": "1358547726.000003",
            |    "messages": [
            |        {
            |            "type": "message",
            |            "ts": "1358546515.000008",
            |            "user": "U2147483896",
            |            "text": "Hello"
            |        },
            |        {
            |            "type": "message",
            |            "ts": "1358546515.000007",
            |            "user": "U2147483896",
            |            "text": "World",
            |            "is_starred": true
            |        }
            |    ],
            |    "has_more": false
            |}
          """.stripMargin))
    when(mockHttpClient.get("im.list", Map("token" -> testApiKey)))
        .thenReturn(parse(
          """
            |{
            |    "ok": true,
            |    "ims": [
            |        {
            |           "id": "D024BFF1M",
            |           "is_im": true,
            |           "user": "USLACKBOT",
            |           "created": 1372105335,
            |           "is_user_deleted": false
            |        },
            |        {
            |           "id": "D024BE7RE",
            |           "is_im": true,
            |           "user": "U024BE7LH",
            |           "created": 1356250715,
            |           "is_user_deleted": false
            |        }
            |    ]
            |}
          """.stripMargin))
    when(mockHttpClient.get("im.mark", Map("channel" -> "D12345", "ts" -> "1234567890.123456", "token" -> testApiKey)))
        .thenReturn(success)

    when(mockHttpClient.get("im.open", Map("user" -> "U12345", "token" -> testApiKey)))
        .thenReturn(("ok" -> true) ~ ("channel" -> ("id" -> "D024BFF1M")))
    when(mockHttpClient.get("im.open", Map("user" -> "U54321", "token" -> testApiKey)))
        .thenReturn(parse(
          """
            |{
            |    "ok": true,
            |    "no_op": true,
            |    "already_open": true,
            |    "channel": {
            |        "id": "D024BFF1M"
            |    }
            |}
          """.stripMargin))
    im = new IM(mockHttpClient, testApiKey)
  }

  "IM.close()" should "make a call to im.close and return the response in an IMCloseResponse object" in {
    val responseOk = im.close("D12345").get
    responseOk.ok shouldBe true

    val responseErr = im.close("D54321").get
    responseErr.ok shouldBe true
    responseErr.noOp shouldBe Some(true)
    responseErr.alreadyClosed shouldBe Some(true)
  }

  "IM.history()" should "make a call to im.history and return the response in an ChannelHistoryResponse object" in {
    val response = im.history("C12345").get
    response.ok shouldBe true
    response.hasMore shouldBe true
    response.isLimited shouldBe None
    response.messages should have length 3

    val message = response.messages.head
    message.`type` shouldBe "message"
    message.ts shouldBe "1358546515.000008"
    message.user.get shouldBe "U2147483896"
    message.text.get shouldBe "Hello"

    verify(mockHttpClient).get("im.history", Map("channel" -> "C12345", "token" -> testApiKey))
  }

  "IM.historyStream()" should "make repeated calls to im.history and return a stream of messages" in {
    val messages = im.historyStream("C12345").toList
    messages should have length 5

    verify(mockHttpClient).get("im.history", Map("channel" -> "C12345", "latest" -> "1358546515.000007", "token" -> testApiKey))
  }

  "IM.list()" should "make a call to im.list and return the response in an IMListResponse object" in {
    val response = im.list().get
    response.ok shouldBe true
    response.ims should have length 2

    val im1 = response.ims(0)
    im1.id shouldBe "D024BFF1M"
    im1.user shouldBe "USLACKBOT"
    im1.created shouldBe 1372105335
    im1.isUserDeleted shouldBe false

    val im2 = response.ims(1)
    im2.id shouldBe "D024BE7RE"
    im2.user shouldBe "U024BE7LH"
    im2.created shouldBe 1356250715
    im2.isUserDeleted shouldBe false
  }

  "IM.mark()" should "make a call to im.mark and return the response in an IMMarkResponse object" in {
    val response = im.mark("D12345", "1234567890.123456").get
    response.ok shouldBe true
  }

  "IM.open()" should "make a call to im.open and return the response in an IMOpenResponse object" in {
    val responseOk = im.open("U12345").get
    responseOk.ok shouldBe true
    responseOk.channel.id shouldBe "D024BFF1M"
    responseOk.noOp shouldBe None
    responseOk.alreadyOpen shouldBe None

    val responseErr = im.open("U54321").get
    responseErr.ok shouldBe true
    responseErr.channel.id shouldBe "D024BFF1M"
    responseErr.noOp shouldBe Some(true)
    responseErr.alreadyOpen shouldBe Some(true)
  }
}
