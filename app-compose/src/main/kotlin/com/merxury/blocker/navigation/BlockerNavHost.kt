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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.merxury.blocker.core.designsystem.component.SnackbarHostState
import com.merxury.blocker.core.model.data.IconBasedThemingState
import com.merxury.blocker.feature.appdetail.navigation.componentDetailScreen
import com.merxury.blocker.feature.appdetail.navigation.detailScreen
import com.merxury.blocker.feature.appdetail.navigation.navigateToAppDetail
import com.merxury.blocker.feature.appdetail.navigation.navigateToComponentDetail
import com.merxury.blocker.feature.applist.navigation.APP_LIST_ROUTE
import com.merxury.blocker.feature.applist.navigation.appListScreen
import com.merxury.blocker.feature.generalrules.navigation.generalRuleScreen
import com.merxury.blocker.feature.helpandfeedback.navigation.navigateToSupportAndFeedback
import com.merxury.blocker.feature.helpandfeedback.navigation.supportAndFeedbackScreen
import com.merxury.blocker.feature.ruledetail.navigation.navigateToRuleDetail
import com.merxury.blocker.feature.ruledetail.navigation.ruleDetailScreen
import com.merxury.blocker.feature.search.navigation.searchScreen
import com.merxury.blocker.feature.settings.navigation.navigateToSettings
import com.merxury.blocker.feature.settings.navigation.settingsScreen
import com.merxury.blocker.feature.sort.navigation.appSortScreen
import com.merxury.blocker.feature.sort.navigation.componentSortScreen
import com.merxury.blocker.feature.sort.navigation.navigateToAppSortScreen
import com.merxury.blocker.feature.sort.navigation.navigateToComponentSortScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun BlockerNavHost(
    bottomSheetNavigator: BottomSheetNavigator,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    dismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = APP_LIST_ROUTE,
    updateIconBasedThemingState: (IconBasedThemingState) -> Unit = {},
) {
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
            appListScreen(
                navigateToAppDetail = navController::navigateToAppDetail,
                navigateToSettings = navController::navigateToSettings,
                navigateToSupportAndFeedback = navController::navigateToSupportAndFeedback,
                navigateTooAppSortScreen = navController::navigateToAppSortScreen,
            )
            detailScreen(
                onBackClick = onBackClick,
                snackbarHostState = snackbarHostState,
                navigateToComponentDetail = navController::navigateToComponentDetail,
                navigatedToComponentSortScreen = navController::navigateToComponentSortScreen,
                updateIconBasedThemingState = updateIconBasedThemingState,
            )
            generalRuleScreen(
                navigateToRuleDetail = navController::navigateToRuleDetail,
            )
            ruleDetailScreen(
                onBackClick = onBackClick,
                snackbarHostState = snackbarHostState,
                navigateToAppDetail = navController::navigateToAppDetail,
                updateIconBasedThemingState = updateIconBasedThemingState,
            )
            searchScreen(
                snackbarHostState = snackbarHostState,
                navigateToAppDetail = navController::navigateToAppDetail,
                navigateToRuleDetail = navController::navigateToRuleDetail,
            )
            settingsScreen(
                onBackClick,
                snackbarHostState = snackbarHostState,
            )
            supportAndFeedbackScreen(
                onBackClick = onBackClick,
                snackbarHostState = snackbarHostState,
            )
            componentDetailScreen(
                dismissHandler = onBackClick,
            )
            componentSortScreen(
                dismissHandler = dismissBottomSheet,
            )
            appSortScreen(
                dismissHandler = dismissBottomSheet,
            )
        }
    }
}
