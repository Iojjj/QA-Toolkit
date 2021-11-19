package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.AnnotatedString
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute

@Composable
internal fun AttributeBitmap(
    name: AnnotatedString,
    attribute: BitmapAttribute,
) {
    val painter = remember(attribute) {
        BitmapPainter(attribute.bitmap.asImageBitmap())
    }
    AttributeTemplate(
        name = name,
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .previewBackground()
        )
    }
}