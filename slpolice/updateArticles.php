<?php 
include("db.php");

$cDate = $_POST['cdate'];
$title = $_POST['title'];
$body = $_POST['body'];
$date = $_POST['date'];

$sql = "update article set title ='".$title."', body ='".$body."', date ='".$date."' where date='".$cDate."'";
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