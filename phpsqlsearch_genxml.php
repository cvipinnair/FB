<?php
require("phpsqlsearch_dbinfo.php");

// Get parameters from URL
$SOURCERNCID = $_GET["SOURCERNCID"];
$SOURCECELLID = $_GET["SOURCECELLID"];
//$radius = $_GET["radius"];

// Start XML file, create parent node
$dom = new DOMDocument("1.0");
$node = $dom->createElement("neighbor_defined");
$parnode = $dom->appendChild($node);

// Opens a connection to a mySQL server
$connection=mysql_connect ('localhost', $username, $password);
if (!$connection) {
  die("Not connected : " . mysql_error());
}

// Set the active mySQL database
$db_selected = mysql_select_db($database, $connection);
if (!$db_selected) {
  die ("Can\'t use db : " . mysql_error());
}

// Search the rows in the markers table
$query = sprintf("SELECT SOURCERNCID, SOURCECELLID, TARGETRNCID, TARGETCELLID FROM neighbor_defined where SOURCECELLID = '". mysql_real_escape_string($SOURCECELLID)."' and SOURCERNCID = '".mysql_real_escape_string($SOURCERNCID)."'");
$result = mysql_query($query);

$result = mysql_query($query);
if (!$result) {
  die("Invalid query: " . mysql_error());
}

header("Content-type: text/xml");

// Iterate through the rows, adding XML nodes for each
while ($row = @mysql_fetch_assoc($result)){
  $node = $dom->createElement("neighbor_defined");
  $newnode = $parnode->appendChild($node);
  $newnode->setAttribute("SOURCERNCID", $row['SOURCERNCID']);
  $newnode->setAttribute("SOURCECELLID", $row['SOURCECELLID']);
  $newnode->setAttribute("TARGETRNCID", $row['TARGETRNCID']);
  $newnode->setAttribute("TARGETCELLID", $row['TARGETCELLID']);
  //$newnode->setAttribute("distance", $row['distance']);
}

echo $dom->saveXML();
?>
