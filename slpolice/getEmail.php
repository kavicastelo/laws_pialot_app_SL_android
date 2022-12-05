<?php 
include("db.php");

$mail = $_POST['email'];

$email = "select email from users where email='".$mail."'";
$checkMail = mysqli_query($connect,$email);

try
{
    while($found=mysqli_fetch_array($checkMail))
    {
    	if(!$found['email']==$mail || $found['email']=="")
    	{
    		echo ("notfound");
    	}
    	else
    	{
    		echo ("found");
    	}
    }
    mysqli_close($connect);
}
catch(Exception $found)
{
    echo $found;
}
?>
