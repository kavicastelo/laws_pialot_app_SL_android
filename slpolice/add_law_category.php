<?php 
include ("db.php");

$category = $_POST['cat'];
$book = $_POST['book'];
$amendment = $_POST['amendment'];
$bookpdf = $_POST['bookurl'];
$amendmentpdf = $_POST['amendmenturl'];
$lang =$_POST['language'];

$sqlbook = "insert into laws(category,name,url,type,language) values ('$category','$book','$bookpdf','Book','$lang')";
$bookRes = mysqli_query($connect,$sqlbook);

if($amendment != "")
{
	$sqlamendment = "insert into laws(category,name,url,type,language) values ('$category','$amendment','$amendmentpdf','Amendment','$lang')";
	$amendmentRes = mysqli_query($connect,$sqlamendment);
}

if(!$bookRes)
{
	echo ("booknotadd");
	printf("Error: %s\n", mysqli_error($connect));
}
else
{
	echo ("bookadd");
}
?>
