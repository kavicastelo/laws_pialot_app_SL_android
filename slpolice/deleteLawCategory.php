<?php 
include("db.php");

$cat = $_POST["cat"];

	$delete = "delete from laws where category='".$cat."'";
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