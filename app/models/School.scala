package models

/**
 * Created by mikio on 2017/03/18.
 */
sealed trait School {
  val url: String
}

case object UndergraduateSchool extends School {
  val url: String = "http://kyoumu.office.uec.ac.jp/kyuukou/kyuukou.html"
}

case object GraduateSchool extends School {
  val url: String = "http://kyoumu.office.uec.ac.jp/kyuukou/kyuukou2.html"
}
