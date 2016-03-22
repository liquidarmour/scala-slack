package com.ponkotuy.slack.responses

import org.json4s._

sealed abstract class Presence(val value: String)

object Presence {
  case object Active extends Presence("active")
  case object Away extends Presence("away")
  case object Auto extends Presence("auto")

  val values = Vector(Active, Away, Auto)
  def findName(name: String): Option[Presence] = values.find(_.value == name)
}

class PresenceSerializer extends Serializer[Presence] {
  private val PresenceClass = classOf[Presence]

  override def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Presence] = {
    case (TypeInfo(PresenceClass, _), json) => json match {
      case JString(name) => Presence.findName(name).get
      case _ => throw new MappingException(s"Can't convert ${json} to Presence value")
    }
  }

  override def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case p: Presence => JString(p.value)
  }
}
