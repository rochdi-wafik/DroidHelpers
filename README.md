# ----------------------------------------------------------------
#    DroidHelpers    | Last Update = 24/08/2024
# ----------------------------------------------------------------

# Overview
- This library wrap a lot of android helper classes to help build apps in less time.

# Helpers Examples:
- Notification: 
* `NotificationMaker.class` used to create and manage notifications.
- Alerts: 
* `AlertMaker.class` used to create sweet ui alerts like Bootstrap alerts (not dialog)
- Converters: 
* `DataSizeConverter.class` used to convert between data like long, int, bytes, dates, etc.
* `JsonConverter.class` used to convert and manage json format
- Crypto:
* `StringCrypto.class` used to encrypt and decrypt string data using XOR or AES etc
- DB
* `SimpleDB.class` Lightweight db that uses SharedPreferences to store and manage data.
* `SqlPreferences.class` Act like SharedPreferences but is uses SqLite instead of Xml.
* `LocalStorage.class` Until now, it's just an  alias to SqlPreferences (for class naming)
- Network:
* `AddressHelper.class` provides utils related to addressing. like generate or get local IP.
* `NetworkHelper.class` provides utils related to networking. like is network connected.
=> This is one of the library helpers, other helpers will be added and documented later.

### Info ----------------------------------
> Created By: Rochdi Wafik
> Last Update: 24-08-2024
> Uploaded On: 25-08-2024
