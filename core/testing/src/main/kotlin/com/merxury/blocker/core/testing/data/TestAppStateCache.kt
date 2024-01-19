/*
 * Copyright 2024 Blocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merxury.blocker.core.testing.data

import com.merxury.blocker.core.data.appstate.AppState
import com.merxury.blocker.core.data.appstate.IAppStateCache
import javax.inject.Inject

class TestAppStateCache @Inject constructor() : IAppStateCache {
    private val cache = mutableMapOf<String, AppState>()
    override fun getOrNull(packageName: String): AppState? {
        return cache[packageName]
    }

    override suspend fun get(packageName: String): AppState {
        return cache[packageName] ?: AppState(packageName = packageName)
    }

    fun putAppState(vararg appState: AppState) {
        appState.forEach {
            cache[it.packageName] = it
        }
    }
}
