package com.apsl.surveyapp.feature.survey.allsurveys

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apsl.surveyapp.core.ui.theme.SurveyAppTheme
import com.apsl.surveyapp.feature.survey.common.DrawerItem
import com.apsl.surveyapp.feature.survey.common.SurveyListItem
import com.apsl.surveyapp.feature.survey.common.SurveyUiModel
import kotlinx.coroutines.launch
import com.apsl.surveyapp.core.ui.R as CoreR

@Composable
fun AllSurveysScreen(
    viewModel: AllSurveysViewModel = hiltViewModel(),
    onNavigateToCreateSurvey: () -> Unit,
    onNavigateToMySurveys: () -> Unit,
    onNavigateToSurvey: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.run {
            getAllSurveys()
            updateSelectedDrawerItemIndex(0)
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AllSurveysScreenContent(
        uiState = uiState,
        onCreateSurveyClick = onNavigateToCreateSurvey,
        onMySurveysClick = onNavigateToMySurveys,
        onSurveyClick = onNavigateToSurvey,
        onSignOutClick = viewModel::signOut,
        onUpdateSelectedDrawerItemIndex = viewModel::updateSelectedDrawerItemIndex
    )
}

@Composable
fun AllSurveysScreenContent(
    uiState: AllSurveysUiState,
    onCreateSurveyClick: () -> Unit,
    onMySurveysClick: () -> Unit,
    onSurveyClick: (String) -> Unit,
    onSignOutClick: () -> Unit,
    onUpdateSelectedDrawerItemIndex: (Int) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = remember(onMySurveysClick, onSignOutClick) {
        listOf(
            DrawerItem.AllSurveys(onClick = {}),
            DrawerItem.MySurveys(onClick = onMySurveysClick),
            DrawerItem.SignOut(onClick = onSignOutClick)
        )
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerContent = {
            DismissibleDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Spacer(Modifier.height(16.dp))
                drawerItems.forEachIndexed { index, item ->
                    if (item is DrawerItem.SignOut) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(item.titleResId)) },
                        selected = index == uiState.selectedDrawerItemIndex,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = { Icon(imageVector = item.imageVector, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            item.onClick()
                            onUpdateSelectedDrawerItemIndex(index)
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface,
                            unselectedContainerColor = MaterialTheme.colorScheme.background,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                    if (index == 0) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(CoreR.string.all_surveys),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateSurveyClick,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = uiState.surveys, key = SurveyUiModel::id) { survey ->
                    SurveyListItem(survey = survey, onClick = { onSurveyClick(survey.id) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllSurveysScreenPreview() {
    SurveyAppTheme {
        AllSurveysScreenContent(
            uiState = AllSurveysUiState(),
            onCreateSurveyClick = {},
            onSurveyClick = {},
            onMySurveysClick = {},
            onSignOutClick = {},
            onUpdateSelectedDrawerItemIndex = {}
        )
    }
}
