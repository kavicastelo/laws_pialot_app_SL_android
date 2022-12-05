<?php
include("db.php");

$Date = $_POST['date'];

 
$sql = "select * from feedbacks where date='".$Date."'";
 
$res = mysqli_query($connect,$sql);

 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array('name'=>$row[1],
'feedback'=>$row[2]));
}
//printf("Error: %s\n", mysqli_error($connect)); 
echo json_encode(array("result"=>$result));
 
mysqli_close($connect);
 
?>