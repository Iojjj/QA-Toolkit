package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute

@Composable
internal fun AttributeCommon(
    name: AnnotatedString,
    attribute: CommonAttribute,
) {
    AttributeTemplate(
        name = name,
    ) {
        Text(
            text = attribute.value,
            style = MaterialTheme.typography.body1
        )
    }
}