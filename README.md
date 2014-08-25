
kmlutils - utils for KML files
==============================

The purpose of this project is to provide functions for XML-based "visual" file formats. 
Main Focus is KML, SVG and GraphML file formats.

The main functionality of this project is a tool for converting SVG files into KML files. Currently it (svg2kml)
partially converts polygons (tbrugz.geo.SVG2KML)

This project also provides a conversion tool for SVG to GraphML (tbrugz.geo.SVG2GraphML)

The project's [wiki](https://bitbucket.org/tbrugz/kmlutils/wiki) also have some useful information.

Author:
Telmo Brugnara <[tbrugz@gmail.com](mailto:tbrugz@gmail.com)>

License:
[GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl.html)


svg2kml howto
-------------
- build with ant
- copy `svg2kml.properties.template` to `svg2kml.properties`
- edit `svg2kml.properties`, set "svgin", "kmlout", "maxX", "minX", "maxY", "minY" properties
- (optional) edit `snippets.properties` to set constant parts to be inserted in the KML file
- (optional) edit `idmappings.properties` to translate IDs from the SVG file to the KML file
- run `java -cp dist/svg2kml.jar;lib/commons-logging-1.1.1.jar;lib/log4j-1.2.15.jar tbrugz/geo/SVG2KML`


svg2graphml howto
-----------------
- similar to svg2kml ;) but you have to edit `svg2graphml.properties`
- run `java -cp dist/svg2kml.jar;lib/commons-logging-1.1.1.jar;lib/log4j-1.2.15.jar tbrugz/geo/SVG2GraphML`


Dependencies
------------
- jdk 1.5+
- log4j (included)
- commons-logging (included)
- [ant](http://ant.apache.org/) (not included - build only)
- [ivy](http://ant.apache.org/ivy/) (not included - build only)


Running - classes with main()
-----------------------------
- tbrugz.geo.SVG2KML (main)
- tbrugz.geo.SVG2GraphML
- tbrugz.geo.test.Test
- tbrugz.xml.test.Test


Included media
--------------
  
Municipalities_of_RS.svg originated from:
http://commons.wikimedia.org/wiki/File:Municipalities_of_the_South_Region_of_Brazil.svg
licensed under: [CC-BY-SA](http://creativecommons.org/licenses/by-sa/3.0/)

SantaCatarina_MesoMicroMunicip_municipios.svg originated from:
http://commons.wikimedia.org/wiki/File:SantaCatarina_MesoMicroMunicip.svg
licensed under: [CC-BY-SA](http://creativecommons.org/licenses/by-sa/3.0/)

Brazil_Labelled_Map_v2.svg originated from:
http://pt.wikipedia.org/wiki/Ficheiro:Brazil_Labelled_Map.svg
licensed under: [CC-BY-SA](http://creativecommons.org/licenses/by-sa/3.0/)


KML Schema - urls
-----------------
- http://schemas.opengis.net/kml/2.2.0/
- http://code.google.com/intl/pt-BR/apis/kml/schema/kml21.xsd


Specs...
--------
- http://code.google.com/intl/en/apis/kml/documentation/kml_tut.html
- http://code.google.com/intl/en/apis/kml/documentation/kmlreference.html
- http://www.w3.org/TR/SVG/
- http://www.w3.org/TR/SVG/paths.html


Changelog?
----------
see: [https://bitbucket.org/tbrugz/kmlutils/commits/](https://bitbucket.org/tbrugz/kmlutils/commits/)
