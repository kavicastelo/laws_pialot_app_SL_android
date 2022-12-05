<?php 
include("db.php");

$id = $_POST["id"];
$name = $_POST["name"];
$range =$_POST["range"];
$tp = $_POST["tp"];
$office = $_POST["office"];
$fax = $_POST["fax"];
$type = $_POST["type"];

if(!$name=="" or !$tp=="" or !$office=="" or !$fax=="" or !$type=="")
{
	//update digs table details
	$update = "update digs set name='$name',aria='$range',mobile='$tp',office='$office',fax='$fax',type='$type' where DID='$id'";
	if(!mysqli_query($connect,$update))
	{
		echo ("notupdate");
	}
	else
	{
		echo ("update");
	}
	mysqli_close($connect);
}
else
{
    echo("empty");
}
?>