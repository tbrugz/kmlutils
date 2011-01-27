
kmlutils - utils for KML files
------------------------------

The main purpose of this project is a tool for converting SVG files into KML files. Currently it (svg2kml)
partially converts polygons.

svg2kml howto:
- build with ant
- edit svg2kml.properties, set "svgin", "kmlout", "maxX", "minX", "maxY", "minY" properties
- (optional) edit snippets.properties to set constant parts to be inserted in the KML file
- (optional) edit idmappings.properties to translate IDs from the SVG file to the KML file
- run "java -cp dist/svg2kml.jar;lib/commons-logging-1.1.1.jar;lib/log4j-1.2.15.jar tbrugz/geo/SVG2KML"

Author:
Telmo Brugnara <tbrugz@gmail.com>

License:
GNU Lesser General Public License - http://www.gnu.org/licenses/lgpl.html

Dependencies:
- jdk 1.5
- log4j (included)
- commons-logging (included)
- ant (not included - build only)

Municipalities_of_RS.svg originated from:
http://commons.wikimedia.org/wiki/File:Municipalities_of_the_South_Region_of_Brazil.svg
licensed under: http://creativecommons.org/licenses/by-sa/3.0/deed.en


running - classes with main()
-----------------------------
- tbrugz.geo.SVG2KML
- tbrugz.geo.test.Test
- tbrugz.xml.test.Test
