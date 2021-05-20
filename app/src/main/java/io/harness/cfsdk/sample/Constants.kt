package io.harness.cfsdk.sample

object Constants {

    const val CF_SDK_API_KEY = "YOUR_API_KEY"

    const val CF_SERVER = "https://config.feature-flags.uat.harness.io"
    const val BASE_URL = "$CF_SERVER/api/1.0"
    const val STREAM_URL = "$BASE_URL/stream/environments/"

    enum class Account(var accountName: String) {
        APTIV("Aptiv"),
        EXPERIAN("Experian"),
        FISERV("Fiserv"),
        HARNESS("Harness"),
        PAOLO("Paolo Alto Networks");

    }

    public var selectedAccount = Account.EXPERIAN.accountName
    enum class CFFlags(var flag: String) {
        CF_RIBBON("harnessappdemocfribbon"),

        ENABLE_CF_MODULE("harnessappdemoenablecfmodule"),ENABLE_CE_MODULE("harnessappdemoenablecemodule"),
        ENABLE_CI_MODULE("harnessappdemoenablecimodule"), ENABLE_CV_MODULE("harnessappdemoenablecvmodule"),
        TRIAL_LIMIT_CI("harnessappdemocitriallimit"), TRIAL_LIMIT_CF("harnessappdemocftriallimit"),
        TRIAL_LIMIT_CE("harnessappdemocetriallimit"), TRIAL_LIMIT_CV("harnessappdemocvtriallimit"),

        ENABLE_GLOBAL_HELP ("harnessappdemoenableglobalhelp"),DARK_MODE("harnessappdemodarkmode");

    }
}