/*
 * Copyright (c) 2016 ponkotuy, LLC
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
import com.ponkotuy.slack.responses.{Presence, PresenceSerializer}
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JString
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class UsersSpec extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {
  private var mockHttpClient: HttpClient = _
  var users: Users = _

  private val testApiKey = "TEST_API_KEY"

  override def beforeEach(): Unit = {
    mockHttpClient = mock[HttpClient]
    setInfoMock()
    setListMock()
    setGetPresenceMock()
    users = new Users(mockHttpClient, testApiKey)
  }

  private[this] def setInfoMock(): Unit = {
    val json = parse(
      """
        |{
        |    "ok": true,
        |    "user": {
        |        "id": "U023BECGF",
        |        "name": "bobby",
        |        "deleted": false,
        |        "color": "9f69e7",
        |        "profile": {
        |            "first_name": "Bobby",
        |            "last_name": "Tables",
        |            "real_name": "Bobby Tables",
        |            "email": "bobby@slack.com",
        |            "skype": "my-skype-name",
        |            "phone": "+1 (123) 456 7890",
        |            "image_24": "https:\/\/...",
        |            "image_32": "https:\/\/...",
        |            "image_48": "https:\/\/...",
        |            "image_72": "https:\/\/...",
        |            "image_192": "https:\/\/..."
        |        },
        |        "is_admin": true,
        |        "is_owner": true,
        |        "has_2fa": true,
        |        "has_files": true
        |    }
        |}
      """.stripMargin)
    when(mockHttpClient.get("users.info", Map("user" -> "U023BECGF", "token" -> testApiKey))).thenReturn(json)
  }

  private[this] def setListMock(): Unit = {
    val json = parse("""
        |{
        |    "ok": true,
        |    "members": [
        |        {
        |            "id": "U023BECGF",
        |            "name": "bobby",
        |            "deleted": false,
        |            "color": "9f69e7",
        |            "profile": {
        |                "first_name": "Bobby",
        |                "last_name": "Tables",
        |                "real_name": "Bobby Tables",
        |                "email": "bobby@slack.com",
        |                "skype": "my-skype-name",
        |                "phone": "+1 (123) 456 7890",
        |                "image_24": "https:\/\/...",
        |                "image_32": "https:\/\/...",
        |                "image_48": "https:\/\/...",
        |                "image_72": "https:\/\/...",
        |                "image_192": "https:\/\/..."
        |            },
        |            "is_admin": true,
        |            "is_owner": true,
        |            "has_2fa": false,
        |            "has_files": true
        |        }
        |    ]
        |}
      """.stripMargin)
    when(mockHttpClient.get("users.list", Map("token" -> testApiKey))).thenReturn(json)
  }

  private[this] def setGetPresenceMock(): Unit = {
    val json = ("ok" -> true) ~ ("presence" -> "active")
    when(mockHttpClient.get("users.getPresence", Map("user" -> "hoge" ,"token" -> testApiKey))).thenReturn(json)
  }

  "Users.info(user)" should "get slack member from user id" in {
    val user = "U023BECGF"
    val response = users.info(user)
    response.ok shouldBe true
    response.user.id shouldBe user
    response.user.deleted shouldBe false
  }

  "Users.list()" should "get slack members" in {
    val response = users.list()
    response.ok shouldBe true
    response.members.nonEmpty shouldBe true

    val member = response.members.head
    member.id shouldBe "U023BECGF"
    member.deleted shouldBe false
    member.isAdmin shouldBe Some(true)
    member.isOwner shouldBe Some(true)
    member.has2fa shouldBe Some(false)
    member.profile.size shouldBe 11
  }

  "Users.getPresence" should "get slack member presence" in {
    val response = users.getPresence("hoge")
    response.ok shouldBe true
    response.presence shouldBe Presence.Active
  }
}
