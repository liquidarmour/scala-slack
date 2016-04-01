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
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class ChannelsSpec extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {

  private val testApiKey = "TEST_API_KEY"

  private var mockHttpClient: HttpClient = _
  var channels: Channels = _

  val success: JObject = "ok" -> true

  private[this] def setHistoryMock(): Unit = {
    val json = ("ok" -> true) ~
        ("latest", "1358547726.000003") ~
        ("messages", JArray(List(
          ("type" -> "message") ~ ("ts" -> "1358546515.000008") ~ ("user" -> "U2147483896") ~ ("text" -> "Hello"),
          ("type" -> "message") ~ ("ts" -> "1358546515.000007") ~ ("user" -> "U2147483896") ~ ("text" -> "World") ~ ("is_starred", true),
          ("type" -> "something_else") ~ ("ts" -> "1358546515.000007") ~ ("wibblr" -> true)
        ))) ~
        ("has_more" -> true)
    when(mockHttpClient.get("channels.history", Map("channel" -> "C12345", "token" -> testApiKey)))
        .thenReturn(json)
  }

  private[this] def setHistoryWIthLastestMock(): Unit = {
    val params = Map("channel" -> "C12345", "latest" -> "1358546515.000007", "token" -> testApiKey)
    val json = ("ok" -> true) ~
        ("lastest" -> "1358547726.000003") ~
        ("messages" -> JArray(List(
          ("type" -> "message") ~ ("ts" -> "1358546515.000008") ~ ("user" -> "U2147483896") ~ ("text" -> "Hello"),
          ("type" -> "message") ~ ("ts" -> "1358546515.000007") ~ ("user" -> "U2147483896") ~ ("text" -> "World") ~ ("is_starred", true)
        ))) ~
        ("has_more" -> false)
    when(mockHttpClient.get("channels.history", params))
        .thenReturn(json)
  }

  private[this] def setListMock(): Unit = {
    val channelA = ("id" -> "C12345") ~
        ("name" -> "testchannel1") ~
        ("is_channel" -> true) ~
        ("created" -> 1408475169) ~
        ("creator" -> "U12345") ~
        ("is_archived" -> false) ~
        ("is_general" -> false) ~
        ("is_member" -> true) ~
        ("members" -> JArray(List("U12345", "U23456", "U34567"))) ~
        ("topic" ->
            ("value" -> "") ~ ("creator" -> "") ~ ("last_set" -> "0")) ~
        ("purpose" ->
            ("value" -> "a test channel") ~ ("creator" -> "U12345") ~ ("last_set" -> "1408475170")) ~
        ("num_members" -> 3)
    val channelB = ("id" -> "C23456") ~
        ("name" -> "testchannel2") ~
        ("is_channel" -> true) ~
        ("created" -> 1408419457) ~
        ("creator" -> "U12345") ~
        ("is_archived" -> false) ~
        ("is_general" -> false) ~
        ("is_member" -> true) ~
        ("members" -> JArray(List("U12345", "U23456", "U34567", "U45678", "U56789"))) ~
        ("topic" ->
            ("value" -> "") ~ ("creator" -> "") ~ ("last_set" -> "0")) ~
        ("purpose" ->
            ("value" -> "Hang out with developers here!") ~ ("creator" -> "U23456") ~ ("last_set" -> "1408419457")) ~
        ("num_members" -> 5)
    val channelC = ("id" -> "C34567") ~
        ("name" -> "testchannel3") ~
        ("is_channel" -> true) ~
        ("created" -> 1408419260) ~
        ("creator" -> "U23456") ~
        ("is_archived" -> false) ~
        ("is_general" -> true) ~
        ("is_member" -> true) ~
        ("members" -> JArray(List("U12345", "U23456", "U34567", "U45678", "U56789", "U67890"))) ~
        ("topic" ->
            ("value" -> "") ~ ("creator" -> "") ~ ("last_set" -> "0")) ~
        ("purpose" ->
            ("value" -> "This channel is for team-wide communication and announcements.") ~ ("creator" -> "") ~ ("last_set" -> "0")) ~
        ("num_members" -> 6)

    val json = ("ok" -> true) ~ ("channels" -> JArray(List(channelA, channelB, channelC)))

    when(mockHttpClient.get("channels.list", Map("token" -> testApiKey)))
        .thenReturn(json)
  }

  private[this] def setTopicMock(): Unit = {
    when(mockHttpClient.get("channels.setTopic", Map("channel" -> "C12345", "topic" -> "Test Topic", "token" -> testApiKey)))
        .thenReturn(("ok" -> true) ~ ("topic" -> "Test Topic"))
  }

  private[this] def setArchiveMock(): Unit = {
    when(mockHttpClient.get("channels.archive", Map("channel" -> "C12345", "token" -> testApiKey)))
        .thenReturn(success)
    when(mockHttpClient.get("channels.unarchive", Map("channel" -> "C12345", "token" -> testApiKey)))
        .thenReturn(success)
  }

  private[this] def setInfoMock(): Unit = {
    val json = parse(
      """
        |{
        |    "ok": true,
        |    "channel": {
        |        "id": "C024BE91L",
        |        "name": "fun",
        |
        |        "created": 1360782804,
        |        "creator": "U024BE7LH",
        |
        |        "is_archived": false,
        |        "is_general": false,
        |        "is_member": true,
        |
        |        "members": [],
        |
        |        "topic": {},
        |        "purpose": {},
        |
        |        "last_read": "1401383885.000061",
        |        "latest": {},
        |        "unread_count": 0,
        |        "unread_count_display": 0
        |    }
        |}
      """.stripMargin)
    when(mockHttpClient.get("channels.info", Map("channel" -> "C024BE91L", "token" -> testApiKey)))
        .thenReturn(json)
  }

  private[this] def setInviteMock(): Unit = {
    val json = parse(
      """
        |{
        |    "ok": true,
        |    "channel": {
        |        "id": "C024BE91L",
        |        "name": "fun",
        |        "created": 1360782804,
        |        "creator": "U024BE7LH",
        |        "is_archived": false,
        |        "is_member": true,
        |        "is_general": false,
        |        "last_read": "1401383885.000061",
        |        "latest": {},
        |        "unread_count": 0,
        |        "unread_count_display": 0,
        |        "members": ["U12345"],
        |        "topic": {
        |            "value": "Fun times",
        |            "creator": "U024BE7LV",
        |            "last_set": 1369677212
        |        },
        |        "purpose": {
        |            "value": "This channel is for fun",
        |            "creator": "U024BE7LH",
        |            "last_set": 1360782804
        |        }
        |    }
        |}
      """.stripMargin
    )
    when(mockHttpClient.get("channels.invite", Map("channel" -> "C024BE91L", "user" -> "U12345", "token" -> testApiKey)))
        .thenReturn(json)
  }

  override def beforeEach() {
    mockHttpClient = mock[HttpClient]
    setHistoryMock()
    setHistoryWIthLastestMock()
    setListMock()
    setTopicMock()
    setArchiveMock()
    setInfoMock()
    setInviteMock()
    channels = new Channels(mockHttpClient, testApiKey)
   }

  "Channels.history()" should "make a call to channels.history and return the response in an ChannelHistoryResponse object" in {
    val response = channels.history("C12345")

    response.ok shouldBe true
    response.hasMore shouldBe true
    response.isLimited shouldBe None
    response.messages should have length 3

    val message = response.messages.head

    message.`type` shouldBe "message"
    message.ts shouldBe "1358546515.000008"
    message.user.get shouldBe "U2147483896"
    message.text.get shouldBe "Hello"

    verify(mockHttpClient).get("channels.history", Map("channel" -> "C12345", "token" -> testApiKey))
  }

  "Channels.historyStream()" should "make repeated calls to channels.history and return a stream of messages" in {
    val messages = channels.historyStream("C12345").toList

    messages should have length 5

    verify(mockHttpClient).get("channels.history", Map("channel" -> "C12345", "latest" -> "1358546515.000007", "token" -> testApiKey))
  }

  "Channels.list()" should "list all channels available to a user" in {
    val response = channels.list()

    response.ok shouldBe true
    response.channels should have length 3

    val channel = response.channels.head
    channel.id shouldBe "C12345"
    channel.name shouldBe "testchannel1"
    channel.created shouldBe 1408475169
    channel.creator shouldBe "U12345"
    channel.isArchived shouldBe false
    channel.isMember shouldBe true
    channel.members should have length 3
    channel.numMembers shouldBe 3
    (channel.purpose \ "value") shouldBe JString("a test channel")

    verify(mockHttpClient).get("channels.list", Map("token" -> testApiKey))
  }

  "Channels.setTopic()" should "make a call to channels.setTopic and return the response in an ChannelSetTopicResponse object" in {
    val response = channels.setTopic("C12345", "Test Topic")

    response.ok shouldBe true
    response.topic shouldBe "Test Topic"

    verify(mockHttpClient).get("channels.setTopic", Map("channel" -> "C12345", "topic" ->
        "Test Topic", "token" -> testApiKey))
  }

  "Channels.archive()" should "archive and return true if succeeded" in {
    channels.archive("C12345") shouldBe true
  }

  "Channels.unarchive()" should "unarchive and return true if succeeded" in {
    channels.unarchive("C12345") shouldBe true
  }

  "Channels.info()" should "get channel info" in {
    val response = channels.info("C024BE91L")
    response.ok shouldBe true

    val channel = response.channel
    channel.id shouldBe "C024BE91L"
    channel.isGeneral shouldBe false
    channel.lastRead shouldBe 1401383885.000061
    channel.unreadCount shouldBe 0
    channel.unreadCountDisplay shouldBe 0
  }

  "Channels.invite()" should "invite user and return the response in an ChannelInfoResponse" in {
    val response = channels.invite("C024BE91L", "U12345")
    response.ok shouldBe true
    val channel = response.channel
    channel.id shouldBe "C024BE91L"
    channel.name shouldBe "fun"
    channel.members should contain("U12345")
    channel.lastRead shouldBe 1401383885.000061
  }
}
