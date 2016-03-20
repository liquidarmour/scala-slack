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

package com.ponkotuy.slack.methods

import com.ponkotuy.slack.HttpClient
import com.ponkotuy.slack.responses.{UserInfoResponse, UserListResponse}
import org.json4s.DefaultFormats


/**
  * The container for Slack's 'users' methods (https://api.slack.com/methods).
  */
class Users(httpClient: HttpClient, apiToken: String) {
  implicit val format = DefaultFormats

  /**
    * https://api.slack.com/methods/users.info
    */
  def info(user: String): UserInfoResponse = {
    val params = Map("token" -> apiToken, "user" -> user)
    val response = httpClient.get("users.info", params)
    response.camelizeKeys.extract[UserInfoResponse]
  }

  /**
    * https://api.slack.com/methods/users.list
    */
  def list(params: Map[String, String] = Map()): UserListResponse = {
    val cleanedParams = params + ("token" -> apiToken)
    val responseDict = httpClient.get("users.list", cleanedParams)
    responseDict.camelizeKeys.extract[UserListResponse]
  }
}
