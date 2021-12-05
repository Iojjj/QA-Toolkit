package com.github.iojjj.bootstrap.qatoolkit.inspector

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.core.stdlib.toIntAlpha
import com.github.iojjj.bootstrap.pub.coroutines.launchUnit
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes
import com.github.iojjj.bootstrap.qatoolkit.BridgeInfo
import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.getColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.EnterInspectDetailsMode
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.PinNode
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.ToggleDimensionType
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.ToggleLayer
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.TogglePercentVisibility
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeMenuItem.ToggleSettings
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.AttributesStatus
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.InspectorDetailsState
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BRIDGE_SERVICE_NAME
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BridgeMessage.LoadAttributesRequest
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.ClientToServiceMessenger
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.ServiceMessage
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("StaticFieldLeak")
class InspectorModeViewModel(
    private val context: Context,
    private val storage: SharedPreferences,
    isNightModeState: StateFlow<Boolean>,
) : ToolkitModeViewModel<InspectorModeMenuItem>() {

    private val state = MutableStateFlow<InspectorState>(InspectorState.NoHierarchy)
    private val nodeStrokeScaleState = MutableStateFlow(storage.getFloat(KEY_NODE_STROKE_SCALE, DEFAULT_NODE_STROKE_SCALE))
    private val textScaleState = MutableStateFlow(storage.getFloat(KEY_TEXT_SCALE, DEFAULT_TEXT_SCALE))

    private val dayNodeStrokeColorState = MutableStateFlow(storage.getColor(KEY_DAY_NODE_STROKE_COLOR, DEFAULT_DAY_NODE_STROKE_COLOR))
    private val daySelectedStrokeColorState = MutableStateFlow(storage.getColor(KEY_DAY_SELECTED_STROKE_COLOR, DEFAULT_DAY_SELECTED_STROKE_COLOR))
    private val dayPinnedStrokeColorState = MutableStateFlow(storage.getColor(KEY_DAY_PINNED_STROKE_COLOR, DEFAULT_DAY_PINNED_STROKE_COLOR))
    private val dayOverlayColorState = MutableStateFlow(storage.getColor(KEY_DAY_OVERLAY_COLOR, DEFAULT_DAY_OVERLAY_COLOR))
    private val dayArrowColorState = MutableStateFlow(storage.getColor(KEY_DAY_ARROW_COLOR, DEFAULT_DAY_ARROW_COLOR))
    private val dayTextColorState = MutableStateFlow(storage.getColor(KEY_DAY_TEXT_COLOR, DEFAULT_DAY_TEXT_COLOR))

    private val nightNodeStrokeColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_NODE_STROKE_COLOR, DEFAULT_NIGHT_NODE_STROKE_COLOR))
    private val nightSelectedStrokeColorState = MutableStateFlow(
        storage.getColor(KEY_NIGHT_SELECTED_STROKE_COLOR, DEFAULT_NIGHT_SELECTED_STROKE_COLOR)
    )
    private val nightPinnedStrokeColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_PINNED_STROKE_COLOR, DEFAULT_NIGHT_PINNED_STROKE_COLOR))
    private val nightOverlayColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_OVERLAY_COLOR, DEFAULT_NIGHT_OVERLAY_COLOR))
    private val nightArrowColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_ARROW_COLOR, DEFAULT_NIGHT_ARROW_COLOR))
    private val nightTextColorState = MutableStateFlow(storage.getColor(KEY_NIGHT_TEXT_COLOR, DEFAULT_NIGHT_TEXT_COLOR))

    private val dimensionTypeState = MutableStateFlow(DimensionType.DP)
    private val areSettingsShownState = MutableStateFlow(false)
    private val isPercentVisibleState = MutableStateFlow(false)
    private val nodeDetailsState = MutableStateFlow<ViewNode?>(null)
    private val attributesSearchQueryState = MutableStateFlow("")

    private var bridgeServiceMessenger: ClientToServiceMessenger? = null

    override val menuItems: StateFlow<List<ToolkitBarMenuItem>> = combine(
        state,
        dimensionTypeState,
        isPercentVisibleState,
        areSettingsShownState,
        ::makeMenuItems
    ).stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.Lazily
    )
    override val onMenuItemClick: (InspectorModeMenuItem) -> Unit = OnMenuItemClickListener()
    override val onBackPressedCallback: OnBackPressedCallback = OnBackPressListener()

    override fun onCleared() {
        bridgeServiceMessenger?.disconnect()
        super.onCleared()
    }

    val rootNode: StateFlow<ViewNode?> = state
        .map { (it as? InspectorState.HasHierarchy)?.rootNode }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.Lazily
        )
    val selectedNode: StateFlow<ViewNode?> = state
        .map { (it as? InspectorState.HasSelected)?.selectedNode }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.Lazily
        )
    val pinnedNode: StateFlow<ViewNode?> = state
        .map { (it as? InspectorState.HasPinned)?.pinnedNode }
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.Lazily
        )
    val nodeStrokeScale: StateFlow<Float> = nodeStrokeScaleState.asStateFlow()
    val textScale: StateFlow<Float> = textScaleState.asStateFlow()

    val nodeStrokeColor: StateFlow<Color> = switchColors(isNightModeState, dayNodeStrokeColorState, nightNodeStrokeColorState)
    val selectedStrokeColor: StateFlow<Color> = switchColors(isNightModeState, daySelectedStrokeColorState, nightSelectedStrokeColorState)
    val pinnedStrokeColor: StateFlow<Color> = switchColors(isNightModeState, dayPinnedStrokeColorState, nightPinnedStrokeColorState)
    val overlayColor: StateFlow<Color> = switchColors(isNightModeState, dayOverlayColorState, nightOverlayColorState)
    val arrowColor: StateFlow<Color> = switchColors(isNightModeState, dayArrowColorState, nightArrowColorState)
    val textColor: StateFlow<Color> = switchColors(isNightModeState, dayTextColorState, nightTextColorState)

    val dimensionType: StateFlow<DimensionType> = dimensionTypeState.asStateFlow()
    val areSettingsShown: StateFlow<Boolean> = areSettingsShownState.asStateFlow()
    val isPercentVisible: StateFlow<Boolean> = isPercentVisibleState.asStateFlow()
    val details: StateFlow<InspectorDetailsState?> = nodeDetailsState
        .flatMapLatest(::produceInspectorDetailsState)
        .combine(attributesSearchQueryState, ::filterAttributes)
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.Lazily
        )

    val attributesSearchQuery: StateFlow<String> = attributesSearchQueryState.asStateFlow()

    val onNodeStrokeScaleChange: (Float) -> Unit = {
        nodeStrokeScaleState.value = it
    }
    val onTextScaleChange: (Float) -> Unit = {
        textScaleState.value = it
    }
    val onOverlayColorChange: (Color) -> Unit = updateColors(isNightModeState, dayOverlayColorState, nightOverlayColorState)
    val onArrowColorChange: (Color) -> Unit = updateColors(isNightModeState, dayArrowColorState, nightArrowColorState)
    val onNodeStrokeColorChange: (Color) -> Unit = updateColors(isNightModeState, dayNodeStrokeColorState, nightNodeStrokeColorState)
    val onSelectedStrokeColorChange: (Color) -> Unit = updateColors(isNightModeState, daySelectedStrokeColorState, nightSelectedStrokeColorState)
    val onPinnedStrokeColorChange: (Color) -> Unit = updateColors(isNightModeState, dayPinnedStrokeColorState, nightPinnedStrokeColorState)
    val onTextColorChange: (Color) -> Unit = updateColors(isNightModeState, dayTextColorState, nightTextColorState)

    val onAreSettingsShownChange: (Boolean) -> Unit = {
        areSettingsShownState.value = it
    }
    val onDetailsDismiss: () -> Unit = ::hideNodeDetails

    val onSearchQueryChange: (String) -> Unit = {
        attributesSearchQueryState.value = it.trim().lowercase()
    }

    val onNodeSelectionRequested: (Offset) -> Unit = {
        exhaustive..when (val currentState = state.value) {
            InspectorState.NoHierarchy -> {
                error("Impossible route.")
            }
            is InspectorState.HasHierarchy -> {
                val nodes = currentState.rootNode.findAllNodesUnder(it)
                if (nodes.isNotEmpty()) {
                    state.value = when (currentState) {
                        is InspectorState.HasHierarchy.NotSelected,
                        is InspectorState.HasHierarchy.Selected -> {
                            InspectorState.HasHierarchy.Selected(
                                rootNode = currentState.rootNode,
                                selectedCollection = nodes,
                                selectedIndex = 0,
                                selectedX = it.x,
                                selectedY = it.y,
                            )
                        }
                        is InspectorState.HasHierarchy.Pinned -> {
                            InspectorState.HasHierarchy.PinnedAndSelected(
                                rootNode = currentState.rootNode,
                                pinnedCollection = currentState.pinnedCollection,
                                pinnedIndex = currentState.pinnedIndex,
                                pinnedX = currentState.pinnedX,
                                pinnedY = currentState.pinnedY,
                                selectedCollection = nodes,
                                selectedIndex = 0,
                                selectedX = it.x,
                                selectedY = it.y,
                            )
                        }
                        is InspectorState.HasHierarchy.PinnedAndSelected -> {
                            currentState.copy(
                                selectedCollection = nodes,
                                selectedIndex = 0,
                                selectedX = it.x,
                                selectedY = it.y,
                            )
                        }
                    }
                }
                Unit
            }
        }
    }

    init {
        updateBackPressListenerEnabledState()
        observeAndSaveChanges()
    }

    fun updateState(node: ViewNode?) {
        state.value = when (node) {
            null -> InspectorState.NoHierarchy
            else -> InspectorState.HasHierarchy.NotSelected(node)
        }
    }

    private fun updateBackPressListenerEnabledState() = viewModelScope.launchUnit {
        combine(
            state,
            nodeDetailsState,
            areSettingsShownState,
        ) { currentState, nodeDetails, areSettingsShown ->
            when {
                areSettingsShown -> {
                    true
                }
                nodeDetails != null -> {
                    true
                }
                currentState is InspectorState.HasSelected || currentState is InspectorState.HasPinned -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
            .collectLatest {
                onBackPressedCallback.isEnabled = it
            }
    }

    private fun observeAndSaveChanges() {
        updateFloat(storage, KEY_NODE_STROKE_SCALE, nodeStrokeScaleState)
        updateFloat(storage, KEY_TEXT_SCALE, textScaleState)

        updateColor(storage, KEY_DAY_OVERLAY_COLOR, dayOverlayColorState)
        updateColor(storage, KEY_DAY_ARROW_COLOR, dayArrowColorState)
        updateColor(storage, KEY_DAY_NODE_STROKE_COLOR, dayNodeStrokeColorState)
        updateColor(storage, KEY_DAY_SELECTED_STROKE_COLOR, daySelectedStrokeColorState)
        updateColor(storage, KEY_DAY_PINNED_STROKE_COLOR, dayPinnedStrokeColorState)
        updateColor(storage, KEY_DAY_TEXT_COLOR, dayTextColorState)

        updateColor(storage, KEY_NIGHT_OVERLAY_COLOR, nightOverlayColorState)
        updateColor(storage, KEY_NIGHT_ARROW_COLOR, nightArrowColorState)
        updateColor(storage, KEY_NIGHT_NODE_STROKE_COLOR, nightNodeStrokeColorState)
        updateColor(storage, KEY_NIGHT_SELECTED_STROKE_COLOR, nightSelectedStrokeColorState)
        updateColor(storage, KEY_NIGHT_PINNED_STROKE_COLOR, nightPinnedStrokeColorState)
        updateColor(storage, KEY_NIGHT_TEXT_COLOR, nightTextColorState)
    }

    private fun makeMenuItems(
        currentState: InspectorState,
        dimensionType: DimensionType,
        isPercentVisible: Boolean,
        areSettingsShown: Boolean,
    ): List<ToolkitBarMenuItem> {
        return listOf(
            ToggleDimensionType(
                text = dimensionType.name
            ),
            TogglePercentVisibility(
                isChecked = isPercentVisible
            ),
            ToggleLayer(
                text = when (currentState) {
                    is InspectorState.HasSelected -> {
                        (currentState.selectedCollection.size - currentState.selectedIndex).toString()
                    }
                    is InspectorState.HasPinned -> {
                        (currentState.pinnedCollection.size - currentState.pinnedIndex).toString()
                    }
                    else -> {
                        ""
                    }
                },
                isEnabled = currentState is InspectorState.HasSelected || currentState is InspectorState.HasPinned,
            ),
            PinNode(
                icon = when (currentState is InspectorState.HasPinned) {
                    true -> R.drawable.qatoolkit_inspector_ic_pin_filled_24dp
                    false -> R.drawable.qatoolkit_inspector_ic_pin_outlined_24dp
                },
                isEnabled = currentState != InspectorState.NoHierarchy && currentState !is InspectorState.HasHierarchy.NotSelected
            ),
            EnterInspectDetailsMode(
                isEnabled = currentState is InspectorState.HasSelected || currentState is InspectorState.HasPinned,
            ),
            ToggleSettings(
                isChecked = areSettingsShown
            )
        )
    }

    private suspend fun produceInspectorDetailsState(node: ViewNode?): Flow<InspectorDetailsState?> {
        return when (node) {
            null -> {
                flowOf(null)
            }
            else -> {
                when (node.bridgeInfo) {
                    is BridgeInfo.NotAvailable -> {
                        flowOf(InspectorDetailsState.Loaded(node.bridgeInfo.categories, AttributesStatus.BridgeNotConfigured))
                    }
                    is BridgeInfo.ViewNotConfigured -> {
                        flowOf(InspectorDetailsState.Loaded(node.bridgeInfo.categories, AttributesStatus.VirtualView))
                    }
                    is BridgeInfo.Available -> {
                        flowOf<InspectorDetailsState>(InspectorDetailsState.Loading)
                            .onCompletion {
                                emitAll(loadAttributes(node.bridgeInfo))
                            }
                    }
                }
            }
        }
    }

    private fun filterAttributes(
        detailsState: InspectorDetailsState?,
        searchQuery: String,
    ): InspectorDetailsState? {
        return when (detailsState) {
            null -> {
                null
            }
            InspectorDetailsState.Loading -> {
                detailsState
            }
            is InspectorDetailsState.Loaded -> {
                detailsState.copy(
                    categorizedAttributes = detailsState.categorizedAttributes.filterAttributes(searchQuery)
                )
            }
        }
    }

    private fun List<CategorizedAttributes>.filterAttributes(
        searchQuery: String
    ): List<CategorizedAttributes> {
        return if (searchQuery.isEmpty()) {
            this
        } else {
            asSequence()
                .mapNotNull { category ->
                    val filteredAttributes = category.attributes.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    if (filteredAttributes.isNotEmpty()) {
                        category.copy(
                            attributes = filteredAttributes
                        )
                    } else {
                        null
                    }
                }
                .toList()
        }
    }

    private fun showNodeDetails() {
        val node = when (val currentState = state.value) {
            is InspectorState.HasHierarchy.Selected -> {
                currentState.selectedNode
            }
            is InspectorState.HasHierarchy.Pinned -> {
                currentState.pinnedNode
            }
            is InspectorState.HasHierarchy.PinnedAndSelected -> {
                currentState.selectedNode
            }
            InspectorState.NoHierarchy,
            is InspectorState.HasHierarchy.NotSelected -> {
                error("")
            }
        }

        nodeDetailsState.value = node
    }

    private fun hideNodeDetails() {
        nodeDetailsState.value = null
        attributesSearchQueryState.value = ""
        bridgeServiceMessenger?.disconnect()
    }

    private fun pinNode() {
        state.value = when (val currentState = state.value) {
            is InspectorState.HasHierarchy.Selected -> {
                InspectorState.HasHierarchy.Pinned(
                    rootNode = currentState.rootNode,
                    pinnedCollection = currentState.selectedCollection,
                    pinnedIndex = currentState.selectedIndex,
                    pinnedX = currentState.selectedX,
                    pinnedY = currentState.selectedY,
                )
            }
            is InspectorState.HasHierarchy.Pinned -> {
                InspectorState.HasHierarchy.Selected(
                    rootNode = currentState.rootNode,
                    selectedCollection = currentState.pinnedCollection,
                    selectedIndex = currentState.pinnedIndex,
                    selectedX = currentState.pinnedX,
                    selectedY = currentState.pinnedY,
                )
            }
            is InspectorState.HasHierarchy.PinnedAndSelected -> {
                InspectorState.HasHierarchy.Pinned(
                    rootNode = currentState.rootNode,
                    pinnedCollection = currentState.selectedCollection,
                    pinnedIndex = currentState.selectedIndex,
                    pinnedX = currentState.selectedX,
                    pinnedY = currentState.selectedY,
                )
            }
            InspectorState.NoHierarchy,
            is InspectorState.HasHierarchy.NotSelected -> {
                error("Impossible route")
            }
        }
    }

    private fun toggleDimensionType() {
        dimensionTypeState.value = when (dimensionTypeState.value) {
            DimensionType.DP -> DimensionType.PX
            DimensionType.PX -> DimensionType.DP
        }
    }

    private fun switchLayer() {
        state.value = when (val currentState = state.value) {
            is InspectorState.HasHierarchy.Selected -> {
                currentState.copy(
                    selectedIndex = (currentState.selectedIndex + 1)
                        .takeIf { it < currentState.selectedCollection.size }
                        ?: 0
                )
            }
            is InspectorState.HasHierarchy.Pinned -> {
                currentState.copy(
                    pinnedIndex = (currentState.pinnedIndex + 1)
                        .takeIf { it < currentState.pinnedCollection.size }
                        ?: 0
                )
            }
            is InspectorState.HasHierarchy.PinnedAndSelected -> {
                currentState.copy(
                    selectedIndex = (currentState.selectedIndex + 1)
                        .takeIf { it < currentState.selectedCollection.size }
                        ?: 0
                )
            }
            InspectorState.NoHierarchy,
            is InspectorState.HasHierarchy.NotSelected -> {
                error("Impossible route")
            }
        }
    }

    private fun togglePercentVisibility() {
        isPercentVisibleState.value = !isPercentVisibleState.value
    }

    private fun toggleSettingsShown() {
        areSettingsShownState.value = !areSettingsShownState.value
    }

    private fun loadAttributes(
        bridgeInfo: BridgeInfo.Available
    ): Flow<InspectorDetailsState.Loaded> {
        val messenger = bridgeServiceMessenger
        val newMessenger = if (messenger == null || messenger.packageName != bridgeInfo.servicePackageName) {
            messenger?.disconnect()
            ClientToServiceMessenger(context, bridgeInfo.servicePackageName, BRIDGE_SERVICE_NAME)
                .also { bridgeServiceMessenger = it }
        } else {
            messenger
        }
        return flow {
            emit(
                when (val response = newMessenger.send(LoadAttributesRequest(bridgeInfo.uuid))) {
                    is ServiceMessage.Received -> {
                        when (val data = response.data) {
                            is LoadAttributesRequest.Response.ViewFound -> {
                                InspectorDetailsState.Loaded(data.categories, AttributesStatus.Ok)
                            }
                            LoadAttributesRequest.Response.ViewNotFound -> {
                                InspectorDetailsState.Loaded(bridgeInfo.categories, AttributesStatus.ViewNotFound)
                            }
                        }
                    }
                    is ServiceMessage.ConnectionFailure -> {
                        InspectorDetailsState.Loaded(bridgeInfo.categories, AttributesStatus.ConnectionIssue)
                    }
                    is ServiceMessage.PermissionRequired -> {
                        InspectorDetailsState.Loaded(bridgeInfo.categories, AttributesStatus.ConnectionIssue)
                    }
                    is ServiceMessage.Unknown -> {
                        InspectorDetailsState.Loaded(bridgeInfo.categories, AttributesStatus.InternalError)
                    }
                    is ServiceMessage.UnsupportedType -> {
                        InspectorDetailsState.Loaded(bridgeInfo.categories, AttributesStatus.InternalError)
                    }
                }
            )
        }
    }

    private inner class OnMenuItemClickListener : (InspectorModeMenuItem) -> Unit {

        override fun invoke(menuItem: InspectorModeMenuItem) {
            exhaustive..when (menuItem) {
                is EnterInspectDetailsMode -> {
                    showNodeDetails()
                }
                is PinNode -> {
                    pinNode()
                }
                is ToggleDimensionType -> {
                    toggleDimensionType()
                }
                is ToggleLayer -> {
                    switchLayer()
                }
                is TogglePercentVisibility -> {
                    togglePercentVisibility()
                }
                is ToggleSettings -> {
                    toggleSettingsShown()
                }
            }
        }
    }

    private inner class OnBackPressListener : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            when {
                nodeDetailsState.value != null -> {
                    hideNodeDetails()
                }
                areSettingsShownState.value -> {
                    toggleSettingsShown()
                }
                else -> {
                    switchState()
                }
            }
        }

        private fun switchState() {
            state.value = when (val currentState = state.value) {
                is InspectorState.HasHierarchy.Selected -> {
                    InspectorState.HasHierarchy.NotSelected(
                        rootNode = currentState.rootNode
                    )
                }
                is InspectorState.HasHierarchy.Pinned -> {
                    InspectorState.HasHierarchy.Selected(
                        rootNode = currentState.rootNode,
                        selectedCollection = currentState.pinnedCollection,
                        selectedIndex = currentState.pinnedIndex,
                        selectedX = currentState.pinnedX,
                        selectedY = currentState.pinnedY,
                    )
                }
                is InspectorState.HasHierarchy.PinnedAndSelected -> {
                    InspectorState.HasHierarchy.Selected(
                        rootNode = currentState.rootNode,
                        selectedCollection = currentState.selectedCollection,
                        selectedIndex = currentState.selectedIndex,
                        selectedX = currentState.selectedX,
                        selectedY = currentState.selectedY,
                    )
                }
                InspectorState.NoHierarchy,
                is InspectorState.HasHierarchy.NotSelected -> {
                    error("")
                }
            }
        }

    }

    companion object {

        private const val KEY_NODE_STROKE_SCALE = "inspector_node_stroke_scale"
        private const val KEY_TEXT_SCALE = "inspector_text_scale"

        private const val KEY_DAY_OVERLAY_COLOR = "inspector_day_overlay_color"
        private const val KEY_DAY_ARROW_COLOR = "inspector_day_arrow_color"
        private const val KEY_DAY_NODE_STROKE_COLOR = "inspector_day_node_stroke_color"
        private const val KEY_DAY_SELECTED_STROKE_COLOR = "inspector_day_selected_stroke_color"
        private const val KEY_DAY_PINNED_STROKE_COLOR = "inspector_day_pinned_stroke_color"
        private const val KEY_DAY_TEXT_COLOR = "inspector_day_text_color"

        private const val KEY_NIGHT_OVERLAY_COLOR = "inspector_night_overlay_color"
        private const val KEY_NIGHT_ARROW_COLOR = "inspector_night_arrow_color"
        private const val KEY_NIGHT_NODE_STROKE_COLOR = "inspector_night_node_stroke_color"
        private const val KEY_NIGHT_SELECTED_STROKE_COLOR = "inspector_night_selected_stroke_color"
        private const val KEY_NIGHT_PINNED_STROKE_COLOR = "inspector_night_pinned_stroke_color"
        private const val KEY_NIGHT_TEXT_COLOR = "inspector_night_text_color"

        private const val DEFAULT_NODE_STROKE_SCALE = 1.0f
        private const val DEFAULT_TEXT_SCALE = 1.0f

        private val DEFAULT_DAY_OVERLAY_COLOR = Color.White.copy(alpha = 0.9f)
        private val DEFAULT_DAY_ARROW_COLOR = Color.Black
        private val DEFAULT_DAY_NODE_STROKE_COLOR = Color(red = 1, green = 87, blue = 155, alpha = 0.25.toIntAlpha())
        private val DEFAULT_DAY_SELECTED_STROKE_COLOR = Color(red = 13, green = 130, blue = 233)
        private val DEFAULT_DAY_PINNED_STROKE_COLOR = Color(red = 245, green = 127, blue = 23)
        private val DEFAULT_DAY_TEXT_COLOR = Color.Black

        private val DEFAULT_NIGHT_OVERLAY_COLOR = Color(red = 18, green = 18, blue = 18, alpha = 0.9.toIntAlpha())
        private val DEFAULT_NIGHT_ARROW_COLOR = Color.White
        private val DEFAULT_NIGHT_NODE_STROKE_COLOR = Color.White.copy(alpha = 0.25f)
        private val DEFAULT_NIGHT_SELECTED_STROKE_COLOR = Color(red = 13, green = 130, blue = 233)
        private val DEFAULT_NIGHT_PINNED_STROKE_COLOR = Color(red = 245, green = 127, blue = 23)
        private val DEFAULT_NIGHT_TEXT_COLOR = Color.White
    }
}