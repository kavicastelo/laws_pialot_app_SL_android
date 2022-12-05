<?php 
include ("db.php");

$category = $_POST['cat'];
$amendment = $_POST['amendment'];
$amendmentpdf = $_POST['amendmenturl'];
$lang =$_POST['language'];


	$sqlamendment = "insert into laws(category,name,url,type,language) values ('$category','$amendment','$amendmentpdf','Amendment','$lang')";
	$amendmentRes = mysqli_query($connect,$sqlamendment);
	

if(!$amendmentRes)
{
	echo ("notupdate");
	//printf("Error: %s\n", mysqli_error($connect));
}
else
{
	echo ("update");
}
?>