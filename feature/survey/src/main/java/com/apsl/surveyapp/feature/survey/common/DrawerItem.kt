package com.apsl.surveyapp.feature.survey.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderShared
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.ui.graphics.vector.ImageVector
import com.apsl.surveyapp.core.ui.R as CoreR

sealed class DrawerItem(
    val imageVector: ImageVector,
    @StringRes val titleResId: Int,
    val onClick: () -> Unit
) {
    class AllSurveys(onClick: () -> Unit) : DrawerItem(
        imageVector = Icons.Rounded.List,
        titleResId = CoreR.string.all_surveys,
        onClick = onClick
    )

    class MySurveys(onClick: () -> Unit) : DrawerItem(
        imageVector = Icons.Rounded.FolderShared,
        titleResId = CoreR.string.my_surveys,
        onClick = onClick
    )

    class SignOut(onClick: () -> Unit) : DrawerItem(
        imageVector = Icons.Rounded.Logout,
        titleResId = CoreR.string.sign_out,
        onClick = onClick
    )
}
