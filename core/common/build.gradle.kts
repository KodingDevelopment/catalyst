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
    shadow("org.slf4j:slf4j-api:2.0.3")
    shadow("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // Injection
    shadow("com.google.inject:guice:5.1.0")
    shadow("com.google.inject.extensions:guice-assistedinject:5.1.0")
    shadow("dev.misfitlabs.kotlinguice4:kotlin-guice:1.6.0")
}
