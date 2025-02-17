/*
 * Copyright 2023 Blocker
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

package com.merxury.blocker.core.controllers.di

import com.merxury.blocker.core.controllers.IAppController
import com.merxury.blocker.core.controllers.root.api.RootApiAppController
import com.merxury.blocker.core.controllers.root.command.RootAppController
import com.merxury.blocker.core.controllers.shizuku.ShizukuAppController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppControllerModule {
    @Binds
    @RootCommandAppControl
    fun bindsRootAppController(rootAppController: RootAppController): IAppController

    @Binds
    @ShizukuAppControl
    fun bindsShizukuAppController(shizukuAppController: ShizukuAppController): IAppController

    @Binds
    @RootApiAppControl
    fun bindsRootApiAppController(rootApiAppController: RootApiAppController): IAppController
}
