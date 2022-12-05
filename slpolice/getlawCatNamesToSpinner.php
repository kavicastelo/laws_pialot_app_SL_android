<?php 
include("db.php");

$sql = "select * from laws";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
	array_push($result,array('cat'=>$row[1]));
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>