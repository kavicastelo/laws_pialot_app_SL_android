<?php 
include("db.php");

$date = $_POST['date'];

$sql = "update article set title ='', body ='', date ='0000-00-00' where date='".$date."'";
$res = mysqli_query($connect,$sql);


if(!$res)
{
    echo "notdelete";
    printf("Error: %s\n", mysqli_error($connect));
}
else
{
    echo "delete";
}
?>