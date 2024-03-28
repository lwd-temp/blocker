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

package com.merxury.blocker.feature.generalrule

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import app.cash.turbine.test
import com.merxury.blocker.core.domain.InitializeRuleStorageUseCase
import com.merxury.blocker.core.domain.SearchGeneralRuleUseCase
import com.merxury.blocker.core.domain.UpdateRuleMatchedAppUseCase
import com.merxury.blocker.core.model.ComponentType.ACTIVITY
import com.merxury.blocker.core.model.ComponentType.PROVIDER
import com.merxury.blocker.core.model.ComponentType.RECEIVER
import com.merxury.blocker.core.model.ComponentType.SERVICE
import com.merxury.blocker.core.model.data.ComponentInfo
import com.merxury.blocker.core.model.data.GeneralRule
import com.merxury.blocker.core.model.data.InstalledApp
import com.merxury.blocker.core.model.preference.AppPropertiesData
import com.merxury.blocker.core.testing.repository.TestAppPropertiesRepository
import com.merxury.blocker.core.testing.repository.TestAppRepository
import com.merxury.blocker.core.testing.repository.TestComponentRepository
import com.merxury.blocker.core.testing.repository.TestGeneralRuleRepository
import com.merxury.blocker.core.testing.repository.TestUserDataRepository
import com.merxury.blocker.core.testing.repository.defaultUserData
import com.merxury.blocker.core.testing.util.MainDispatcherRule
import com.merxury.blocker.feature.generalrules.GeneralRuleUiState.Loading
import com.merxury.blocker.feature.generalrules.GeneralRuleUiState.Success
import com.merxury.blocker.feature.generalrules.GeneralRulesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class GeneralRuleViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val tempFolder: TemporaryFolder = TemporaryFolder.builder()
        .assureDeletion()
        .build()

    private val appRepository = TestAppRepository()
    private val appPropertiesRepository = TestAppPropertiesRepository()
    private val generalRuleRepository = TestGeneralRuleRepository()
    private val userDataRepository = TestUserDataRepository()
    private val componentRepository = TestComponentRepository()
    private val dispatcher: CoroutineDispatcher = mainDispatcherRule.testDispatcher
    private lateinit var viewModel: GeneralRulesViewModel
    private lateinit var workManager: WorkManager

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        workManager = WorkManager.getInstance(context)
        // Use actual filesDir in the AndroidTest
        val filesDir = context.filesDir
        val initGeneralRuleUseCase = InitializeRuleStorageUseCase(
            filesDir = filesDir,
            ruleBaseFolder = filesDir.absolutePath,
            ioDispatcher = dispatcher,
            workManager = workManager,
        )
        val searchRule = SearchGeneralRuleUseCase(
            generalRuleRepository = generalRuleRepository,
            userDataRepository = userDataRepository,
            filesDir = tempFolder.newFolder(),
            ruleBaseFolder = tempFolder.newFolder().absolutePath,
        )
        val updateRule = UpdateRuleMatchedAppUseCase(
            generalRuleRepository = generalRuleRepository,
            appRepository = appRepository,
            userDataRepository = userDataRepository,
            componentRepository = componentRepository,
            ioDispatcher = dispatcher,
        )

        viewModel = GeneralRulesViewModel(
            appRepository = appRepository,
            appPropertiesRepository = appPropertiesRepository,
            generalRuleRepository = generalRuleRepository,
            initGeneralRuleUseCase = initGeneralRuleUseCase,
            searchRule = searchRule,
            updateRule = updateRule,
            ioDispatcher = dispatcher,
        )
    }

    @Test
    fun uiState_whenInitial_thenShowLoading() = runTest {
        assertEquals(Loading, viewModel.uiState.value)
    }

    @Test
    fun uiState_whenSuccess_thenShowData() = runTest {
        viewModel.uiState.test {
            appPropertiesRepository.sendAppProperties(AppPropertiesData())
            appRepository.sendAppList(sampleAppList)
            userDataRepository.sendUserData(defaultUserData)
            componentRepository.sendComponentList(sampleComponentList)
            generalRuleRepository.sendRuleList(sampleRuleList)
            assertEquals(Loading, awaitItem())
            assertEquals(Success(
                rules = sampleRuleList,
                matchProgress = 0F,
            ), awaitItem())
            assertEquals(Success(rules = sampleRuleList, matchProgress = 1F), awaitItem())
        }
    }
}

private val sampleRuleList = listOf(
    GeneralRule(
        id = 1,
        name = "Rule1",
        company = "Rule1 company",
        description = "Rule1 description",
        sideEffect = "Unknown",
        safeToBlock = true,
        contributors = listOf("Online contributor"),
        searchKeyword = listOf("androidx.google.example1"),
    ),
    GeneralRule(
        id = 2,
        name = "Rule2",
        company = "Rule2 company",
        description = "Rule2 description",
        sideEffect = "Unknown",
        safeToBlock = false,
        contributors = listOf("Google"),
        searchKeyword = listOf(
            "androidx.google.example1",
            "androidx.google.example2",
            "androidx.google.example3",
            "androidx.google.example4",
        ),
    ),
    GeneralRule(
        id = 3,
        name = "Rule3",
        company = "Rule3 company",
        description = "Rule3 description",
        sideEffect = "Unknown",
        safeToBlock = false,
        contributors = listOf("Tester"),
        searchKeyword = listOf(
            "com.ss.android.socialbase.",
            "com.ss.android.downloadlib.",
            "com.example.component.",
            "com.example.component.",
            "com.example.tea.component.",
            "com.example.sdk.component.",
        ),
    ),
)

private val sampleAppList = listOf(
    InstalledApp(
        label = "App1",
        packageName = "com.merxury.test1",
    ),
    InstalledApp(
        label = "App2",
        packageName = "com.merxury.test2",
    ),
    InstalledApp(
        label = "App3",
        packageName = "com.merxury.test3",
    ),
)

private val sampleComponentList = listOf(
    ComponentInfo(
        simpleName = "Activity1",
        name = "com.merxury.blocker.test.activity1",
        packageName = "com.merxury.test1",
        type = ACTIVITY,
        description = "An example activity",
    ),
    ComponentInfo(
        simpleName = "Service1",
        name = "com.merxury.blocker.test.service1",
        packageName = "com.merxury.test1",
        type = SERVICE,
        description = "An example service",
        pmBlocked = true,
    ),
    ComponentInfo(
        simpleName = "Service2",
        name = "com.merxury.blocker.test.service2",
        packageName = "com.merxury.test1",
        type = SERVICE,
        description = "An example service",
    ),
    ComponentInfo(
        simpleName = "Receiver1",
        name = "com.merxury.blocker.test.receiver1",
        packageName = "com.merxury.test1",
        type = RECEIVER,
        description = "An example receiver",
    ),
    ComponentInfo(
        simpleName = "Provider1",
        name = "com.merxury.blocker.test.provider1",
        packageName = "com.merxury.test1",
        type = PROVIDER,
        description = "An example provider",
    ),
)
