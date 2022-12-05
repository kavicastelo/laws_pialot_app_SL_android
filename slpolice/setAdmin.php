<?php
include("db.php");

$mail = $_POST['email'];

$update = "update logins set loginlevel= 1 where email='".$mail."'";
try
{
	if(!mysqli_query($connect,$update))
	{
		echo "notadd";
	}
	else
	{
		echo "added";
	}
}
catch(Exception $update)
{
	echo "dberror";
}
?>
