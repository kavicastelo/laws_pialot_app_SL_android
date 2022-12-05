<?php 
include("db.php");

$date = $_POST['date'];

$sql = "select * from article where date='".$date."'";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
	array_push($result,array('title'=>$row[1],'body'=>$row[2]));
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>