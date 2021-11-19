package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed class InspectorState {

    /**
     * Hierarchy not captured.
     */
    @Stable
    object NoHierarchy : InspectorState()

    /**
     * Hierarchy captured.
     */
    @Stable
    sealed class HasHierarchy(
        open val rootNode: ViewNode
    ) : InspectorState() {

        /**
         * Hierarchy captured, but view not selected.
         */
        @Immutable
        data class NotSelected(
            override val rootNode: ViewNode
        ) : HasHierarchy(rootNode)

        /**
         * Hierarchy captured, view selected.
         */
        @Immutable
        data class Selected constructor(
            override val rootNode: ViewNode,
            override val selectedCollection: List<ViewNode>,
            override val selectedIndex: Int,
            override val selectedX: Float,
            override val selectedY: Float,
        ) : HasHierarchy(rootNode),
            HasSelected {

            init {
                require(selectedCollection.isNotEmpty())
                require(selectedIndex in selectedCollection.indices)
            }
        }

        /**
         * Hierarchy captured, view pinned.
         */
        @Immutable
        data class Pinned(
            override val rootNode: ViewNode,
            override val pinnedCollection: List<ViewNode>,
            override val pinnedIndex: Int,
            override val pinnedX: Float,
            override val pinnedY: Float,
        ) : HasHierarchy(rootNode),
            HasPinned {

            init {
                require(pinnedCollection.isNotEmpty())
                require(pinnedIndex in pinnedCollection.indices)
            }
        }

        @Immutable
        data class PinnedAndSelected(
            override val rootNode: ViewNode,
            override val pinnedCollection: List<ViewNode>,
            override val pinnedIndex: Int,
            override val pinnedX: Float,
            override val pinnedY: Float,
            override val selectedCollection: List<ViewNode>,
            override val selectedIndex: Int,
            override val selectedX: Float,
            override val selectedY: Float,
        ) : HasHierarchy(rootNode),
            HasPinned,
            HasSelected {

            init {
                require(pinnedCollection.isNotEmpty())
                require(pinnedIndex in pinnedCollection.indices)
                require(selectedCollection.isNotEmpty())
                require(selectedIndex in selectedCollection.indices)
            }
        }
    }

    @Stable
    interface HasSelected {
        val selectedCollection: List<ViewNode>
        val selectedIndex: Int
        val selectedX: Float
        val selectedY: Float
        val selectedNode: ViewNode
            get() = selectedCollection[selectedIndex]
    }

    @Stable
    interface HasPinned {
        val pinnedCollection: List<ViewNode>
        val pinnedIndex: Int
        val pinnedX: Float
        val pinnedY: Float
        val pinnedNode: ViewNode
            get() = pinnedCollection[pinnedIndex]
    }
}