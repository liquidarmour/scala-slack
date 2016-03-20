/*
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
import com.ponkotuy.slack.responses._
import org.json4s.DefaultFormats

/**
 * The container for Slack's 'im' methods (https://api.slack.com/methods).
 *
 * <i>Note: This is a partial implementation, and some (i.e. most) methods are unimplemented.</i>
 */
class IM(httpClient: HttpClient, apiToken: String) {
  implicit val default = DefaultFormats

  /**
    * https://api.slack.com/methods/im.close
    *
    * @param channel The channel ID for the direct message history to close.
    * @return IMCloseResponse
    */
  def close(channel: String): IMCloseResponse = {
    val params = Map("channel" -> channel, "token" -> apiToken)
    val responseDict = httpClient.get("im.close", params)
    responseDict.camelizeKeys.extract[IMCloseResponse]
  }

  /**
    * https://api.slack.com/methods/im.history
    *
    * The format is exactly the same as channels.history, with the exception that we call im.history.
    * Code copied from [[com.ponkotuy.slack.methods.Channels]]
    *
    * @param channel The channel ID of the IM to get history for.
    * @param params  A map of optional parameters and their values.
    * @return ChannelHistoryResponse
    */
  def history(channel: String, params: Map[String, String] = Map()): ChannelHistoryResponse = {
    val cleanedParams = params +("channel" -> channel, "token" -> apiToken)
    val responseDict = httpClient.get("im.history", cleanedParams)
    responseDict.camelizeKeys.extract[ChannelHistoryResponse]
  }

  /**
    * A wrapper around the im.history method that allows users to stream through a channel's past messages
    * seamlessly without having to worry about pagination and multiple queries.
    *
    * @param channel The channel ID to fetch history for.
    * @param params  A map of optional parameters and their values.
    * @return Iterator of SlackMessages, ordered by time in descending order.
    */
  def historyStream(channel: String, params: Map[String, String] = Map()): Iterator[SlackMessage] = {
    new Iterator[SlackMessage] {
      var hist = history(channel, params = params)
      var messages = hist.messages

      def hasNext = messages.nonEmpty

      def next() = {
        val m = messages.head
        messages = messages.tail

        if (messages.isEmpty && hist.hasMore) {
          hist = history(channel, params = params + ("latest" -> m.ts))
          messages = hist.messages
        }

        m
      }
    }
  }

  /**
    * https://api.slack.com/methods/im.list
    *
    * @return IMListResponse of all open IM channels
    */
  def list(): IMListResponse = {
    val params = Map("token" -> apiToken)
    val responseDict = httpClient.get("im.list", params)
    responseDict.camelizeKeys.extract[IMListResponse]
  }

  /**
    * https://api.slack.com/methods/im.mark
    *
    * @param channel The channel ID for the direct message history to set reading cursor in.
    * @param ts Timestamp of the most recently seen message.
    * @return IMMarkResponse
    */
  def mark(channel: String, ts: String): IMMarkResponse = {
    val params = Map("channel" -> channel, "ts" -> ts, "token" -> apiToken)
    val responseDict = httpClient.get("im.mark", params)
    responseDict.camelizeKeys.extract[IMMarkResponse]
  }

  /**
    * https://api.slack.com/methods/im.open
    *
    * @param user The user ID for the user to open a direct message channel with.
    * @return IMOpenResponse
    */
  def open(user: String): IMOpenResponse = {
    val params = Map("user" -> user, "token" -> apiToken)

    val responseDict = httpClient.get("im.open", params)

    responseDict.camelizeKeys.extract[IMOpenResponse]
  }
}
