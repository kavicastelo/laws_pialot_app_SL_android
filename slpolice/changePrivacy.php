<?php
include("db.php");

$CE = $_POST['currentEmail'];
$NE = $_POST['newEmail'];
$NP = $_POST['newPassword'];

$sql = "select * from logins where email='".$CE."'";
$sqluser = "select email from users where email='".$CE."'";
$update = "update logins set email='".$NE."', pwd='".md5($NP)."' where email='$CE'";
$updateuser = "update users set email='".$NE."' where email='$CE'";

$checkMail = mysqli_query($connect,$sql);
while($mail = mysqli_fetch_array($checkMail))
{
	if($mail['email']=!$_POST['currentEmail'])
	{
		echo "no";
	}
	else
	{
		
		if(!mysqli_query($connect,$update))
		{
			echo "notupdated";
			//printf("Error: %s\n", mysqli_error($connect));
		}
		else
		{
			echo "updated";
		}
		mysqli_errno($connect);
	}
}

$checkUser = mysqli_query($connect,$sqluser);
while($user = mysqli_fetch_array($checkUser))
{
    if($userl['email']=!$_POST['currentEmail'])
	{
		echo "no";
	}
	else
	{
		
		if(!mysqli_query($connect,$updateuser))
		{
			echo "notupdated";
			//printf("Error: %s\n", mysqli_error($connect));
		}
		else
		{
			echo "updated";
		}
		mysqli_errno($connect);
	}
}
?>
