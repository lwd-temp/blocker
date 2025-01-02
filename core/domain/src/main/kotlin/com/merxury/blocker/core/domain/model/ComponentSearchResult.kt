/*
 * Copyright 2025 Blocker
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

package com.merxury.blocker.core.domain.model

import com.merxury.blocker.core.model.data.AppItem
import com.merxury.blocker.core.model.data.ComponentInfo

data class ComponentSearchResult(
    val app: AppItem?,
    val activity: List<ComponentInfo> = listOf(),
    val service: List<ComponentInfo> = listOf(),
    val receiver: List<ComponentInfo> = listOf(),
    val provider: List<ComponentInfo> = listOf(),
)
