<?php 
include("db.php");

$email = $_POST["email"];
$pass = $_POST["pass"];
//$email = "thenu@gmail.com";
//$pass = "1234";

//if(!$email=="" and !$pass=="")
//{
	$login = "select * from logins where email='".$email."'";
	$logindet = mysqli_query($connect,$login);
	
	while($log=mysqli_fetch_array($logindet))
	{
		if($log['email']==$email)
		{
			if($log['pwd']==md5($pass))
			{
				if($log['loginlevel']=="0")
				{
					echo ("user");
				}
				else if($log['loginlevel']=="1")
				{
					echo ("admin");
				}
				else if($log['loginlevel']=="2")
				{
				    echo ("ban");
				}
			}
			else
			{
				echo ("invalidPass");
			}
		}
		else
		{
			echo ("invalidEmail");
		}
	}
	mysqli_close($connect);
/*}
else
{
	echo ("empty values");
}*/
?>
