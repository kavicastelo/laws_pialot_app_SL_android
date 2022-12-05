<?php 
include("db.php");

$email = $_POST['email'];
$pass = $_POST['pass'];

$sql = "update logins set pwd ='".md5($pass)."' where email='".$email."'";
$res = mysqli_query($connect,$sql);

if(!$res)
{
    echo "notupdate";
}
else
{
    echo "update";
}
?>