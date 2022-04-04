package io.harness.sample.cfsdk

class ModuleViewConfig(var backgroundColor: Int, var textColor: Int, var imageSrc: Int, var enableRibbon: Boolean = false)

class Module(val description: String, var trialPeriod: Number, var enabed: Boolean, val moduleType: ModuleType, val moduleViewConfig: ModuleViewConfig)