package com.ponkotuy.slack

import com.ponkotuy.slack.Exceptions._
import org.json4s.JsonDSL._
import org.json4s._
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers, PrivateMethodTester}

/*
 * Copyright (c) 2014 Flyberry Capital, LLC
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

class HttpClientSpec extends FlatSpec with Matchers with BeforeAndAfterEach with PrivateMethodTester {

  val httpClient = new HttpClient()
  val checkResponse = PrivateMethod[String]('checkResponse)

  "checkResponse()" should "raise no error if \"ok\" is true" in {
    val response = JObject(JField("ok", true))
    noException should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an InvalidAuthError if the invalid_auth error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "invalid_auth")
    an[InvalidAuthError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a NotAuthedError if the not_authed error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "not_authed")
    a[NotAuthedError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an AccountInactiveError if the account_inactive error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "account_inactive")
    an[AccountInactiveError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a ChannelNotFoundError if the channel_not_found error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "channel_not_found")
    a[ChannelNotFoundError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a NotInChannelError if the not_in_channel error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "not_in_channel")
    a[NotInChannelError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a RateLimitedError if the rate_limited error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "rate_limited")
    a[RateLimitedError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a NoMessageTextProvidedError if the no_text error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "no_text")
    an[NoMessageTextProvidedError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a MessageTooLongError if the msg_too_long error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "msg_too_long")
    a[MessageTooLongError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a MessageNotFoundError if the message_not_found error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "message_not_found")
    an[MessageNotFoundError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an EditWindowClosedError if the edit_window_closed error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "edit_window_closed")
    an[EditWindowClosedError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an CantUpdateMessageError if the cant_update_message error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "cant_update_message")
    an[CantUpdateMessageError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise a CantDeleteMessageError if the cant_delete_message error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "cant_delete_message")
    an[CantDeleteMessageError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an InvalidTsLatestError if the invalid_ts_latest error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "invalid_ts_latest")
    an[InvalidTsLatestError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an InvalidTsOldest if the invalid_ts_oldest error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "invalid_ts_oldest")
    an[InvalidTsOldestError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }

  "checkResponse()" should "raise an UnknownSlackError if an unrecognized error is received" in {
    val response = ("ok" -> false) ~ ("error" -> "unrecognized_error")
    an[UnknownSlackError] should be thrownBy (httpClient invokePrivate checkResponse(response))
  }
}
