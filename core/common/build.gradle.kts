/*
 * Catalyst - Minecraft plugin development toolkit
 * Copyright (C) 2022  Koding Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

dependencies {
    // Logging
    shadow("org.slf4j:slf4j-api:2.0.4")
    shadow("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // Injection
    shadow("org.kodein.di:kodein-di:7.16.0")
    shadow("org.kodein.di:kodein-di-jxinject-jvm:7.16.0")
}

configurations {
    "api" {
        isCanBeConsumed = true
    }
}