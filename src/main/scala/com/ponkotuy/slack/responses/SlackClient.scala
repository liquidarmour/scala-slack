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

package com.ponkotuy.slack.responses

import com.ponkotuy.slack.HttpClient

/**
 * A simple Scala client for Slack (http://slack.com/).
 *
 * @param apiToken Your Slack API token (https://api.slack.com/).
 */
class SlackClient(private val apiToken: String) {
	protected val BASE_URL = "https://slack.com/api"

   protected val httpClient = new HttpClient()

   import com.ponkotuy.slack.methods._

   val api = new API(httpClient, apiToken)
   val auth = new Auth(httpClient, apiToken)
   val channels = new Channels(httpClient, apiToken)
   val chat = new Chat(httpClient, apiToken)
   val im = new IM(httpClient, apiToken)
   val users = new Users(httpClient, apiToken)

   def connTimeout(ms: Int): SlackClient = {
      httpClient.connTimeout(ms)

      this
   }

   def readTimeout(ms: Int): SlackClient = {
      httpClient.readTimeout(ms)

      this
   }
}
