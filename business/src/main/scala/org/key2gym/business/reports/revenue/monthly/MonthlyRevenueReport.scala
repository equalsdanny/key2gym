/*
 * Copyright 2012-2013 Danylo Vashchilenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.key2gym.business.reports.revenue.monthly

import annotation.target.field
import org.key2gym.business.entities.MonthlyRevenue
import java.util.Date
import java.util.List
import javax.xml.bind.annotation._

/**
 *
 * @author Danylo Vashchilenko
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = Array("title", "intervalStart", "intervalEnd", 
			   "generated", "months"))
class MonthlyRevenueReport(
  
  val title: String = null,
  
  @(XmlSchemaType @field)(name="date") 
  val intervalStart: Date = null,
  
  @(XmlSchemaType @field)(name="date") 
  val intervalEnd: Date = null,
  
  val generated: Date = null,

  @(XmlElement @field)(name="month") 
  val months: List[MonthlyRevenue] = null,

  /* Required for a no-arg constructor. */
  u: Unit = ()

) {

  /* No-arg constructor required by JAXB. */
  def this() { this(u = ()) }

}
