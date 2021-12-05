package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

const val INSPECTOR_ORDER_STEP = 100

const val CATEGORY_VIEW_BACKGROUND: String = "View: Background"
const val CATEGORY_VIEW_FOREGROUND: String = "View: Foreground"
const val CATEGORY_VIEW_TRANSFORM: String = "View: Transform"
const val CATEGORY_NAME_UNCATEGORIZED = "Uncategorized"

const val CATEGORY_ORDER_DEFAULT = 0
const val CATEGORY_ORDER_STEP_LARGE = 10_000_000
const val CATEGORY_ORDER_STEP_SMALL = 1_000

// Always the first one
const val CATEGORY_ORDER_MAIN = Int.MIN_VALUE

// Always the last one
const val CATEGORY_ORDER_UNCATEGORIZED = Int.MAX_VALUE

// Always the second one
const val CATEGORY_ORDER_VIEW_LAYOUT = CATEGORY_ORDER_MAIN + 1

// Always before uncategorized
const val CATEGORY_ORDER_VIEW_STATE = CATEGORY_ORDER_UNCATEGORIZED - CATEGORY_ORDER_STEP_LARGE

// Always before View: State
const val CATEGORY_ORDER_VIEW_TRANSFORM = CATEGORY_ORDER_VIEW_STATE - CATEGORY_ORDER_STEP_LARGE

// Always before View: Transform
const val CATEGORY_ORDER_VIEW_FOREGROUND = CATEGORY_ORDER_VIEW_TRANSFORM - CATEGORY_ORDER_STEP_LARGE

// Always before View: Foreground
const val CATEGORY_ORDER_VIEW_BACKGROUND = CATEGORY_ORDER_VIEW_FOREGROUND - CATEGORY_ORDER_STEP_LARGE