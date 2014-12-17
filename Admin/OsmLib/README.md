# **OSM Library**
*Kurzbeschreibung + aussagekräftiges Bild (optional)*

*Beispiel:*

This repository contains the source code for .....

![Testbild](https://developers.arcgis.com/img/homepage/github-browser.png "")


## **Features** (optional)
*Kurze Funktionsbeschreibung in Stichpunkten*

*Beispiel:*
* View and search for web maps
* Navigate a map with your fingertips

## **Getting Started**
*Beschreibung des Set-ups*


## **Beschreibung**
*Beschreibung der Anwendung, Hinweise zum Code usw.*

*Beispiel:*

Folgender Code macht dies und jenes:

```html
<!DOCTYPE html>
<html>
  <head>
     <!-- Load Leaflet from CDN-->
    <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.css" />
    <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>

    <!-- Load Esri Leaflet from CDN -->
    <script src="http://cdn-geoweb.s3.amazonaws.com/esri-leaflet/0.0.1-beta.5/esri-leaflet.js"></script>

    <style>
      html, body,  #map {
        width : 100%;
        height : 100%;
      }
    </style>
  </head>
  <body>
    <div id="map"></div>
    <script>
      var map = L.map('map').setView([45.528, -122.680], 13);

      L.esri.basemapLayer("Gray").addTo(map);

      var parks = new L.esri.FeatureLayer("http://services.arcgis.com/rOo16HdIMeOBI4Mb/arcgis/rest/services/Portland_Parks/FeatureServer/0", {
       style: function () {
          return { color: "#70ca49", weight: 2 };
        }
      }).addTo(map);

      var popupTemplate = "<h3>{NAME}</h3>{ACRES} Acres<br><small>Property ID: {PROPERTYID}<small>";

      parks.bindPopup(function(feature){
        return L.Util.template(popupTemplate, feature.properties)
      });
    </script>
  </body>
</html>
```

## **Ressourcen**
*Ressourcen und Third-Party-Lizenzbestimmungen*

*Beispiel:*

* [ArcGIS Runtime SDK for .NET](https://developers.arcgis.com/net/ "")
* [MVVM Light Toolkit](https://mvvmlight.codeplex.com/ "")

## **FAQ (optional)**


## **Lizenzbestimmungen**
*Beschreibung der Nutzungsbedingungen und Copyright*

*Beispiel:*

Copyright 2013 Esri

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's [license.txt]( https://raw.github.com/Esri/esri-leaflet/master/license.txt) file.
