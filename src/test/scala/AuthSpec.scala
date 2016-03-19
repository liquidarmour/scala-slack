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

import com.ponkotuy.slack.HttpClient
import com.ponkotuy.slack.Methods.Auth
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}
import org.json4s.JsonDSL._

class AuthSpec extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {

  private val testApiKey = "TEST_API_KEY"

  private var mockHttpClient: HttpClient = _
  var auth: Auth = _

  override def beforeEach() {
    val json = ("ok" -> true) ~
        ("url" -> "test.slack.com") ~
        ("team" -> "Test Team") ~
        ("user" -> "testuser") ~
        ("team_id" -> "T12345") ~
        ("user_id" -> "U12345")
    mockHttpClient = mock[HttpClient]
    when(mockHttpClient.get("auth.test", Map("token" -> testApiKey)))
        .thenReturn(json)

    auth = new Auth(mockHttpClient, testApiKey)
  }

  "Auth.test()" should
      "make a call to auth.test and return the response in an AuthTestResponse object" in {
    val response = auth.test().get

    response.ok shouldBe true
    response.url shouldBe "test.slack.com"
    response.team shouldBe "Test Team"
    response.user shouldBe "testuser"
    response.teamId shouldBe "T12345"
    response.userId shouldBe "U12345"

    verify(mockHttpClient).get(
      "auth.test", Map("token" -> testApiKey))
  }

}
