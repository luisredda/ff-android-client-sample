package io.harness.sample.cfsdk

import io.harness.cfsdk.R

sealed class ModuleType(val resourceLight: Int, val resourceDark: Int) {
    object CI : ModuleType(R.drawable.ci, R.drawable.ci_dark)
    object CE : ModuleType(R.drawable.ce, R.drawable.ce_dark)
    object CV : ModuleType(R.drawable.cv, R.drawable.cv_dark)
    object CF : ModuleType(R.drawable.cf, R.drawable.cf_dark)
}