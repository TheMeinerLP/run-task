/*
 * Run Paper Gradle Plugin
 * Copyright (c) 2021 Jason Penilla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.jpenilla.runpaper.util

import java.time.Duration
import java.time.temporal.ChronoUnit

internal object DurationParser {
  /**
   * Map of accepted abbreviation [Char]s to [ChronoUnit].
   */
  val units = mapOf(
    'd' to ChronoUnit.DAYS,
    'h' to ChronoUnit.HOURS,
    'm' to ChronoUnit.MINUTES,
    's' to ChronoUnit.SECONDS
  )

  /**
   * Parses a [Duration] from [input].
   *
   * Accepted format is a number followed by a unit abbreviation.
   * See [units] for possible units.
   * Example input strings: `["1d", "12h", "1m", "30s"]`
   *
   * @param input formatted input string
   * @throws InvalidDurationException when [input] is improperly formatted
   */
  @Throws(InvalidDurationException::class)
  fun parse(input: String): Duration {
    if (input.trim().isEmpty()) {
      throw InvalidDurationException.noInput()
    }
    if (input.length < 2) {
      throw InvalidDurationException.invalidInput(input)
    }
    val unitAbbreviation = input.last()

    val unit = this.units[unitAbbreviation] ?: throw InvalidDurationException.invalidInput(input)

    val length = try {
      input.substring(0, input.length - 1).toLong()
    } catch (ex: NumberFormatException) {
      throw InvalidDurationException.invalidInput(input, ex)
    }

    return Duration.of(length, unit)
  }

  class InvalidDurationException private constructor(
    message: String,
    cause: Throwable? = null
  ) : IllegalArgumentException(message, cause) {
    internal companion object {
      private val infoMessage = """Accepted format is a number followed by a unit abbreviation.
        |Possible units: $units
        |Example input strings: ["1d", "12h", "1m", "30s"]
      """.trimMargin()

      fun noInput(): InvalidDurationException =
        InvalidDurationException("Cannot parse a Duration from an empty input string.\n${this.infoMessage}")

      fun invalidInput(input: String, cause: Throwable? = null) =
        InvalidDurationException("Cannot parse a Duration from input '$input'.\n${this.infoMessage}", cause)
    }
  }
}
