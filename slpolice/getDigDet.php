<?php 
include("db.php");

$id = $_POST['id'];

$sql = "select * from digs where DID='".$id."'";
$res = mysqli_query($connect,$sql);
$result = array();

while($row = mysqli_fetch_array($res))
{
	array_push($result,array('name'=>$row[1],'aria'=>$row[2],'mobile'=>$row[3],
	'office'=>$row[4],'fax'=>$row[5],'type'=>$row[6]));
}
echo json_encode(array('result'=>$result));
 
mysqli_close($connect);
?>