<?php 
include("db.php");

$date = $_POST['date'];

	$sql = "select * from article where date='".$date."'";
	$getDate = mysqli_query($connect,$sql);
	
	while($row=mysqli_fetch_array($getDate))
	{
		if($row['date']==$date)
		{
			echo ("match");
		}
		else
		{
			echo ("notmatch");
		}
	}
	mysqli_close($connect);

?>