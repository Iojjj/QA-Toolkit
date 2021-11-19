package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val BUTTON_SIZE = 48.dp

@Composable
fun <T : SettingsMenuItem> MenuList(
    modifier: Modifier = Modifier,
    menuItems: List<T>,
    selectedMenuItem: T,
    menuItemKey: ((T) -> Any)? = null,
    onMenuItemClicked: (menuItem: T) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .wrapContentWidth()
            .selectableGroup(),
    ) {
        items(menuItems, key = menuItemKey) { menuItem ->
            val onClick = remember(menuItemKey) {
                {
                    onMenuItemClicked(menuItem)
                }
            }
            Box(
                modifier = Modifier.size(BUTTON_SIZE)
            ) {
                IconRadioButton(
                    painter = painterResource(menuItem.icon),
                    contentDescription = null,
                    isSelected = menuItem == selectedMenuItem,
                    onClick = onClick,
                )
                if (menuItem is HasText) {
                    Text(
                        text = menuItem.text,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.BottomEnd)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}
