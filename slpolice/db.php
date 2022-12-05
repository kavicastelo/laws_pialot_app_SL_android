<?php
$location ="localhost";
$username = "id19456271_kavi";
$password = "Kavi@2000Test";
$databasename = "id19456271_slpolice";

	//connecting to database on phpmyadmin
//$connect = mysqli_connect($location,$username,$password,$databasename);
$connect = new mysqli($location,$username,$password,$databasename);
if(mysqli_connect_errno($connect))
{
echo ("DB Not Connected".mysqli_connect_error());
}
else
{
//echo ("DB Connected");
}
?>

