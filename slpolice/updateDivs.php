<?php 
include("db.php");

$Cstation = $_POST["cStation"];
$station = $_POST["station"];
$oic = $_POST["oic"];
$office = $_POST["office"];

//update digs table details
$update = "update divisions set station='$station',oic='$oic',office='$office' where station='$Cstation'";
if(!mysqli_query($connect,$update))
{
	echo ("notupdate");
}
else
{
	echo ("update");
}
mysqli_close($connect);

?>