package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_NAME_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes.AttributeBitmap
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes.AttributeColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes.AttributeCommon
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes.AttributeDimension
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes.highlightSearchQuery
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.style.InspectorDetailsStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AttributesList(
    categorizedAttributes: List<CategorizedAttributes>,
    searchQuery: String,
) {
    val categorizedAttributesState = rememberUpdatedState(categorizedAttributes)
    val stickyHeaderFactory = remember<LazyListScope.(CategorizedAttributes) -> Unit> {
        { category ->
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(InspectorDetailsStyle.Colors.stickyHeaderBackground)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }
    }
    val itemsFactory = remember<LazyListScope.(CategorizedAttributes, String, Color) -> Unit> {
        { category, query, highlightColor ->
            items(category.attributes.size) { index ->
                val attribute = category.attributes[index]
                val name = highlightSearchQuery(attribute.name, query, highlightColor)
                exhaustive..when (attribute) {
                    is CommonAttribute -> AttributeCommon(name, attribute)
                    is DimensionAttribute -> AttributeDimension(name, attribute)
                    is ColorAttribute -> AttributeColor(name, attribute)
                    is BitmapAttribute -> AttributeBitmap(name, attribute)
                }
                if (index < category.attributes.lastIndex) {
                    Divider()
                }
            }
        }
    }
    val searchQueryHighlightColor = InspectorDetailsStyle.Colors.searchQueryHighlight
    LazyColumn {
        categorizedAttributesState.value.fastForEach { category ->
            if (category.name != CATEGORY_NAME_MAIN) {
                stickyHeaderFactory(this, category)
            }
            itemsFactory(this, category, searchQuery, searchQueryHighlightColor)
        }
    }
}
