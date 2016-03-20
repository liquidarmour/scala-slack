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

/**
 * Response object wrappers for responses returned by the web API.
 */
case class PostMessageResponse(ok: Boolean, ts: String, channel: String) {
  def timeStamp: Double = ts.toDouble
}
case class AuthTestResponse(ok: Boolean, url: String, team: String, user: String, teamId: String, userId: String)
case class APITestResponse(ok: Boolean, args: Option[Map[String, String]])

case class ChannelHistoryResponse(ok: Boolean, messages: List[SlackMessage], hasMore: Boolean, isLimited: Option[Boolean])
case class ChannelListResponse(ok: Boolean, channels: List[SlackChannel])
case class ChannelSetTopicResponse(ok: Boolean, topic: String)

case class IMCloseResponse(ok: Boolean, noOp: Option[Boolean], alreadyClosed: Option[Boolean])
case class IMMarkResponse(ok: Boolean)
case class IMOpenResponse(ok: Boolean, channel: Channel, noOp: Option[Boolean], alreadyOpen: Option[Boolean])

case class Channel(id: String)

case class IMListResponse(ok: Boolean, ims: List[SlackIM])
case class IMHistoryResponse(ok: Boolean, messages: List[SlackMessage], hasMore: Boolean, isLimited: Boolean)

case class UserListResponse(ok: Boolean, members: List[SlackMember])
case class UserInfoResponse(ok: Boolean, user: SlackMember)
case class UserPresenceResponse(ok: Boolean, presence: Presence)
