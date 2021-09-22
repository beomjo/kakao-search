/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.beomjo.search.datasource.local

import io.github.beomjo.search.datasource.local.converter.DateConverter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.util.Date

internal class DateConverterSpec : FunSpec({
    val converter = DateConverter()
    test("timestamp is null, converted timestamp return null") {
        val timestamp = null

        val date = converter.fromTimestamp(timestamp)

        date shouldBe null
    }

    test("date is null, converted date return null") {
        val date = null

        val timestamp = converter.dateToTimestamp(date)

        timestamp shouldBe null
    }

    test("success converted from timestamp to date") {
        val timestamp = 100000L

        val date = converter.fromTimestamp(timestamp)

        date.shouldNotBeNull()
        date.time shouldBe timestamp
    }

    test("success converted from date to timestamp") {
        val date = mockk<Date>(relaxed = true)

        val timestamp = converter.dateToTimestamp(date)

        timestamp.shouldNotBeNull()
        timestamp shouldBe date.time
    }
})
