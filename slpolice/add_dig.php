<?php 
include("db.php");

$name = $_POST["name"];
$range = $_POST["range"];
$tp = $_POST["tp"];
$office = $_POST["office"];
$fax = $_POST["fax"];
$type = $_POST["type"];

if(!$name=="" or !$tp=="" or !$office=="" or !$fax=="" or !$type=="")
{
	//add digs table details
	$dig = "insert into digs(name, aria, mobile, office, fax, type) values('".$name."','".$range."','".$tp."','".$office."','".$fax."','".$type."')";
	if(!mysqli_query($connect,$dig))
	{
		echo ("notadd");
	}
	else
	{
		echo ("add");
	}
	mysqli_close($connect);
}
else
{
    echo("empty");
}
?>
