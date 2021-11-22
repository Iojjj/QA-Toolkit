# QA Toolkit

QA Toolkit is a set of tools for Software and QA Engineers to verify
design implementation. It consists of two components: QA Toolkit
application and QA Toolkit Bridge module. Both can be downloaded from
releases page.

## Application Setup

Once you install application follow instructions to configure QA Toolkit.

1. First you need to allow app to display over other apps. Depending on
Android OS version and manufacturer of your device instead of QA Toolkit
application settings system might open the list of apps that can request
this permission. Find QA Toolkit app and grant access.  
&nbsp;  
![Setup Wizard, Step 1](/readme/setup_step_1_1.png)&nbsp;
![Display over other apps settings screen](/readme/setup_step_1_2.png)

2. Then enable QA Toolkit Accessibility Service. Once again depending on
Android OS version and manufacturer of your device system may open
different screen. Find list of installed (downloaded) services and
click on QA Toolkit.  
&nbsp;  
![Setup Wizard, Step 2](/readme/setup_step_2_1.png)&nbsp;
![Accessibility settings screen](/readme/setup_step_2_2.png)  
![QA Toolkit accessibility service settings screen](/readme/setup_step_2_3.png)

3. Once you enable Accessibility Service and return back you should
see a message informing that everything configured correctly. Also
QA Toolkit overlay will become visible.  
&nbsp;  
![Setup Wizard, Step 3](/readme/setup_step_3_1.png)

## Toolkit Bar

Initial Toolkit Bar menu has such elements:

