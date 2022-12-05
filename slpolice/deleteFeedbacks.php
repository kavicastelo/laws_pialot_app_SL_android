<?php 
include("db.php");

$name = $_POST["name"];

	$delete = "delete from feedbacks where name='".$name."'";
	if(!mysqli_query($connect,$delete))
	{
		echo ("notdeleted");
		//printf("Error: %s\n", mysqli_error($connect));
	}
	else
	{
		echo ("deleted");
	}
	mysqli_close($connect);
?>