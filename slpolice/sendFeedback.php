<?php 
include("db.php");

$name = $_POST["name"];
$fb = $_POST["feedback"];
$date = $_POST["date"];

$feedback = "insert into feedbacks(name,feedback,date) values('$name','$fb','$date')";
if(!mysqli_query($connect,$feedback))
	{
		echo ("notsend");
	}
	else
	{
		echo ("send");
	}
	mysqli_errno($connect);
?>
