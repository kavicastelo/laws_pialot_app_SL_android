<?php 
include("db.php");

$province = $_POST['province'];

$sql = "select * from divisions where province='".$province."'";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
	array_push($result,array('division'=>$row[2]));
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>