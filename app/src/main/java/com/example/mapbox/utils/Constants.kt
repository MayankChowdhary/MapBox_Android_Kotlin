package com.example.mapbox.utils

object Constants {
    const val BarikoiKey = "bkoi_4121ae0e2b728d973956d4b66f22a9ab5bab0fffa483a41b226bce3bf2456fe5"

    class MapType {
        companion object {
           const val barikoiLiberty =
                "https://map.barikoi.com/styles/osm-liberty/style.json?key=${BarikoiKey}"
         const  val barikoiDark =
                "https://map.barikoi.com/styles/barikoi-dark/style.json?key=${BarikoiKey}"
           const val default = "https://demotiles.maplibre.org/style.json"
        }
    }
}