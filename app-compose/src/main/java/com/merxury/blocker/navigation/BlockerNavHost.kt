/*
 * Copyright 2023 Blocker
 * Copyright 2022 The Android Open Source Project
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

package com.merxury.blocker.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.merxury.blocker.feature.applist.appdetail.navigation.componentDetailScreen
import com.merxury.blocker.feature.applist.appdetail.navigation.navigateToComponentDetail
import com.merxury.blocker.feature.applist.navigation.appListRoute
import com.merxury.blocker.feature.applist.navigation.appListScreen
import com.merxury.blocker.feature.applist.navigation.navigateToAppList
import com.merxury.blocker.feature.generalrules.navigation.generalRuleScreen
import com.merxury.blocker.feature.helpandfeedback.navigation.navigateToSupportAndFeedback
import com.merxury.blocker.feature.helpandfeedback.navigation.supportAndFeedbackScreen
import com.merxury.blocker.feature.ruledetail.navigation.navigateToRuleDetail
import com.merxury.blocker.feature.ruledetail.navigation.ruleDetailScreen
import com.merxury.blocker.feature.search.navigation.searchScreen
import com.merxury.blocker.feature.settings.navigation.navigateToSettings
import com.merxury.blocker.feature.settings.navigation.settingsScreen
import com.merxury.blocker.ui.BlockerAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */

@Composable
fun BlockerNavHost(
    appState: BlockerAppState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = appListRoute,
) {
    val navController = appState.navController
    val listState = rememberLazyListState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        appListScreen(
            onBackClick = onBackClick,
            shouldShowTwoPane = appState.shouldShowTwoPane,
            snackbarHostState = snackbarHostState,
            listState = listState,
            navigateToAppDetail = navController::navigateToAppList,
            navigateToSettings = navController::navigateToSettings,
            navigateToSupportAndFeedback = navController::navigateToSupportAndFeedback,
            navigateToComponentDetail = navController::navigateToComponentDetail,
        )
        generalRuleScreen(
            navigateToRuleDetail = navController::navigateToRuleDetail,
        )
        ruleDetailScreen(
            onBackClick = onBackClick,
            navigateToAppDetail = navController::navigateToAppList,
        )
        searchScreen(
            navigateToAppDetail = navController::navigateToAppList,
            navigateToRuleDetail = navController::navigateToRuleDetail,
        )
        settingsScreen(
            onBackClick,
            snackbarHostState = snackbarHostState,
        )
        supportAndFeedbackScreen(onBackClick)
        componentDetailScreen(
            dismissHandler = onBackClick,
        )
    }
}
