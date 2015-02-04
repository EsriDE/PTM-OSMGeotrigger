# **OSM ArcGIS Loader**
*The OSM ArcGIS Loader converts [OpenStreetMap](http://www.openstreetmap.org) (OSM) data to ArcGIS feature services. This library is part of the OSM-Geotrigger-App.*

## **Features**

This application fills ArcGIS feature services with OSM objects based on [Overpass QL](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL).

### Overpass requirements
* output Format JSON: [\[out:json\]](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL#Output_Format_.28out.29)
* print out including the geometry: [out geom;](http://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL#Print_.28out.29)

Example: [http://www.overpass-api.de/api/interpreter?data=\[out:json\];node\(48.3946,11.5962,48.4151,11.6305\)\[amenity=post_box\];out geom;](http://www.overpass-api.de/api/interpreter?data=%5Bout:json%5D;node%2848.3946,11.5962,48.4151,11.6305%29%5Bamenity=post_box%5D;out%20geom;;)

### Supported objects
* OSM nodes can be interpreted as points, e.g. points of interest.
* OSM ways can be interpreted as polylines and polygons, e.g. highways or lakes.
* OSM relations can rudimentarily be interpreted as polylines and polygons, e.g. administrative boundaries. The relation role is not interpreted.

### Fields
* The fields of the feature service can be filled with OSM tags.
* The feature service additionally contains fields for the OSM ID, the URL to the OSM object, and a summary field for all OSM tags.

## **Getting Started**

* Create the configuration file.
* Create the feature services in advance (see configuration file).
 * [Sample Web Map](http://esri-de-dev.maps.arcgis.com/home/item.html?id=72117966ec084c3b95ead6dc359e3764) with sample feature services
* Start the OSM ArcGIS Loader as command line tool without installation.
* Pass the path to the configuration file as argument. 

## **Requirements**

Used libraries:
* commons-logging-1.1.3.jar
* httpcore-4.3.2.jar
* httpclient-4.3.5.jar
* json.jar
* log4j-api-2.0.2.jar
* log4j-core-2.0.2.jar

## **Contributing**

You are welcome to checkout the this sample code to get inspirated for your own projects.


## **Licensing**
Copyright 2015 Esri Deutschland GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's LICENSE-2.0.txt file.
