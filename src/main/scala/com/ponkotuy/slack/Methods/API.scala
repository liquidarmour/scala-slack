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

package com.ponkotuy.slack.Methods

import com.ponkotuy.slack.HttpClient
import org.json4s.DefaultFormats


/**
 * The container for Slack's 'api' methods (https://api.slack.com/methods).
 */
class API(httpClient: HttpClient, apiToken: String) {
  import com.ponkotuy.slack.Responses._

  implicit val formats = DefaultFormats

  /**
    * See: https://api.slack.com/methods/api.test
    *
    * @param error  Example error response to return. If not given, Slack will return an "ok" status.
    * @param params Map of example key-value pairs to return.
    * @return APITestResponse object
    */
  def test(error: String = null, params: Map[String, String] = Map()): Option[APITestResponse] = {
    var newParams: Map[String, String] = params

    if (error != null)
      newParams = params + ("error" -> error)

    val responseDict = httpClient.get("api.test", newParams)

    responseDict.extractOpt[APITestResponse]
  }

}