1. **Drag icon**. It allows to drag Toolkit Bar over the screen.  
![Drag icon](/readme/overlay_drag_light.png#gh-light-mode-only)
![Drag icon](/readme/overlay_drag_dark.png#gh-dark-mode-only)
2. **Inspector tool icon** that launches Inspector Tool.  
![Inspector Tool icon](/readme/overlay_inspector_light.png#gh-light-mode-only)
![Inspector tool icon](/readme/overlay_inspector_dark.png#gh-dark-mode-only)
3. **Grid tool icon** that launches Grid Tool.  
![Grid Tool icon](/readme/overlay_grid_light.png#gh-light-mode-only)
![Grid tool icon](/readme/overlay_grid_dark.png#gh-dark-mode-only)
4. **Ruler tool icon** that launches Ruler Tool.  
![Ruler Tool icon](/readme/overlay_ruler_light.png#gh-light-mode-only)
![Ruler tool icon](/readme/overlay_ruler_dark.png#gh-dark-mode-only)
5. **Rotate bar icon**. It changes orientation of bar.  
![Rotate icon](/readme/overlay_rotate_light.png#gh-light-mode-only)
![Rotate icon](/readme/overlay_rotate_dark.png#gh-dark-mode-only)

## Inspector Tool

Inspector tool allows you to verify UI elements size and position.
In advanced inspection you can also verify view attributes.

### General Inspection

General inspection mode enabled by default and uses layout
information available to Accessibility Service.

Once you launched Inspector Tool and selected UI element you will see
its bounds, size and relative distances within parent layout.

![Youtube, Android Developers channel](/readme/inspector_0.png)&nbsp;
![Inspector Tool](/readme/inspector_14.png)  
![Inspector Tool, selected UI element](/readme/inspector_1.png)

#### Layers

Sometimes applications might have layouts that recognized by
Accessibility Service but not visible to users. They will intercept
touches and block you from selecting UI elements behind them. In such
cases you need to follow next steps:
1. Click on UI element that you're interested in. Inspector Tool will
select overlay (invisible) view.
2. Cycle through hierarchy using Layer icon until required UI element
is selected.

In this example Home tab was selected by clicking on it. Inspector Tool
found all UI elements at touch coordinates and sort them using their
z-index. The top most element is selected by default and it has a layer
number 17. So to select Home tab we need to cycle through layers by
clicking Layer icon. In this case Home tab has a layer number 16.
Depending on layout hierarchy of inspected app you may need to click
Layer icon multiple times.

![Inspector Tool, selected overlay](/readme/inspector_1.png)&nbsp;
![Inspector Tool, selected Home tab](/readme/inspector_2.png)

#### Measuring distance between elements

To inspect distances between two UI elements follow next steps:
1. Select the first UI element.
2. Click Pin icon to remember selected UI element.
3. Select the seconds UI element.

After that Inspector Tool will display relative distances between edges
of selected UI elements.

![Inspector Tool, distances between UI elements](/readme/inspector_3.png)

In case if distance equals to zero it will not be displayed.

![Inspector Tool, no distances #1](/readme/inspector_10.png)&nbsp;
![Inspector Tool, no distances #2](/readme/inspector_11.png)  
![Inspector Tool, no distances #3](/readme/inspector_15.png)

#### Measurement units

By default sizes and distances measured in DPs (density-independent pixels)
but you can also view them in PXs by clicking DP/PX icon.

![DP](/readme/inspector_3.png)&nbsp;
![PX](/readme/inspector_4.png)

#### Percentage mode

Sometimes you may need to verify that size or distance depends on
device screen's width and/or height. In this case you can toggle
percentage mode by clicking Percentage Mode icon. Note that this mode
uses real screen size ignoring system insets such as status/navigation
bars, display cutouts, etc.

![Inspector Tool, percentage mode off](/readme/inspector_1.png)
![Inspector Tool, percentage mode on](/readme/inspector_5.png)

#### Zoom and Pan

Sometimes UI element bounds or distances between edges are so small
that they can't be displayed. For such cases you can use pinch gesture
to zoom layout in or out.

![Inspector Tool, zoomed out](/readme/inspector_5.png)&nbsp;
![Inspector Tool, zoomed in](/readme/inspector_6.png)

#### Attributes Inspection

By default Accessibility Service provides few attributes that can be
viewed by clicking Details icon. In General Inspection mode you'll
always see a warning icon. By clicking it Inspector Tool will provide
an additional information about the reason why inspectable app can be
inspected in General Inspection mode.

![Inspector Tool, attributes list](/readme/inspector_7.png)&nbsp;
![Inspector Tool, filtered attributes list](/readme/inspector_9.png)  
![Inspector Tool, information message](/readme/inspector_8.png)

### Advanced Inspection

Advanced inspection available only for those applications that added
QA Toolkit Bridge module. You can determine if advanced inspection
available by checking Drag Icon. Small chain icon in the corner means
that currently inspected application has QA Toolkit Bridge and can
provide additional view attributes.

In the sample below you can see some preconfigured `TextView` and list
of its attributes such as Auto Size, Shadow, Paragraph and Compound
Drawables.

![Inspector Tool, advanced inspection available](/readme/inspector_advanced_4.png)&nbsp;
![Inspector Tool, auto size attributes](/readme/inspector_advanced_5.png)  
![Inspector Tool, shadow attributes](/readme/inspector_advanced_6.png)&nbsp;
![Inspector Tool, compound drawables attributes](/readme/inspector_advanced_7.png)

Sometimes you may inspect views that have their own implementation of
Accessibility Delegate. Usually such views draw their content on `Canvas`
on their own so they need to provide additional information about
their "virtual" children to Accessibility Service
(for example, `CalendarView`). Such views can't be inspected even in
Advanced Inspection mode. You will see a warning icon and a message
explaining why.

![Inspector Tool, CalendarView](/readme/inspector_advanced_1.png)&nbsp;
![Inspector Tool, selected virtual view](/readme/inspector_advanced_3.png)  
![Inspector Tool, information message](/readme/inspector_advanced_2.png)

Note: advanced inspection supports only `View`-based layouts.
This means that Jetpack Compose layouts will have only basic information
(similar to General Inspection mode).

#### Extending Advanced Inspection

You can add support of advanced inspection for custom or 3rd party views
by implementing your own `InspectorConfiguration`.

Additional details TBD.

## Grid Tool

Grid tool allows you to verify alignment of UI elements within a grid.

![Grid Tool](/readme/grid_1.png)

You can pan grid in any direction to adjust cells position. By default
cells will remain at position where you leave them, but also you can
configure grid to snap cells to screen edges. To do so toggle modes for
start/end and top/bottom edges using corresponding toggles in
Toolkit Bar menu.

1. Don't snap at edges (default, horizontal and vertical edges accordingly).  
&nbsp;  
![Grid Tool, no horizontal snap](/readme/grid_snap_free_horizontal_light.png#gh-light-mode-only)
![Grid Tool, no horizontal snap](/readme/grid_snap_free_horizontal_dark.png#gh-dark-mode-only)&nbsp;
![Grid Tool, no vertical snap](/readme/grid_snap_free_vertical_light.png#gh-light-mode-only)
![Grid Tool, no vertical snap](/readme/grid_snap_free_vertical_dark.png#gh-dark-mode-only)
2. Snap horizontally (start and end edges accordingly).  
&nbsp;  
![Grid Tool, snap at start](/readme/grid_snap_start_light.png#gh-light-mode-only)
![Grid Tool, snap at start](/readme/grid_snap_start_dark.png#gh-dark-mode-only)&nbsp;
![Grid Tool, snap at end](/readme/grid_snap_end_light.png#gh-light-mode-only)
![Grid Tool, snap at end](/readme/grid_snap_end_dark.png#gh-dark-mode-only)
3. Snap vertically (top and bottom edges accordingly).  
&nbsp;  
![Grid Tool, snap at top](/readme/grid_snap_top_light.png#gh-light-mode-only)
![Grid Tool, snap at top](/readme/grid_snap_top_dark.png#gh-dark-mode-only)&nbsp;
![Grid Tool, snap at bottom](/readme/grid_snap_bottom_light.png#gh-light-mode-only)
![Grid Tool, snap at bottom](/readme/grid_snap_bottom_dark.png#gh-dark-mode-only)

### Cell Size

You can change cell size using pinch-to-zoom gesture or via settings.

![Grid Tool, cell size 8dp](/readme/grid_1.png)&nbsp;
![Grid Tool, cell size 16dp](/readme/grid_2.png)

## Ruler Tool

Ruler tool allows you to check distances between multiple UI elements
within a single orientation (horizontal or vertical). Note that this
tool is less accurate than Inspector as rulers are not stick to UI
elements' bounds. Consider using Ruler Tool for measuring distances
between elements that doesn't have their own bounds (for example, line
spacing in text, distance between compound drawables and text, etc.).

![Ruler Tool](/readme/ruler_1.png)

### Measurement units

By default sizes and distances measured in DPs (density-independent pixels)
but you can also view them in PXs by clicking DP/PX icon.

![DP](/readme/ruler_1.png)&nbsp;
![PX](/readme/ruler_2.png)

### Percentage mode

Sometimes you may need to verify that size or distance depends on
device screen's width and/or height. In this case you can toggle
percentage mode by clicking Percentage Mode icon. Note that this mode
uses real screen size ignoring system insets such as status/navigation
bars, display cutouts, etc.

![Ruler Tool, percentage mode off](/readme/ruler_1.png)&nbsp;
![Ruler Tool, percentage mode on](/readme/ruler_3.png)

### Adding and removing rulers

You can add a new ruler by clicking Plus icon. This will pin previous
ruler and display a single distance to top/bottom screen edge or another
ruler (depending on position of currently active ruler). To remove a
ruler you can click Minus icon or press Back.

![Ruler Tool, one ruler](/readme/ruler_1.png)&nbsp;
![Ruler Tool, two rulers](/readme/ruler_4.png)

### Position of measurements

You can change position of measurements by swiping screen. Direction of
swiping depends on ruler orientation and current measurements position.

![Ruler Tool, measurement position: default](/readme/ruler_4.png)&nbsp;
![Ruler Tool, measurement position: opposite](/readme/ruler_5.png)

### Sensitivity

You can change sensitivity of ruler sliding using pinch-to-zoom gesture
or via settings.

### Ruler orientation

Ruler orientation can be changed only if there is only one ruler on the
screen using rotate gesture.

![Ruler Tool, orientation: horizontal](/readme/ruler_1.png)&nbsp;
![Ruler Tool, orientation: vertical](/readme/ruler_6.png)

## Dark Mode

QA Toolkit supports dark mode according to device settings.

![Dark Mode](/readme/dark_mode.png)

## Settings

Each mode have its own settings allowing you to configure application
in the way you want. Color settings stored per light/dark mode.

![Overlay Color Settings of Inspector Tool](/readme/settings.png)

TBD