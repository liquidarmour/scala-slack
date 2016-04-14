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

import com.ponkotuy.slack.responses._
import com.ponkotuy.slack.HttpClient
import org.json4s._


/**
 * The container for Slack's 'channels' methods (https://api.slack.com/methods).
 *
 * <i>Note: This is a partial implementation, and some (i.e. most) methods are unimplemented.</i>
 */
class Channels(httpClient: HttpClient, apiToken: String) {
  implicit val format = DefaultFormats

  /**
    * https://api.slack.com/methods/channels.history
    *
    * @param channel The channel ID to fetch history for.
    * @param params  A map of optional parameters and their values.
    * @return ChannelHistoryResponse
    */
  def history(channel: String, params: Map[String, String] = Map()): ChannelHistoryResponse = {
    val cleanedParams = params +("channel" -> channel, "token" -> apiToken)
    val responseDict = httpClient.get("channels.history", cleanedParams)
    responseDict.camelizeKeys.extract[ChannelHistoryResponse]
  }

  /**
    * A wrapper around the channels.history method that allows users to stream through a channel's past messages
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
    * https://api.slack.com/methods/channels.list
    *
    * @param params A map of optional parameters and their values.
    * @return A ChannelListResponse object.
    */
  def list(params: Map[String, String] = Map()): ChannelListResponse = {
    val cleanedParams = params + ("token" -> apiToken)
    val responseDict = httpClient.get("channels.list", cleanedParams)
    responseDict.camelizeKeys.extract[ChannelListResponse]
  }

  /**
    * https://api.slack.com/methods/channels.setTopic
    *
    * @param channel The channel ID to set topic for
    * @param topic   The topic to set
    * @param params  A map of optional parameters and their values.
    * @return A ChannelSetTopicResponse object.
    */
  def setTopic(channel: String, topic: String, params: Map[String, String] = Map()): ChannelSetTopicResponse = {
    val cleanedParams = params +("channel" -> channel, "topic" -> topic, "token" -> apiToken)
    val responseDict = httpClient.get("channels.setTopic", cleanedParams)
    responseDict.camelizeKeys.extract[ChannelSetTopicResponse]
  }

  /**
    * https://api.slack.com/methods/channels.archive
    */
  def archive(channel: String): Boolean = {
    val response = httpClient.get("channels.archive", Map("token" -> apiToken, "channel" -> channel))
    responseBoolean(response)
  }

  /**
    * https://api.slack.com/methods/channels.unarchive
    */
  def unarchive(channel: String): Boolean = {
    val response = httpClient.get("channels.unarchive", Map("token" -> apiToken, "channel" -> channel))
    responseBoolean(response)
  }

  /**
    * https://api.slack.com/methods/channels.info
    */
  def info(channel: String): ChannelInfoResponse = {
    val response = httpClient.get("channels.info", Map("token" -> apiToken, "channel" -> channel))
    response.transformField(SlackChannelInfo.transform).camelizeKeys.extract[ChannelInfoResponse]
  }

  /**
    * https://api.slack.com/methods/channels.invite
    */
  def invite(channel: String, user: String): ChannelInfoResponse = {
    val response = httpClient.get("channels.invite", Map("token" -> apiToken, "channel" -> channel, "user" -> user))
    response.transformField(SlackChannelInfo.transform).camelizeKeys.extract[ChannelInfoResponse]
  }

  /**
    * https://api.slack.com/methods/channels.join
    */
  def join(name: String): ChannelInfoResponse = {
    val response = httpClient.get("channels.join", Map("token" -> apiToken, "name" -> name))
    response.transformField(SlackChannelInfo.transform).camelizeKeys.extract[ChannelInfoResponse]
  }

  /**
    * https://api.slack.com/methods/channels.kick
    */
  def kick(channel: String, user: String): Boolean = {
    val response = httpClient.get("channels.kick", Map("token" -> apiToken, "channel" -> channel, "user" -> user))
    responseBoolean(response)
  }

  /**
    * https://api.slack.com/methods/channels.leave
    */
  def leave(channel: String): ChannelLeaveResponse = {
    val response = httpClient.get("channels.leave", Map("token" -> apiToken, "channel" -> channel))
    response.camelizeKeys.extract[ChannelLeaveResponse]
  }

  def responseBoolean(response: JValue) = (response \ "ok").extract[Boolean]
}
