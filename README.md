OSM2Hive
========

How to load OSM file for GB into a SOLR index?
-------

1. Load data into a Hive table
 * Transform osmdata into osmways, osmnodes, osmrelations
2. Create a Parquet table for each transformed table for faster SQL queries
3. Denormalize the datamodel
4. Index all objectes to SOLR 

OSM2Hive
-----

OSM2Hive is a collection of User-defined functions for Hive to allow OSM XML data import.
It reads a XML file in a Hive table, and parses it to create new tables, in an easier to use
format. The application tests use JUnit 4 framework.

Usage
-----

OSM2Hive has to be called directly in Hive. To do so, use the following commands (in Hive) :

* Add JAR location
```
ADD JAR /path/to/osm2hive.jar;
```
* Create functions
```
CREATE TEMPORARY FUNCTION OSMImportNodes AS 'info.pavie.osm2hive.controller.HiveNodeImporter';
CREATE TEMPORARY FUNCTION OSMImportWays AS 'info.pavie.osm2hive.controller.HiveWayImporter';
CREATE TEMPORARY FUNCTION OSMImportRelations AS 'info.pavie.osm2hive.controller.HiveRelationImporter';
```
* Create table for OSM XML file and load it
```
CREATE TABLE osmdata(osm_content STRING) STORED AS TEXTFILE;
LOAD DATA LOCAL INPATH '/path/to/data.osm' OVERWRITE INTO TABLE osmdata;
```
* Create tables for each OSM type
```
CREATE TABLE osmnodes AS SELECT OSMImportNodes(osm_content) FROM osmdata;
CREATE TABLE osmways AS SELECT OSMImportWays(osm_content) FROM osmdata;
CREATE TABLE osmrelations AS SELECT OSMImportRelations(osm_content) FROM osmdata;
```

That's all.

License
-------

Copyright 2014-2015 Adrien PAVIE

Licensed under Apache License 2.0. See LICENSE for complete license.
