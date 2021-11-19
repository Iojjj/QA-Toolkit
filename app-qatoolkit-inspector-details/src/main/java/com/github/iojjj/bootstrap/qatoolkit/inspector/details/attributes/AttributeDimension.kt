package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalDpFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPxFormatter
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType

@Composable
internal fun AttributeDimension(
    name: AnnotatedString,
    attribute: DimensionAttribute,
) {
    val displayedUnitState = remember {
        mutableStateOf(attribute.displayUnits[0])
    }
    val density = LocalDensity.current.density
    val fontScale = LocalDensity.current.fontScale
    AttributeTemplate(
        name = name,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = {
                    val index = attribute.displayUnits.indexOf(displayedUnitState.value)
                        .takeUnless { it == attribute.displayUnits.lastIndex }
                        ?.plus(1)
                        ?: 0
                    displayedUnitState.value = attribute.displayUnits[index]
                }
            )
    ) {
        Text(
            text = when (displayedUnitState.value) {
                DimensionType.DP -> LocalDpFormatter.current.invoke(attribute.value / density)
                DimensionType.SP -> LocalDpFormatter.current.invoke(attribute.value / fontScale).replace("dp", "sp")
                DimensionType.PX -> LocalPxFormatter.current.invoke(attribute.value)
            }.toString(),
            style = MaterialTheme.typography.body1
        )
    }
}