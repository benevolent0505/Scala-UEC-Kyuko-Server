package models

import org.joda.time.LocalDateTime
import org.scalatest.FlatSpec
import utils.Factory

/**
 * Created by mikio on 2017/03/23.
 */
class KyukoTest extends FlatSpec {

  behavior of "KyukoDate"

  it should "have id, lecture, date, remark, createdAt" in {
    val id = 1l
    val lecture = Factory.createLecture()
    val date = new LocalDateTime(2016, 5, 10, 0, 0)
    val remark = "GW延長"
    val now = LocalDateTime.now

    val kyuko = KyukoDays(id, lecture, date, remark, now)

    assert(kyuko.id === id)
    assert(kyuko.lecture === lecture)
    assert(kyuko.date === date)
    assert(kyuko.remark === remark)
    assert(kyuko.createdAt === now)
  }
}
