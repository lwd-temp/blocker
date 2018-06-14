package com.merxury.blocker.ui.settings.entity

data class BlockerRule(
        var packageName: String = "",
        var versionName: String = "",
        var versionCode: Int = -1,
        var components: MutableList<ComponentRule> = ArrayList()
)