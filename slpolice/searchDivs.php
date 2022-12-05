<?php
include("db.php");

$station = $_POST['station'];
 
$sql = "select * from divisions where station='".$station."'";
 
$res = mysqli_query($connect,$sql);

 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array('station'=>$row[3],
'oic'=>$row[4],
'office'=>$row[5]
));
}
//printf("Error: %s\n", mysqli_error($connect)); 
echo json_encode(array("result"=>$result));
 
mysqli_close($connect);
 
?>