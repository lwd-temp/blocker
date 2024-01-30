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

package com.merxury.blocker.core.ui.extension

import com.merxury.blocker.core.domain.model.ComponentSearchResult
import com.merxury.blocker.core.model.data.ComponentInfo
import com.merxury.blocker.core.model.data.ControllerType
import com.merxury.blocker.core.model.data.ControllerType.IFW
import com.merxury.blocker.core.result.Result

/**
 * Utility function to update the switch state of a [ComponentSearchResult]
 * It is used in doing async operations on the switch button and updating the state of the ui
 * before the actual operation is completed
 */
fun Result<ComponentSearchResult>.updateComponentInfoSwitchState(
    list: List<ComponentInfo>,
    controllerType: ControllerType,
    enabled: Boolean,
): Result<ComponentSearchResult> {
    // If the result is not success, return the result as it is
    if (this !is Result.Success) return this
    // Find the matching components in the list and change the ifwBlocked to the new value
    val activity = updateComponentInfoListState(list, this.data.activity, controllerType, enabled)
    val service = updateComponentInfoListState(list, this.data.service, controllerType, enabled)
    val receiver = updateComponentInfoListState(list, this.data.receiver, controllerType, enabled)
    val provider = updateComponentInfoListState(list, this.data.provider, controllerType, enabled)
    return Result.Success(
        data.copy(
            activity = activity,
            service = service,
            receiver = receiver,
            provider = provider,
        ),
    )
}

private fun updateComponentInfoListState(
    current: List<ComponentInfo>,
    change: List<ComponentInfo>,
    controllerType: ControllerType,
    enabled: Boolean,
): List<ComponentInfo> {
    val updatedItems = current.filter { currentItem ->
        change.any { changedItem ->
            currentItem.packageName == changedItem.packageName && currentItem.name == changedItem.name
        }
    }.map {
        if (controllerType == IFW) {
            it.copy(ifwBlocked = !enabled)
        } else {
            it.copy(pmBlocked = !enabled)
        }
    }
    // Update the current list, replace the updated items with the old items
    return current.map { currentItem ->
        updatedItems.find {
            it.packageName == currentItem.packageName && it.name == currentItem.name
        } ?: currentItem
    }
}
