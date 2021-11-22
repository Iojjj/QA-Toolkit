package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import com.github.iojjj.bootstrap.qatoolkit.settings.style.SettingsStyle

private val RIPPLE_RADIUS = BUTTON_SIZE / 2

@Composable
fun IconRadioButton(
    painter: Painter,
    contentDescription: String?,
    isSelected: Boolean,
    onClick: (() -> Unit)?,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val selectableModifier = if (onClick != null) {
        Modifier.selectable(
            selected = isSelected,
            onClick = onClick,
            enabled = enabled,
            role = Role.RadioButton,
            interactionSource = interactionSource,
            indication = rememberRipple(
                bounded = false,
                radius = RIPPLE_RADIUS
            )
        )
    } else {
        Modifier
    }
    val tintColor by animateColorAsState(
        SettingsStyle.Colors.contentColor(isChecked = isSelected, isEnabled = true)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .then(selectableModifier)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = tintColor,
        )
    }
}